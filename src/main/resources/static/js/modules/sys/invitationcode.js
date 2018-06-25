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

        var path = ctx + "/filecoin/dinvitationcodeinfo/getCountByInvitationCode";
        var ajax = new AJAXPacket(path, "正在执行...请稍后");
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;

            if (resultCode == "0") {
                var count = packet.count;
                var invitationCode = packet.invitationCode;
                var lotteryCode = packet.lotteryCode;
                var rate = packet.rate;
                var countlit = Number(10)-Number(count);

                $("#invitationCode_h3").html("邀请码："+invitationCode+" ");
                $("#invitationCount_h3").html("邀请人数："+count+" / 10");
                $("#invitationCountLit_h3").html(""+countlit);
                $("#invitationCode_h3_lit").html(""+countlit);
                $("#invitationCode_h3_lit_2").html(""+countlit);
                $("#invitationCode_h3_lit_3").html(""+countlit);
                $("#invitationRate_h3").html("执行费率："+rate);
                $("#invitationLotteryCode_h3").html("抽奖码："+lotteryCode);
                $("#invitationLotteryCode_h3_2").html("您的抽奖码为："+lotteryCode);

                // alert(count+"-"+invitationCode+"-"+lotteryCode+"-"+rate);
            }else{
                Core.alert("获取邀请码信息错误！", 2,false, function () {

                });
            }
        }, true,false);
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