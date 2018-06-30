/**
 * 用户面板相关方法
 * 20180620
 * @author g20416
 */


var DashboardFunc = (function () {
    var dashboardFunc = {};

    /**
     * 获取交易所实时价格方法
     */
    dashboardFunc.getTickers = function () {
        var path = ctx + "/sys/getCoinTickers";
        var ajax = new AJAXPacket(path, "正在查询实时信息...请稍后");
        Core.sendPacket(ajax, function (packet) {
            var resultCode = packet.code;
            var message = packet.msg;
            if (resultCode == "0") {
                var coinData = packet.coinData;
                var jsondata = $.parseJSON(coinData);
                var code = jsondata.code;
                var message = jsondata.message;
                if(code == "0"){
                    $("#coinTickersTbody").empty();
                    var tickers = jsondata.data.tickers;
                    for (var i = 0; i < tickers.length ; i++) {
                        //涨幅
                        var change1d = tickers[i].change1d;
                        var change1dHtml = "";
                        if((""+change1d).indexOf("-") != -1){
                            change1dHtml = "<font color='green'>"+change1d+"%</font>";
                        }else{
                            change1dHtml = "<font color='red'>"+change1d+"%</font>";
                        }
                        //交易所
                        var exchange_display_name = tickers[i].exchange_display_name;
                        //币种
                        var display_pair_name = tickers[i].display_pair_name;
                        //价格
                        var price = (tickers[i].price+"").substring(0,6);
                        //更新时间
                        // var timestamps = tickers[i].timestamps;
                        // var timenow = new Date(timestamps).Format("yyyy-MM-dd hh:mm:ss");
                        var innerHtml = "<tr>";
                        innerHtml += "<th><i class=\"fa fa-dot-circle-o complete\"></i>"+exchange_display_name+"</th>";
                        innerHtml += "<td>"+display_pair_name+"</td>";
                        innerHtml += "<td>$"+price+"</td>";
                        innerHtml += "<td>"+change1dHtml+"</td>";
                        // innerHtml += "<td>"+timenow+"</td>";
                        innerHtml += "</tr>";

                        $("#coinTickersTbody").append(innerHtml);
                    }
                }else{
                    Core.alert(message, 2,false, function () {
                    });
                }

            }else{
                Core.alert(message, 2,false, function () {
                });
            }
        }, false, false);
    };

    /***
     * 初始化信息方法
     */
    dashboardFunc.init = function () {
        //初始化
    };

    return dashboardFunc;

})();
$(function () {
    DashboardFunc.init();

    LoginFunc.getLoginUser();
    DashboardFunc.getTickers();
});