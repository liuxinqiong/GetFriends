package cn.com.entity;

public class Mf_comment {
	private String comment_id;
	private Mf_new new_id;
	private String content;
	private Mf_user commenter_id;
	private String create_date;
	
	public Mf_comment(){}
	
	
	public Mf_comment(Mf_new new_id, String content, Mf_user commenter_id) {
		super();
		this.new_id = new_id;
		this.content = content;
		this.commenter_id = commenter_id;
	}


	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public Mf_new getNew_id() {
		return new_id;
	}
	public void setNew_id(Mf_new new_id) {
		this.new_id = new_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Mf_user getCommenter_id() {
		return commenter_id;
	}
	public void setCommenter_id(Mf_user commenter_id) {
		this.commenter_id = commenter_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
}
