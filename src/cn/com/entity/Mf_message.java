package cn.com.entity;

public class Mf_message {
	private String message_id;
	private Mf_user from_id;
	private Mf_user to_id;
	private String content;
	private Integer type;
	private String create_date;
	private Integer isRead;
	private Integer isDeal;
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public Mf_user getFrom_id() {
		return from_id;
	}
	public void setFrom_id(Mf_user from_id) {
		this.from_id = from_id;
	}
	public Mf_user getTo_id() {
		return to_id;
	}
	public void setTo_id(Mf_user to_id) {
		this.to_id = to_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public Integer getIsDeal() {
		return isDeal;
	}
	public void setIsDeal(Integer isDeal) {
		this.isDeal = isDeal;
	}
	
	
}
