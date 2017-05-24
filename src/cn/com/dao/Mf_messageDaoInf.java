package cn.com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.entity.Mf_message;
import cn.com.entity.Mf_user;

public interface Mf_messageDaoInf {
	public int insertMessage(Mf_message message);

	public List<Mf_message> selectMessage(@Param(value = "user") Mf_user user,
			@Param(value = "isRead") int isRead,
			@Param(value = "type") int type,
			@Param(value = "start") Integer start,
			@Param(value = "end") Integer end);

	public int updateMessage(Mf_message message);

	public int countAllForMessage(@Param(value = "user") Mf_user user,
			@Param(value = "isRead") int isRead, @Param(value = "type") int type);
}
