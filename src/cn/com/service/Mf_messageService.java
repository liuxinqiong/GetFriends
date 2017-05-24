package cn.com.service;

import java.util.List;

import cn.com.dao.Mf_messageDaoInf;
import cn.com.entity.Mf_message;
import cn.com.entity.Mf_user;
import cn.com.util.PageParam;
import cn.com.util.PojoDomain;

public class Mf_messageService {
	private Mf_messageDaoInf mf_messageDao;

	public Mf_messageDaoInf getMf_messageDao() {
		return mf_messageDao;
	}

	public void setMf_messageDao(Mf_messageDaoInf mf_messageDao) {
		this.mf_messageDao = mf_messageDao;
	}
	
	public int sendMessage(Mf_message message){
		return mf_messageDao.insertMessage(message);
	}
	
	public PojoDomain<Mf_message> selectMessage(Mf_user user,int isRead,int type,int pageNum){
		int start = (pageNum-1)*PageParam.PAGE_SIZE;
		int end = pageNum*PageParam.PAGE_SIZE+1;
		PojoDomain<Mf_message> pojo = new PojoDomain<Mf_message>();
		List<Mf_message> messages=mf_messageDao.selectMessage(user, isRead, type,start,end);
		pojo.setTotal_count(mf_messageDao.countAllForMessage(user, isRead, type));
		pojo.setPage_number(pageNum);
		pojo.setPage_size(PageParam.PAGE_SIZE);
		pojo.setPojolist(messages);
		return pojo;
	}
	
	public int updateMessage(Mf_message message){
		return mf_messageDao.updateMessage(message);
	}
}