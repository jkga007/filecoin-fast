/**
 * 登录相关方法
 * 20180620
 * @author g20416
 */


var LoginFunc = (function () {
    var loginFunc = {};
    /**
     * 登录方法
     */

    loginFunc.doLogin = function (jsonParam) {

        var path = ctx + "/sys/login";
        var ajax = new AJAXPacket(path, "正在登录...请稍后");

        ajax.addJsonArray(jsonParam);

        Core.sendPacketJson(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            if (resultCode == "0") {
                localStorage.removeItem("token");
                localStorage.setItem("token",packet.token);
                // window.location.href = ctx + "/sys/gomasterindex";
                window.location.href = ctx + "/modules/filecoin/index-dashboard.html";
            } else if (resultCode == "2") {
                var userId = packet.userId;
                var step = packet.step;
                Core.alert(message, 1, false, function () {
                    $("#user_id").val(userId);
                    //模拟点击下一步
                    globleIndex = Number(step)-1;
                    $('.processorBox li').eq(globleIndex).click();
                    // base64 encrypt
                    var rawStr = step + "#" + userId;
                    var wordArray = CryptoJS.enc.Utf8.parse(rawStr);
                    var base64 = CryptoJS.enc.Base64.stringify(wordArray);
                    var url = "/modules/filecoin/regist.html?value=" + base64;
                    window.location.href = url;
                });
            } else {
                Core.alert(message, 2,false, function () {
                    // window.location.href = ctx + "/modules/filecoin/login.html";
                });
            }
        }, true,true);
    };

    loginFunc.getLoginUser = function () {

        var path = ctx + "/sys/user/info";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            if (resultCode == "0") {
                var userObj = packet.user;
                var sysUserExtend = packet.sysUserExtend;
                $("#userNameo").html(userObj.username);
                $("#trueName").val(sysUserExtend.trueName);
                $("#userMailInner").val(userObj.email);
                $("#mobilePhone").val(userObj.mobile);
                $("#iccid").val(sysUserExtend.iccid);
                $("#userMailInnerSmall").html("您认证的邮箱地址："+userObj.email+" 已通过认证！");
                $("#userPhoneSmall").html("您认证的手机号码："+userObj.mobile+" 已通过认证！");
            }else{
                window.location.href = ctx + "/modules/filecoin/login.html";
            }
        }, false, false);
    };

    loginFunc.doLogout = function () {
        var token = localStorage.getItem("token");
        if (token != 'null') {
            //同步退出
            var path = ctx + "/sys/logout";
            var ajax = new AJAXPacket(path, "正在退出...请稍后");

            Core.sendPacket(ajax, function (packet) {
                var resultCode = packet.code;
                var message = packet.msg;
                if (resultCode == "0") {
                    localStorage.removeItem("token");
                    window.location.href = ctx + "/modules/filecoin/login.html";
                } else {

                }
            }, false, true);
        } else {
            localStorage.removeItem("token");
            window.location.href = ctx + "/modules/filecoin/login.html";
        }
    };

    loginFunc.getCaptcha = function(){
        $("#captchaImgId").attr("src","/captcha.jpg?t=" + $.now());
    };

    /***
     * 初始化信息方法
     */
    loginFunc.init = function () {
        //初始化
    };

    return loginFunc;

})();
$(function () {
    LoginFunc.init();

    $("#captcha_changeTipsHere").click(function () {
        LoginFunc.getCaptcha();
    });

    $("#msg_validate").validate({
        rules: {
            username: {
                required: true,
                email: true
            },
            password: {
                required: true
            },
            captcha: {
                required: true
            }
        },
        messages: {
            username: {
                required: "请输入邮箱地址~",
                email: "您的邮箱格式错咯~"
            },
            password: {
                required: "请输入密码~"
            },
            captcha: {
                required: "请输入验证码~"
            }
        },
        submitHandler: function () {
            var jsonParam = Core.serializeJsonStr("msg_validate");
            LoginFunc.doLogin(jsonParam);
        }
    });

    $("#captcha").keyup(function(event){
        if(event.keyCode ==13){
            $("#msg_validate").submit();
        }
    });

    $("#doLoginBtn").click(function(){
        $("#msg_validate").submit();
    });
    $("#logoutBtn").click(function(){
        LoginFunc.doLogout();
    });
});