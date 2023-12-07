package chatProject;

import java.io.IOException;

import chatProject.server.ChatServer;

public class StartServer {

	public static void main(String[] args) throws IOException {


		// DB 접속
		DB.init();


//		// 테이블 생성
		DB.createTable();

		// 서버 실행
		new ChatServer();

	}

}
