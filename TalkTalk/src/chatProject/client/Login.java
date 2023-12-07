package chatProject.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

import chatProject.DB;
import chatProject.server.ChatServer;
import chatProject.utill.BtnStyle;
import chatProject.utill.GetJPwField;

public class Login extends JFrame implements ActionListener {

	private JPanel pInfo, pTitle;
	private JTextField tfId;
	private JButton btnLogin;

	final int PointColor = 0xD9E5FF;
	final int BGCOLOR = 0x001054;
	private JLabel imgLabel;
	private JPasswordField tfPw;
	private JButton btnSignup;
	private JPanel pSignup;
	private JPanel pId;
	private JLabel lblId;
	private JPanel pPw;
	private JLabel lblPw;
	private JPanel pLogin;
	private boolean loginCheck;

	public Login() {

		setTitle("로그인");
		setSize(400, 540);

		// 타이틀 추가
		addTitle();
		add(pTitle, BorderLayout.NORTH); // 정보를 입력 받는 패널 추가

		// 정보 입력
		addInput();
		add(pInfo, BorderLayout.CENTER); // 정보를 입력 받는 패널 추가

		// 회원가입 버튼 추가
		addSignUp();

		add(pSignup, BorderLayout.SOUTH); // 정보를 입력 받는 패널 추가

		setLocationRelativeTo(null); // 화면 중앙에 출력
		setResizable(false); // 크기 고정
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	// 타이틀 추가
	private void addTitle() {

		pTitle = new JPanel();
		pTitle.setBackground(Color.white);
		pTitle.setLayout(new FlowLayout(FlowLayout.CENTER));
		pTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

		imgLabel = new JLabel();
		ImageIcon icon = new ImageIcon(Login.class.getResource("/chatProject/images/all4land.png"));
		Image im = icon.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
		ImageIcon icon2 = new ImageIcon(im);
		imgLabel.setIcon(icon2);

		pTitle.add(imgLabel);

	}

	// 정보 입력
	private void addInput() {

		// 아이디를 입력 받는 패널 생성
		pInfo = new JPanel();
		pInfo.setBackground(Color.white); // 색 변경
		pInfo.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 30));

		pInfo.setLayout(new GridLayout(4, 1, 0, 0));

		// id 입력받는 텍스트 필드
		pId = new JPanel();
		pId.setBackground(Color.white); // 색 변경
		pId.setLayout(new BorderLayout());

		lblId = new JLabel("| ID");
		pId.add(lblId, BorderLayout.WEST);

		pInfo.add(pId);

		tfId = new JTextField();
		tfId.addActionListener(this);
		pInfo.add(tfId);

		pPw = new JPanel();
		pPw.setBackground(Color.white); // 색 변경
		pPw.setLayout(new BorderLayout());

		lblPw = new JLabel("| PW");
		pPw.add(lblPw, BorderLayout.WEST);
		pInfo.add(pPw);

		// pw 입력받는 텍스트 필드
		tfPw = new JPasswordField();
		tfPw.addActionListener(this);
		pInfo.add(tfPw);

	}

	// 회원가입 버튼
	private void addSignUp() {

		pSignup = new JPanel();
		pSignup.setBackground(Color.white);
		pSignup.setBorder(BorderFactory.createEmptyBorder(0, 30, 40, 30));
		pSignup.setLayout(new BorderLayout(0, 20));

		// 로그인 버튼

		btnLogin = new JButton("L O G I N");
		BtnStyle.setStyle(btnLogin, BGCOLOR, 0XFFFFFF, 70, 45);
		btnLogin.setBorderPainted(false);
		btnLogin.addActionListener(this);
		pSignup.add(btnLogin, BorderLayout.CENTER);

		btnSignup = new JButton("회원가입");
		BtnStyle.setStyle(btnSignup, 0xFFFFFF, 0X000000, 20, 35);
		btnSignup.setBorderPainted(false);
		btnSignup.addActionListener(this);

		pSignup.add(btnSignup, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {

		new Login();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// 로그인 버튼
		if (obj == btnLogin || obj == tfId || obj == tfPw) {

			String id = tfId.getText();
			String pw = GetJPwField.getPw(tfPw);
			String nickname = "";

			loginCheck = false;

			if (id.equals("") || pw.equals("")) {
				JOptionPane.showMessageDialog(this, "모든 정보를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			} else {

				String selectStr = "SELECT user_id, \"password\", nickname " + "FROM users " + "WHERE user_id ='"
						+ tfId.getText() + "' and \"password\" = '" + pw + "'";

				ResultSet rs = DB.getResultSet(selectStr);

				// 아이디 확인
				try {

					while (rs.next()) {
						nickname = rs.getString(3);
						loginCheck = true;
					}

				} catch (SQLException e2) {
					nickname = "";
					loginCheck = false;
				}

				if (loginCheck == true) {
					
					ChatClient c = new ChatClient("127.0.0.1", tfId.getText(), nickname);
					try {
						c.init(); // 소켓 실행
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					c.startMessage(); // server에 시작 메시지 전송
					dispose();

				} else {
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못되었습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					tfId.setText("");
					tfPw.setText("");
					tfId.requestFocus();
				}
			}
		}
		// 회원가입 버튼 클릭
		else if (obj == btnSignup) {
			new SignUp(this);
			setVisible(false);
		}
	}
}
