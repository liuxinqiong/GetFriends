package cn.com.service;

import cn.com.dao.Mf_joinDaoInf;
import cn.com.entity.Mf_join;

public class Mf_joinService {
	private Mf_joinDaoInf mf_joinDao;

	public Mf_joinDaoInf getMf_joinDao() {
		return mf_joinDao;
	}

	public void setMf_joinDao(Mf_joinDaoInf mf_joinDao) {
		this.mf_joinDao = mf_joinDao;
	}

	public int addJoinForActivity(Mf_join join){
		return mf_joinDao.insertJoin(join);
	}
	
	public int deleteJoiner(Mf_join join){
		return mf_joinDao.deleteJoiner(join);
	}
	
}
