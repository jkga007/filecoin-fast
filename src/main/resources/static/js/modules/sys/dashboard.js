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
                        /**
                         * 循环增加实时信息
                         * <th>交易所</th>
                         <th data-priority="1">币种</th>
                         <th data-priority="2">价格</th>
                         <th data-priority="3">涨幅</th>
                         * @type {string}
                         */
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
                        var timestamps = tickers[i].timestamps;
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
        }, true,false);
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
});

Date.prototype.Format = function (fmt) { //
    var o = {
        "M+": this.getMonth() + 1, //Month
        "d+": this.getDate(), //Day
        "h+": this.getHours(), //Hour
        "m+": this.getMinutes(), //Minute
        "s+": this.getSeconds(), //Second
        "q+": Math.floor((this.getMonth() + 3) / 3), //Season
        "S": this.getMilliseconds() //millesecond
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() +

        "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1,

            (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};