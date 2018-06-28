var Core = Core
    || (function () {

        var core = {};

        /**
         * 将jquery对象转换成json串
         * @param jqObj
         * @returns {{}}
         */
        core.serializeJsonStr = function (jqObj) {
            var o = {};
            var objArr = new Array();
            // if(jqObj.indexOf(",") != -1){
            objArr = jqObj.split(",");
            // }
            for(var i=0;i<objArr.length;i++){
                var a = $("#"+objArr[i]).serializeArray();
                $.each(a, function () {
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [o[this.name]];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
            }
            return o;
        };

        /*睡眠多久*/
        core.sleepMillis = function (numberMillis) {
            var now = new Date();
            var exitTime = now.getTime() + numberMillis;
            while (true) {
                now = new Date();
                if (now.getTime() > exitTime)
                    return;
            }
        };

        core.init = function () {

            core.resizeIframe();

            $(window).resize(function () {
                core.resizeIframe();
            });
        };

        //获取i18n信息方法
        core.getI18n = function (key) {
            var result, parentI18nVal, grandpaI18nVal, i18nVal;
            i18nVal =key;
            return key;

        };

        // 打开遮罩
        core.loading = function (info, times) {
            if (typeof(times) == "undefined") {
                times = 100000;
            }
            _layerIndex = layer.msg(info, {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, time: times});
            return _layerIndex;
        };

        core.confirm = function (msg, confirmFunc, cancelFunc) {
            layer.confirm(msg, {
                btn: [Core.getI18n("confirm"), Core.getI18n("cancel")]
                // 按钮
            }, function (index) {
                confirmFunc();
                layer.close(index);
            }, function (index) {
                cancelFunc();
                layer.close(index);
            });
        };

        core.noF5 = function (flag) {
            if (flag) {
                //屏蔽F5
                document.onkeydown = function (e) {
                    e = window.event || e;
                    var keycode = e.keyCode || e.which;
                    if (e.ctrlKey || e.altKey || e.shiftKey
                        || keycode >= 112 && keycode <= 123) {
                        if (window.event) {// ie
                            try {
                                e.keyCode = 0;
                            } catch (e) {
                            }
                            e.returnValue = false;
                        } else {// ff
                            e.preventDefault();
                        }
                    }
                }
            }

        };

        core.noRightBtn = function (flag) {
            if (flag) {
                //禁止鼠标右键菜单
                document.oncontextmenu = function (e) {
                    return false;
                }
            }

        };

        core.layerSkinConfig = function () {
            layer.config({
                //extend: 'myskin/style.css', //加载新皮肤
                skin: 'layui-layer-molv' //一旦设定，所有弹层风格都采用此主题。
            });
        };

        core.showDialog = function (msg, type, timeout) {
            //var typeN = null;
            //var timeoutN = null;
            if (typeof(type) == "undefined") {
                type = 1;
            }
            if (typeof(timeout) == "undefined") {
                timeout = 1000;
            }
            layer.msg(msg, {
                icon: type, // 1：对钩，2：叉叉，3：问号，4：锁符号，5：哭脸，6：笑脸
                time: timeout
//					shade : [ 0.3, '#000' ],
//					offset : '70px'
            }, function () {

            });
        };
        core.showTips = function (obj, msg, i18nFlag, times) {
            var tip_index;
            if (typeof(times) == "undefined") {
                times = 100000;
            }
            if (!i18nFlag) {
                i18nFlag = false;
            }
            if (i18nFlag) {
                msg = msg;
            }
            tip_index = layer.tips(msg, obj, {
                time: times,
                tipsMore: true,
                style: ['background-color:#0FA6D8; color:#fff', '#0FA6D8'],
                maxWidth: 250
            });
            return tip_index;
        };

        core.closeTips = function () {
            layer.closeAll('tips');
        };

        core.close = function (_layerIndex) {
            layer.close(_layerIndex);
        };

        core.alert = function (msg, type, i18nFlag, retFunc) {
            if (typeof(type) == "undefined") {
                type = 2;
            }
            if (!i18nFlag) {
                i18nFlag = false;
            }
            if (i18nFlag) {
                msg = msg;
            }
            layer.alert(msg, {icon: type}, function (index) {
                if (retFunc) {
                    retFunc();
                }
                layer.close(index);
            });
        };
        core.alert_I18N_Msg = function (msg, type, oldStr, newStr) {
            if (typeof(type) == "undefined") {
                type = 2;
            }
            if (typeof(oldStr) == "undefined" && typeof(newStr) == "undefined") {
                oldStr = "";
                newStr = "";
            }

            var errorMsgi18n = msg;
            if (typeof(oldStr) != "undefined" && typeof(newStr) != "undefined") {
                if (oldStr.indexOf("|") != -1) {
                    var oldStrArray = oldStr.split("|");
                    var newStrArray = newStr.split("|");
                    for (var i = 0; i < oldStrArray.length; i++) {
                        errorMsgi18n = errorMsgi18n.replace(oldStrArray[i], newStrArray[i]);
                    }
                }
            }
            layer.alert(errorMsgi18n, {icon: type}, function (index) {
                layer.close(index);
            });
        };

        /*ajax 方法*/
        core.sendPacket = function (packet, process, aysncflag,loadingflag, timeouts) {
            var _layerIndex;

            if (!process) {
                process = doProcess;
            }

            if (!aysncflag) {
                aysncflag = false;
            }
            if (!loadingflag) {
                loadingflag = false;
            }
            if (!timeouts) {
                timeouts = 1000 * 15 * 60;//15分钟
            }

            $.ajax({
                url: packet.url,
                timeout: timeouts,
                data: packet.data,
                type: "POST",
                async: aysncflag,
                dataType: "html",
                cache: false,
                beforeSend:function(){
                    if(loadingflag){
                        _layerIndex = layer.msg(packet.statusText, {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, time: 1000000});
                    }
                },
                success: function (data, textStatus, xhr) {
                    var response;
                    if (data) {
                        response = $.parseJSON(data);
                    }
                    process(response);

                },
                error: function (xhr, status, error) {
                    if (xhr.status == "404") {
                        core.alert(Core.getI18n("addressNotExist"));
                    } else if (xhr.status == "500") {
                        core.alert(Core.getI18n("compileWrong"));
                    } else {
                        core.alert(Core.getI18n("systemWrongPleaseRefresh"));
                    }
                },
                complete: function (xhr) {
                    if(loadingflag) {
                        core.close(_layerIndex);
                    }
                    var jsondata = $.parseJSON(xhr.responseText);
                    //token过期，则跳转到登录页面
                    if(jsondata.code == 401){
                        window.location.href = ctx + "/modules/filecoin/login.html";
                    }
                }
            });
        };


        /**
         * ajax请求并返回结果
         * @param url
         * @param data
         * @param callback
         * @returns {String}
         */
        core.ajaxFunc = function (packet, dataType) {

            var _load;

            if (!dataType) {
                dataType = "html";
            }

            var result = "";
            $.ajax({
                type: "post",
                url: packet.url,
                data: packet.data,
                dataType: dataType,
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                async: false,
                cache: false,
                success: function (response) {
                    result = response;
                }, error: function (xhr, status, error) {
                    //core.close(_layerIndex);
                    if (xhr.status == "404") {
                        core.alert(Core.getI18n("addressNotExist"));
                    } else if (xhr.status == "500") {
                        core.alert(Core.getI18n("compileWrong"));
                    } else {
                        core.alert(Core.getI18n("systemWrongPleaseRefresh"));
                    }

                },
                beforeSend: function () {
                    _load = Core.loading(Core.getI18n("loading"));
                    // setTimeout(function () { },200);
                },
                complete: function (xhr) {

                    setTimeout(function () {
                        Core.close(_load);
                    }, 300);
                    //token过期，则跳转到登录页面
                    if(xhr.responseJSON.code == 401){
                        parent.location.href = ctx + 'login.html';
                    }

                },
            });
            return result;
        };
        /**
         * ajax请求url替换指定div
         * @param divId 返回替换div
         * @param url 请求地址
         * @param data 参数
         * @param callback 回调
         */
        core.ajaxDiv = function (divId, packet) {
            var result = core.ajaxFunc(packet, "html");
            $(divId).html(result);
        };

        // Json ajax
        core.sendPacketJson = function (packet, process, aysncflag, loadingFlag, timeouts) {
            var _layerIndex;

            if (typeof(loadingFlag) == "undefined") {
                loadingFlag = true;
            }

            if (loadingFlag) {
                _layerIndex = core.loading(packet.statusText);
            }

            if (!process) {
                process = doProcess;
            }

            if (!aysncflag) {
                aysncflag = false;
            }
            if (!timeouts) {
                timeouts = 1000 * 15 * 60;//15分钟
            }

            var dataString = '';
            if (packet.jsonArray.length == 0) {
                dataString = JSON.stringify(packet.data);
            } else {
                dataString = JSON.stringify(packet.jsonArray);
            }
            var tmpUrl = packet.url;
            if (tmpUrl.indexOf("?") > 0) {
                var params = tmpUrl.substring(tmpUrl.indexOf("?") + 1,
                    tmpUrl.length);
                tmpUrl = tmpUrl.substring(0, tmpUrl.indexOf("?") + 1)
                    + core.encodeURIParamsC(params);
            }
            $.ajax({
                url: tmpUrl,
                timeout: timeouts,
                data: dataString,
                type: "POST",
                async: aysncflag,
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                cache: false,
                success: function (data, textStatus, xhr) {
                    // var response = $.parseJSON(data);
                    process(data);

                },
                error: function (xhr, status, error) {
                    //core.close(_layerIndex);
                    if (xhr.status == "404") {
                        core.alert(Core.getI18n("fileNotExist"));
                    } else if (xhr.status == "500") {
                        core.alert(Core.getI18n("compileWrong"));
                    } else {
                        core.alert(Core.getI18n("systemWrong"));
                    }

                },
                complete: function (xhr) {
                    if (loadingFlag) {
                        core.close(_layerIndex);
                    }
                    console.log(xhr.responseJSON);
                    //token过期，则跳转到登录页面
                    // if(xhr.responseJSON.code == 401){
                    //     parent.location.href = ctx + 'login.html';
                    // }
                }
            });
        };


        core.encodeURIParams = function (value) {
            var components = value.split('&');
            for (var i = 0; i < components.length; i++) {
                components[i] = encodeURIComponent(components[i]);
            }
            return components.join('&');
        };

        core.encodeURIParamsC = function (value) {
            var components = value.split('&');
            for (var i = 0; i < components.length; i++) {
                var array = components[i].split('=');
                array[1] = encodeURIComponent(array[1]);
                components[i] = array.join('=');
            }
            return components.join('&');
        };


        // frame等分
        core.resizeIframe = function () {
            var w = $(window).width();
            var h = $(window).height();
            $('iframe[id^="iframe"]').each(
                function (e, i) {
                    var percents = parseFloat('0.'
                        + $(this).attr('id').substr(6));
                    var heightPercents = parseFloat('0.'
                        + $(this).attr('height').substr(0, 2));
                    $(this).height(h * heightPercents - 2).css('float',
                        'left').width((w * percents) - 10);
                });
        };


        /**
         * jsonArrayValue是一个json数组对象
         */
        core.submit = function (action, jsonArrayValue) {
            var form = '<form id="fakeForm" action="'
                + action
                + '" method="post" style="display: none;" enctype="application/json" >';//
            form += '  <input name="jsonArrayValue" value=""/>';
            form += '</form>';
            $('body').html(form);

            $('input[name="jsonArrayValue"]').val(
                JSON.stringify(jsonArrayValue));
            $('#fakeForm').submit();
        };

        core.openDchnGroup = function (url, width, height) {
            var t = 'top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no';
            if (!width) {
                width = '560';
            }
            t += ',width=' + width;

            if (!height) {
                height = '415';
            }
            t += ',height=' + height;
            t += ';';
            return window.open(url, '_blank', t);
        };

        core.checkAll = function (checkAllButtonId, checkboxClass) {
            $('body').delegate(
                '#' + checkAllButtonId,
                'click',
                function (e) {
                    e.stopPropagation();

                    if ($('#' + checkAllButtonId).is(':checked')) {
                        $('input.' + checkboxClass).attr('checked',
                            true);
                    } else {
                        $('input.' + checkboxClass).attr('checked',
                            false);
                    }
                });
        };
        //url通配符以及特殊字符重写
        core.urlWildcards = function (str) {
            if (str == null || str == '') {
                return str;
            }
            str = str.replace(/\%/g, '%25');//去掉
            str = str.replace(/\&/g, '%26');//去掉
            str = str.replace(/\!/g, '%21');//去掉
            str = str.replace(/\@/g, '%40');//去掉
            str = str.replace(/\#/g, '%23');//去掉
            str = str.replace(/\$/g, '%24');//去掉
            str = str.replace(/\^/g, '%5E');//去掉
            str = str.replace(/\*/g, '%2A');//去掉
            str = str.replace(/\(/g, '%28');//去掉
            str = str.replace(/\)/g, '%29');//去掉
            str = str.replace(/\'/g, '%27');//去掉
            str = str.replace(/\"/g, '%22');//去掉
            str = str.replace(/\\/g, '%5C');//去掉
            str = str.replace(/\>/g, '%3E');//去掉
            str = str.replace(/\</g, '%3C');//去掉
            str = str.replace(/\?/g, '%3F');//去掉
            str = str.replace(/\:/g, '%3A');//去掉
            str = str.replace(/\[/g, '%5B');
            str = str.replace(/\]/g, '%5D');//去掉
            str = str.replace(/\{/g, '%7B');//去掉
            str = str.replace(/\}/g, '%7D');//去掉
            str = str.replace(/\-/g, '%2D');//去掉
            str = str.replace(/\_/g, '%5F');//去掉
            str = str.replace(/\`/g, '%60');//去掉
            str = str.replace(/\=/g, '%3D');//去掉
            str = str.replace(/\+/g, '%2B');//去掉
            str = str.replace(/\;/g, '%3B');//去掉
            str = str.replace(/\,/g, '%2C');//去掉
            str = str.replace(/\./g, '%2E');//去掉
            str = str.replace(/\|/g, '%7C');//去掉
            str = str.replace(/\//g, '%2F');//去掉
            str = str.replace(/\~/g, '%7E');//去掉
            str = str.replace(/\s/g, ' ');//去掉
            return str;
        };
        //通配符以及特殊字符置空
        core.wildcardsReomve = function (str) {
            if (str == null || str == '') {
                return str;
            }
            str = str.replace(/\%/g, '');//去掉
            str = str.replace(/\！/g, '');//去掉
            str = str.replace(/\&/g, '');//去掉
            str = str.replace(/\!/g, '');//去掉
            str = str.replace(/\@/g, '');//去掉
            // str = str.replace(/\#/g,'');//去掉
            str = str.replace(/\$/g, '');//去掉
            str = str.replace(/\^/g, '');//去掉
            str = str.replace(/\*/g, '');//去掉
            str = str.replace(/\(/g, '');//去掉
            str = str.replace(/\)/g, '');//去掉
            // str = str.replace(/\'/g,'');//去掉
            str = str.replace(/\"/g, '');//去掉
            str = str.replace(/\\/g, '');//去掉
            str = str.replace(/\>/g, '');//去掉
            str = str.replace(/\</g, '');//去掉
            str = str.replace(/\?/g, '');//去掉
            str = str.replace(/\:/g, '');//去掉
            str = str.replace(/\[/g, '');
            str = str.replace(/\]/g, '');//去掉
            str = str.replace(/\{/g, '');//去掉
            str = str.replace(/\}/g, '');//去掉
            // str = str.replace(/\-/g,'');//去掉
            // str = str.replace(/\_/g,'');//去掉
            str = str.replace(/\`/g, '');//去掉
            str = str.replace(/\=/g, '');//去掉
            str = str.replace(/\+/g, '');//去掉
            str = str.replace(/\;/g, '');//去掉
            str = str.replace(/\,/g, '');//去掉
            // str = str.replace(/\./g,'');//去掉
            // str = str.replace(/\|/g,'');//去掉
            str = str.replace(/\//g, '');//去掉
            str = str.replace(/\~/g, '');//去掉
            // str = str.replace(/\s/g,' ');//去掉
            return str;
        };

        return core;
    })();

function AJAXPacket(url, text) {
    this.url = url;
    this.statusText = text;
    this.data = {};
    this.jsonArray = [];

    this.add = function (key, value) {
        this.data[key] = value;
    };

    this.addData = function (dataparam) {
        this.data = dataparam;
    };

    this.addJsonArray = function (array) {
        this.jsonArray = array;
    };

    this.addJson = function (json) {
        this.jsonArray.push(json);
    };
}

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};
$(function () {


    Core.init();
    Core.layerSkinConfig();
    /**
     * var ajax = new AJAXPacket('xxxx/xxx', '提示内容！~'); ajax.add("xxx", '123');
     * ajax.add("ggg", '12344'); Core.sendPacket(ajax, function(data){ {'aaa':
	 * 'bbb'} var aaa = data['aaa'];//bbb [{'aaa': 'bbb'}, {'asdf': 'aaab'}]
	 * for(i in data){ } }); Core.sendPacketHtml(ajax, function(html){
	 * $('#xxx').html(html); });
     */
});


String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.ltrim = function () {
    return this.replace(/(^\s*)/g, "");
};
String.prototype.rtrim = function () {
    return this.replace(/(\s*$)/g, "");
};
String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
};

Array.prototype.remove = function (dx) {
    if (typeof (dx) == "undefined") {
        return false;
    }
    var F = false;
    for (var i = 0; i < this.length; i++) {
        if (F || this[i] == dx) {
            // 找到了，他以后的元素向前提一位
            F = true;
            this[i] = this[i + 1];
        }
    }
    this.length -= 1;
};
