package cn.com.service;

import java.util.List;

import cn.com.dao.Mf_commentDaoInf;
import cn.com.entity.Mf_comment;
import cn.com.entity.Mf_new;

public class Mf_commentService {
	private Mf_commentDaoInf mf_commentDao;

	public Mf_commentDaoInf getMf_commentDao() {
		return mf_commentDao;
	}

	public void setMf_commentDao(Mf_commentDaoInf mf_commentDao) {
		this.mf_commentDao = mf_commentDao;
	}
	
	public int addCommment(Mf_comment comment){
		return mf_commentDao.insertComment(comment);
	}
	
	public List<Mf_comment> getCommentByNew(Mf_new n){
		return mf_commentDao.selCommentByNew(n);
	}
}
