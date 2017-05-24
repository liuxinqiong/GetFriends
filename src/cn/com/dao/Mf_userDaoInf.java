package cn.com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.entity.Mf_fndpwd;
import cn.com.entity.Mf_friend;
import cn.com.entity.Mf_user;

public interface Mf_userDaoInf {
	public List<Mf_user> selAllUsers();

	public int insertPrimaryUser(Mf_user user);
	
	public Mf_user selectUser(Mf_user user);
	
	public List<Mf_user> selUsersByKey(@Param(value = "user") Mf_user user,@Param(value = "flag") int flag,@Param(value = "key") String key);
	
	public int addFriend(Mf_friend friend);
	
	public Mf_user selectUserByEmail(Mf_user user);
	
	public Mf_fndpwd selFndpwd(Mf_fndpwd fndpwd);
	
	public int insertFndpwd(Mf_fndpwd fndpwd);
	
	public int updateFndpwd(Mf_fndpwd fndpwd);
	
	public int updateUser(Mf_user user);
}
