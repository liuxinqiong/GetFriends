package cn.com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.entity.Mf_new;
import cn.com.entity.Mf_user;

public interface Mf_newDaoInf {
	public int insertPrimaryNew(Mf_new n);

	public Mf_user selFriendsByUser(@Param(value = "user") Mf_user user,
			@Param(value = "start") Integer start,
			@Param(value = "end") Integer end);

	public int countAllForFriends(Mf_user user);

	public List<Mf_new> selectNewsByUser(@Param(value = "user") Mf_user user,
			@Param(value = "self") Integer self,
			@Param(value = "start") Integer start,
			@Param(value = "end") Integer end);

	public int countAllForNews(@Param(value = "user") Mf_user user,
			@Param(value = "self") Integer self);
}
