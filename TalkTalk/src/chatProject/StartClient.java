package chatProject;

import chatProject.client.Login;

public class StartClient {

	public static void main(String[] args) {
		
		// DB 접속
		DB.init();
		
		// 클라이언트 시작
		new Login();
	}
 
}
