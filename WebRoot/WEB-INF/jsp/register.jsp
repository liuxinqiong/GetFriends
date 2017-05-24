<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新用户注册</title>
<link href="../css/register.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../js/user.js" charset="utf-8"></script>
</head>
<body>
	<div class="messageBox" id="global-message-box"></div>
	<div class="wrap">
		<div class="banner-show" id="js_ban_content">
			<div class="cell bns-01">
				<div class="con"></div>
			</div>
			<div class="cell bns-02" style="display:none;">
				<div class="con">
					<a href="#" target="_blank" class="banner-link"> <i>知音</i> </a>
				</div>
			</div>
			<div class="cell bns-03" style="display:none;">
				<div class="con">
					<a href="#" target="_blank" class="banner-link"> <i>企业云</i> </a>
				</div>
			</div>
		</div>
		<div class="banner-control" id="js_ban_button_box">
			<a href="javascript:;" class="left">左</a> <a href="javascript:;"
				class="right">右</a>
		</div>
		<script type="text/javascript">
			;
			(function() {

				var defaultInd = 0;
				var list = $('#js_ban_content').children();
				var count = 0;
				var change = function(newInd, callback) {
					if (count)
						return;
					count = 2;
					$(list[defaultInd]).fadeOut(400, function() {
						count--;
						if (count <= 0) {
							if (start.timer)
								window.clearTimeout(start.timer);
							callback && callback();
						}
					});
					$(list[newInd]).fadeIn(400, function() {
						defaultInd = newInd;
						count--;
						if (count <= 0) {
							if (start.timer)
								window.clearTimeout(start.timer);
							callback && callback();
						}
					});
				};

				var next = function(callback) {
					var newInd = defaultInd + 1;
					if (newInd >= list.length) {
						newInd = 0;
					}
					change(newInd, callback);
				};

				var start = function() {
					if (start.timer)
						window.clearTimeout(start.timer);
					start.timer = window.setTimeout(function() {
						next(function() {
							start();
						});
					}, 8000);
				};

				start();

				$('#js_ban_button_box').on('click', 'a', function() {
					var btn = $(this);
					if (btn.hasClass('right')) {
						//next
						next(function() {
							start();
						});
					} else {
						//prev
						var newInd = defaultInd - 1;
						if (newInd < 0) {
							newInd = list.length - 1;
						}
						change(newInd, function() {
							start();
						});
					}
					return false;
				});

			})();
		</script>
		<div class="container">
			<div class="register-box">
				<div class="reg-slogan">新用户注册</div>
				<form action="register" method="post" id="registerForm">
					<div class="reg-form" id="js-form-mail">
						<div class="cell">
							<!--    <label for="js-mail_ipt">输入你的常用邮箱</label> -->
							<input type="text" name="email" id="js-mail_ipt" class="text"
								placeholder="输入你的常用邮箱" autocomplete="off" />
						</div>
						<div class="cell">
							<!--   <label for="js-mail_pwd_ipt">设置密码</label> -->
							<input type="password" name="password" id="js-mail_pwd_ipt"
								class="text" placeholder="设置密码" autocomplete="off"/>
						</div>
						<div class="cell vcode">
							<!--  <label for="js-mail_vcode_ipt">输入验证码</label> -->
							<input type="text" name="code" id="js-mail_vcode_ipt"
								class="text" maxlength="4" placeholder="输入验证码" autocomplete="off"/> <img
								id="js-mail_vcode_img" src="../valCodeAction/valCode" alt="code" />
							<span> <a id="js-mail_vcode_a"
								href="javascript:changeImage();"> 换一张</a> </span>
						</div>
						
						<div>
							
							<span style="float:right"> <a id="js-mail_vcode_a"
								href="goLogin">已有帐号？去登录</a> </span>
							<div style="clear:both"></div>
						</div>
						
						<div style="color:red">
							${message} 
						</div>
						
						<div class="bottom">
						 <a id="js-mail_btn" href="javascript:register();" class="button btn-green">
								立即注册</a> 	
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
