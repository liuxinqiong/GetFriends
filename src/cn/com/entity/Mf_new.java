package cn.com.entity;

public class Mf_new {
	private String new_id;
	private Mf_user ower_id;
	private String create_date;
	private String content_text;
	private String picture_url;
	private String video_url;
	private int comment_num;
	
	public Mf_new(){
		
	}
	
	
	public Mf_new(Mf_user ower_id, String content_text, String picture_url,
			String video_url) {
		super();
		this.ower_id = ower_id;
		this.content_text = content_text;
		this.picture_url = picture_url;
		this.video_url = video_url;
	}


	public String getNew_id() {
		return new_id;
	}
	public void setNew_id(String new_id) {
		this.new_id = new_id;
	}
	public Mf_user getOwer_id() {
		return ower_id;
	}
	public void setOwer_id(Mf_user ower_id) {
		this.ower_id = ower_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getContent_text() {
		return content_text;
	}
	public void setContent_text(String content_text) {
		this.content_text = content_text;
	}
	public String getPicture_url() {
		return picture_url;
	}
	public void setPicture_url(String picture_url) {
		this.picture_url = picture_url;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	
}
