
//登录token
var token = localStorage.getItem("token");


//jquery全局配置
$.ajaxSetup({
	cache: false,
    headers: {
        "token": token
    },
    xhrFields: {
	    withCredentials: true
    }
});