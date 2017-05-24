package cn.com.entity;

public class Mf_join {
	private Mf_activity activity_id;
	private Mf_user joiner_id;
	private String joiner_time;
	public Mf_activity getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(Mf_activity activity_id) {
		this.activity_id = activity_id;
	}
	public Mf_user getJoiner_id() {
		return joiner_id;
	}
	public void setJoiner_id(Mf_user joiner_id) {
		this.joiner_id = joiner_id;
	}
	public String getJoiner_time() {
		return joiner_time;
	}
	public void setJoiner_time(String joiner_time) {
		this.joiner_time = joiner_time;
	}
	
	
}
