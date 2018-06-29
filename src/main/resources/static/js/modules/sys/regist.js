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

        // var email = $.trim($('.email').val());
        // var vcode = $.trim($('#vcode').val());
        // var password = $.trim($('.passwd').val());
        // var captcha = $.trim($('.verifyCode').val());
        // var registType = $.trim($('#regist_type').val());
        // var userId = $.trim($('#user_id').val());

        var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

        var path = ctx + "/sys/regist/emailRegist";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        // ajax.add("email", email);
        // ajax.add("vcode", vcode);
        // ajax.add("password", password);
        // ajax.add("captcha", captcha);
        // ajax.add("registType", registType);
        // ajax.add("userId", userId);
        ajax.addJsonArray(jsonParam);
        Core.sendPacketJson(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            var data = packet.data;

            if (resultCode == "0") {
                var email = packet.registEmail;
                var userId = packet.userId;
                var mailUrl = packet.mailUrl;
                Core.alert("注册成功~ 点击进入下一步", 1,false, function () {
                    $("#registEmail").html(email);
                    $('#regist_type').val("U");
                    $("#user_mail").val(email);
                    $("#user_id").val(userId);
                    $("#mailUrl").val(mailUrl);
                    //模拟点击第二步
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            } else if(resultCode == "1"){
                var email = packet.registEmail;
                var userId = packet.userId;
                var mailUrl = packet.mailUrl;
                //待激活的信息,跳转激活页面(意外关闭页面或者怎么样)
                Core.alert(message, 2,false, function () {
                    $("#registEmail").html(email);
                    $('#regist_type').val("U");
                    $("#user_mail").val(email);
                    $("#user_id").val(userId);
                    $("#mailUrl").val(mailUrl);
                    //模拟点击第二步
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            }else {
                Core.alert(message, 2,false, function () {
                    getCaptcha();
                });
            }
        }, true,true);

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
        if(userMail.length != 0 && userId.length != 0){
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
                    getCaptcha();
                    $('#registType').val("U");

                } else {
                    Core.alert(message, 2,false, function () {
                    });
                }
            }, true,true);
        }else{
            Core.alert("您的操作有误,请重新注册!", 2,false, function () {

            });
        }
    };

    /**
     * 重新发送邮件信息
     */
    registFunc.resendMail = function (obj) {
        var _this = $(obj);
        var times = 1 * 60;
        _this.html(times + '秒后可以重新发送');
        var timer = setInterval(function(){
            times--;
            _this.html(times + '秒后可以重新发送');
            if(times == 0){
                _this.html('重新发送');
                _this.attr('onclick','RegistFunc.resendMail(this);');
                _this.attr('style','');
                clearInterval(timer);
            }
        },1000);
        _this.attr('onclick','javascript:return false;');
        _this.attr('style','opacity: 0.2');
        /**
         * 发送邮件
         */
        var userMail = $.trim($("#user_mail").val());
        var userId = $.trim($("#user_id").val());
        if(userMail.length != 0 && userId.length != 0){
            var path = ctx + "/sys/resendMail";
            var ajax = new AJAXPacket(path, "正在重新发送邮件...请稍后");

            ajax.add("userMail", userMail);
            ajax.add("userId", userId);

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    Core.alert("重新发送邮件成功!,请注意查收!", 1,false, function () {
                    });
                } else {
                    Core.alert(message, 2,false, function () {
                    });
                }
            }, true,true);
        }else{
            Core.alert("您的操作有误,请重新注册!", 2,false, function () {

            });
        }
    };

    /**
     * 注册手机信息
     */
    registFunc.ajaxPhoneMsgReg = function () {

        var email = $.trim($('.email').val());
        var vcode = $.trim($('#vcode').val());
        var password = $.trim($('.passwd').val());
        var captcha = $.trim($('.verifyCode').val());
        var registType = $.trim($('#regist_type').val());
        var userId = $.trim($('#user_id').val());

        var path = ctx + "/sys/regist";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        ajax.add("email", email);
        ajax.add("vcode", vcode);
        ajax.add("password", password);
        ajax.add("captcha", captcha);
        ajax.add("registType", registType);
        ajax.add("userId", userId);
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            var data = packet.data;

            if (resultCode == "0") {
                var email = packet.registEmail;
                var userId = packet.userId;
                var mailUrl = packet.mailUrl;
                Core.alert("注册成功~ 点击进入下一步", 1,false, function () {
                    $("#registEmail").html(email);
                    $('#regist_type').val("U");
                    $("#user_mail").val(email);
                    $("#user_id").val(userId);
                    $("#mailUrl").val(mailUrl);
                    //模拟点击第二步
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            } else {
                Core.alert(message, 2,false, function () {
                    getCaptcha();
                });
            }
        }, true,true);

    };

    //动态验证开启
    registFunc.dynamicValidate = function(objId){
        switch (objId) {
            case "email":
                $("#"+objId).rules("add", {
                    remote: {
                        //验证邮件是否已注册
                        type: "post",
                        url: "/regist/doValidate/" + objId,
                        cache: false,
                        data: {
                            email: function () {
                                return $("#"+objId).val();
                            }
                        }
                    },
                    messages: {remote: "该邮件已被注册~请更换~"}
                });
                break;
            case "vcode":
                $("#"+objId).rules("add", {
                    remote: {
                        //验证编码是否重复
                        type: "post",
                        url: "/regist/doValidate/" + objId,
                        cache: false,
                        data: {
                            vcode: function () {
                                return $("#"+objId).val();
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

    registFunc.onblurRemoveRules = function(){
        $("#email").keyup(function(){
            $("#email").rules("remove", "remote");
        });
        $("#vcode").keyup(function(){
            $("#vcode").rules("remove", "remote");
        });
    };

    var closeTipId;
    registFunc.sendSmsMessage = function(obj){

        var _this = $(obj);
        var phone = $.trim($('#phone').val());
        var userId = $.trim($('#user_id').val());

        if(phone.length == 0){
            closeTipId = Core.showTips($("#phone"),"请输入手机号码~");
            return false;
        }else{
            var times = 1 * 4;
            _this.val(times + '秒后可以重新发送');
            var timer = setInterval(function(){
                times--;
                _this.val(times + '秒后可以重新发送');
                if(times == 0){
                    _this.val('重新发送');
                    _this.attr('disabled',false);
                    clearInterval(timer);
                }
            },1000);
            _this.attr('disabled','disabled');

            Core.close(closeTipId);
        }

        if(phone.length != 0 && userId.length != 0){
            var path = ctx + "/filecoin/wsendmessage/save/"+phone+"/"+userId;
            var ajax = new AJAXPacket(path, "正在发送短信...请稍后");

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    Core.alert("发送成功~ ", 1,false, function () {
                        var nextBtn2 = $("#nextBtn2");
                        nextBtn2.attr('onclick','javascript:$("#step3_frm").submit();');
                        nextBtn2.attr('style','');
                    });
                } else {
                    Core.alert(message, 2,false, function () {
                        // var nextBtn2 = $("#nextBtn2");
                        // nextBtn2.attr('onclick','javascript:return false;');
                        // nextBtn2.attr('style','opacity: 0.2');
                    });
                }
            }, true,true);
        }
    };

    //验证手机验证码是否正确
    registFunc.smsMessageValide = function(){

        var phone = $.trim($('#phone').val());
        var phoneYzm = $.trim($('#phoneYzm').val());
        var userId = $.trim($('#user_id').val());
        if(phone.length != 0 && phoneYzm.length != 0 && userId.length != 0){
            var path = ctx + "/filecoin/wsendmessage/validate/"+phone+"/"+userId+"/"+phoneYzm;
            var ajax = new AJAXPacket(path, "正在验证手机验证码...请稍后");

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {

                    //提交绑定手机
                    RegistFunc.bindPhoneMsg();

                } else {
                    Core.alert(message, 2,false, function () {

                    });
                }
            }, true,true);
        }
    };

    registFunc.bindPhoneMsg = function(){

        var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

        var path = ctx + "/sys/regist/phoneBind";
        var ajax = new AJAXPacket(path, "正在绑定手机...请稍后");

        ajax.addJsonArray(jsonParam);
        Core.sendPacketJson(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var userId = packet.userId;
                Core.alert(message, 1,false, function () {
                    $("#user_id").val(userId);
                    //模拟点击第4步
                    globleIndex = 3;
                    $('.processorBox li').eq(globleIndex).click();
                    // base64 encrypt
                    var rawStr = 4+"#"+userId;
                    var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                    var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                    var url = "/modules/filecoin/regist.html?value="+base64;
                    window.location.href = url;
                });
            } else {
                Core.alert(message, 2,false, function () {

                });
            }
        }, true,true);
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
    // RegistFunc.onblurRemoveRules();

    // $("#step3_frm").submit();
    var nextBtn2 = $("#nextBtn2");
    nextBtn2.attr('onclick','javascript:return false;');
    nextBtn2.attr('style','opacity: 0.2');

    //重发邮件按钮点击事件
    $("#resendMailBtn").click(function(){
        var obj = $(this)[0];
        RegistFunc.resendMail(obj);
    });

    $(".changeVerifyCode").click(function(){
        $("#captchaImgId").attr("src",ctx+"/captcha.jpg?t=" + $.now());
    });

    //AJAX提交以及验证表单
    $('#nextBtn').click(function(){
        // RegistFunc.dynamicValidate("email");
        // RegistFunc.dynamicValidate("vcode");
        $("#step1_frm").submit();
    });

    // //验证手机号码
    // $('#nextBtn2').click(function(){
    //     // $("#step3_frm").submit();
    //     var nextBtn2 = $("#nextBtn2");
    //     nextBtn2.attr('onclick','javascript:return false;');
    //     nextBtn2.attr('style','opacity: 0.2');
    // });

    //发送手机验证码
    $("#phoneYzm_changeTipsHere").click(function(){
        var obj = $(this)[0];
        RegistFunc.sendSmsMessage(obj);
    });

    //验证矿工资料
    $('#finishedBtn').click(function(){
        $("#step4_frm").submit();
    });

    $("#step1_frm").validate({
        rules: {
            email: {
                required: true,
                email:true
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
                email:"您的邮箱格式错咯~"
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
        submitHandler:function(form){

            RegistFunc.ajaxBaseEmailReg();

            return false;
        }
    });

    $("#step3_frm").validate({
        rules: {
            trueName: {
                required: true
            },
            iccid: {
                required: true
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
                required: "请输入真实姓名~"
            },
            iccid: {
                required: "请输入身份证号~"
            },
            phone: {
                required: "请输入手机号码~",
                isPhone: "请输入正确的手机号码~"
            },
            phoneYzm: {
                required: "请输入手机验证码~"
            }
        },
        submitHandler:function(form){

            RegistFunc.smsMessageValide();

            return false;
        }
    });


    $("#step4_frm").validate({
        rules: {
            minerMachineAddr: {
                required: true
            }
        },
        messages: {
            minerMachineAddr: {
                required: "请输入矿机位置"
            }
        },
        submitHandler:function(form){

            var jsonParam = Core.serializeJsonStr("mainForm,step1_frm,step3_frm,step4_frm");

            var path = ctx + "/sys/regist/minerInput";
            var ajax = new AJAXPacket(path, "正在执行...请稍后");

            ajax.addJsonArray(jsonParam);
            Core.sendPacketJson(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;

                if (resultCode == "0") {
                    var userId = packet.userId;
                    var token = packet.token;
                    var expire = packet.expire;

                    Core.alert(message, 1,false, function () {
                        $("#user_id").val(userId);
                        //模拟点击第5步
                        globleIndex = 4;
                        $('.processorBox li').eq(globleIndex).click();
                        //登录
                        localStorage.removeItem("token");
                        localStorage.setItem("token",token);
                        // base64 encrypt
                        var rawStr = 5+"#"+userId;
                        var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                        var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                        var url = "/modules/filecoin/regist.html?value="+base64;
                        window.location.href = url;
                    });
                } else {
                    Core.alert(message, 2,false, function () {

                    });
                }
            }, true,true);

            return false;
        }
    });

    //切换步骤（目前只用来演示）
    $('.processorBox li').click(function(){
        Core.closeTips();
        var i = $(this).index();
        if(i == globleIndex){
            $('.processorBox li').removeClass('current').eq(i).addClass('current');
            $('.step').fadeOut(300).eq(i).fadeIn(500);
        }
    });

    if(step != '' && userId != ''){
        //在这里跳转
        //模拟点击
        globleIndex = Number(step) - 1;
        $('.processorBox li').eq(globleIndex).click();
        $("#user_id").val(userId);
        //第五步获取注册码
        if(step == "5"){

            var path = ctx + "/filecoin/dinvitationcodeinfo/getInvitaCodeByUserId";
            var ajax = new AJAXPacket(path, "正在执行...请稍后");

            ajax.add("userId", userId);
            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;

                if (resultCode == "0") {
                    var invitationCode = packet.invitationCode;
                    $("#invitationCodeA").html(invitationCode);
                } else {
                    Core.alert(message, 2,false, function () {

                    });
                }
            }, true,false);
        }
    }else{
        globleIndex = 0;
        $('.processorBox li').eq(globleIndex).click();
    }

});