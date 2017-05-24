
function changeImage() {
	var img = document.getElementById("js-mail_vcode_img");
	img.src = "../valCodeAction/valCode?id=" + Math.random();
}

function register(){
	var registerForm=$("#registerForm");
	if(checkLoginAndRegisterItem("registerForm")){
		isEmailRegister($(registerForm.find("input")[0]).val(),registerForm);	
	}
}

function login(){
	var loginForm=$("#loginForm");
	if(checkLoginAndRegisterItem("loginForm")){
		loginForm.submit();
	}
}

function checkLoginAndRegisterItem(id){
	var inputs=$("#"+id).find("input");
	var email=$(inputs[0]).val();
	var reg=new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	if(email===""){
		showMessage("邮箱不能为空");
		return false;
	}
	if(!reg.test(email)){
		showMessage("非法的邮箱地址");
		return false;
	}
	var pwd=$(inputs[1]).val();
	if(pwd===""){
		showMessage("密码不能为空");
		return false;
	}
	var code=$(inputs[2]).val();
	if(code===""){
		showMessage("验证码不能为空");
		return false;
	}
	return true;
}

function isEmailRegister(email,registerForm){
	var url="/GetFriends/userAction/checkEmail";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"email="+email,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			if(data.result==="notExist"){
				registerForm.submit();
			}else{
				showMessage("邮箱已经注册，如果已经忘记密码，使用密码找回功能");
			}	
		}
	});
}

//友好提示，取代alert
function showMessage(content){
	var box=$("#global-message-box");
	box.text(content).show();
	setTimeout(function(){
		box.hide();
	},1500);
}