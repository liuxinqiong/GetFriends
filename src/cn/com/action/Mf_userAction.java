package cn.com.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.entity.Mf_fndpwd;
import cn.com.entity.Mf_friend;
import cn.com.entity.Mf_user;
import cn.com.service.Mf_userService;
import cn.com.util.IDUtil;
import cn.com.util.MD5Util;

@Controller
@RequestMapping(value = "/userAction")
public class Mf_userAction {
	@Resource
	private Mf_userService mf_userService;

	@RequestMapping(value = "/allUser", method = RequestMethod.GET)
	public String test() {
		List<Mf_user> users = mf_userService.selAllUsers();
		for (Mf_user mf_user : users) {
			System.out.println(mf_user.getUser_id());
			System.out.println(mf_user.getBirthday());
		}
		return "index";
	}

	@RequestMapping(value = "/goRegister")
	public String goRegister() {
		return "register";
	}

	@RequestMapping(value = "/register")
	public String register(HttpServletRequest request,
			@RequestParam String email, @RequestParam String password,
			@RequestParam String code) {
		HttpSession session = request.getSession();
		String rightCode = (String) session.getAttribute("code");
		if (rightCode.equalsIgnoreCase(code)) {
			Mf_user user = mf_userService.userPrimaryinfoWrap(email,
					MD5Util.hash(password));
			int success = mf_userService.register(user, password);
			if (success == 1) {
				// 放入session，表示直接登录了 但是需要注意的是此时是没有user_id的
				user = mf_userService.login(user);
				session.setAttribute("loginUser", user);
				return "redirect:/zhiyin/index";
			} else {
				request.setAttribute("message", "注册失败");
				return "register";
			}
		} else {
			request.setAttribute("message", "验证码错误");
			return "register";
		}
	}

