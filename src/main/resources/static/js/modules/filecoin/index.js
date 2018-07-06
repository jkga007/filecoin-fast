/**
 * 20180620
 * @author g20416
 */
var IndexFunc = (function () {
    var indexFunc = {};

    indexFunc.getCounts = function () {
        //获取各类数据count
        var path = "/sysUserExtend/getSomeCount";
        var ajax = new AJAXPacket(path, "正在查询count数据...请稍后");

        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            if (resultCode == "0") {
                var pcs = packet.pcs;
                var bindwidth = packet.bindwidth;
                var storageLen = packet.storageLen;
                var arr = new Array();
                arr.push(pcs+"pcs");
                arr.push(bindwidth+"MBps");
                arr.push(storageLen+"T");
                $(".nodecolor").each(function(i){
                    $(this).html(arr[i]+"");
                });

            } else {
            }
        }, false, false);
    };
    /***
     * 初始化信息方法
     */
    indexFunc.init = function () {
        //初始化
    };

    return indexFunc;

})();
$(function () {
    IndexFunc.init();
    IndexFunc.getCounts();
    $(".custom-btn").click(function(){
        window.location.href = "/modules/filecoin/regist.html";
    });
});