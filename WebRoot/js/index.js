/**
 * Created by sky on 2016/4/19.
 */
// 想办法只检查前所在屏
var scrollPageUtil=createScollPageUtil("new_friend");
var global_type="new_friend";
var currentUser={};


function createScollPageUtil(type){
	if(type==="new_friend"){
		return {container:"new_friend_container",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="new_self"){
		return {container:"new_self_container",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="new_in"){
		return {container:"new_in_container",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="activity_container"){
		return {container:"activity_container",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="activity_container_my"){
		return {container:"activity_container_my",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="activity_container_in"){
		return {container:"activity_container_in",childName:"new-info",isAll:false,isLoading:false};
	}else if(type==="message_friend"){
		return {container:"message_friend",childName:"message-friend",isAll:false,isLoading:false};
	}else if(type==="message_words"){
		return {container:"message_words",childName:"message-friend",isAll:false,isLoading:false};
	}else if(type==="message_system"){
		return {container:"message_system",childName:"message-friend",isAll:false,isLoading:false};
	}else{
		return {container:"",childName:"",isAll:false,isLoading:false};
	}
}

window.onload=function(){
	/*5、21 去掉前台是用jstl标签*/
	getNews("new_friend", 1);
	getCurrentUser();
};

$(document).ready(function() {
	$("#news-publish").on("click", function() {
		$("#new-box").show("fast");
		$("#news-publish").hide();
	});

	$("#new-cancel").on("click", function() {
		$("#new-box").hide();
		$("#news-publish").show("fast");
	});
	
	checkNewContent();

	$("#menu li").on("click",function(e){
		var obj= e.target;
		obj=obj.nodeName==="A"?obj.parentNode:obj;
		var id=$(obj).data("id");
		showPane(id,obj);
	});
	/* 4/23 */
	$("#activity-publish").on("click", function() {
		$("#activity-box").show("fast");
		$("#activity-publish").hide();
	});

	$("#activity-cancel").on("click", function() {
		$("#activity-box").hide();
		$("#activity-publish").show("fast");
	});

	checkActivityContent();
	
	fillAddress("activity-info",1);
	
	/* 5/2 */
	initForFriendModal();
	initForNewPicModal();
	/* 5/4 */
	$("#myTab_message a").click(function(e) {
		e.preventDefault();
		var a =e.target;
		var id=$(a).data("id");
		if(id==="message_friend"){
			getMessageByType(1,"message_friend",1);
			scrollPageUtil=createScollPageUtil("message_friend");
			global_type="message_friend";
		}else if(id==="message_words"){
			getMessageByType(2,"message_words",1);
			scrollPageUtil=createScollPageUtil("message_words");
			global_type="message_words";
		}else{
			getMessageByType(0,"message_system",1);
			scrollPageUtil=createScollPageUtil("message_system");
			global_type="message_system";
		}
		$(this).tab("show");
	});
	
	/* 5/11 */
	$("#myTab_activity a").click(function(e) {
		e.preventDefault();
		var a =e.target;
		var id=$(a).data("id");
		if(id==="activity_ing"){
			getActivities("activity_container",1);
			scrollPageUtil=createScollPageUtil("activity_container");
			global_type="activity_container";
		}else if(id==="activity_my"){
			getActivities("activity_container_my",1);
			scrollPageUtil=createScollPageUtil("activity_container_my");
			global_type="activity_container_my";
		}else{
			getActivities("activity_container_in",1);
			scrollPageUtil=createScollPageUtil("activity_container_in");
			global_type="activity_container_in";
		}
		$(this).tab("show");
	});
	
	/* 5/14 */
	$("#myTab_new a").click(function(e) {
		e.preventDefault();
		var a =e.target;
		var id=$(a).data("id");
		if(id==="new_friend"){
			getNews("new_friend",1);
			scrollPageUtil=createScollPageUtil("new_friend");
			global_type="new_friend";
		}else if(id==="new_self"){
			getNews("new_self",1);
			scrollPageUtil=createScollPageUtil("new_self");
			global_type="new_self";
		}else{
			getNews("new_in",1);
			scrollPageUtil=createScollPageUtil("new_in");
			global_type="new_in";
		}
		$(this).tab("show");
	});
	/* 5/17 */
	datePickerInit();
	/* 5/14 实现滚动分页 */
	window.onscroll=function(){
		// 最后一张动态出现之前实现加载
		if(global_type===""||scrollPageUtil.container===""){return;}
        if(checkFlag(scrollPageUtil.container,scrollPageUtil.childName)){
        	var cparent=document.getElementById(scrollPageUtil.container);
            var ccontent=getChildElement(cparent,scrollPageUtil.childName);
            var pageNum=$(ccontent[ccontent.length-1]).data("pagenum");
            var pageTotal=$(ccontent[ccontent.length-1]).data("pagetotal");
            if(pageNum<pageTotal&&!scrollPageUtil.isLoading){
            	scrollPageUtil.isAll=false;
            	scrollPageUtil.isLoading=true;
            	appendLoadingBox($("#"+scrollPageUtil.container));  	
            	setTimeout(function(){
            		if(global_type.indexOf("new")!=-1){
            			getNews(global_type,parseInt(pageNum)+1); 		
            		}else if(global_type.indexOf("activity")!=-1){
            			getActivities(global_type, parseInt(pageNum)+1);
            		}else if(global_type==="message_friend"){
            			getMessageByType(1,"message_friend",parseInt(pageNum)+1);
            		}else if(global_type==="message_words"){
            			getMessageByType(2,"message_words", parseInt(pageNum)+1);
            		}else if(global_type==="message_system"){
            			getMessageByType(0,"message_system",parseInt(pageNum)+1);
            		}
            	}, 500);    	
            }else{
            	if(scrollPageUtil.isLoading){
            		return;
            	}
            	if(!scrollPageUtil.isAll){
            		showMessage("已经是全部数据啦");
            		scrollPageUtil.isAll=true;
            	} 	
            }
        }
	};
});


function datePickerInit(){
	$("#birthDatePicker").datepicker({
        showOtherMonths: true,
        selectOtherMonths: true,
        changeMonth: true,
        changeYear: true,
        dateFormat: "yy-mm-dd",
        showAnim: "show",
        maxDate:0,
        minDate: new Date(1900, 1, 1)
    });
    $("#birthDatePicker").attr("readonly", "readnly");
}

function checkFlag(containerId,childElementName){
	var cparent=document.getElementById(containerId);
    var ccontent=getChildElement(cparent,childElementName);
    if(ccontent.length===0){return false;}
    var lastContentHeight=ccontent[ccontent.length-1].offsetTop;
    var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;// 被隐藏部分的高度
    var pageHeight=document.documentElement.clientHeight||document.body.clientHeight;// 页面可见高度
    if(lastContentHeight<scrollTop+pageHeight){
        return true;
    }
}


function getChildElement(parent,content){
    var contentArr=[];
    var allContent=parent.getElementsByTagName("*");
    for(var i=0;i<allContent.length;i++){
        if(allContent[i].className==content){
            contentArr.push(allContent[i]);
        }
    }
    return contentArr;
}

function getCurrentUser(){
	var url = "/GetFriends/userAction/currentUser";
	$.ajax({
		url : url,
		type : "POST",
		cache : false,
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			console.log(data);
			if(data.result==="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
			}else{
				currentUser=data.result;
				fillPrimaryInfo();
			}
		}
	});
}


function fillAddress(containerId,index){
	$.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js',function(){
		var inputs=$("#"+containerId).find("input");
		// 设置当地的地址
		$(inputs[index++]).val(remote_ip_info.country);// 国家
		$(inputs[index++]).val(remote_ip_info.province);// 省份
		$(inputs[index++]).val(remote_ip_info.city);// 城市
	});
}

function getNews(id,pageNum){
	var url = "/GetFriends/newAction/getNews";
	$.ajax({
		url : url,
		type : "POST",
		data : "id=" + id+"&pageNum="+pageNum,
		cache : false,
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(data);
			if(data.code===-1){
				showMessage(data.message);
				redirectToLogin();
			}else{
				var content="";
				var news=data.data.data;
				for(var i=0;i<news.length;i++){
					var newObj=news[i];
					var operationType="评论";
					if(id==="new_self"){
						operationType="回复";
					}
					content+='<div class="new-info" data-pagenum="'+data.pageNumber+'" data-pagetotal="'+data.pageTotal+'"><div class="self-info"><img width="45px" height="45px"src="../'+newObj.ower_id.picture_url+'" alt="" />';
					content+='<b>'+newObj.ower_id.user_name+'</b><p class="publish-date">'+newObj.create_date+'</p></div>';
					content+='<div class="text-content">'+newObj.content_text+'</div><div class="media-content">'+createImgMediaForNew(newObj.picture_url)+'</div>';
					content+='<div class="comment-content"><button class="btn btn-default pull-right btn-comment" onclick="showCommentPane('+newObj.new_id+',\''+id+'\')">'+operationType+'</button>';
					content+='<div style="clear: both"></div></div></div>';
					content+='<div class="comment_content" id="comment_content_'+id+'_'+newObj.new_id+'" style="display: none">';
					content+='<div class="content-pane" id="content_pane_'+id+'_'+newObj.new_id+'"></div></div>';
				}
				removeLoadingBox($("#"+scrollPageUtil.container));  	
				if(pageNum===1){
					$("#"+id+"_container").html(content);
				}else{
					$("#"+id+"_container").append(content);
				}
				scrollPageUtil.isLoading=false;
			}
		}
	});
}

function createImgMediaForNew(picture_url){
	if(picture_url==null||picture_url==""){
		return "";
	}
	var picture_urls=picture_url.split("|");
	var content="";
	
	for(var i=0;i<picture_urls.length;i++){
		content+='<div class="img-new-container"><img src="../'+picture_urls[i]+'"  class="img-thumbnail" alt=""/></div>';
	}
	return content;
}

function checkNewContent() {
	var obj_content = $("#new-content");
	var obj_button = $("#new-publish");
	var content = obj_content.text();
	if (content === "") {
		obj_button.addClass("disabled");
		obj_button.removeAttr("onclick");
	}
	obj_content.on("keyup", function() {
		var content = $(this).text();
		if (content === "") {
			obj_button.addClass("disabled");
			obj_button.removeAttr("onclick");
		} else {
			obj_button.removeClass("disabled");
			obj_button.attr("onclick","addNew()");
		}
	});
}

function createNewJson(){
	var json={"content":"","picture_urls":[]};
	var content=$("#new-content").text();
	var pics=$("#media-pic").find("img");
	var ary=[];
	for(var i=0;i<pics.length;i++){
		var pic=pics[i];
		var value=$(pic).attr("src");
		var index=value.indexOf("/");
		value=value.substring(index+1);
		ary[i]=value;
	}
	json.content=content;
	json.picture_urls=ary;
	return json;
}

function addNew() {
	var url = "/GetFriends/newAction/addNew";
	var obj = createNewJson();
	$.ajax({
		url : url,
		type : "POST",
		data : JSON.stringify(obj),
		cache : false,
		dataType : "json",
		contentType:"application/json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result=="success"){
				showMessage("动态发表成功");
				redirectToIndex();
			}else{
				showMessage("动态发表失败");
			}
		}
	});
}

