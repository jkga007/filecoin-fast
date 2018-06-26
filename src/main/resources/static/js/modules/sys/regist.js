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
                Core.alert("提交成功~ 点击进入下一步", 1,false, function () {
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