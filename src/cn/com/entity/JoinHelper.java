package cn.com.entity;

/*
 * ˫������
 * һ����Ϊ���Ĳ�������ʾ�����ߺͲ���ʱ�� user joiner_time
 * ������Ϊ���ݿ��ѯ�Ĳ���������״̬�͵�¼�û���ͬʱ����flag�ж��Ƿ�ѡ�����Ļ���û�в���� 1��ʾ���� 0��ʾû���� user state flag
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