function hideCommentPane(id,idType){
	$("#comment_content_"+idType+"_"+id).hide();
}

function showCommentPane(id,idType){
	$("#comment_content_"+idType+"_"+id).show();
	getComments(id,idType);
}

function addComment(new_id,idType){
	var url = "/GetFriends/commentAction/addComment";
	var content = $("#comment-tip-"+idType+"-"+new_id).text();
	if(content===""){
		showMessage("请填写评论内容");
		return;
	}
	$.ajax({
		url : url,
		type : "POST",
		data : "comment_content=" + content+"&new_id="+new_id,
		cache : false,
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result=="success"){
				showMessage("评论成功");
				getComments(new_id,idType);
			}else{
				showMessage("评论失败");
			}
		}
	});
}

function getComments(new_id,idType){
	var url = "/GetFriends/commentAction/getComments";
	$.ajax({
		url : url,
		type : "POST",
		data : "new_id="+new_id,
		cache : false,
		dataType : "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			var content="";
			// alert(data.length);data.commenter_id.picture_url
			for(var i=0;i<data.length;i++){
				var comment_info=data[i];
				var type="";
				comment_info.commenter_id.user_id===comment_info.new_id.ower_id.user_id?type="回复":type="评论";
				content+='<div class="comment-info"><img width="30px" height=30px" src="../'+comment_info.commenter_id.picture_url+'" alt="" />';
				content+=' <b>'+comment_info.commenter_id.user_name+'</b> '+comment_info.create_date+' '+type+' <p class="comm-detail-info">'+comment_info.content+'</p></div>';
			}
			var obj=$("#content_pane_"+idType+"_"+new_id);
			obj.siblings().remove();
			obj.html(content).after(appendCommentBox(new_id,idType)).before(appendHideComment_a(new_id,idType));
		}
	});
}

