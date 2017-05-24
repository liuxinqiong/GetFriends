package cn.com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.entity.Mf_comment;
import cn.com.entity.Mf_new;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_commentService;

@Controller
@RequestMapping("/commentAction")
public class CommentAction {
	@Resource
	private Mf_commentService mf_commentService;
	
	@RequestMapping("/addComment")
	public void addComment(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String comment_content,
			@RequestParam String new_id) throws IOException {
		HttpSession session=request.getSession();
		Mf_user loginUser=(Mf_user) session.getAttribute("loginUser");
		StringBuilder result=new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message="";
		if(loginUser==null){
			message="noLogin";		
		}else{
			Mf_new n=new Mf_new();
			n.setNew_id(new_id);
			Mf_comment comment=new Mf_comment(n,comment_content,loginUser);
			int success=mf_commentService.addCommment(comment);
			if(success==1){
				message="success";
			}else{
				message="error";
			}
		}
		result.append("\"result\":\""+message+"\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}
	
	@RequestMapping("/getComments")
	public void getComments(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String new_id) throws IOException{
		Mf_new n=new Mf_new();
		n.setNew_id(new_id);
		List<Mf_comment> comments=mf_commentService.getCommentByNew(n);
		PrintWriter out = resp.getWriter();	
		out.print(JSONArray.fromObject(comments).toString());
		out.flush();
		out.close();
	}

}
