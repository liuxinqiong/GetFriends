package cn.com.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.entity.Mf_new;
import cn.com.entity.Mf_user;
import cn.com.entity.NewHelper;
import cn.com.service.Mf_newService;
import cn.com.util.IDUtil;
import cn.com.util.MD5Util;
import cn.com.util.PaginationResult;
import cn.com.util.PojoDomain;

@Controller
@RequestMapping("/newAction")
public class NewAction {
	@Resource
	private Mf_newService mf_newService;

	@RequestMapping("/addNew")
	@ResponseBody
	public void addNew(HttpServletRequest request, HttpServletResponse resp,
			@RequestBody NewHelper obj) throws IOException {
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		StringBuilder result = new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		String message = "";
		if (loginUser == null) {
			message = "noLogin";
		} else {
			Mf_new n = mf_newService.createNewPojo(obj);
			n.setOwer_id(loginUser);
			int success = mf_newService.addNew(n);
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

	@SuppressWarnings("unchecked")
	@RequestMapping("/uploadPic")
	public void uploadPic(HttpServletRequest request,
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
			tmpFile.mkdir();// ������ʱĿ¼
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
				// ���մ�ͳ��ʽ��ȡ����
				return;
			}
			List<FileItem> files = upload.parseRequest(request);
			Iterator<FileItem> iterator = files.iterator();
			List<String> picPaths = new ArrayList<String>();
			while (iterator.hasNext()) {
				FileItem item = iterator.next();
				if (!item.isFormField()) {
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt,�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
					filename = IDUtil.getUUID()
							+ filename
									.substring(filename.lastIndexOf("\\") + 1);
					String savePath = context.getRealPath("/upload/new") + "\\"
							+ loginUser.getUser_id();
					// ���û�ж�Ӧ�ļ��еĻ���Ҫ�ȴ����ļ���
					File fileDir = new File(savePath);
					if (!fileDir.exists()) {
						fileDir.mkdir();
					}
					// ʹ��MD5���ܵ�ԭ�������㣺1.������Դ�����鷳 2.url����̫�� ע���ʽ����
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
					picture_url = picture_url.substring(index).replace("\\",
							"/");
					picPaths.add(picture_url);
					message = "success";
				}
			}
			result.append("\"result\":\"" + message + "\",");
			result.append("\"picture_urls\":"
					+ JSONArray.fromObject(picPaths).toString() + "}");
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

	@RequestMapping("/getFriends")
	public void getFriends(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam Integer pageNum)
			throws IOException {
		PaginationResult result = new PaginationResult();
		pageNum = pageNum == null ? 1 : pageNum;
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		// StringBuilder result=new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		JSONObject resultJson = null;
		if (loginUser == null) {
			result.setMessage("���ڳ�ʱ��û�в�������Ҫ���µ�¼");
			result.setCode(-1);
		} else {
			PojoDomain<Mf_user> friends = mf_newService.getFriendByUser(
					loginUser, pageNum);
			result.getData().put("data", friends.getPojolist());
			result.setPageNumber(friends.getPage_number());
			result.setPageSize(friends.getPage_size());
			result.setPageTotal(friends.getPage_total());
			result.setTotalCount(friends.getTotal_count());
			result.setCode(0);
			resultJson = JSONObject.fromObject(result);
		}
		out.print(resultJson.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("appGetNews")
	public void appGetNews(HttpServletRequest request,
			HttpServletResponse resp, @RequestParam String id,
			@RequestParam Integer pageNum) throws IOException {
		PaginationResult result = new PaginationResult();
		pageNum = pageNum == null ? 1 : pageNum;
		// HttpSession session=request.getSession();
		// Mf_user loginUser=(Mf_user) session.getAttribute("loginUser");
		Mf_user loginUser = new Mf_user();
		loginUser.setUser_id("2016041500000025");
		// StringBuilder result=new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		JSONObject resultJson = null;

		PojoDomain<Mf_new> news = mf_newService.getNewsByUser(loginUser, id,
				pageNum);
		result.getData().put("data", news.getPojolist());
		result.setPageNumber(news.getPage_number());
		result.setPageSize(news.getPage_size());
		result.setPageTotal(news.getPage_total());
		result.setTotalCount(news.getTotal_count());
		result.setCode(0);
		resultJson = JSONObject.fromObject(result);

		out.print(resultJson.toString());
		out.flush();
		out.close();
	}

	@RequestMapping("getNews")
	public void getNews(HttpServletRequest request, HttpServletResponse resp,
			@RequestParam String id, @RequestParam Integer pageNum)
			throws IOException {
		PaginationResult result = new PaginationResult();
		pageNum = pageNum == null ? 1 : pageNum;
		HttpSession session = request.getSession();
		Mf_user loginUser = (Mf_user) session.getAttribute("loginUser");
		// StringBuilder result=new StringBuilder("{");
		PrintWriter out = resp.getWriter();
		JSONObject resultJson = null;
		if (loginUser == null) {
			result.setMessage("���ڳ�ʱ��û�в�������Ҫ���µ�¼");
			result.setCode(-1);
		} else {
			PojoDomain<Mf_new> news = mf_newService.getNewsByUser(loginUser,
					id, pageNum);
			result.getData().put("data", news.getPojolist());
			result.setPageNumber(news.getPage_number());
			result.setPageSize(news.getPage_size());
			result.setPageTotal(news.getPage_total());
			result.setTotalCount(news.getTotal_count());
			result.setCode(0);
			resultJson = JSONObject.fromObject(result);
		}
		out.print(resultJson.toString());
		out.flush();
		out.close();
	}
}