function appendCommentBox(new_id,idType){
	var content="";
	content+='<div class="comment-box"><img width="30px" height="30px" src="../'+currentUser.picture_url+'" />'+currentUser.user_name+'<br />';
	content+='<div contenteditable="true" class="comment-tip pull-left" id="comment-tip-'+idType+"-"+new_id+'"></div>';
	content+='<button class="btn btn-default pull-right" onclick="addComment('+new_id+',\''+idType+'\')">发表评论</button>';
	content+='<div style="clear: both"></div></div>';
	return content;
}

function appendHideComment_a(new_id,idType){
	var content='<div><a href="javascript:hideCommentPane('+new_id+',\''+idType+'\');">收起评论</a></div>';
	return content;
}


function getFriends(pageNum,pageTotal){
	// 双重验证
	if(!(pageNum<=pageTotal&&pageNum>0)){return;}
	var url = "/GetFriends/newAction/getFriends";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"pageNum="+pageNum,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			
			if(data.data.data.length===0){
				var obj=$("#friend-container");
				var content='<div style="text-align:center">暂时没有任何好友，快去添加好友吧！</div>';
				obj.html(content);
			}else if(data.code===-1){
				showMessage(data.message);
				redirectToLogin();
			}else{
				var friends=data.data.data;
				var content=createFriendCard(friends,"words");	
				var obj=$("#friend-container");
				obj.html(content);
				obj.append(pageUtilForFriend(data));
			}
		}
	});
}

function createFriendCard(friends,flag){
	var content="";
	for(var i=0;i<friends.length;i++){
		var friend=friends[i];
		var sex="";
		var dbSex=friend.sex;	
		var op="";
		if(!dbSex||dbSex==""||dbSex==null){
			sex="unknow.png";
		}else if(dbSex==="男"){
			sex="man.png";
		}else{
			sex="woman.png";
		}
		if(flag==="words"){
			op='<button class="btn btn-default btn-xs" data-friend=\''+JSON.stringify(friend)+'\' data-toggle="modal" data-target="#friendWordsModal">留言</button>';
		}else if(flag==="add"){
			op='<button class="btn btn-default btn-xs" data-friend=\''+JSON.stringify(friend)+'\' data-toggle="modal" data-target="#addFriendModal">添加</button>';
		}
		// console.log(JSON.stringify(friend));
		content+='<div class="friend-card"><img width="45px" height="45px" src="../'+friend.picture_url+'" alt="" />';
		content+='<b>'+friend.user_name+'</b> <img width="15px" height="15px" src="../icon/'+sex+'" alt="" style="float:right"/><br/>';
		content+='<label class="friend-mail">'+friend.email+'</label>'+op+' </div>';
	}
	content+='<div style="clear:both"></div>';
	return content;
}

/* 4/23 */
function checkActivityContent(){
	var obj_content=$("#activity-content");
	var obj_button=$("#activity-button");
	var inputs=$("#activity-box").find("input");
	var content = obj_content.text();
	if (content === "") {
		obj_button.addClass("disabled");
		obj_button.removeAttr("onclick");
	}

	inputs.on("keyup", function() {
		if (!checkInputs(inputs)||obj_content.text()==="") {
			obj_button.addClass("disabled");
			obj_button.removeAttr("onclick");
		} else {
			obj_button.removeClass("disabled");
			obj_button.attr("onclick","addActivity()");
		}
	});
	obj_content.on("keyup", function() {
		var content = $(this).text();
		if (content === ""||!checkInputs(inputs)) {
			obj_button.addClass("disabled");
			obj_button.removeAttr("onclick");
		} else {
			obj_button.removeClass("disabled");
			obj_button.attr("onclick","addActivity()");
		}
	});
}

function checkInputs(inputs){
	for(var i=0;i<inputs.length;i++){
		if($(inputs[i]).val()===""){
			return false;
		}
	}
	return true;
}

