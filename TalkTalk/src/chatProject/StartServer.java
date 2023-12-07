package chatProject;

import java.io.IOException;

import chatProject.server.ChatServer;

public class StartServer {

	public static void main(String[] args) throws IOException {


		// DB 접속
		DB.init();


//		// 테이블 생성 -> 첫 실행 시 주석 해제하면 테이블 생성 (생성 후에는 주석)
//		DB.createTable();

		// 서버 실행
		new ChatServer();

	}

}
