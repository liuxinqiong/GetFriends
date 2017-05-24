package cn.com.entity;

/*
 * 双向作用
 * 一：作为活动类的参数，表示参与者和参与时间 user joiner_time
 * 二：作为数据库查询的参数，根据状态和登录用户；同时根据flag判断是否选择参与的还是没有参与的 1表示参与 0表示没参与 user state flag
 * */
public class JoinHelper {
	private String joiner_id;
	private Mf_user user;
	private String joiner_time;
	private int flag;
	private int state;
	public Mf_user getUser() {
		return user;
	}
	public void setUser(Mf_user user) {
		this.user = user;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getJoiner_time() {
		return joiner_time;
	}
	public void setJoiner_time(String joiner_time) {
		this.joiner_time = joiner_time;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getJoiner_id() {
		return joiner_id;
	}
	public void setJoiner_id(String joiner_id) {
		this.joiner_id = joiner_id;
	}
	
	
}