function addActivity(){
	var max_num=$("#activity-max-num").val();
	if(!(max_num<1000&&max_num>0)){
		showMessage("最大人数应在0-1000之间");
		return;
	}
	var inputs=$("#activity-info").find("input");
	var address=$(inputs[1]).val()+"-"+$(inputs[2]).val()+"-"+$(inputs[3]).val()+"-"+$(inputs[4]).val();
	var topic=$(inputs[0]).val();
	var max_num=$(inputs[5]).val();
	var content=$("#activity-content").text();
	var url = "/GetFriends/activityAction/addActivity";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		data:"topic="+topic+"&address="+address+"&max_num="+max_num+"&content="+content,
		dataType: "json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result=="success"){
				showMessage("发布成功");
				getActivities("activity_container",1);
				// 清空原内容+恢复界面
				$("#activity-box").hide();
				$("#activity-publish").show("fast");
				inputs.val("");
				$("#activity-content").empty();
				fillAddress("activity-info",1);
			}else{
				showMessage("发布失败");
			}
		}
	});
}

function getActivities(containerId,pageNum){
	var url = "/GetFriends/activityAction/selectActivity";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"type="+containerId+"&pageNum="+pageNum,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			 console.log(data);
			if(data.result.code===-1){
				showMessage(data.result.message);
				redirectToLogin();
			}else{
				
				var activities=data.result.data.data;
				var currentUser=data.currentUser;
				var content="";
				if(activities.length==0){
					content+='<div style="text-align:center">暂时没有相关活动</div>';
				}
				for(var i=0;i<activities.length;i++){
					var activity=activities[i];
					var state=activity.state===-1?"已结束":"进行中";	
					var contentType='';
					if(currentUser.user_id===activity.owner_id.user_id){
						contentType='<label class="pull-right btn-comment">你发布的</label>';
					}else if(judgeIn(activity.joiners,currentUser)){
						contentType='<label class="pull-right btn-comment">你已参与</label>';
					}else if(activity.joiners.length>=activity.max_num){
						contentType='<label class="pull-right btn-comment">活动人数已满</label>';
					}else{
						contentType='<button class="btn btn-default pull-right btn-comment" onclick="addJoin('+activity.activity_id+','+activity.max_num+','+activity.joiners.length+')">参与</button>';
					}
					content+='<div class="new-info" data-pagenum="'+data.result.pageNumber+'" data-pagetotal="'+data.result.pageTotal+'"><div class="self-info"><img width="45px" height="45px" src="../'+activity.owner_id.picture_url+'" alt=""/>';
					content+='<b>'+activity.owner_id.user_name+'</b><p class="publish-date">发布时间：'+activity.create_date+' 状态：'+state+'</p></div>';
					content+='<div class="text-content">活动主题：'+activity.topic+'</div><div class="text-content">活动内容：'+activity.content+'</div><div class="text-content">活动地点：'+activity.address+'</div><div class="text-content">最大人数：'+activity.max_num+'人 <a href="javascript:void(0)" onclick="toggleJoinsPane('+activity.activity_id+',\''+containerId+'\')">当前人数：'+activity.joiners.length+'人</a></div>';
					content+='<div class="comment-content">'+contentType+'<div style="clear: both"></div></div>';
					content+=appendJoinsPane(activity,containerId);
				}
				var obj=$("#"+containerId);
				removeLoadingBox(obj);  	
				if(pageNum===1){
					obj.html(content);
				}else{
					obj.append(content);
				}
				scrollPageUtil.isLoading=false;
			}
		}
	});
}

function judgeIn(joins,user){
	if(joins.length==0){return false;}
	var isIn=false;
	for(var i=0;i<joins.length;i++){
		if(joins[i].user.user_id===user.user_id){
			isIn=true;
			break;
		}
	}
	return isIn;
}

function toggleJoinsPane(activity_id,containerId){
	$("#joiner-pane-"+containerId+activity_id).toggle();
}

function appendJoinsPane(activity,containerId){
	var content='<div class="joiner-pane" id="joiner-pane-'+containerId+activity.activity_id+'">已经参与的：';
	var joiners=activity.joiners;
	if(joiners==null||joiners.length===0){
		content+="暂时没有人参与";
		if(containerId==="activity_container_my"){
			var stateInfo=activity.state===-1?"已结束":'<a href="javascript:setActivityFinished('+activity.activity_id+')">结束</a>';	
			content+='</div><div class="message-friend-operation"><ul><li>'+stateInfo+'</li></ul></div></div>';
		}else if(containerId==="activity_container_in"){
			content+='</div><div class="message-friend-operation"><ul><li><a href="javascript:quitJoin('+activity.activity_id+')">退出</a></li></ul></div></div>';
		}else{	
			content+='</div></div>';
		}
		return content;
	}
	for(var i=0;i<joiners.length;i++){
		var joiner=joiners[i];
		content+=joiner.user.user_name+';';
	}
	// content.substring(0,content.length-1);
	if(containerId==="activity_container_my"){
		var stateInfo=activity.state===-1?"已结束":'<a href="javascript:setActivityFinished('+activity.activity_id+')">结束</a>';	
		content+='</div><div class="message-friend-operation"><ul><li>'+stateInfo+'</li></ul></div></div>';
	}else if(containerId==="activity_container_in"){
		content+='</div><div class="message-friend-operation"><ul><li><a href="javascript:quitJoin('+activity.activity_id+')">退出</a></li></ul></div></div>';
	}else{
		content+='</div></div>';
	}
	return content;
}

function setActivityFinished(activity_id){
	var activity={"activity_id":activity_id,"state":-1};
	updateActivity(activity);
}

function updateActivity(activity){
	var url = "/GetFriends/activityAction/updateActivity";	
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:JSON.stringify(activity),// JSON.stringify(friend)
		contentType:"application/json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			if(data.result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(data.result=="success"){
				showMessage("处理成功");
				// 刷新页面
				getActivities("activity_container_my",1);
			}else{
				showMessage("处理失败");
			}
		}
	});
}


