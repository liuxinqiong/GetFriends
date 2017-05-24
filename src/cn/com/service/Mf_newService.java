package cn.com.service;

import java.util.List;

import cn.com.dao.Mf_newDaoInf;
import cn.com.dao.Mf_wordDaoInf;
import cn.com.entity.Mf_new;
import cn.com.entity.Mf_user;
import cn.com.entity.Mf_word;
import cn.com.entity.NewHelper;
import cn.com.util.PageParam;
import cn.com.util.PojoDomain;

public class Mf_newService {
	private Mf_newDaoInf mf_newDao;
	private Mf_wordDaoInf mf_wordDao;

	public Mf_wordDaoInf getMf_wordDao() {
		return mf_wordDao;
	}

	public void setMf_wordDao(Mf_wordDaoInf mf_wordDao) {
		this.mf_wordDao = mf_wordDao;
	}
	
	public List<Mf_word> getAllwords(){
		return mf_wordDao.selAllWord();
	}
	
	public Mf_newDaoInf getMf_newDao() {
		return mf_newDao;
	}

	public void setMf_newDao(Mf_newDaoInf mf_newDao) {
		this.mf_newDao = mf_newDao;
	}

	public int addNew(Mf_new n){
		return mf_newDao.insertPrimaryNew(n);
	}
	

	public PojoDomain<Mf_user> getFriendByUser(Mf_user loginUser,Integer pageNum) {
		// TODO Auto-generated method stub
		int start = (pageNum-1)*PageParam.PAGE_SIZE_FRIEND;
		int end = pageNum*PageParam.PAGE_SIZE_FRIEND+1;
		PojoDomain<Mf_user> pojo = new PojoDomain<Mf_user>();
		Mf_user user=mf_newDao.selFriendsByUser(loginUser, start, end);
		pojo.setTotal_count(mf_newDao.countAllForFriends(loginUser));
		pojo.setPage_number(pageNum);
		pojo.setPage_size(PageParam.PAGE_SIZE_FRIEND);
		pojo.setPojolist(user.getFriends());
		return pojo;
	}
	
	public PojoDomain<Mf_new> getNewsByUser(Mf_user loginUser,String id,Integer pageNum){
		int start = (pageNum-1)*PageParam.PAGE_SIZE;
		int end = pageNum*PageParam.PAGE_SIZE+1;
		PojoDomain<Mf_new> pojo = new PojoDomain<Mf_new>();
		if(id==null){
			return pojo;
		}
		List<Mf_new> news=null;
		if(id.equals("new_friend")){
			news=mf_newDao.selectNewsByUser(loginUser,1,start,end);	
			pojo.setTotal_count(mf_newDao.countAllForNews(loginUser, 1));
		}else if(id.equals("new_self")){
			news= mf_newDao.selectNewsByUser(loginUser,0,start,end);
			pojo.setTotal_count(mf_newDao.countAllForNews(loginUser, 0));
		}else{
			news =mf_newDao.selectNewsByUser(loginUser,2,start,end);
			pojo.setTotal_count(mf_newDao.countAllForNews(loginUser, 2));
		}	
		pojo.setPage_number(pageNum);
		pojo.setPage_size(PageParam.PAGE_SIZE);
		pojo.setPojolist(news);
		return pojo;
	}
	
	//目前只对图片进行处理，没有做视频
	public Mf_new createNewPojo(NewHelper helper){
		Mf_new newInfo=new Mf_new();
		newInfo.setContent_text(clearXss(filterCharacter(helper.getContent())));
		StringBuffer picture_urls=new StringBuffer();
		if(helper.getPicture_urls().size()!=0){
			for (String picture_url : helper.getPicture_urls()) {
				picture_urls.append(picture_url);
				picture_urls.append("|");
			}
			picture_urls.deleteCharAt(picture_urls.length()-1);
		}
		newInfo.setPicture_url(picture_urls.toString());
		newInfo.setVideo_url("");
		return newInfo;
	}
	
	//使用requestBody标签导致过滤器失效
	public String filterCharacter(String value){
		List<Mf_word> words=mf_wordDao.selAllWord();
		for (Mf_word word : words) {
			if(value.contains(word.getWord())){
				 value = value.replace(word.getWord(), word.getReplace());
			}
		}
		return value;
	}
	//使用requestBody标签导致过滤器失效
	public String clearXss(String value) {
		if (value == null || "".equals(value)) {
			return value;
		}
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
				"\"\"");
		value = value.replaceAll("script", "");

		return value;
	}
}
