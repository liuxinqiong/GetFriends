<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>知音网密码找回</title>
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<link rel="stylesheet" href="../css/fndpwd.css" type="text/css"></link>
<script src="../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../js/fndpwd.js" charset="utf-8"></script>
</head>
<body>
	<div class="messageBox" id="global-message-box"></div>
	<div class="fndpwd_container">
		<h4 style="text-align: center">知音网密码找回系统</h4>
		<p class="fnd_tips">注意：请在下方输入您注册使用的邮箱，点击找回密码，我们将会发送修改密码链接至邮箱，请关注邮箱动态，按照邮件内容操作即可，欢迎使用。</p>
		<form role="form">
			<div class="form-group">
				<input type="email" class="form-control" placeholder="输入注册邮箱" id="email-content"/>
			</div>
		</form>
		<button class="btn btn-info btn-md" onclick="fndpwd()">找回密码</button>
	</div>
</body>
</html>