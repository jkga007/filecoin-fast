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

        var email = $.trim($('.email').val());
        var vcode = $.trim($('#vcode').val());
        var password = $.trim($('.passwd').val());
        var captcha = $.trim($('.verifyCode').val());

        var path = ctx + "/sys/regist";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");

        ajax.add("email", email);
        ajax.add("vcode", vcode);
        ajax.add("password", password);
        ajax.add("captcha", captcha);
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            var data = packet.data;
            var email = packet.registEmail;
            if (resultCode == "0") {
                Core.alert("提交成功~ 点击进入下一步", 1,false, function () {
                    $("#registEmail").html(email);
                    globleIndex = 1;
                    $('.processorBox li').eq(globleIndex).click();
                });
            } else {
                Core.alert("错误代码："+resultCode+",错误信息："+message, 2,false, function () {

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
});