function fndpwd() {
	var email = $("#email-content").val();
	var reg = new RegExp(
			"^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	if (email === "") {
		showMessage("邮箱不能为空");
		return false;
	}
	if (!reg.test(email)) {
		showMessage("非法的邮箱地址");
		return false;
	}
	var url = "/GetFriends/userAction/fndpwd";
	$.ajax({
		url : url,
		type : "POST",
		cache : false,
		dataType : "json",
		data : "email=" + email,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			var result = data.result;
			if (result === "notExist") {
				showMessage("此邮箱不存在");
			} else if (result === "success") {
				showMessage("重置密码邮件发送成功");
			} else {
				showMessage("服务器出错");
			}
		}
	});
}

function modifyPassword() {
	var container = $(".fndpwd_container_modify");
	var email = $(container.find("label")[1]).text();
	var pwd1 = $(container.find("input")[2]).val();
	var pwd2 = $(container.find("input")[3]).val();
	if (pwd1 == null || pwd1 == "" || pwd2 == null || pwd2 == "") {
		showMessage("信息填写不完整");
		return;
	}
	if (!(pwd1 === pwd2)) {
		showMessage("两次密码不一致");
		return;
	}
	$("#modify_form").submit();
}

// 友好提示，取代alert
function showMessage(content) {
	var box = $("#global-message-box");
	box.text(content).show();
	setTimeout(function() {
		box.hide();
	}, 1500);
}