/**
 * 邀请码相关方法
 * 20180620
 * @author g20416
 */

var InvitationCodeFunc = (function () {
    var invitationCode = {};

    /**
     * 邀请码相关信息
     */
    invitationCode.getCountByInvitationCode = function () {

        var path = ctx + "/dinvitationcodeinfo/getCountByInvitationCode";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                //邀请人数
                var count = packet.count;
                //邀请码
                var invitationCode = packet.invitationCode;
                //抽奖码
                var lotteryCode = packet.lotteryCode;
                //执行费率
                var rate = packet.rate;
                //还剩余多少人
                var countlit = Number(10) - Number(count);

                //邀请码DIV
                var invCodeDiv = $("#invCodeDiv");
                //邀请人数DIV
                var invCountDiv = $("#invCountDiv");
                //折扣券DIV
                var invZkjDiv = $("#invZkjDiv");
                //执行费率DIV
                var invZxflDiv = $("#invZxflDiv");
                //抽奖码DIV
                var invCjmDiv = $("#invCjmDiv");
                //矿机抽奖DIV
                var invKJCJDiv = $("#invKJCJDiv");
                //如果邀请人数完成
                if (Number(countlit) == 0) {
                    invCountDiv.removeClass("has-gray-to-right-bottom");
                    invCountDiv.addClass("has-register-to-right-bottom");
                    invZkjDiv.removeClass("has-gray-to-right-bottom");
                    invZkjDiv.addClass("has-register-to-right-bottom");
                    invZxflDiv.removeClass("has-gray-to-right-bottom");
                    invZxflDiv.addClass("has-register-to-right-bottom");
                    invCjmDiv.removeClass("has-gray-to-right-bottom");
                    invCjmDiv.addClass("has-register-to-right-bottom");
                }

                //赋值邀请码
                invCodeDiv.find("div").eq(1).find("h3").eq(0).html("邀请码：" + invitationCode + " ");
                //赋值邀请码剩余次数
                invCodeDiv.find("div").eq(1).find("span").eq(0).find("span").eq(0).html(countlit);
                //赋值邀请人数
                invCountDiv.find("div").eq(1).find("h3").eq(0).html("邀请人数：" + count + " / 10");
                //赋值邀请码剩余次数
                invCountDiv.find("div").eq(1).find("span").eq(0).find("span").eq(0).html(countlit);
                //赋值折扣卷剩余次数
                invZkjDiv.find("div").eq(1).find("span").eq(0).find("span").eq(0).html(countlit);
                //赋值执行费率
                invZxflDiv.find("div").eq(1).find("h3").eq(0).html("执行费率：" + rate);

                //赋值抽奖码
                invCjmDiv.find("div").eq(1).find("h3").eq(0).html("抽奖码：" + invitationCode + " ");
                //赋值抽奖码剩余次数
                invCjmDiv.find("div").eq(1).find("span").eq(0).find("span").eq(0).html(countlit);
                //赋值矿机抽奖抽奖码
                invKJCJDiv.find("h4").eq(0).html("您的抽奖码为：" + invitationCode);

            } else {
                Core.alert("获取邀请码信息错误！", 2, false, function () {

                });
            }
        }, false, false);
    };

    /***
     * 初始化信息方法
     */
    invitationCode.init = function () {
        //初始化
    };

    return invitationCode;

})();
$(function () {
    InvitationCodeFunc.init();
});