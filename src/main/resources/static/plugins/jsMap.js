!(function (window) {

    /**
     * Map对象构造函数
     * @class
     * @constructor
     */
    var Map = function () {
        /**
         * 存档所有Key值的数组对象
         * @type {any[]|Array}
         */
        this.keys = new Array();
        /**
         * 存放数据对象
         * @type {Object}
         */
        this.values = new Object;
    }

    /**
     * 扩充Map对象方法
     * @type {{}}
     */
    Map.prototype = {
        /**
         * 存放键值对
         * @function
         * @param key
         * @param value
         */
        put: function (key, value) {
            if (!this.values[key])
                this.keys.push(key);
            this.values[key] = value;
        },
        /**
         * 根据key值获取对象
         * @param key
         * @returns {*}
         */
        get: function (key) {
            return this.values[key];
        },
        /**
         * 根据key值删除对象
         * @param key
         */
        remove: function (key) {
            this.keys.remove(key);
            this.values[key] = null;
        },
        /**
         * 循环遍历，执行回调函数
         * @param fn 回调函数
         */
        each: function (fn) {
            if (typeof fn != 'function') {
                return;
            }
            for (var i = 0; i < this.keys.length;i++)
            {
                var k = this.keys[i];
                fn(k, this.values[k], i);
            }
        },
        /**
         * 获取键值数组(类似Java的entrySet())
         * @return 键值对象{key,value}的数组
         */
        entrys: function () {
            var len = this.keys.length;
            var entrys = new Array(len);
            for (var i = 0; i < len; i++) {
                entrys[i] = {
                    key: this.keys[i],
                    value: this.values[i]
                };
            }
            return entrys;
        },

        /**
         * 判断Map是否为空
         */
        isEmpty: function () {
            return this.keys.length == 0;
        },

        /**
         * 获取键值对数量
         */
        size: function () {
            return this.keys.length;
        },

        /**
         * 重写toString
         */
        toString: function () {
            var s = "{";
            for (var i = 0; i < this.keys.length; i++, s += ',') {
                var k = this.keys[i];
                s += k + "=" + this.values[k];
            }
            s += "}";
            return s;
        }
    }

    window.Map = Map;
})(window);