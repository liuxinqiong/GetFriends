package cn.com.util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailConfig {
		//初始化配置文件
		public MailConfig() {
			initMail();
		}
		
		/**
		 * 邮件相关
		 */
		public String fromAddress = null;
		public String userName = null;
		public String password = null;
		public String smtpHostName=null;
		public void initMail() {
			Properties props = new Properties();			
			String fileName = "mail.properties";
	        InputStream in = null ;
			try {
				
				in=new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(fileName));
				props.load(in);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fromAddress=props.getProperty("fromAddress").trim();
			userName=props.getProperty("userName").trim();
			password=props.getProperty("password").trim();
			smtpHostName=props.getProperty("smtpHostName").trim();
		}
}
