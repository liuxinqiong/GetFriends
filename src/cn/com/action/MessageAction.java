package cn.com.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.entity.Mf_message;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_messageService;
import cn.com.util.GetRequestWrap;
import cn.com.util.GlobalParam;
import cn.com.util.PaginationResult;
import cn.com.util.PojoDomain;

@Controller
@RequestMapping("/messageAction")
public class MessageAction {
	@Resource
	private Mf_messageService mf_messageService;

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	@ResponseBody
	public void sendMessage(HttpServletRequest request,
			HttpServletResponse resp, @RequestBody Mf_user friend)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			// 因为前台使用post提交方式，进入编码过滤器时，并没有进入get的处理方式，因此这里手动调用一下，否则会乱码
			String content = new GetRequestWrap(request)
					.getParameter("content");
			String flag = new GetRequestWrap(request).getParameter("flag");
			int type = 0;
			if (flag.equals("friend")) {
				type = GlobalParam.TYPE_FRIEND;
			} else if (flag.equals("words")) {
				type = GlobalParam.TYPE_WORDS;
			} else if (flag.equals("system")) {
				type = GlobalParam.TYPE_SYSTEM;
			} else {
				type = GlobalParam.TYPE_REPLY;
			}
			Mf_message messageBean = new Mf_message();
			messageBean.setContent(content);
			messageBean.setFrom_id(loginUser);
			messageBean.setTo_id(friend);
			messageBean.setType(type);
			int success = mf_messageService.sendMessage(messageBean);
			if (success == 1) {
				message = "success";
			} else {
				message = "error";
			}
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/getMessageByType")
	public void getMessageByType(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam Integer type,@RequestParam Integer pageNum)
			throws IOException {
		PaginationResult result = new PaginationResult();
		pageNum = pageNum==null?1:pageNum;
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		//StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		JSONObject resultJson=null;
		if (loginUser == null) {
			result.setMessage("由于长时间没有操作，需要重新登录");
			result.setCode(-1);
		} else {
			PojoDomain<Mf_message> messages=mf_messageService.selectMessage(loginUser, GlobalParam.IS_READ_ALL, type, pageNum);
			result.getData().put("data", messages.getPojolist());
			result.setPageNumber(messages.getPage_number());
			result.setPageSize(messages.getPage_size());
			result.setPageTotal(messages.getPage_total());
			result.setTotalCount(messages.getTotal_count());
			result.setCode(0);
			resultJson=JSONObject.fromObject(result);		
		}
		out.print(resultJson.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/updateMessage")
	@ResponseBody
	public void updateMessage(HttpServletRequest request,
			HttpServletResponse resp, @RequestBody Mf_message message)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String resultInfo = "";
		if (loginUser == null) {
			resultInfo = "noLogin";
		} else {
			if(message.getIsRead()!=null){
				message.setTo_id(loginUser);
			}
			int success = mf_messageService.updateMessage(message);
			resultInfo = success != 0 ? "success" : "error";
		}
		result.append("\"result\":\"" + resultInfo + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}
}