function addJoin(activity_id,max_num,cur_num){
	if(cur_num>=max_num){
		showMessage("该活动人数已满");
		// 多人同时操作的情况，服务器端还需要做一次验证
		return;
	}
	var url = "/GetFriends/joinAction/addJoin";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"activity_id="+activity_id,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result=="success"){
				showMessage("参与成功");
				getActivities("activity_container",1);// 刷新页面
			}else if(result=="full"){
				showMessage("当前活动过于火爆，人数已满");
				getActivities("activity_container",1);// 刷新页面
			}else{
				showMessage("参与失败");
			}
		}
	});
}


function quitJoin(activity_id){
	var url = "/GetFriends/joinAction/quitJoin";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"activity_id="+activity_id,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result=="success"){
				showMessage("退出成功");
				getActivities("activity_container_in",1);// 刷新页面
			}else{
				showMessage("退出失败");
			}
		}
	});
}

function searchUser(flag){
	var inputId=flag===0?"friend-search-notInput":"friend-search-isInput";
	var resultPaneId=flag===0?"friend-search-result-not":"friend-search-result-is";
	var input=$("#"+inputId);
	var key=input.val();
	if(key===null||key===""){
		showMessage("请输入关键字");
		return;
	}
	var url = "/GetFriends/userAction/searchUser";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"key="+key+"&flag="+flag,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			var result=data.result;
			if(result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result==null){
				showMessage("没有符合条件的好友");
			}else{
				var content='<p>查询结果 <a href="javascript:searchFriendsPaneRemove(\''+resultPaneId+'\')">&times</a></p>';
				if(flag==0){
					content+=createFriendCard(result, "add");
				}else{
					content+=createFriendCard(result, "words");
				}	
				$("#"+resultPaneId).html(content);
			}
		}
	});
}

function searchFriendsPaneRemove(resultPaneId){
	$("#"+resultPaneId).empty();
}

/* 5/2 */
function initForFriendModal(){
	$("#addFriendModal").on("show.bs.modal",function(e){
		var button=$(e.relatedTarget);
		var friend=button.data("friend");
		var modal=$(this);
	    modal.find(".modal-title").text("添加好友-"+friend.user_name);
	    var obj_button=modal.find("#sendAddFriendMessageBtn");
	    obj_button.off("click");
	    obj_button.on("click",function(e){
	    	sendAddFriendMessage(friend);
	    });
	});
	$("#friendWordsModal").on("show.bs.modal",function(e){
		var editBox=$(this).find(".edit-box");
		editBox.focus().css({"border":"2px solid #ddd"});
		var button=$(e.relatedTarget);
		var friend=button.data("friend");
		var modal=$(this);
	    modal.find(".modal-title").text("留言好友-"+friend.user_name);
	    var obj_button=modal.find("#sendFriendWordsMessageBtn");
	    obj_button.off("click");
	    obj_button.on("click",function(e){
	    	sendFriendWordsMessage(friend);
	    });
	});
}

function sendAddFriendMessage(friend){
	var content=$("#addFriendModal").find("input").val();
	if(content==null||content==""){
		showMessage("请填写验证信息");
		return;
	}
	sendMessage(friend,content,"好友申请","friend");
}

function sendFriendWordsMessage(friend){
	var content=$("#friendWordsModal").find(".edit-box").text();
	if(content==null||content==""){
		showMessage("请填写留言信息");
		return;
	}
	sendMessage(friend,content,"留言","words");
}


function sendMessage(friend,content,tips,flag,message){
	var url = "/GetFriends/messageAction/sendMessage?content="+content+"&flag="+flag;	
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:JSON.stringify(friend),// JSON.stringify(friend)
		contentType:"application/json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			if(data.result=="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(data.result=="success"){
				showMessage(tips+"发送成功");
				if(message){
					updateMessage(message);
				}
			}else{
				showMessage(tips+"发送失败");
			}
		}
	});
}

function getMessageByType(type,containerId,pageNum){
	var url = "/GetFriends/messageAction/getMessageByType";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"type="+type+"&pageNum="+pageNum,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			console.log(data);
			var container=$("#"+containerId);
			var content="";
			if(data.code===-1){
				showMessage(data.message);
				redirectToLogin();
			}else{
				if(containerId==="message_friend"){
					content=createMessageForFriend(data);
				}else if(containerId==="message_words"){
					content=createMessageForWords(data);
				}else{
					content=createMessageForSystem(data);
				}
			}
			removeLoadingBox($("#"+containerId));  	
			if(pageNum===1){
				container.html(content);
			}else{
				container.append(content);
			}
			scrollPageUtil.isLoading=false;
		}
	});
}


function createMessageForFriend(data){
	var content="";
	var messages=data.data.data;
	if(data.pageNumber===1&&messages.length===0){
		content+='<div style="text-align:center">暂时没有好友申请通知</div>';
	}else{
		
		for(var i=0;i<messages.length;i++){
			var message=messages[i];
			content+='<div class="message-friend" data-pagenum="'+data.pageNumber+'" data-pagetotal="'+data.pageTotal+'"><div class="self-info"><img width="45px" height="45px" src="../'+message.from_id.picture_url+'" alt=""/>';
			content+='<b>'+message.from_id.user_name+'</b><p class="publish-date">时间：'+message.create_date+'</p></div>';
			if(message.type===10){
				content+='<div class="text-content">请求添加你为好友，验证消息如下：<br/>'+message.content+'</div>';
				if(message.isDeal==1){
					content+='<div class="message-friend-operation">已处理</div></div>';
				}else{
					content+='<div class="message-friend-operation"><ul><li><a href="javascript:friendOperation(\'agree\','+message.message_id+','+message.from_id.user_id+')">同意</a></li><li><a href="javascript:friendOperation(\'refuse\','+message.message_id+','+message.from_id.user_id+')">拒绝</a></li><li><a href="javascript:friendOperation(\'ignore\','+message.message_id+','+message.from_id.user_id+')">忽略</a></li></ul></div></div>';
				}
			}else{
				content+='<div class="text-content">'+message.content+'</div></div>';
			}
		}		
	}
	return content;
}

