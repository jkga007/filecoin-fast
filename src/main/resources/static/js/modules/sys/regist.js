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
                    getCaptcha();
                });
            }
        }, true,true);

    };

    /**
     * 返回修改基本信息
     */
    registFunc.returnEditMail = function () {

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