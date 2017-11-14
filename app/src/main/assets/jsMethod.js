
function showToast(data) {
    var json = {"name":data};
    console.log("js调取获取验证码接口："+json);
    //javaInterface是上面所说的字符串映射对象
    window.javaInterface.showToast(JSON.stringify(json));
}
//有参有返回值的方法
function sum(a,b) {
    return a+b;
}
//有参无返回值的方法
function alertMessage(message) {
    alert(message);
}


