<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>成功页面</title>
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<link rel="stylesheet" href="../css/fndpwd.css" type="text/css"></link>
<script src="../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../js/fndpwd.js" charset="utf-8"></script>
</head>
<body>
	<div class="messageBox" id="global-message-box"></div>
	<div class="fndpwd_container">
		<h4 style="text-align: center">成功页面</h4>
		<p class="fnd_tips">${message }</p>
		<p class="fnd_tips" id="goLogin">3秒后跳转到登录页面</p>
	</div>
	<script>
	var i=3;
	setInterval(function(e){
		$("#goLogin").html(--i+"秒后跳转到登录页面");
		if(i==0){
			window.location.href="/GetFriends/userAction/goLogin";
		}
	}, 1000);
	</script>
</body>
</html>