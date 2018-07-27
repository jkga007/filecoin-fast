/**
 * 注册验证方法
 * 20180620
 * @author g20416
 */


var RegistFunc = (function () {
    var registFunc = {};

    /**
     * 填写基本信息(邮箱、内测邀请码、密码、验证码)
     * @param dictGuid
     */
    registFunc.ajaxBaseEmailReg = function () {
        var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

        var path = ctx + "/sys/regist/emailRegist";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        ajax.addJsonArray(jsonParam);
        Core.sendPacketJson(ajax, function (packet) {

            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var email = packet.registEmail;
                var userId = packet.userId;
                var mailUrl = packet.mailUrl;
                Core.alert("注册成功~ 点击进入下一步", 1, false, function () {
                    $("#registEmail").html(email);
                    $('#regist_type').val("U");
                    $("#user_mail").val(email);
                    $("#user_id").val(userId);
                    $("#mailUrl").val(mailUrl);
                    //模拟点击第二步
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            } else if (resultCode == "1") {
                var email = packet.registEmail;
                var userId = packet.userId;
                var mailUrl = packet.mailUrl;
                //待激活的信息,跳转激活页面(意外关闭页面或者怎么样)
                Core.alert(message, 2, false, function () {
                    $("#registEmail").html(email);
                    $('#regist_type').val("U");
                    $("#user_mail").val(email);
                    $("#user_id").val(userId);
                    $("#mailUrl").val(mailUrl);
                    //模拟点击第二步
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            } else if (resultCode == "2") {
                var userId = packet.userId;
                var step = packet.step;
                Core.alert(message, 1, false, function () {
                    $("#user_id").val(userId);
                    //模拟点击第4步
                    globleIndex = Number(step) - 1;
                    $('.processorBox li').eq(globleIndex).click();
                    // base64 encrypt
                    var rawStr = step + "#" + userId;
                    var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                    var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                    var url = "/modules/filecoin/regist.html?value=" + base64;
                    window.location.href = url;
                });
            } else {
                Core.alert(message, 2, false, function () {
                    LoginFunc.getCaptcha();
                });
            }
        }, false, true);

    };

    /**
     * 返回修改基本信息
     */
    registFunc.getEditMailUser = function () {
        /**
         * 返回修改
         */
        var userMail = $.trim($("#user_mail").val());
        var userId = $.trim($("#user_id").val());
        if (userMail.length != 0 && userId.length != 0) {
            var path = ctx + "/sys/getEditMailUser";
            var ajax = new AJAXPacket(path, "正在查询原注册信息...请稍后");

            ajax.add("userMail", userMail);
            ajax.add("userId", userId);

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    var userInfo = packet.retEditMailUser;
                    //模拟点击第一步
                    globleIndex = 0;
                    $('.processorBox li').eq(globleIndex).click();
                    //各种赋值啦
                    $('.email').val(userInfo.email);
                    $('#vcode').val(userInfo.invitationCode);
                    $('.passwd').val("");
                    $('.passwd2').val("");
                    $('.verifyCode').val("");
                    LoginFunc.getCaptcha();
                    $('#registType').val("U");

                } else {
                    Core.alert(message, 2, false, function () {
                    });
                }
            }, false, true);
        } else {
            Core.alert("您的操作有误,请重新注册!", 2, false, function () {

            });
        }
    };

    /**
     * 重新发送邮件信息
     */
    registFunc.resendMail = function (obj) {
        /**
         * 发送邮件
         */
        var userMail = $.trim($("#user_mail").val());
        var userId = $.trim($("#user_id").val());
        if (userMail.length != 0 && userId.length != 0) {

            var path = ctx + "/sys/resendMail";
            var ajax = new AJAXPacket(path, "正在重新发送邮件...请稍后");

            ajax.add("userMail", userMail);
            ajax.add("userId", userId);

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    Core.alert("重新发送邮件成功!,请注意查收!", 1, false, function () {
                        Core.setTimerFunc(obj, 30, "href", 'RegistFunc.resendMail(this)');
                    });

                } else {
                    Core.alert(message, 2, false, function () {
                    });
                }
            }, false, true);
        } else {
            Core.alert("您的操作有误,请重新注册!", 2, false, function () {

            });
        }
    };

    /**
     * 动态验证开启
     */
    registFunc.dynamicValidate = function (objId) {
        switch (objId) {
            case "email":
                $("#" + objId).rules("add", {
                    remote: {
                        //验证邮件是否已注册
                        type: "post",
                        url: "/regist/doValidate/" + objId,
                        cache: false,
                        data: {
                            email: function () {
                                return $("#" + objId).val();
                            }
                        }
                    },
                    messages: {remote: "该邮件已被注册~请更换~"}
                });
                break;
            case "vcode":
                $("#" + objId).rules("add", {
                    remote: {
                        //验证编码是否重复
                        type: "post",
                        url: "/regist/doValidate/" + objId,
                        cache: false,
                        data: {
                            vcode: function () {
                                return $("#" + objId).val();
                            }
                        }
                    },
                    messages: {remote: "该邀请码不存在~请更换~"}
                });
                break;
            default:
                break;
        }
    };

    /**
     * 删除动态验证remote
     */
    registFunc.onblurRemoveRules = function () {
        $("#email").keyup(function () {
            $("#email").rules("remove", "remote");
        });
        $("#vcode").keyup(function () {
            $("#vcode").rules("remove", "remote");
        });
    };

    /**
     * 发送手机短信
     */
    var closeTipId;
    registFunc.sendSmsMessage = function (obj) {

        var phone = $.trim($('#phone').val());
        var userId = $.trim($('#user_id').val());

        if (phone.length == 0) {
            closeTipId = Core.showTips($("#phone"), "请输入手机号码~");
            return false;
        } else {
            Core.close(closeTipId);
        }

        if (phone.length != 0 && userId.length != 0) {
            var path = ctx + "/wsendmessage/save/" + phone + "/" + userId;
            var ajax = new AJAXPacket(path, "正在发送短信...请稍后");

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    Core.alert("发送成功~ ", 1, false, function () {
                        var nextBtn2 = $("#nextBtn2");
                        nextBtn2.attr('onclick', 'javascript:$("#step3_frm").submit();');
                        nextBtn2.attr('style', '');
                        //发送成功之后,才开始计时
                        Core.setTimerFunc(obj, 120, "button");
                    });

                } else {
                    Core.alert(message, 2, false, function () {
                        // var _this = $(obj);
                        // _this.val('重新发送');
                        // _this.attr("disabled",false);
                        return false;
                    });
                }
            }, false, true);
        }
    };

    /**
     * 验证手机验证码是否正确
     */
    registFunc.smsMessageValide = function () {

        var phone = $.trim($('#phone').val());
        var phoneYzm = $.trim($('#phoneYzm').val());
        var userId = $.trim($('#user_id').val());
        if (phone.length != 0 && phoneYzm.length != 0 && userId.length != 0) {
            var path = ctx + "/wsendmessage/validate/" + phone + "/" + userId + "/" + phoneYzm;
            var ajax = new AJAXPacket(path, "正在验证手机验证码...请稍后");

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {

                    //提交绑定手机
                    RegistFunc.bindPhoneMsg();

                } else {
                    Core.alert(message, 2, false, function () {

                    });
                }
            }, false, true);
        }
    };

    /**
     * 绑定手机信息
     */
    registFunc.bindPhoneMsg = function () {

        var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

        var path = ctx + "/sys/regist/phoneBind";
        var ajax = new AJAXPacket(path, "正在绑定手机...请稍后");

        ajax.addJsonArray(jsonParam);
        Core.sendPacketJson(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var userId = packet.userId;
                Core.alert(message, 1, false, function () {
                    $("#user_id").val(userId);
                    //模拟点击第4步
                    globleIndex = 3;
                    $('.processorBox li').eq(globleIndex).click();
                    // base64 encrypt
                    var rawStr = 4 + "#" + userId;
                    var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                    var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                    var url = "/modules/filecoin/regist.html?value=" + base64;
                    window.location.href = url;
                });
            } else {
                Core.alert(message, 2, false, function () {

                });
            }
        }, false, true);
    };

    /**
     * 绑定矿机信息
     */
    registFunc.bindMinerMsg = function () {

        var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

        var path = ctx + "/sys/regist/minerInput";
        var ajax = new AJAXPacket(path, "正在完善矿工资料...请稍后");

        ajax.addJsonArray(jsonParam);
        Core.sendPacketJson(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var userId = packet.userId;
                var token = packet.token;
                var expire = packet.expire;
                var step = packet.step;

                Core.alert(message, 1, false, function () {
                    //登录
                    localStorage.removeItem("token");
                    localStorage.setItem("token", token);
                    // $("#user_id").val(userId);
                    // //模拟点击第5步
                    // globleIndex = 4;
                    // $('.processorBox li').eq(globleIndex).click();
                    // // base64 encrypt
                    // var rawStr = 5 + "#" + userId;
                    // var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                    // var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                    // var url = "/modules/filecoin/regist.html?value=" + base64;
                    // window.location.href = url;
                    var url = "/modules/filecoin/highway.html";
                    window.location.href = url;

                });
            } else {
                Core.alert(message, 2, false, function () {

                });
            }
        }, false, true);
    };

    /**
     * 通过用户id获取邀请码信息
     */
    registFunc.getInvCodeMsgByUser = function (userId) {
        var path = ctx + "/dinvitationcodeinfo/getInvitaCodeByUserId";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        ajax.add("userId", userId);
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var invitationCode = packet.invitationCode;
                $("#invitationCodeA").html(invitationCode);
            } else {
                Core.alert(message, 2, false, function () {

                });
            }
        }, false, false);
    };

    /**
     * 获取用户信息
     * @param userId
     */
    registFunc.getUserById = function (userId) {
        var path = ctx + "/sys/user/getUserInfoById/" + userId;
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        ajax.add("userId", userId);
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var user = packet.user;
                var mailUrl = packet.mailUrl;
                $("#registEmail").html(user.email);
                $('#regist_type').val("U");
                $("#user_mail").val(user.email);
                $("#user_id").val(userId);
                $("#mailUrl").val(mailUrl);
                //模拟点击第二步
                globleIndex = 1;
                $('.processorBox li').eq(globleIndex).click();
            } else {
                Core.alert(message, 2, false, function () {

                });
            }
        }, false, false);
    };

    /***
     * 初始化信息方法
     */
    registFunc.init = function () {
        //初始化
    };

    return registFunc;

})();

