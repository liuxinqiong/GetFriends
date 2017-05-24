package cn.com.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.com.entity.Mf_word;
import cn.com.service.Mf_wordService;


public class DirtyRequestWrapper extends HttpServletRequestWrapper{

	
	private Mf_wordService mf_wordService=(Mf_wordService) new ClassPathXmlApplicationContext("applicationContext.xml").getBean("mf_wordService");;
	private List<Mf_word> words=mf_wordService.getAllwords();
	
	public DirtyRequestWrapper(HttpServletRequest request) {
		super(request);
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
			encodedValues[i] = filterCharacter(values[i]);
		}
		return encodedValues;
	}
	
	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		String value=super.getParameter(name);
		if(value==null){
			return null;
		}
		return filterCharacter(value);
	}
	
	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		String value= super.getHeader(name);
		if(value==null){
			return null;
		}
		return filterCharacter(value);
	}
	
	
	
	public String filterCharacter(String value){
		for (Mf_word word : words) {
			if(value.contains(word.getWord())){
				 value = value.replace(word.getWord(), word.getReplace());
			}
		}
		return value;
	}
}
