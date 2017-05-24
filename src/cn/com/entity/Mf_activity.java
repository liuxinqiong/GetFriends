package cn.com.entity;

import java.util.List;

public class Mf_activity{

	private String activity_id;
	private Mf_user owner_id; 
	private String create_date;
	private String end_date;
	private String address;
	private Integer max_num;
	private Integer state;
	private String topic;
	private String content;
	private Integer current_num;
	private List<JoinHelper> joiners;
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public Mf_user getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(Mf_user owner_id) {
		this.owner_id = owner_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getMax_num() {
		return max_num;
	}
	public void setMax_num(Integer max_num) {
		this.max_num = max_num;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCurrent_num() {
		return current_num;
	}
	public void setCurrent_num(Integer current_num) {
		this.current_num = current_num;
	}
	public List<JoinHelper> getJoiners() {
		return joiners;
	}
	public void setJoiners(List<JoinHelper> joiners) {
		this.joiners = joiners;
	}
	
	
}