$(function () {

    RegistFunc.init();

    value = $.query.get("value");
    var parsedWordArray = CryptoJS.enc.Base64.parse(value);
    var plaintext = parsedWordArray.toString(CryptoJS.enc.Utf8);
    if (plaintext != '') {
        step = plaintext.split("#")[0];
        userId = plaintext.split("#")[1];
    }

    var nextBtn2 = $("#nextBtn2");
    nextBtn2.attr('onclick', 'javascript:return false;');
    nextBtn2.attr('style', 'opacity: 0.2');

    //返回修改邮件信息
    $("#resendMailBtn").click(function () {
        var obj = $(this)[0];
        RegistFunc.resendMail(obj);
    });

    //再换一张验证码
    $("#needCliekP").click(function () {
        LoginFunc.getCaptcha();
    });

    //换一张验证码
    $("#captcha_changeTipsHere").click(function () {
        LoginFunc.getCaptcha();
    });

    //重发邮件按钮点击事件
    $("#editMailUserBtn").click(function () {
        RegistFunc.getEditMailUser();
    });

    $(".changeVerifyCode").click(function () {
        $("#captchaImgId").attr("src", ctx + "/captcha.jpg?t=" + $.now());
    });

    //提交邮件注册基本信息
    $('#nextBtn').click(function () {
        $("#step1_frm").submit();
    });

    //发送手机验证码
    $("#phoneYzm_changeTipsHere").click(function () {
        var obj = $(this)[0];
        RegistFunc.sendSmsMessage(obj);
    });

    //验证矿工资料
    $('#finishedBtn').click(function () {
        $("#step4_frm").submit();
    });

    $("#step1_frm").validate({
        rules: {
            email: {
                required: true,
                email: true
            },
            vcode: {
                required: true
            },
            passwd: {
                required: true
            },
            passwd2: {
                required: true,
                equalTo: "#passwd"
            },
            captcha: {
                required: true
            }
        },
        messages: {
            email: {
                required: "请填写您的邮箱~",
                email: "您的邮箱格式错咯~"
            },
            vcode: {
                required: "请输入邀请码~"
            },
            passwd: {
                required: "请输入密码~"
            },
            passwd2: {
                required: "请再次输入您的密码~",
                equalTo: "两次密码输入不一致呢~"
            },
            captcha: {
                required: "请输入验证码~"
            }
        },
        submitHandler: function (form) {
            RegistFunc.ajaxBaseEmailReg();
            return false;
        }
    });

    //绑定手机信息验证
    $("#step3_frm").validate({
        rules: {
            trueName: {
                required: true,
                zh_and_en2: true
            },
            iccid: {
                required: true,
                isIdCode: true
            },
            phone: {
                required: true,
                isPhone: true
            },
            phoneYzm: {
                required: true
            }
        },
        messages: {
            trueName: {
                required: "请输入真实姓名~",
                zh_and_en2: "真实姓名只能输入中文和字母咯~"
            },
            iccid: {
                required: "请输入身份证号~",
                isIdCode: "请输入正确的身份证号码咯~"
            },
            phone: {
                required: "请输入手机号码~",
                isPhone: "请输入正确的手机号码~"
            },
            phoneYzm: {
                required: "请输入手机验证码~"
            }
        },
        submitHandler: function (form) {
            RegistFunc.smsMessageValide();
            return false;
        }
    });

    /**
     * 完善矿工信息
     */
    $("#step4_frm").validate({
        rules: {
            minerMachineAddr: {
                required: false
            },
            minerMachineEnv: {
                required: false
            },
            onLineTime: {
                required: false
            },
            storageLen: {
                required: false,
                integerA: true
            },
            bandWidth: {
                required: true,
                integerA: true
            }
        },
        messages: {
            minerMachineAddr: {},
            minerMachineEnv: {},
            onLineTime: {},
            storageLen: {
                integerA: "请选择存储大小~"
            },
            bandWidth: {
                required: "请进行在线测速,测试带宽~",
                integerA: "请进行带宽测速~"
            }
        },
        submitHandler: function (form) {
            // RegistFunc.bindMinerMsg();
            return false;
        }
    });

    //切换步骤（目前只用来演示）
    $('.processorBox li').click(function () {
        Core.closeTips();
        var i = $(this).index();
        if (i == globleIndex) {
            $('.processorBox li').removeClass('current').eq(i).addClass('current');
            $('.step').fadeOut(300).eq(i).fadeIn(500);
        }
    });

    //在线测速方法
    $("#bandWidth_changeTipsHere").click(function () {
        loadingIndex = Core.loading("正在测速...请稍后", 20000);
        bindTime = new Date();
        var szsrc = "http://hongkong2.bandwidthplace.com/static/4096.jpg?id=" + $.now();
        $("#bandWidthImg").attr("onload", "Core.bandWidthImgonLoad();");
        $("#bandWidthImg").attr("src", szsrc);
    });

    if (step != '' && userId != '') {
        //在这里跳转
        //模拟点击
        globleIndex = Number(step) - 1;
        $('.processorBox li').eq(globleIndex).click();
        $("#user_id").val(userId);

        //第二步加载用户其他信息
        if (step == "2") {
            //获取用户信息
            RegistFunc.getUserById(userId);
        }

        //第五步获取注册码
        if (step == "5") {
            RegistFunc.getInvCodeMsgByUser(userId);
        }
    } else {
        globleIndex = 0;
        $('.processorBox li').eq(globleIndex).click();
    }


    $("#openMailUrlBtn").click(function () {
        var url = $("#mailUrl").val();
        window.open("https://" + url, "_blank");
    });

    /**
     * 原页面自带
     */

    $.fn.iVaryVal = function (iSet, CallBack) {

        /*

         * Minus:点击元素--减小

         * Add:点击元素--增加

         * Input:表单元素

         * Min:表单的最小值，非负整数

         * Max:表单的最大值，正整数

         */

        iSet = $.extend({Minus: $('.J_minus'), Add: $('.J_add'), Input: $('.J_input'), Min: 0, Max: 2048}, iSet);

        var C = null, O = null;

//插件返回值

        var $CB = {};

//增加

        iSet.Add.each(function (i) {

            $(this).click(function () {

                O = parseInt(iSet.Input.eq(i).val());

                (O + 1 <= iSet.Max) || (iSet.Max == null) ? iSet.Input.eq(i).val(O + 1) : iSet.Input.eq(i).val(iSet.Max);

//输出当前改变后的值

                $CB.val = iSet.Input.eq(i).val();

                $CB.index = i;

//回调函数

                if (typeof CallBack == 'function') {

                    CallBack($CB.val, $CB.index);

                }

            });

        });

//减少

        iSet.Minus.each(function (i) {

            $(this).click(function () {

                O = parseInt(iSet.Input.eq(i).val());

                O - 1 < iSet.Min ? iSet.Input.eq(i).val(iSet.Min) : iSet.Input.eq(i).val(O - 1);

                $CB.val = iSet.Input.eq(i).val();

                $CB.index = i;

//回调函数

                if (typeof CallBack == 'function') {

                    CallBack($CB.val, $CB.index);

                }

            });

        });

//手动

        iSet.Input.bind({

            'click': function () {

                O = parseInt($(this).val());

                $(this).select();

            },

            'keyup': function (e) {

                if ($(this).val() != '') {

                    C = parseInt($(this).val());

//非负整数判断

                    if (/^[1-9]\d*|0$/.test(C)) {

                        $(this).val(C);

                        O = C;

                    } else {

                        $(this).val(O);

                    }

                }

//键盘控制：上右--加，下左--减

                if (e.keyCode == 38 || e.keyCode == 39) {

                    iSet.Add.eq(iSet.Input.index(this)).click();

                }

                if (e.keyCode == 37 || e.keyCode == 40) {

                    iSet.Minus.eq(iSet.Input.index(this)).click();

                }

//输出当前改变后的值

                $CB.val = $(this).val();

                $CB.index = iSet.Input.index(this);

//回调函数

                if (typeof CallBack == 'function') {

                    CallBack($CB.val, $CB.index);

                }

            },

            'blur': function () {

                $(this).trigger('keyup');

                if ($(this).val() == '') {

                    $(this).val(O);

                }

//判断输入值是否超出最大最小值

                if (iSet.Max) {

                    if (O > iSet.Max) {

                        $(this).val(iSet.Max);

                    }

                }

                if (O < iSet.Min) {

                    $(this).val(iSet.Min);

                }

//输出当前改变后的值

                $CB.val = $(this).val();

                $CB.index = iSet.Input.index(this);

//回调函数

                if (typeof CallBack == 'function') {

                    CallBack($CB.val, $CB.index);

                }

            }

        });

    }

    $('.i_box').iVaryVal({}, function (value, index) {

//网页显示以下内容，可以隐藏掉

        $('.i_tips').html('您的存储硬盘空间大小是：' + value + 'T');

    });

});