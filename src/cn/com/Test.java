package cn.com;



public class Test {
	
	public static void main(String[] args) {
		String abc="abc";
		String[] ss=abc.split("-");
		System.out.println(ss[0]);
	}
}	
class TestThread implements Runnable{
	int count=1;
	public void run() {
		// TODO Auto-generated method stub
		while(count<20){
			System.out.println(count);
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

/*
 *TestFriendServiceImpl testFriendService=(TestFriendServiceImpl)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("testFriendServiceImpl");
		System.out.println(testFriendService==null);
		List<TestFriend> fs=testFriendService.selAllFriend();
		
		for (TestFriend f : fs) {
			System.out.println(f.getName()+":"+f.getPassword());
		}
 * */
 /*
SqlSessionFactoryBuilder sqlBuilder = new SqlSessionFactoryBuilder();
		Reader reader;
		try {
			reader = Resources.getResourceAsReader("cn/com/config_mybatis/Configuration.xml");
			SqlSessionFactory sqlFactory = sqlBuilder.build(reader);
			SqlSession session = sqlFactory.openSession();
//			List<TestFriend> fs=session.selectList("selAllFriend");
//			for (TestFriend f : fs) {
//				System.out.println(f.getName());
//			}
//			TestFriend f=session.selectOne("selFriendByName", "ÁõÐÂÇí1");
//			System.out.println(f.getPassword());
			TestFriendDaoInf friendDao=session.getMapper(TestFriendDaoInf.class);
			List<TestFriend> fs=friendDao.selAllFriend();
			for (TestFriend test_friend : fs) {
				System.out.println(test_friend.getName());
			}
			TestFriend f=friendDao.selFriendByName("ÁõÐÂÇí1");
			System.out.println(f.getPassword());
//			Test_friend fDate=new Test_friend();
//			fDate.setName("ÁõÐÂÇí1");
//			fDate.setPassword("123456789");
//			friendDao.updatePasswordByName(fDate);
//			session.commit();
			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
*/