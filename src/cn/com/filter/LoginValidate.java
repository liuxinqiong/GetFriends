package cn.com.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class LoginValidate implements Filter {
	private FilterConfig filterConfig;
	private String filePath;
	private List<ValidateFile> validateFiles;
	
	public LoginValidate() {
		this.validateFiles = new ArrayList<ValidateFile>();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 会话对象
		HttpSession session = req.getSession(true);
		String requestPath = req.getServletPath();
		// 查询字符串 ?后边的内容
		String requestQuery = req.getQueryString();
		/*String path = req.getContextPath();
		//得到绝对路径
		String basePath = req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + path + "/";
		System.out.println(requestPath);
		System.out.println(requestQuery);
		System.out.println(path);
		System.out.println(basePath);*/
		// 检查需要登录验证
		if (checkFile(requestPath, requestQuery)) {
		//	System.out.println("enter-need");
			Object loginUser=session.getAttribute("loginUser") ;
			if (loginUser != null) {
				chain.doFilter(req, resp);
			} else {
				req.getRequestDispatcher("/userAction/goLogin").forward(req, resp);
			}
		} else {
			// 登录
			chain.doFilter(req, resp);
		}
	}

	private boolean checkFile(String requestPath, String requestQuery) {

		for (int i = 0; i < this.validateFiles.size(); i++) {
			ValidateFile validateFile = this.validateFiles.get(i);
			if (validateFile.fileName.equals(requestPath)) {
				if (validateFile.method != null) {
					if (requestQuery != null
							&& requestQuery.contains(validateFile.method)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	// 初始化
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.filePath = this.filterConfig.getInitParameter("ValidatefilePath");
		// 解析xml
		parserXml();
	}

	private void parserXml() {
		// dom sax dom4j
		SAXReader saxReader = new SAXReader();
		ValidateFile validataFile = null;
		try {
			URL url = this.filterConfig.getServletContext().getResource(
					filePath);
			Document document = saxReader.read(url);
			// 根元素
			Element rootEle = document.getRootElement();
			// file元素
			
			@SuppressWarnings("unchecked")
			List<Element> fileEles = rootEle.elements("file");
			for (int i = 0; i < fileEles.size(); i++) {
				Element fileEle = fileEles.get(i);
				validataFile = new ValidateFile();
				validataFile.fileName = fileEle.getText();
				if (fileEle.attribute("method") != null) {
					validataFile.method = fileEle.attributeValue("method");
				}
				this.validateFiles.add(validataFile);
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {

	}

	private class ValidateFile {
		String fileName;
		String method;
	}
}
