/**
 * 20180620
 * @author g20416
 */
var SettingsFunc = (function () {
    var settingsFunc = {};

    /**
     * 个人信息修改密码方法
     * 1.记录修改表
     * 2.记录邮件表,定时任务发送邮件
     * 3.邮件确认后真正修改密码
     * 4.15分钟不用邮件确认,则此次密码修改失效
     */
    settingsFunc.editPassWord = function(){
        var jsonParam = Core.serializeJsonStr("pass_validate");

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

                });
            } else {
                Core.alert(message, 2, false, function () {
                    LoginFunc.getCaptcha();
                });
            }
        }, false, true);
    };

    /***
     * 初始化信息方法
     */
    settingsFunc.init = function () {
        //初始化
    };

    return settingsFunc;

})();
$(function () {
    SettingsFunc.init();
    LoginFunc.getLoginUser();

    $("#pass_validate").validate({
        rules: {
            oldPassWord: {
                required: true
            },
            newPassWord: {
                required: true,
                notEqualTo:"#oldPassWord"
            },
            newPassWordConfirm: {
                required: true,
                equalTo: "#newPassWord"
            }
        },
        messages: {
            oldPassWord: {
                required: true
            },
            newPassWord: {
                required: "请输入新密码~",
                notEqualTo:"新旧密码不能一样咯~"
            },
            newPassWordConfirm: {
                required: "请再次输入密码~",
                equalTo: "两次输入的密码不一样咯~"
            }
        },
        submitHandler: function () {

        }
    });

});