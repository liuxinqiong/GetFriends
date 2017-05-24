package cn.com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.entity.JoinHelper;
import cn.com.entity.Mf_activity;

public interface Mf_activityDaoInf {
	public int insertActivity(Mf_activity activity);

	public List<JoinHelper> selJoinersByActivity(Mf_activity activity);

	public int updateActivity(Mf_activity activity);

	public Mf_activity selectActivityById(String activityId);

	public List<Mf_activity> selectActivity(
			@Param(value = "helper") JoinHelper helper,
			@Param(value = "start") Integer start,
			@Param(value = "end") Integer end);

	public List<Mf_activity> selectActivityByJoinerId(
			@Param(value = "helper") JoinHelper helper,
			@Param(value = "start") Integer start,
			@Param(value = "end") Integer end);

	public int countAllForActivityByJoinerId(JoinHelper helper);

	public int countAllForActivity(JoinHelper helper);
}
