<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en">
<head>
<meta charset="UTF-8">
<title>首页</title>
<link rel="stylesheet" href="../css/bootstrap.min.css" />
<link rel="stylesheet" href="../css/index.css" />
<link rel="stylesheet" href="../css/jquery-ui.min.css" />
</head>
<body>
	<div class="messageBox" id="global-message-box"></div>

	<div class="modal" id="addFriendModal" role="dialog" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" data-dismiss="modal" class="close">
						<span>&times;</span>
					</button>
					<h4 class="modal-title">添加好友</h4>
				</div>
				<div class="modal-body">
					<form role="form">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="请输入验证消息" />
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
					<button type="button" data-dismiss="modal" class="btn btn-primary"
						id="sendAddFriendMessageBtn">发送</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal" id="addPicForNewModal" role="dialog" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" data-dismiss="modal" class="close">
						<span>&times;</span>
					</button>
					<h4 class="modal-title">添加动态图片</h4>
				</div>
				<div class="modal-body">
					<form role="form" method="post" enctype="multipart/form-data" id="picForm" onsubmit="return false">
						<div class="form-group" id="pic_new_fileInput"></div>
					</form>
				</div>
				<div class="modal-footer">
					<p style="color:red"></p>
					<button type="button" data-dismiss="modal" class="btn btn-default">取消</button>
					<button type="button" data-dismiss="modal" class="btn btn-primary"
						id="addPicForNewBtn" onclick="uploadPic()">上传</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal" id="friendWordsModal" role="dialog" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" data-dismiss="modal" class="close">
						<span>&times;</span>
					</button>
					<h4 class="modal-title">留言</h4>
				</div>
				<div class="modal-body">
					<div class="edit-box" tabindex="0" contenteditable="true"
						name="words_content" id="words-content"></div>
				</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
					<button type="button" data-dismiss="modal" class="btn btn-primary"
						id="sendFriendWordsMessageBtn">留言</button>
				</div>
			</div>
		</div>
	</div>

	<nav class="navbar-diy navbar navbar-fixed-top" role="navigation">
	<div class="navbar-header">
		<a href="#" class="navbar-brand">知音网</a>
	</div>
	<div class="collapse navbar-collapse">
		<ul class="nav navbar-nav pull-right">
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown"> <img src="../icon/tab_person.png"
					width="20px" height="20px"></img><b class="caret"></b> </a>
				<ul class="dropdown-menu pull-right">
					<li><a href="#">${loginUser.user_name }</a></li>
					<li class="divider"></li>
					<li><a href="#">${loginUser.email }</a></li>
					<li class="divider"></li>
					<li><a href="#">知音交友欢迎您</a></li>
				</ul>
			</li>

			<li><a href="/GetFriends/userAction/logout" title="注销"><img
					src="../icon/navbar_logout.png" width="20px" height="20px"></img> </a>
			</li>
		</ul>

	</div>
	</nav>


	<div class="container-diy">
		<div class="col-lg-2 col-md-2 col-sm-2 col-xs-2 menu" id="menu">
			<ul class="list-group">
				<li class="list-group-item active" data-id="newPane"><a
					href="javascript:void(0)">首页</a>
				</li>
				<li class="list-group-item" data-id="activityPane"><a
					href="javascript:void (0)">活动</a>
				</li>
				<li class="list-group-item" data-id="friendPane"><a
					href="javascript:void (0)">好友</a>
				</li>
				<li class="list-group-item" data-id="selfPane"><a
					href="javascript:void (0)">个人资料</a></li>
				<li class="list-group-item" data-id="messagePane"><a
					href="javascript:void (0)">消息中心<span id="messageSizeBadge"
						class="badge pull-right">${notReadMessageSize }</span> </a></li>
			</ul>
		</div>
		<div class="col-lg-10 col-md-10 col-sm-10 col-xs-10">

			<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
				<div id="newPane">
					<ul class="nav nav-tabs" id="myTab_new" role="tablist">
						<li role="presentation" class="active"><a href="#new_friend"
							data-toggle="tab" data-id="new_friend">好友动态</a></li>
						<li role="presentation"><a href="#new_self" data-toggle="tab"
							data-id="new_self">原创内容 </a></li>
						<li role="presentation"><a href="#new_in" data-toggle="tab"
							data-id="new_in">我参与的 </a></li>
					</ul>
					<div class="tab-content" id="mytabContent_new">
						<div class="tab-pane active" id="new_friend">
							<div class="text-box" id="news-publish">
								<div class="edit-box-before" contenteditable="true">
									你现在在想什么呢？</div>
							</div>
							<div class="news-box" id="new-box" style="display: none">
								<div class="text-box">
									<div class="edit-box" contenteditable="true" name="new_content"
										id="new-content"></div>
									<div class="media-pic" id="media-pic"></div>
									<div class="pull-right button-div">
										<button class="btn btn-default" id="pic-upload" data-toggle="modal" data-target="#addPicForNewModal">添加图片</button>
										<button class="btn btn-default" id="new-cancel">取消</button>
										<button class="btn btn-primary" id="new-publish"
											onclick="addNew()">发布</button>
									</div>
									<div style="clear: both"></div>
								</div>
							</div>
							<div id="new_friend_container">
								<%-- <c:forEach items="${news }" var="new_info">
									<div class="new-info" data-pagenum="1"
										data-pagetotal="${pagetotal }">
										<div class="self-info">
											<img width="45px" height="45px"
												src="../${new_info.ower_id.picture_url}" alt="" /> <b>${new_info.ower_id.user_name
												}</b>
											<p class="publish-date">${new_info.create_date}</p>
										</div>
										<div class="text-content">${new_info.content_text }</div>
										<div class="media-content"></div>
										<div class="comment-content">
											<button class="btn btn-default pull-right btn-comment"
												onclick="showCommentPane('${new_info.new_id }','new_friend')">评论</button>
											<div style="clear: both"></div>
										</div>
									</div>
									<div class="comment_content"
										id="comment_content_new_friend_${new_info.new_id }"
										style="display: none">
										<div class="content-pane"
											id="content_pane_new_friend_${new_info.new_id }"></div>
									</div>
								</c:forEach> --%>
							</div>
						</div>

						<div class="tab-pane" id="new_self">
							<div id="new_self_container"></div>
						</div>

						<div class="tab-pane" id="new_in">
							<div id="new_in_container"></div>
						</div>
					</div>
				</div>

				<div id="friendPane" style="display: none">
					<ul class="nav nav-tabs" id="myTab_friend" role="tablist">
						<li role="presentation" class="active"><a href="#fiend_my"
							data-toggle="tab">我的好友</a>
						</li>
						<li role="presentation"><a href="#friend_intro"
							data-toggle="tab">好友推荐</a>
						</li>
					</ul>
					<div class="tab-content" id="mytabContent">
						<div class="tab-pane active" id="fiend_my">
							<div class="friend-search" id="friend-search">
								<form role="form">
									<div class="form-group">
										<input type="text" id="friend-search-isInput"
											class="form-control" placeholder="查找好友" /> <a
											href="javascript:searchUser(1)"><img
											src="../icon/search.png" alt="" /> </a>
									</div>
								</form>
							</div>
							<div class="friend-search-result" id="friend-search-result-is"></div>
							<div class="friend-container" id="friend-container"></div>
						</div>
						<div class="tab-pane" id="friend_intro">
							<div class="friend-search" id="friend-search">
								<form role="form">
									<div class="form-group">
										<input type="text" id="friend-search-notInput"
											class="form-control" placeholder="添加好友" /> <a
											href="javascript:searchUser(0)"><img
											src="../icon/search.png" alt="" /> </a>
									</div>
								</form>
							</div>
							<div class="friend-search-result" id="friend-search-result-not"></div>
							<div class="friend-container" id="friend-container-intro"></div>
						</div>
					</div>
				</div>

				<div id="activityPane" style="display: none">
					<ul class="nav nav-tabs" id="myTab_activity" role="tablist">
						<li role="presentation" class="active"><a
							href="#activity_ing" data-toggle="tab" data-id="activity_ing">进行活动</a>
						</li>
						<li role="presentation"><a href="#activity_my"
							data-toggle="tab" data-id="activity_my">原创活动</a>
						</li>
						<li role="presentation"><a href="#activity_in"
							data-toggle="tab" data-id="activity_in">参与活动</a>
						</li>
					</ul>
					<div class="tab-content" id="mytabContent_activity">
						<div class="tab-pane active" id="activity_ing">
							<div class="text-box" id="activity-publish">
								<div class="edit-box-before" contenteditable="true">
									组织一个活动？</div>
							</div>
							<div class="news-box" id="activity-box" style="display: none">
								<div class="text-box">
									<div class="activity-topic" id="activity-info">

										<div>
											活动主题：<input type="text" />
										</div>
										<div>
											活动地点：<input type="text" placeholder="国家" />- <input
												type="text" placeholder="省份" />- <input type="text"
												placeholder="城市" />- <input type="text" placeholder="详细地址" />
										</div>
										<div>
											最大人数：<input id="activity-max-num" type="text"
												onkeyup="value=value.replace(/[^\d]/g,'')"
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
										</div>
									</div>
									<div class="edit-box" contenteditable="true"
										id="activity-content"></div>
									<div class="pull-right button-div">
										<button class="btn btn-default" id="activity-cancel">取消</button>
										<button class="btn btn-primary" id="activity-button"
											onclick="addActivity()">发布</button>
									</div>
									<div style="clear: both"></div>
								</div>
							</div>
							<div id="activity_container"></div>
						</div>
						<div class="tab-pane" id="activity_in">
							<div id="activity_container_in"></div>
						</div>
						<div class="tab-pane" id="activity_my">
							<div id="activity_container_my"></div>
						</div>
					</div>
				</div>

				<div id="messagePane" style="display: none">
					<ul class="nav nav-tabs" id="myTab_message" role="tablist">
						<li role="presentation" class="active"><a
							href="#message_friend" data-id="message_friend" data-toggle="tab">好友申请</a>
						</li>
						<li role="presentation"><a href="#message_words"
							data-id="message_words" data-toggle="tab">好友留言</a>
						</li>
						<li role="presentation"><a href="#message_system"
							data-id="message_system" data-toggle="tab">系统消息</a>
						</li>
					</ul>
					<div class="tab-content" id="mytabContent_Message">
						<div class="tab-pane active" id="message_friend"></div>
						<div class="tab-pane" id="message_words"></div>
						<div class="tab-pane" id="message_system"></div>
					</div>
				</div>

				<div id="selfPane" style="display: none">
					<ul class="nav nav-tabs" id="myTab_self" role="tablist">
						<li role="presentation" class="active"><a
							href="#self_primary" data-toggle="tab">基本资料</a>
						</li>
						<li role="presentation"><a href="#self_pwd" data-toggle="tab">密码修改</a>
						</li>
					</ul>

					<div class="tab-content" id="mytabContent_self">
						<div class="tab-pane active" id="self_primary">
							<div class="panel panel-default">
								<div class="panel-heading">
									个人基本资料
									<button class="btn btn-default btn-xs pull-right"
										id="modifySelfBtn" onclick="showModify()">编辑</button>
								</div>

								<div class="panel-body">
									<div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">
										<form role="form" id="selfInfo_form">
											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
												<div class="form-group">
													<label>用户名:</label> <label class="show-info"></label> <input
														type="text" name="user_name" class="form-control" />
												</div>
												<div class="form-group">
													<label>学校:</label> <label class="show-info"></label> <input
														type="text" name="school" class="form-control" />
												</div>
												<div class="form-group">
													<label>性别:</label><label class="show-info"></label> <label
														class="modify"> 男 <input type="radio" name="sex"
														value="男"> </label> <label class="modify"> 女 <input
														type="radio" name="sex" value="女"> </label>
												</div>
											</div>

											<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6">
												<div class="form-group">
													<label>生日:</label> <label class="show-info"></label> <input
														type="text" name="birthday" class="form-control"
														id="birthDatePicker" />
												</div>
												<div class="form-group">
													<label>手机号码:</label> <label class="show-info"></label> <input
														type="text" name="telephone" class="form-control" />
												</div>
											</div>
											<div style="clear: both"></div>
											<div class="form-group" id="self-info-address">
												<label>地址:</label> <label class="show-info"></label>
												<table>
													<tr>
														<td><input type="text" name="address"
															class="form-control" />
														</td>
														<td><input type="text" name="address"
															class="form-control" />
														</td>
														<td><input type="text" name="address"
															class="form-control" />
														</td>
														<td><input type="text" name="address"
															class="form-control" />
														</td>
													</tr>
												</table>
											</div>
											<input type="hidden" name="picture_url" value=""
												id="picture_url_input" />
										</form>
									</div>

									<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
										<label>用户头像</label>
										<form method="post" enctype="multipart/form-data"
											id="iconForm" onsubmit="return false">
											<div>
												<img id="iconContainer" src="../icon/defaultIcon.png"
													width="80px" height="80px" alt="" class="img-thumbnail" />
												<input type="file" name="icon" id="icon_file_input" />
												<p class="help-block"></p>
												<button class="btn btn-default btn-xs" id="uploadBtn"
													onclick="doUpload()">确认上传</button>
											</div>
										</form>
									</div>

								</div>

								<div class="panel-footer">
									<button class="btn btn-primary" id="saveModify"
										style="display:none" onclick="modifySelfInfo()">保存修改</button>
								</div>

							</div>

						</div>

						<div class="tab-pane" id="self_pwd">
							<div class="panel panel-default">
								<div class="panel-heading">密码修改</div>
								<div class="panel-body">
									<form role="form" id="modifyPwdForm">
										<div class="form-group">
											<label>旧密码</label> <input type="password"
												class="form-control" name="oldPwd" placeholder="请输入旧密码" />
										</div>
										<div class="form-group">
											<label>新密码</label> <input type="password"
												class="form-control" name="newPwd" placeholder="请输入新密码" />
										</div>
										<div class="form-group">
											<label>确认密码</label> <input type="password"
												class="form-control" name="surePwd" placeholder="确认密码" />
										</div>
									</form>

								</div>
								<div class="panel-footer">
									<button class="btn btn-primary" id="modifyPwd"
										onclick="modifyPwd()">确认修改</button>
								</div>

							</div>
						</div>

					</div>
				</div>

			</div>

			<div class="col-lg-4 col-md-4 col-sm-4 col-xs-4">
				<div class="back_top">
					<a href="#"><img width="64px" height="64px"
						src="../icon/top.png" alt="" /> </a>
				</div>
			</div>
		</div>
	</div>


</body>
<script src="../js/jquery-1.9.1.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/jquery-ui.min.js" charset="utf-8"></script>
<script src="../js/md5.js" charset="utf-8"></script>
<script src="../js/index.js" charset="utf-8"></script>

</html>