function createMessageForWords(data){
	var content="";
	var messages=data.data.data;
	if(data.pageNumber===1&&messages.length===0){
		content+='<div style="text-align:center">暂时没有好友留言消息</div>';
	}else{
		
		for(var i=0;i<messages.length;i++){
			var message=messages[i];
			content+='<div class="message-friend" data-pagenum="'+data.pageNumber+'" data-pagetotal="'+data.pageTotal+'"><div class="self-info"><img width="45px" height="45px" src="../'+message.from_id.picture_url+'" alt=""/>';
			content+='<b>'+message.from_id.user_name+'</b><p class="publish-date">时间：'+message.create_date+'</p></div>';
			content+='<div class="text-content">'+message.content+'</div></div>';
		}		
	}
	return content;
}

function createMessageForSystem(data){
	var content="";
	var messages=data.data.data;
	if(data.pageNumber===1&&messages.length===0){
		content+='<div style="text-align:center">暂时没有系统消息</div>';
	}else{
		
		for(var i=0;i<messages.length;i++){
			var message=messages[i];
			content+='<div class="message-friend" data-pagenum="'+data.pageNumber+'" data-pagetotal="'+data.pageTotal+'"><div class="self-info"><img width="45px" height="45px" src="../'+message.from_id.picture_url+'" alt=""/>';
			content+='<b>'+message.from_id.user_name+'</b><p class="publish-date">时间：'+message.create_date+'</p></div>';
			content+='<div class="text-content">'+message.content+'</div></div>';
		}		
	}
	return content;
}

function friendOperation(way,message_id,to_user_id){
	var friend={"user_id":to_user_id};
	var message={"message_id":message_id,"isDeal":1};
	if(way==="agree"){
		// 插入成为朋友数据+发送同意的消息+将此消息置为已处理状态
		addFriend(friend,message);
	}else if(way==="refuse"){
		// 发送拒绝消息+将此消息置为已处理状态
		sendMessage(friend,"对方拒绝添加您为好友","回执","reply",message);
	}else{
		// 将此消息置为已处理状态
		updateMessage(message);
	}
	// 刷新页面 不能再此处更新，因为是异步处理
	// getMessageByType(1,"message_friend");
}

function addFriend(friend,message){
	var url="/GetFriends/userAction/addFriend";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:"friend_id="+friend.user_id,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			if(data.result==="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(data.result==="success"){
				sendMessage(friend,"对方同意添加您为好友","回执","reply",message);
			}else{
				showMessage("系统出现错误");
			}
		}
	});
}

function setMessageIsReaded(){
	var message={"isRead":1};
	updateMessage(message,flushPageAboutMessage);
}

function flushPageAboutMessage(){
	$("#messageSizeBadge").html("");
}

function updateMessage(message,flush){
	var url="/GetFriends/messageAction/updateMessage";
	$.ajax({
		url : url,
		type:"POST",
		cache : false,
		dataType : "json",
		data:JSON.stringify(message),
		contentType:"application/json",
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + ":" + textStatus + ":" + errorThrown);
		},
		success : function(data) {
			// console.log(JSON.stringify(data));
			var result=data.result;
			if(data.result==="noLogin"){
				showMessage("由于长时间没有操作，需要重新登录");
				redirectToLogin();
			}else if(result==="success"){
				// 已读的话刷新页面
				if(flush){
					// 说明点击消息
					flush();
				}else{
					// 进行朋友操做
					getMessageByType(1,"message_friend",1);
				}
			}else{
				showMessage("系统出现未知错误");
			}
		}
	});
}

// 面板切换的全局方法 写在最后，持续补充
function showPane(id,obj){
	$(obj).addClass("active").siblings().removeClass("active");
	$("#"+id).show().siblings().hide();
	if(id==="friendPane"){
		getFriends(1,1);
		global_type="";
		// 设置默认第一个tab页显示
		$("#myTab_friend a:first").tab("show");
	}else if(id==="activityPane"){
		getActivities("activity_container",1);
		scrollPageUtil=createScollPageUtil("activity_container");
		global_type="activity_container";
		// 设置默认第一个tab页显示
		$("#myTab_activity a:first").tab("show");
	}else if(id==="messagePane"){
		getMessageByType(1,"message_friend",1);
		scrollPageUtil=createScollPageUtil("message_friend");
		global_type="message_friend";
		// 消息置为已读，同时界面刷新
		setMessageIsReaded();
		// 设置默认第一个tab页显示
		$("#myTab_message a:first").tab("show");
	}else if(id==="newPane"){
		getNews("new_friend",1);
		scrollPageUtil=createScollPageUtil("new_friend");
		global_type="new_friend";
		// 设置默认第一个tab页显示
		$("#myTab_new a:first").tab("show");
	}else{
		// 个人资料
		getCurrentUser();
		fillPrimaryInfo();
		global_type="";
		// 设置默认第一个tab页显示
		$("#myTab_self a:first").tab("show");
	}
}

function showModify(){
	$("#modifySelfBtn").text("取消").attr("onclick","cancelModify()");
	var obj=$("#selfInfo_form");
	var showElement=obj.find(".show-info");
	showElement.hide();
	var inputs=obj.find("input");
	var modifyElement=obj.find(".modify");
	inputs.show();
	modifyElement.show();
	$("#saveModify").show();
	$("#iconForm").find("input").show();
	$("#uploadBtn").show();
}

function cancelModify(){
	$("#modifySelfBtn").text("编辑").attr("onclick","showModify()");
	var obj=$("#selfInfo_form");
	var showElement=obj.find(".show-info");
	showElement.show();
	var inputs=obj.find("input");
	var modifyElement=obj.find(".modify");
	inputs.hide();
	modifyElement.hide();
	$("#saveModify").hide();
	$("#iconForm").find("input").hide();
	$("#uploadBtn").hide();
}

