package cn.com.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.entity.Mf_message;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_messageService;
import cn.com.util.GlobalParam;
import cn.com.util.PojoDomain;

@Controller
@RequestMapping("/zhiyin")
public class IndexAction {
//	@Resource
//	private Mf_newService mf_newService;
	@Resource
	private Mf_messageService mf_messageService;
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request){
		//进入首页要封装好首页数据
		//1.动态信息 前台是用ajax得到
		HttpSession session=request.getSession();
		Mf_user loginUser=(Mf_user) session.getAttribute("loginUser");
//		PojoDomain<Mf_new> pojo=mf_newService.getNewsByUser(loginUser,"new_friend",1);
//		request.setAttribute("news", pojo.getPojolist());
//		request.setAttribute("pagetotal", pojo.getPage_total());
		
		//2.消息信息 -1表示未读已读全部查询
		PojoDomain<Mf_message> messages=mf_messageService.selectMessage(loginUser, GlobalParam.IS_READ_FALSE, GlobalParam.TYPE_ALL,1);
		Integer number=messages.getPojolist().size()==0?null:messages.getPojolist().size();
		request.setAttribute("notReadMessageSize",number);
		return "index";
	}
}
