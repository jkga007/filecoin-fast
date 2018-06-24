/**
 * 注册验证方法
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
                window.location.href = ctx + "/sys/gomasterindex";
            } else {
                Core.alert("账号或密码错误！", 2,false, function () {
                    window.location.href = ctx + "/sys/gologin";
                });
            }
        }, true,true);


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
});