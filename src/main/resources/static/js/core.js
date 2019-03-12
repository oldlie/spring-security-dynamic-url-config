var axios = axios || null;
(function (axios) {

    if (!axios) {
        console.error("没有引入axios组件。")
        return null;
    }

    var token = !!document.getElementById('token') ? document.getElementById('token').value : '';
    var Http = function () {};
    var config = {
        header: {'Authorization': 'DynamicUrl ' + token,
            'Content-Type': 'application/json;charset=UTF-8'}
    };

    Http.prototype.request = function (url, method, data) {
        config['method'] = method;
        config['url'] = url;
        config['params'] = data;
        return axios.request(config);
    };

    Http.prototype.get = function (url, data) {
        return http.request(url, 'get', data);
    };

    Http.prototype.post = function (url, data) {
        return http.request(url, 'post', data);
    };

    Http.prototype.delete = function (url, data) {
        return http.request(url, 'delete', data);
    };

    Http.prototype.pull = function (url, data) {
        return http.request(url, 'pull', data);
    };

    window.http = new Http();

    console.log(window.http);
})(axios);

var core = {

    formPost: function (url, params, target) {
        var _form = document.getElementById("postForm");
        if (_form) {
            document.body.removeChild(_form);
        }
        _form = document.createElement("form");
        _form.action = url;
        if (target) {
            _form.target = target;
        } else {
            // _self
            _form.target = '_blank';
        }

        _form.method = 'post';
        _form.style.display = 'none';
        for (var item in params) {
            var _input = document.createElement('input');
            _input.name = item;
            _input.value = params[item];
            _form.appendChild(_input);
        }
        document.body.appendChild(_form);
        _form.submit();
    },

    /**
     * 深拷贝，数据源复杂时慎用，毕竟是递归
     * @param source 源数据
     * @returns {*} 目标数据
     */
    deepCopy: function (source) {

        if (typeof source === 'function') {
            return source;
        }

        if (source === undefined || source === null) {
            // console.log('common.deepCopy:undefined or null', source);
            return source;
        }

        if (typeof source === 'number' ||
            typeof source === 'string' ||
            typeof source === 'boolean') {
            // console.log('common.deepCopy:basic type', source);
            return source;
        }

        var target, i = 0, length, key, _item, _temp;

        if (source instanceof Array) {
            target = [];
            if (source.length > 0) {
                // console.log('common.deepCopy:Array type', source);
                length = source.length;
                for (; i < length; i++) {
                    _item = source[i];
                    _temp = common.deepCopy(_item);
                    target[i] = _temp;
                }
            }
            return target;
        }

        // console.log('common.deepCopy:Object type', source);
        target = {};
        for (key in source) {
            if (source.hasOwnProperty(key)) {
                _item = source[key];
                _temp = common.deepCopy(_item);
                target[key] = _temp;
            }
        }
        return target;
    }
};