function fillPrimaryInfo(){
	var obj=$("#selfInfo_form");
	var inputs=obj.find("input");
	var modifyElement=obj.find(".modify");
	inputs.hide();
	modifyElement.hide();
	$("#iconForm").find("input").hide();
	$("#uploadBtn").hide();
	var showElement=obj.find(".show-info");
	showElement.show();
	$("#saveModify").hide();
	$("#modifySelfBtn").text("编辑").attr("onclick","showModify()");
	if(currentUser.user_name!=null&&currentUser.user_name!=""){
		$(showElement[0]).text(currentUser.user_name);
		$(inputs[0]).val(currentUser.user_name);
	}else{
		$(showElement[0]).text("未完善");
	}
	if(currentUser.school!=null&&currentUser.school!=""){
		$(showElement[1]).text(currentUser.school);
		$(inputs[1]).val(currentUser.school);
	}else{
		$(showElement[1]).text("未完善");
	}
	if(currentUser.sex!=null&&currentUser.sex!=""){
		$(showElement[2]).text(currentUser.sex);
		if(currentUser.sex==="男"){
			$(inputs[2]).attr("checked","checked");
			$(inputs[3]).removeAttr("checked");
		}else{
			$(inputs[3]).attr("checked","checked");
			$(inputs[2]).removeAttr("checked");
		}
	}else{
		$(showElement[2]).text("未完善");
	}
	if(currentUser.birthday!=null&&currentUser.birthday!=""){
		$(showElement[3]).text(currentUser.birthday.substring(0,10));
		$(inputs[4]).val(currentUser.birthday.substring(0,10));
	}else{
		$(showElement[3]).text("未完善");
	}
	if(currentUser.telephone!=null&&currentUser.telephone!=""){
		$(showElement[4]).text(currentUser.telephone);
		$(inputs[5]).val(currentUser.telephone);
	}else{
		$(showElement[4]).text("未完善");
	}
	if(currentUser.address!=null&&currentUser.address!=""&&currentUser.address!="---"){
		$(showElement[5]).text(currentUser.address);
		var ads=currentUser.address.split("-");
		for(i=0;i<ads.length;i++){
			$(inputs[i+6]).val(ads[i]);
		}
	}else{
		$(showElement[5]).text("未完善");
		fillAddress("self-info-address", 0);
	}
	if(currentUser.picture_url!=null&&currentUser.picture_url!=""){
		$(inputs[10]).val(currentUser.picture_url);
		$("#iconContainer").attr("src","../"+currentUser.picture_url);
	}
}

function uploadPic(){
	var form=$("#picForm")[0];
	var inputs=$(form).find("input");
	if(!(checkEmpty(inputs))){
		showMessage("必须选择至少一个文件");
   	 return;
	}
	if(!formatCheckArray(inputs)){
		showMessage("图片格式必须是png,jpg或jepg");
    	return;
    }
	 var formData = new FormData(form);  
	 var url="/GetFriends/newAction/uploadPic";
     $.ajax({  
          url: url,  
          type: 'POST',  
          data: formData,  
          cache: false,  
          processData: false,  
          contentType: false,
          dataType:"json",
          error: function (data) {  
              showMessage("上传失败");
              console.log(data);
          } ,
          success: function (data) {  
        	  // console.log(data);
        	  if(data.result=="noLogin"){
        		  showMessage("由于长时间没有操作，需要重新登录");
        		  redirectToLogin();
        	  }else if(data.result=="success"){
        		  showMessage("上传成功");
        		  createImgForNew("media-pic",data.picture_urls);
        	  }else{
        		  showMessage("上传失败");
        	  }
          } 
     });  
}

function initForNewPicModal(){
	$("#addPicForNewModal").on("show.bs.modal",function(e){
		//初始化input
		var modal=$(this);
		var num=$("#media-pic").find("img").length;
		var container=$("#pic_new_fileInput");
		var size=6-parseInt(num);
		container.html(createFileInput(size));
	    modal.find(".modal-footer p").text("共可添加6张图片，您已添加"+num+"张");  
	});
}

function createFileInput(size){
	var content="";
	for(var i=0;i<size;i++){
		content+='<input type="file" name="new_pic_'+(i+1)+'" id="new_pic_'+(i+1)+'" class="form-control"/>';
	}
	return content;
}

function createImgForNew(containerId,picture_urls){
	var content="";
	for(var i=0;i<picture_urls.length;i++){
		content+='<div class="img-new-container"><img src="../'+picture_urls[i]+'"  class="img-thumbnail" alt=""/><a href="javascript:void(0)" onclick="deletePic(this)" class="delete-pic-a">&times;</a></div>';
	}
	$("#"+containerId).append(content);
}

function deletePic(obj){
	$(obj).parent().remove();
}

function checkEmpty(inputs){
	var bool=false;
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		var value=$(input).val();
		if(!(value==null||value==="")){
			bool=true;
			break;
		}
	}
	return bool;
}

function formatCheckArray(inputs){
	var bool=true;
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		var value=$(input).val();
		if(value!=null&&value!=""){
			var index=value.lastIndexOf(".");
			if(index===-1){
				bool=false;
				break;
			}
			var format=value.substring(index+1);
			if(!(format==="png"||format==="jepg"||format==="jpg")){
				bool= false;
				break;
			}
		}
	}
	return bool;
}

