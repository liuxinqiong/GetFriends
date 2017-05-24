package cn.com.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class GetRequestWrap extends HttpServletRequestWrapper{

	public GetRequestWrap(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	public String getParameter(String name) {
		// TODO Auto-generated method stub
		String value=super.getParameter(name);
		try {
			return value!=null?new String(value.getBytes("iso-8859-1"),"utf-8"):null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			try {
				encodedValues[i] = values[i]!=null?new String(values[i].getBytes("iso-8859-1"),"utf-8"):null;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return encodedValues;
	}
	
	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		String value=super.getHeader(name);
		try {
			return value!=null?new String(value.getBytes("iso-8859-1"),"utf-8"):null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
