package cn.com.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.com.entity.Mf_activity;
import cn.com.entity.Mf_join;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_activityService;
import cn.com.service.Mf_joinService;

@Controller
@RequestMapping("/joinAction")
public class JoinAction {

	@Resource
	Mf_joinService mf_joinService;
	@Resource
	Mf_activityService mf_activityService;

	@RequestMapping("addJoin")
	public void addJoinForActivity(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String activity_id)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		Mf_activity activity_db = mf_activityService
				.selectActivityById(activity_id);
		if (activity_db.getJoiners().size() >= activity_db.getMax_num()) {
			message = "full";
		} else if (loginUser == null) {
			message = "noLogin";
		} else {
			// 能进入这个方法的表示没有参与活动的
			Mf_join join = new Mf_join();
			join.setJoiner_id(loginUser);
			Mf_activity activity = new Mf_activity();
			activity.setActivity_id(activity_id);
			join.setActivity_id(activity);
			int success = mf_joinService.addJoinForActivity(join);
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
	
	@RequestMapping("/quitJoin")
	public void quitJoinForActivity(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String activity_id) throws IOException{
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			// 能进入这个方法的表示没有参与活动的
			Mf_join join = new Mf_join();
			join.setJoiner_id(loginUser);
			Mf_activity activity = new Mf_activity();
			activity.setActivity_id(activity_id);
			join.setActivity_id(activity);
			int success = mf_joinService.deleteJoiner(join);
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
}
