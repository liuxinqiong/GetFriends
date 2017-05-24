package cn.com.service;

import java.util.List;

import cn.com.dao.Mf_wordDaoInf;
import cn.com.entity.Mf_word;

public class Mf_wordService {
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
}
