<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>密码修改</title>
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<link rel="stylesheet" href="../css/fndpwd.css" type="text/css"></link>
<script src="../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../js/fndpwd.js" charset="utf-8"></script>
</head>
<body>
	<div class="messageBox" id="global-message-box"></div>
	<div class="fndpwd_container_modify">
		<h4 style="text-align: center">知音网密码找回系统</h4>
		<p class="fnd_tips">注意：您在为以下注册邮箱重置密码，欢迎使用。</p>
		
		<label class="email-label">邮箱：</label> <label>${ email}</label>
		<form role="form" method="post" action="/GetFriends/userAction/modifyPwd" id="modify_form">
			<input type="hidden" class="form-control" name="sid" value="${sid}"/>
			<input type="hidden" class="form-control" name="email" value="${email }"/>
			<div class="form-group">
				<input type="password" class="form-control" name="password" placeholder="输入新密码" />
			</div>
			<div class="form-group">
				<input type="password" class="form-control" placeholder="确认新密码" />
			</div>
		</form>
		<button class="btn btn-info btn-md" onclick="modifyPassword()">找回密码</button>
	</div>
</body>
</html>