	@RequestMapping(value = "/goLogin")
	public String goLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0)
			return "login";
		Cookie userCookie = null;
		for (Cookie cookie : cookies) {
			if ("allowUser".equals(cookie.getName())) {
				userCookie = cookie;
				break;
			}
		}
		if (userCookie == null) {
			return "login";
		}
		String[] infos = userCookie.getValue().split("/");
		request.setAttribute("email", infos[0]);
		request.setAttribute("password", infos[1]);
		return "login";
	}

	@RequestMapping(value = "/appLogin")
	@ResponseBody
	public void appLogin(HttpServletRequest req, HttpServletResponse resp,
			 @RequestBody Mf_user user)
			throws IOException {
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
//		Mf_user user = mf_userService.userPrimaryinfoWrap(email,
//				MD5Util.hash(password));
		user.setPassword(MD5Util.hash(user.getPassword()));
		user = mf_userService.login(user);
		if (user != null) {
			message = "success";
		} else {
			message = "not exist";
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String email,
			@RequestParam String password, @RequestParam String code,
			@RequestParam String canNoPasswordLogin) {
		// 密码加密查询数据库
		// password=MD5Util.hash(password);
		HttpSession session = request.getSession();
		String rightCode = (String) session.getAttribute("code");
		if (rightCode.equalsIgnoreCase(code)) {
			Mf_user user = mf_userService.userPrimaryinfoWrap(email,
					MD5Util.hash(password));
			user = mf_userService.login(user);
			if (user != null) {
				// 实现免密码登录
				if (canNoPasswordLogin.contains("allow")) {
					// 表示需要免密码登录
					String value = email + "/" + password;
					Cookie cookie = new Cookie("allowUser", value);
					cookie.setMaxAge(60 * 60 * 24 * 7);
					response.addCookie(cookie);
				} else {
					// 删除之前存入的cookie
					Cookie[] cookies = request.getCookies();
					for (Cookie cookie : cookies) {
						if ("allowUser".equals(cookie.getName())) {
							cookie.setMaxAge(0);
							response.addCookie(cookie);
							break;
						}
					}
				}
				// 放入session，登录成功
				session.setAttribute("loginUser", user);
				return "redirect:/zhiyin/index";
			} else {
				request.setAttribute("message", "用户名或密码错误");
				return "forward:/userAction/goLogin";
			}
		} else {
			request.setAttribute("message", "验证码错误");
			return "forward:/userAction/goLogin";
		}
	}

	@RequestMapping("/searchUser")
	public void searchUser(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String key,
			@RequestParam int flag) throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
			result.append("\"result\":\"" + message + "\"}");
		} else {
			List<Mf_user> users = mf_userService.searchUserByKey(loginUser,
					flag, key);
			if (users == null || users.size() == 0) {
				message = null;
			} else {
				JSONArray userString = JSONArray.fromObject(users);
				message = userString.toString();
			}
			result.append("\"result\":" + message + "}");
		}

		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/addFriend")
	public void addFriend(HttpServletRequest request, HttpServletResponse resp,
			@RequestParam String friend_id) throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			Mf_friend friend = new Mf_friend();
			friend.setMy_id(loginUser.getUser_id());
			friend.setFriend_id(friend_id);
			int success = mf_userService.addFriend(friend);
			if (success > 0) {
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

	@RequestMapping("/checkEmail")
	public void isEmailRegister(HttpServletResponse resp,
			@RequestParam String email) throws IOException {
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		Mf_user user = new Mf_user();
		user.setEmail(email);
		Mf_user u = mf_userService.isEmailRegister(user);
		if (u != null) {
			message = "exist";
		} else {
			message = "notExist";
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/currentUser")
	public void getCurrentUser(HttpServletResponse resp,
			HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
			result.append("\"result\":\"" + message + "\"}");
		} else {
			JSONObject user_json = JSONObject.fromObject(loginUser);
			result.append("\"result\":" + user_json.toString() + "}");
		}
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping(value = "/goFindPwd")
	public String goFindPwd() {
		return "fndpwd";
	}

	@RequestMapping("/fndpwd")
	public void fndpwd(HttpServletRequest request, HttpServletResponse resp,
			@RequestParam String email) throws IOException {
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		Mf_user user = new Mf_user();
		user.setEmail(email);
		Mf_user u = mf_userService.isEmailRegister(user);
		if (u != null) {
			Mf_fndpwd f = new Mf_fndpwd();
			Timestamp outDate = new Timestamp(
					System.currentTimeMillis() + 60 * 60 * 1000);// 一小时过期
			long date = outDate.getTime() / 1000 * 1000; // 忽略毫秒数
			f.setTimestamp(date);
			f.setEmail(email);
			String key = UUID.randomUUID().toString();
			String secretKey = MD5Util.hash(key + email + date);// 生成密钥
			f.setSecretKey(secretKey);
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			int success = mf_userService.saveOrUpdateFndpwd(f, basePath);
			if (success > 0) {
				message = "success";
				// 发送邮件
			} else {
				message = "error";
			}
		} else {
			message = "notExist";
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/resetPwd")
	public ModelAndView resetPwd(@RequestParam String email,
			@RequestParam String sid) throws IOException {
		ModelAndView model = new ModelAndView("error");
		String message = "";
		if (sid.equals("") || email.equals("")) {
			message = "链接不完整,请重新生成";
			model.addObject("message", message);
			return model;
		}
		Mf_fndpwd fndpwd = new Mf_fndpwd();
		fndpwd.setEmail(email);
		fndpwd.setSecretKey(sid);
		Mf_fndpwd fndpwd_db = mf_userService.selFndPwd(fndpwd);
		if (fndpwd_db == null) {
			message = "链接错误,无法找到匹配用户,请重新申请找回密码.";
			model.addObject("message", message);
			return model;
		} else {
			long time = fndpwd_db.getTimestamp();
			if (time <= System.currentTimeMillis()) {
				message = "链接已经过期,请重新申请找回密码.";
				model.addObject("message", message);
				return model;
			}
			if (!sid.equals(fndpwd_db.getSecretKey())) {
				message = "链接不正确,是否已经过期了?重新申请吧";
				model.addObject("message", message);
				return model;
			}
		}
		model.setViewName("modifyPwd"); // 返回到修改密码的界面
		model.addObject("email", email);
		model.addObject("sid", sid);
		return model;
	}

	@RequestMapping("/modifyPwd")
	public ModelAndView modifyPwd(@RequestParam String sid,
			@RequestParam String email, @RequestParam String password) {
		ModelAndView model = new ModelAndView("error");
		String message = "";
		Mf_fndpwd fndpwd = new Mf_fndpwd();
		fndpwd.setEmail(email);
		Mf_fndpwd fndpwd_db = mf_userService.selFndPwd(fndpwd);
		if (fndpwd_db == null) {
			message = "链接错误,无法找到匹配用户,请重新申请找回密码.";
			model.addObject("message", message);
			return model;
		} else {
			long time = fndpwd_db.getTimestamp();
			if (time <= System.currentTimeMillis()) {
				message = "链接已经过期,请重新申请找回密码.";
				model.addObject("message", message);
				return model;
			}
			if (!sid.equals(fndpwd_db.getSecretKey())) {
				message = "链接不正确,是否已经过期了?重新申请吧";
				model.addObject("message", message);
				return model;
			}
		}
		// 验证通过
		Mf_user user = new Mf_user();
		user.setEmail(email);
		Mf_user user_db = mf_userService.isEmailRegister(user);
		if (user_db == null) {
			message = "链接错误,无法找到匹配用户,请重新申请找回密码.";
			model.addObject("message", message);
			return model;
		} else {
			user_db.setPassword(MD5Util.hash(password));
			int success = mf_userService.updateUser(user_db);
			if (success == 1) {
				model.setViewName("modify_success"); // 返回到修改密码的界面
				message = email + "用户密码修改成功";
				model.addObject("message", message);
				return model;
			} else {
				message = "系统错误，重新尝试一下！";
				model.addObject("message", message);
				return model;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/changeIcon")
	public void changeIcon(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String tempPath = context.getRealPath("/temp");
		File tmpFile = new File(tempPath);
		StringBuilder result = new StringBuilder("{");
		String message = "";
		if (!tmpFile.exists()) {
			tmpFile.mkdir();// 创建临时目录
		}
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		if (loginUser == null) {
			message = "noLogin";
			result.append("\"result\":\"" + message + "\"}");
			out.print(result.toString());
			out.flush();
			out.close();
			return;
		}
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(tempPath));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				return;
			}
			List<FileItem> files = upload.parseRequest(request);
			Iterator<FileItem> iterator = files.iterator();
			while (iterator.hasNext()) {
				FileItem item = iterator.next();
				if (!item.isFormField()) {
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt,处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					filename = IDUtil.getUUID()
							+ filename
									.substring(filename.lastIndexOf("\\") + 1);
					String savePath = context.getRealPath("/upload/icon")
							+ "\\" + loginUser.getUser_id();
					// 如果没有对应文件夹的话，要先创建文件夹
					File fileDir = new File(savePath);
					if (!fileDir.exists()) {
						fileDir.mkdir();
					}
					// 使用MD5加密的原因有两点：1.中文资源访问麻烦 2.url不能太长 注意格式问题
					int indexForTitle = filename.lastIndexOf(".");
					String titleByMd5 = MD5Util.hash(filename.substring(0,
							indexForTitle));
					String format = filename.substring(indexForTitle);
					String saveFile = savePath + "\\" + titleByMd5 + format;
					File fileDes = new File(saveFile);
					item.write(fileDes);
					item.delete();
					String picture_url = fileDes.getAbsolutePath();
					int index = picture_url.indexOf("upload");
					picture_url = picture_url.substring(index);
					message = "success";
					result.append("\"result\":\"" + message + "\",");
					result.append("\"picture_url\":\""
							+ picture_url.replace("\\", "/") + "\"}");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message = "error";
			result.append("\"result\":\"" + message + "\"}");
			e.printStackTrace();
		}
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/modifyUser")
	public void modifyUser(HttpServletResponse resp,
			HttpServletRequest request, @ModelAttribute Mf_user user)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			Mf_user userInfo = mf_userService.wrapUser(loginUser, user);
			int success = mf_userService.updateUser(userInfo);
			if (success > 0) {
				// 刷新session
				user = mf_userService.login(loginUser);
				session.setAttribute("loginUser", user);
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

	@RequestMapping("/modifyPassword")
	public void modifyPassword(HttpServletResponse resp,
			HttpServletRequest request, @RequestParam String oldPwd,
			@RequestParam String newPwd, @RequestParam String surePwd)
			throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		Mf_user user = new Mf_user();
		if (loginUser == null) {
			message = "noLogin";
		} else {
			if (oldPwd == null || oldPwd.trim().equals("") || newPwd == null
					|| newPwd.trim().equals("") || surePwd == null
					|| surePwd.trim().equals("")) {
				message = "notComplete";
			} else {

				if (!MD5Util.hash(oldPwd).equals(loginUser.getPassword())) {
					message = "notRight";
				} else {
					if (!newPwd.equals(surePwd)) {
						message = "notSame";
					} else {
						user.setPassword(MD5Util.hash(newPwd));
						user.setUser_id(loginUser.getUser_id());
						int success = mf_userService.updateUser(user);
						if (success > 0) {
							// 清除session，前台重新登录
							session.removeAttribute("loginUser");
							message = "success";
						} else {
							message = "error";
						}
					}
				}
			}
		}
		result.append("\"result\":\"" + message + "\"}");
		out.print(result.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("/logout")
	public String logout(HttpServletResponse resp, HttpServletRequest request)
			throws IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("loginUser");
		return "login";
	}
}
