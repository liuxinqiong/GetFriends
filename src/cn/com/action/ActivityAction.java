package cn.com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.entity.JoinHelper;
import cn.com.entity.Mf_activity;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_activityService;
import cn.com.util.PaginationResult;
import cn.com.util.PojoDomain;

@Controller
@RequestMapping("/activityAction")
public class ActivityAction {
	@Resource
	Mf_activityService mf_activityService;

	@RequestMapping("/test")
	public void test() {
		Mf_activity activity = new Mf_activity();
		activity.setActivity_id("2016041000000001");
		List<JoinHelper> joiners = mf_activityService
				.selJoinersByActivity(activity);
		System.out.println(joiners);
	}

	@RequestMapping("/addActivity")
	public void addActivity(HttpServletRequest request,
			HttpServletResponse resp, @ModelAttribute Mf_activity activity)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			activity.setOwner_id(loginUser);
			int success = mf_activityService.addActivity(activity);
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

	@RequestMapping("/selectActivity")
	public void selectActivity(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String type,@RequestParam Integer pageNum) throws IOException {
		PaginationResult result = new PaginationResult();
		pageNum = pageNum==null?1:pageNum;
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder resultInfo = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		JSONObject resultJson=null;
		if (loginUser == null) {
			result.setMessage("由于长时间没有操作，需要重新登录");
			result.setCode(-1);
		} else {	
			PojoDomain<Mf_activity> activities=mf_activityService.selectActivityByType(loginUser, type, pageNum);
			result.getData().put("data", activities.getPojolist());
			result.setPageNumber(activities.getPage_number());
			result.setPageSize(activities.getPage_size());
			result.setPageTotal(activities.getPage_total());
			result.setTotalCount(activities.getTotal_count());
			result.setCode(0);
			resultJson=JSONObject.fromObject(result);	
			resultInfo.append("\"result\":" + resultJson.toString() + ",");
			// 当前用户也返回出去
			JSONObject currentUser = JSONObject.fromObject(loginUser);
			resultInfo.append("\"currentUser\":" + currentUser.toString() + "}");
		}
		out.print(resultInfo.toString());
		out.flush();
		out.close();
	}
	
	@RequestMapping("/updateActivity")
	@ResponseBody
	public void updateActivity(HttpServletRequest request,
			HttpServletResponse resp, @RequestBody Mf_activity activity) throws IOException{
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";	
		} else {
			int success=mf_activityService.updateActivity(activity);
			if(success>0){
				message = "success";	
			}else{
				message = "error";	
			}
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}
}
