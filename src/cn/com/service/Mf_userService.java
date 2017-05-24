package cn.com.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import cn.com.dao.Mf_userDaoInf;
import cn.com.entity.Mf_fndpwd;
import cn.com.entity.Mf_friend;
import cn.com.entity.Mf_user;
import cn.com.util.SimpleMail;
import cn.com.util.SimpleMailSender;

public class Mf_userService {
	private Mf_userDaoInf mf_userDao;
	
	public Mf_userDaoInf getMf_userDao() {
		return mf_userDao;
	}

	public void setMf_userDao(Mf_userDaoInf mf_userDao) {
		this.mf_userDao = mf_userDao;
	}
	
	public List<Mf_user> selAllUsers(){
		return mf_userDao.selAllUsers();
	}

	public int register(Mf_user user,String password){
		int success=mf_userDao.insertPrimaryUser(user);
		if(success==1){
			//�����ʼ�
			Thread t=new Thread( new MailSendThread(user,"register:"+password,null));
			t.start();
		}	
		return success;
	}
	
	public Mf_user login(Mf_user user){
		return mf_userDao.selectUser(user);
	}
	
	public Mf_user userPrimaryinfoWrap(String email,String password){
		Mf_user user=new Mf_user();
		user.setEmail(email);
		user.setPassword(password);
		user.setUser_name(email);
		user.setPicture_url("icon/defaultIcon.png");
		return user;
	}
	
	public String createMailContentForRegister(Mf_user user,String password){
		String content=user.getUser_name()+"��<br/>"+"��ã���ӭע��֪�����ѣ���Ļ�����Ϣ���£�<br/>";
		content+="���ע������Ϊ��"+user.getEmail()+"<br/>";
		content+="����û���Ϊ��"+user.getEmail()+"<br/>";
		content+="�������Ϊ��"+password+"<br/>";
		content+="��ע�⣺���������Ǽ��ܱ���ģ�������ֻ��ʾһ�Σ������Ʊ��ܣ�ʹ�ù���������������룬����ʹ��ע������ͨ�������һأ�<br/>֪������";
		return content;
	}
	
	public List<Mf_user> searchUserByKey(Mf_user user,int flag,String key){
		return mf_userDao.selUsersByKey(user,flag,key);
	}
	
	public int addFriend(Mf_friend friend){
		return mf_userDao.addFriend(friend);
	}
	
	public Mf_user isEmailRegister(Mf_user user){
		return mf_userDao.selectUserByEmail(user);
	}
	
	
	class MailSendThread implements Runnable{
		private Object o;
		private String flag;
		private String basePath;
		public MailSendThread(Object o,String flag,String basePath){
			this.o= o;
			this.flag=flag;
			this.basePath=basePath;
		}
		public void run() {
			// TODO Auto-generated method stub
			if(flag.contains("register")){
				Mf_user user=(Mf_user)o;
				SimpleMail sm=new SimpleMail("��ӭע��֪������",createMailContentForRegister(user,flag.split(":")[1]));
				SimpleMailSender sms=new SimpleMailSender();
				try {
					sms.send(user.getEmail(), sm);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(flag.contains("fndpwd")){
				Mf_fndpwd f=(Mf_fndpwd)o;
				SimpleMail sm=new SimpleMail("֪�����������һ�",createMailContentForFndpwd(f,basePath));
				SimpleMailSender sms=new SimpleMailSender();
				try {
					sms.send(f.getEmail(), sm);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private String createMailContentForFndpwd(Mf_fndpwd f,String basePath){
         String resetPassHref =  basePath+"userAction/resetPwd?sid="+f.getSecretKey()+"&email="+f.getEmail();
         String emailContent = "����ظ����ʼ�.������������,��������<br/><a href="+resetPassHref +" target='_BLANK'>�����������������</a>" +
                 "<br/>tips:���ʼ�����60����,���ӽ���ʧЧ����Ҫ��������'�һ�����'";
         System.out.print(resetPassHref);  
		return emailContent;
	}

	public int saveOrUpdateFndpwd(Mf_fndpwd f, String basePath) {
		// TODO Auto-generated method stub
		Mf_fndpwd db_fndpwd=mf_userDao.selFndpwd(f);
		int result=0;
		if(db_fndpwd==null){
			result=mf_userDao.insertFndpwd(f);
		}else{
			result=mf_userDao.updateFndpwd(f);
		}
		if(result>0){
			//�����ʼ�
			Thread t=new Thread( new MailSendThread(f,"fndpwd",basePath));
			t.start();
		}
		return result;
	}
	
	public Mf_fndpwd selFndPwd(Mf_fndpwd f){
		return mf_userDao.selFndpwd(f);
	}
	
	public int updateUser(Mf_user user){
		return mf_userDao.updateUser(user);
	}
	
	/*
	 * sql����ʽ ��Ϊ�ղŸ���
	 * �����ֵ��� user_id user_name password email picture_url����Ϊ��
	 * ǰֵ̨����Ϊ�տ���Ϊ�մ�
	 * û���ṩinputΪnull
	 * �ṩ��inputΪ"" inputΪradio,ûѡҲΪnull
	 * ���벻�������մ�
	 * */
	public Mf_user wrapUser(Mf_user loginUser,Mf_user userFront){
		Mf_user user=new Mf_user();
		if(!(userFront.getUser_name()==null||userFront.getUser_name().trim().equals(""))){
			user.setUser_name(userFront.getUser_name());
		}
		if(!(userFront.getSchool()==null||userFront.getSchool().trim().equals(""))){
			user.setSchool(userFront.getSchool());
		}
		if(!(userFront.getAddress()==null||userFront.getAddress().trim().equals(""))){		
			user.setAddress(userFront.getAddress().replace(",", "-"));
		}
		if(!(userFront.getSex()==null||userFront.getSex().trim().equals(""))){
			user.setSex(userFront.getSex());
		}
		if(!(userFront.getBirthday()==null||userFront.getBirthday().trim().equals(""))){
			user.setBirthday(userFront.getBirthday());
		}
		if(!(userFront.getTelephone()==null||userFront.getTelephone().trim().equals(""))){
			user.setTelephone(userFront.getTelephone());
		}
		if(!(userFront.getPicture_url()==null||userFront.getPicture_url().trim().equals(""))){
			user.setPicture_url(userFront.getPicture_url());
		}
		user.setUser_id(loginUser.getUser_id());
		return user;
	}
}
