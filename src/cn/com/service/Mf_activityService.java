package cn.com.service;

import java.util.List;

import cn.com.dao.Mf_activityDaoInf;
import cn.com.entity.JoinHelper;
import cn.com.entity.Mf_activity;
import cn.com.entity.Mf_user;
import cn.com.util.GlobalParam;
import cn.com.util.PageParam;
import cn.com.util.PojoDomain;


public class Mf_activityService {
	private Mf_activityDaoInf mf_activityDao;
	
	public String test(){
		return "hello";
	}

	public Mf_activityDaoInf getMf_activityDao() {
		return mf_activityDao;
	}

	public void setMf_activityDao(Mf_activityDaoInf mf_activityDao) {
		this.mf_activityDao = mf_activityDao;
	}
	
	public int addActivity(Mf_activity activity){
		return mf_activityDao.insertActivity(activity);
	}
	
	public List<Mf_activity> selectActivity(JoinHelper helper,Integer start,Integer end){
		return mf_activityDao.selectActivity(helper,start,end);
	}
	
	public List<JoinHelper> selJoinersByActivity(Mf_activity activity){
		return mf_activityDao.selJoinersByActivity(activity);
	}
	
	public int updateActivity(Mf_activity activity){
		return mf_activityDao.updateActivity(activity);
	}
	
	public Mf_activity selectActivityById(String activity_id){
		return mf_activityDao.selectActivityById(activity_id);
	}
	
	public List<Mf_activity> selectActivityByJoinerId(JoinHelper helper,Integer start,Integer end){
		return mf_activityDao.selectActivityByJoinerId(helper,start,end);
	}
	
	public PojoDomain<Mf_activity> selectActivityByType(Mf_user user,String type,Integer pageNum){
		int start = (pageNum-1)*PageParam.PAGE_SIZE;
		int end = pageNum*PageParam.PAGE_SIZE+1;
		PojoDomain<Mf_activity> pojo = new PojoDomain<Mf_activity>();
		JoinHelper helper=new JoinHelper();
		if(type==null){
			return pojo;
		}
		List<Mf_activity> activities=null;
		if(type.equals("activity_container")){
			helper.setFlag(GlobalParam.WITHOUT_USER);
			helper.setState(GlobalParam.ACTIVITY_NOT_START);
			helper.setUser(user);
			activities=selectActivity(helper,start,end);
			pojo.setTotal_count(mf_activityDao.countAllForActivity(helper));
		}else if(type.equals("activity_container_my")){
			helper.setFlag(GlobalParam.WITH_USER);
			helper.setState(GlobalParam.ACTIVITY_ALL);
			helper.setUser(user);
			activities=selectActivity(helper,start,end);
			pojo.setTotal_count(mf_activityDao.countAllForActivity(helper));
		}else{
			helper.setFlag(GlobalParam.WITH_USER);
			helper.setState(GlobalParam.ACTIVITY_ALL);
			helper.setUser(user);
			activities=selectActivityByJoinerId(helper,start,end);
			pojo.setTotal_count(mf_activityDao.countAllForActivityByJoinerId(helper));
		}
		pojo.setPage_number(pageNum);
		pojo.setPage_size(PageParam.PAGE_SIZE);
		pojo.setPojolist(activities);
		return pojo;
	}
	
}