function doUpload(){
     var iconFile=$("#icon_file_input").val();
     if(iconFile==null||iconFile===""){
    	 showMessage("必须选择上传文件");
    	 return;
     }
     if(!formatCheck(iconFile)){
    	 showMessage("图片格式必须是png,jpg或jepg");
    	 return;
     }
     
	 var formData = new FormData($("#iconForm")[0]);  
	 var url="/GetFriends/userAction/changeIcon";
     $.ajax({  
          url: url,  
          type: 'POST',  
          data: formData,  
          cache: false,  
          processData: false,  
          contentType: false,
          dataType:"json",
          error: function (data) {  
              showMessage("上传失败");
              console.log(data);
          } ,
          success: function (data) {  
        	  // console.log(data);
        	  if(data.result=="noLogin"){
        		  showMessage("由于长时间没有操作，需要重新登录");
        		  redirectToLogin();
        	  }else if(data.result=="success"){
        		  showMessage("上传成功");
        		  $("#picture_url_input").val(data.picture_url);
        		  $("#iconContainer").attr("src","../"+data.picture_url);
        	  }else{
        		  showMessage("上传失败");
        	  }
          } 
     });  
}

function formatCheck(file){
	var index=file.lastIndexOf(".");
	if(index===-1){
		return false;
	}
	var format=file.substring(index+1);
	if(format==="png"||format==="jepg"||format==="jpg"){
		return true;
	}else{
		return false;
	}
}

function modifySelfInfo(){
	var inputs=$("#selfInfo_form").find("input");
	if($(inputs[0]).val().trim()===""){
		showMessage("用户名不能为空");
		return;
	}
	var url="/GetFriends/userAction/modifyUser";
	var obj=$('#selfInfo_form').serialize();
	var objnew=obj.replace(/\s+/g,"");
	$.ajax({  
	     url : url,  
	     type : "POST",  
	     data : objnew,  
	     cache: false,  
	     dataType:"json",
	     success : function(data) {  
	    	 if(data.result==="noLogin"){
	    		 showMessage("由于长时间没有操作，需要重新登录");
       		 	 redirectToLogin();
	    	 }else if(data.result==="success"){
	    		 showMessage("修改成功");
	    		 // session需要刷新
	    		 getCurrentUser();
	    	 }else{
	    		 showMessage("修改失败");
	    	 }
	     },  
	     error : function(data) {  
	         showMessage("修改失败");
	     }  
	});  
}

function modifyPwd(){
	var form=$("#modifyPwdForm");
	var inputs=form.find("input");
	var oldPwd=$(inputs[0]).val();
	var newPwd=$(inputs[1]).val();
	var surePwd=$(inputs[2]).val();
	if(oldPwd===null||oldPwd.trim()===""||newPwd===null||newPwd.trim()===""||surePwd===null||surePwd.trim()===""){
		showMessage("信息不能为空");
		return;
	}
	if(!(newPwd.trim()==surePwd.trim())){
		showMessage("密码不一致");
		return;
	}
	if(!(hex_md5(oldPwd.trim())===currentUser.password)){
		showMessage("原密码不正确");
		return;
	}
	var url="/GetFriends/userAction/modifyPassword";
	$.ajax({  
	     url : url,  
	     type : "POST",  
	     data : form.serialize(),  
	     cache: false,  
	     dataType:"json",
	     success : function(data) {  
	    	 if(data.result==="noLogin"){
	    		 showMessage("由于长时间没有操作，需要重新登录");
      		 	 redirectToLogin();
	    	 }else if(data.result==="success"){
	    		 showMessage("修改成功");
	    		 redirectToLogin();
	    	 }else if(data.result==="notComplete"){
	    		 showMessage("信息不完整");
	    	 }else if(data.result==="notRight"){
	    		 showMessage("原密码不对修改失败");
	    	 }else if(data.result==="notSame"){
	    		 showMessage("密码不一致");
	    	 }else{
	    		 showMessage("修改失败");
	    	 }
	     },  
	     error : function(data) {  
	         showMessage("修改失败");
	     }  
	});  
}

// 友好提示，取代alert
function showMessage(content){
	var box=$("#global-message-box");
	box.text(content).show();
	setTimeout(function(){
		box.hide();
	},1500);
}

function redirectToLogin(){
	setTimeout(function(){
		window.location.href="/GetFriends/userAction/goLogin";
	},1500);
}

function redirectToIndex(){
	setTimeout(function(){
		window.location.href="/GetFriends/zhiyin/index";
	},1500);
}

function pageUtilForFriend(data){
	var preInfo="";
	var nextInfo="";
	var prePage=data.pageNumber-1;
	var nextPage=data.pageNumber+1;
	var hrefOfPre='javascript:void(0)';
	var hrefOfNext='javascript:void(0)';
	if(data.pageTotal===1){
		preInfo='class="disabled"';
		nextInfo='class="disabled"';
		hrefOfPre='javascript:void(0)';
		hrefOfNext='javascript:void(0)';
	}else if(data.pageNumber<data.pageTotal){
		nextInfo="";
		hrefOfNext='javascript:getFriends('+nextPage+','+data.pageTotal+')';
		if(data.pageNumber===1){
			preInfo='class="disabled"';
			hrefOfPre='javascript:void(0)';
		}else{
			preInfo='';
			hrefOfPre='javascript:getFriends('+prePage+','+data.pageTotal+')';
		}
	}else if(data.pageNumber=data.pageTotal){
		nextInfo='class="disabled"';
		hrefOfNext='javascript:void(0)';
		preInfo='';
		hrefOfPre='javascript:getFriends('+prePage+','+data.pageTotal+')';
	}
	var content='<ul class="pager"><li '+preInfo+'><a href="'+hrefOfPre+'" >&larr;上一页</a></li><li '+nextInfo+'><a href="'+hrefOfNext+'" >&rarr;下一页</a></li></ul>';
	return content;
}

function appendLoadingBox(obj){
	var content='<div class="box"><div class="loader"><div class="loading-2"> <i></i> <i></i> <i></i> <i></i> <i></i> </div></div></div>';
	obj.append(content);
}

function removeLoadingBox(obj){
	obj.find(".box").remove();
}

