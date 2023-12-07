package chatProject.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import chatProject.DB;
import chatProject.utill.BtnStyle;

public class ChatClient implements ActionListener {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	final int PointColor = 0xD9E5FF;
	final int BGCOLOR = 0x001054;

	private String ip;
	private String id;

	private JFrame jframe;
	private JPanel pTitle, pChat, pChatTop, pChatBottom, pEmoji, pSend;
	private JLabel lblName, lblNotice, lblNoticeInfo;
	private JButton btnList, btnNotice, btnEmoji, btnClear, btnSend;
	private JTextArea jta;
	private JTextField tfInput;
	private JPanel pInput;

	private ArrayList<String> ulId;
	private UserList uList;
	private Emoji emoji;
	private JButton btnMenu;
	private JPanel pMenuList;
	private AdminMenu adminMenu;
	private Notice notice;
	private Login login;
	private String nickname;

	public ChatClient(String argIp, String argId, String nickname) {

		// ip, id 저장
		ip = argIp;
		id = argId;
		this.nickname = nickname;
		ulId = new ArrayList<>();

		// 프레임 생성
		addFrame(id);

	}

	/**
	 * 프레임 생성
	 */
	private void addFrame(String id) {
		jframe = new JFrame("Chatting");

		// 상단에 타이틀 생성
		addTitle();
		jframe.add(pTitle, BorderLayout.NORTH);

		// 공지 + 채팅 수신 생성
		addChat();
		jframe.add(pChat, BorderLayout.CENTER);

		// 채팅 입력 창 + 이모티콘, 지우기, 전송
		addChatInput();
		jframe.add(pChatBottom, BorderLayout.SOUTH);

		// 프레임 크기 및 위치 설정
		jframe.setSize(400, 600);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		jframe.setLocation((screenWidth - jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
		jframe.setResizable(false); // 크기 고정
		jframe.setVisible(true);

		// 닫을 때 이벤트
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					oos.writeObject(nickname + "#exit");
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				System.exit(0);
			}

			public void windowOpened(WindowEvent e) {
				tfInput.requestFocus();
			}
		});

	}

	/**
	 * 상단에 타이틀 생성 -> ID, 사용자 목록
	 */
	private void addTitle() {

		// 타이틀 패널
		pTitle = new JPanel();
		pTitle.setLayout(new BorderLayout());
		pTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		pTitle.setBackground(new Color(BGCOLOR));

		// 접속한 아이디
		lblName = new JLabel("닉네임  :  " + nickname);
		lblName.setForeground(Color.white);
		pTitle.add(lblName, BorderLayout.WEST); // 패널 좌측에 추가

		pMenuList = new JPanel();
		pMenuList.setLayout(new BorderLayout(10, 0));
		pMenuList.setBackground(new Color(BGCOLOR));

		// 관리자인 경우에만 출력
		if (id.equals("admin")) {
			// 목록 버튼
			btnMenu = new JButton("메뉴");
			BtnStyle.setStyle(btnMenu, PointColor, 0X000, 90, 30);
			btnMenu.addActionListener(this);
			pMenuList.add(btnMenu, BorderLayout.CENTER);
		}

		// 목록 버튼
		btnList = new JButton("목록");
		btnList.addActionListener(this);
		BtnStyle.setStyle(btnList, PointColor, 0X000, 90, 30);

		pMenuList.add(btnList, BorderLayout.EAST);

		pTitle.add(pMenuList, BorderLayout.EAST); // 패널 우측에 추가

	}

	/**
	 * 공지 + 채팅 수신 생성
	 */
	private void addChat() {

		pChat = new JPanel();
		pChat.setLayout(new BorderLayout());

		// 챗 상단 부분
		pChatTop = new JPanel();
		pChatTop.setLayout(new BorderLayout());
		pChatTop.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		pChatTop.setBackground(Color.white);

		// 공지 타이틀
		lblNotice = new JLabel("🔔　");
		pChatTop.add(lblNotice, BorderLayout.WEST); // pChatTop패널 좌측에 추가

		// 공지 입력
		lblNoticeInfo = new JLabel("");
		pChatTop.add(lblNoticeInfo, BorderLayout.CENTER); // pChatTop패널 중앙에 추가

		noticeReset();

		// 공지 입력 버튼
		btnNotice = new JButton("공지 등록");
		BtnStyle.setStyle(btnNotice, PointColor, 0X000000, 90, 30);
		btnNotice.addActionListener(this);
		pChatTop.add(btnNotice, BorderLayout.EAST); // pChatTop 패널 우측에 추가

		pChat.add(pChatTop, BorderLayout.NORTH); // pChat패널 상단에 추가

		// 채팅이 보이는 스크롤 팬 생성
		jta = new JTextArea("", 10, 50);
		jta.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		jta.setEditable(false);
		JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(400, 400));
		jsp.setBackground(Color.white);

		jta.setLineWrap(true); // 자동 줄바꿈

		pChat.add(jsp, BorderLayout.CENTER); // pChat패널 중앙에 추가

	}

	/**
	 * 채팅 입력 창
	 */
	private void addChatInput() {

		pChatBottom = new JPanel();
		pChatBottom.setLayout(new BorderLayout());
		pChatBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		pChatBottom.setBackground(new Color(BGCOLOR));

		// 입력창 패널 생성
		pInput = new JPanel();
		pInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pInput.setLayout(new BorderLayout());
		pInput.setBackground(new Color(BGCOLOR));

		// 입력창 생성
		tfInput = new JTextField();
		tfInput.setPreferredSize(new Dimension(30, 80));
		tfInput.addActionListener(this);

		pInput.add(tfInput, BorderLayout.CENTER);
		pChatBottom.add(pInput, BorderLayout.NORTH);

		// 이모티콘 + 지우기 버튼 패널 생성
		pEmoji = new JPanel();
		pEmoji.setLayout(new FlowLayout(1, 10, 0));
		pEmoji.setBackground(new Color(BGCOLOR));

		// 이모티콘 버튼 생성
		btnEmoji = new JButton("😆");
		BtnStyle.setStyle(btnEmoji, PointColor, 0X000000, 65, 30);
		btnEmoji.addActionListener(this);
		pEmoji.add(btnEmoji);

		// 지우기 버튼 생성
		btnClear = new JButton("Clear");
		BtnStyle.setStyle(btnClear, PointColor, 0X000000, 65, 30);
		btnClear.addActionListener(this);
		pEmoji.add(btnClear);

		pChatBottom.add(pEmoji, BorderLayout.WEST);

		// 전송 버튼 생성
		pSend = new JPanel();
		pSend.setLayout(new FlowLayout(1, 10, 0));
		pSend.setBackground(new Color(BGCOLOR));
		btnSend = new JButton("전송");
		BtnStyle.setStyle(btnSend, PointColor, 0X000000, 65, 30);
		btnSend.addActionListener(this);
		pSend.add(btnSend);

		pChatBottom.add(pSend, BorderLayout.EAST);

	}

	public void noticeReset() {
		String strId = "";
		String strMsg = "";
		String strName = "";

		try {

			String strSelect = "SELECT * FROM \"notice\"";
			ResultSet rs = DB.getResultSet(strSelect);

			while (rs.next()) {
				strId = rs.getString(2); // 공지 작성자
				strMsg = rs.getString(3); // 공지 메시지
			}

		} catch (SQLException e) {
			e.printStackTrace();
			lblNoticeInfo.setText("공지가 없습니다.");
		}

		String strSelectName = "select * from users where user_id = '" + strId + "'";
		ResultSet rs = DB.getResultSet(strSelectName);

		try {
			while (rs.next()) {
				strName = rs.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String strNotice = strName + " : " + strMsg;
		lblNoticeInfo.setText(strNotice);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();

		String msg = tfInput.getText(); // 입력한 메시지 받아옴

		// 전송 버튼 or 입력창 이벤트
		if (obj == tfInput || obj == btnSend) {
			if (msg == null || msg.length() == 0) { // 메시지를 입력하지 않았을 때
				JOptionPane.showMessageDialog(jframe, "메시지를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			} else {

				try {
					oos.writeObject(nickname +"#send#" + id + "#" + msg); // server로 메시지 전송
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				tfInput.setText("");
				if (emoji != null) {
					emoji.dispose(); // 전송 후 이모티콘 창 닫기
				}
			}
		}
		// 사용자 목록 버튼 클릭
		else if (obj == btnList) {

			uList = new UserList(this);
			try {
				oos.writeObject(nickname + "#list"); // server로 메시지 전송
			} catch (IOException ee) {
				ee.printStackTrace();
			}

		}
		// 공지 등록 버튼 클릭
		else if (obj == btnNotice) {
			notice = new Notice(this);
		}
		// 이모티콘 버튼 클릭
		else if (obj == btnEmoji) {
			emoji = new Emoji(this);

		}
		// clear 버튼 클릭
		else if (obj == btnClear) {
			jta.setText("");
		}

		// 관리자 메뉴
		else if (obj == btnMenu) {
			adminMenu = new AdminMenu(this);
			
		}
	}

	public void exit() {
		
		System.exit(0);
	}

	// 소켓 생성 및 입출력 스트림 생성 / 스레드 생성
	public void init() throws IOException {
		socket = new Socket(ip, 5000);
		System.out.println("connected...");
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		ChatClientThread ct = new ChatClientThread(this);
		Thread t = new Thread(ct);
		t.start();
	}

	// 로그인 후 방에 입장했을 때 메시지 출력
	public void startMessage() {
		try {
			oos.writeObject(nickname + "#start#" + " 님이 방에 입장하였습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Getter, Setter
	public ObjectInputStream getOis() {
		return ois;
	}

	public JTextArea getJta() {
		return jta;
	}

	public String getId() {
		return id;
	}

	public JTextField getTfInput() {
		return tfInput;
	}

	public JLabel getLblNoticeInfo() {
		return lblNoticeInfo;
	}


	public UserList getuList() {
		return uList;
	}

	public JFrame returnFrame() {
		return jframe;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public String getNickname() {
		return nickname;
	}

	// 강퇴 메소드
	public void kickOutput(String str) throws IOException {
        
		String strUserId="";
		int kickCount = 0;

		try {
			
			String strSelect = "select * from users where nickname ='" + str + "'";
			ResultSet rs = DB.getResultSet(strSelect);
			
			while (rs.next()) {
				strUserId = rs.getString(1);	// 사용자 id
				kickCount = rs.getInt(4);		// 강퇴 횟수
			}
			
			kickCount++;	// 1회 추가
			
			String strUpdate = "UPDATE users "
					+ "SET kick_count="+ kickCount
					+ " WHERE user_id='"+strUserId+"'";
			
			// db 수정
			DB.executeQuery(strUpdate);
			
			
		} catch (SQLException e) {
			kickCount = 0;
		}

		oos.writeObject(str + "#exit#kick");
	}

}
