package chatProject.client;

import java.io.IOException;

public class ChatClientThread extends Thread {
	private ChatClient mc;

	public ChatClientThread(ChatClient mc) {
		this.mc = mc;
	}

	public void run() {
		String message = null;
		String[] receivedMsg = null;

		boolean isStop = false;
		while (!isStop) {
			try {
				message = (String) mc.getOis().readObject();
				receivedMsg = message.split("#");
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true;
			}
			
			/**
			 * 종료 exit
			 * 
			 * 강퇴 : 받을사람#exit#kick#시간
			 * 종료 : 종료한사용자#exit#시간
			 */
			if (receivedMsg[1].equals("exit")) {
				if (receivedMsg[0].equals(mc.getNickname())) {
					mc.exit();	// 클라이언트 종료
				} else {
					if (receivedMsg.length > 3) { // 강퇴한 경우
						mc.getJta().append("[ " + receivedMsg[0] + " ] 님이 강퇴되었습니다." + receivedMsg[3] + System.getProperty("line.separator"));
						
					} else {	// 강퇴가 아닌 사용자 종료한 경우
						mc.getJta().append("[ " + receivedMsg[0] + " ] 님이 종료 하셨습니다." + receivedMsg[2] + System.getProperty("line.separator"));
					}
					mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
				}
			} 
			/**
			 * 새 사용자가 입장
			 * 
			 * 입장한사용자#start#시작메시지#notice#공지
			 */
			else if (receivedMsg[1].equals("start")) {
				// 입장 메시지 출력
				mc.getJta().append("[ " + receivedMsg[0] + " ]" + receivedMsg[2] + System.getProperty("line.separator"));

			} 
			
			/**
			 * 사용자 목록 기능
			 * 
			 * 현재사용자#list#접속자수#사람1#사람2#....
			 */
			else if (receivedMsg[1].equals("list")) {
				int userCount = Integer.valueOf(receivedMsg[2]); // 사람 수 가져옴

				if (mc.getuList() != null && mc.getuList().getvUserList() != null) {
					mc.getuList().getvUserList().removeAllElements(); // 기존에 존재했던 벡터 지움

					for (int i = 0; i < userCount; i++) {		// 사람 수 만큼 list에 추가
						mc.getuList().getvUserList().add(receivedMsg[3 + i]);
					}
					mc.getuList().repaint();
				}
			} 
			
			/**
			 * 공지 기능
			 * 
			 * 공지등록한사람#notice#공지메시지
			 */
			else if (receivedMsg[1].equals("notice")) {
				mc.getLblNoticeInfo().setText(receivedMsg[0] + " : " + receivedMsg[2]);
			} 
			
			/**
			 * 귓속말 기능
			 * 
			 * 보낸사람#whisp#받는사람#메시지
			 * 
			 */
			else if (receivedMsg[1].equals("Whisp")) {

				// 받는 사람이 내 id && 보내는 사람이 내가 아닌 경우 => 자신에게 보내는 것을 방지하고 상대방에게 전송
				if (receivedMsg[2].equals(mc.getNickname()) && !(receivedMsg[0].equals(mc.getNickname()))) { 
					mc.getJta().append("[ 귓속말 ] " + receivedMsg[0] + " : " + receivedMsg[3]+ System.getProperty("line.separator"));
				} 
				
				// 보낸 사람이 내 id && 받는 사람이 내 id가 아닌 경우 => 전송한 메시지 출력
				else if (receivedMsg[0].equals(mc.getNickname()) && !(receivedMsg[2].equals(mc.getNickname()))) { 
					mc.getJta().append("[ " + receivedMsg[2] + " 님에게 귓속말 전송 ] " + receivedMsg[0] + " : "	+ receivedMsg[3] + System.getProperty("line.separator"));
				}

			} 
			
			/**
			 * 강퇴 기능
			 * 
			 * 보낸사람#kick#받는사람
			 */
			else if (receivedMsg[1].equals("kick")) {

				// 보낸 사람이 admin && 받는 사람이 admin이 아닌 경우
				if (receivedMsg[0].equals("관리자") && !(receivedMsg[2].equals("관리자"))) {

					// 받는 사람 exit
					if (receivedMsg[2].equals(mc.getNickname())) {
						try {
							mc.kickOutput(mc.getNickname());
						} catch (IOException e) {
							e.printStackTrace();
						}

					} 
					// 보낸 사람이 관리자면 userList dispose로 닫아줌
					else if (mc.getNickname().equals("관리자")) {
						if(mc.getuList() != null) {
							mc.getuList().dispose(); 
						}
					}
				}
				

			} else {
				mc.getJta().append(receivedMsg[0] + " : " + receivedMsg[1] + System.getProperty("line.separator"));
				mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());

			}
		}
	}
}