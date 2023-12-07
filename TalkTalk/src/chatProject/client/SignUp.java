package chatProject.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chatProject.DB;
import chatProject.utill.BtnStyle;
import chatProject.utill.GetJPwField;

public class SignUp extends JFrame implements ActionListener {

	private JPanel pInfo, pTitle;
	private JTextField tfId;
	private JButton btnSignup;

	final int PointColor = 0xD9E5FF;
	final int BGCOLOR = 0x001054;
	private JPasswordField tfPw;
	private JLabel lblTitle;
	private JPanel pId;
	private JButton btnCheck;
	private JTextField tfName;
	private JLabel lblId;
	private JPanel pPw;
	private JLabel lblPw;
	private JPanel pPw2;
	private JLabel lblPw2;
	private JPanel pName;
	private JLabel lblName;
	private Login login;
	private JPasswordField tfPw2;

	private boolean idCheck;
	private boolean nameCheck;
	private JButton btnNameCheck;

	public SignUp(Login login) {

		idCheck = false;
		nameCheck = false;

		this.login = login;

		setTitle("톡Talk");
		setSize(400, 500);

		// 타이틀 추가
		addTitle();
		add(pTitle, BorderLayout.NORTH); // 정보를 입력 받는 패널 추가

		// 정보 입력 필드 추가
		addInput();
		add(pInfo, BorderLayout.CENTER); // 정보를 입력 받는 패널 추가

		setLocationRelativeTo(null); // 화면 중앙에 출력
		setResizable(false); // 크기 고정
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				login.setVisible(true);
			}
		});
	}

	// 타이틀 추가
	private void addTitle() {

		pTitle = new JPanel();
		pTitle.setBackground(Color.white);
		pTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pTitle.setBorder(BorderFactory.createEmptyBorder(50, 30, 0, 0));

		lblTitle = new JLabel("| 회원가입");

		pTitle.add(lblTitle);

	}

	// 정보 입력
	private void addInput() {

		// 아이디를 입력 받는 패널 생성
		pInfo = new JPanel();
		pInfo.setBackground(Color.white); // 색 변경
		pInfo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		pInfo.setLayout(new GridLayout(5, 1, 0, 30));

		// id 입력받는 패널
		pId = new JPanel();
		pId.setLayout(new BorderLayout(10, 0));
		pId.setBackground(Color.white);
		// 아이디
		lblId = new JLabel("　　　　ID : ");
		lblId.setPreferredSize(new Dimension(70, 30));
		pId.add(lblId, BorderLayout.WEST);

		// id 입력받는 텍스트 필드
		tfId = new JTextField();
		tfId.addActionListener(this);
		tfId.setPreferredSize(new Dimension(70, 30));
		pId.add(tfId, BorderLayout.CENTER);

		// 중복 체크 버튼
		btnCheck = new JButton("중복");
		BtnStyle.setStyle(btnCheck, BGCOLOR, 0XFFFFFF, 60, 30);
		btnCheck.addActionListener(this);
		pId.add(btnCheck, BorderLayout.EAST);
		pInfo.add(pId);

		// pw 입력받는 패널
		pPw = new JPanel();
		pPw.setLayout(new BorderLayout(10, 0));
		pPw.setBackground(Color.white);
		// pw 라벨
		lblPw = new JLabel("　　　 PW : ");
		lblPw.setPreferredSize(new Dimension(70, 30));
		pPw.add(lblPw, BorderLayout.WEST);

		// pw 입력받는 텍스트 필드
		tfPw = new JPasswordField();
		tfPw.addActionListener(this);
		pPw.add(tfPw, BorderLayout.CENTER);
		pInfo.add(pPw);

		// pw 입력받는 패널
		pPw2 = new JPanel();
		pPw2.setLayout(new BorderLayout(10, 0));
		pPw2.setBackground(Color.white);
		// pw 라벨
		lblPw2 = new JLabel("PW 재확인 : ");
		lblPw2.setPreferredSize(new Dimension(70, 30));
		pPw2.add(lblPw2, BorderLayout.WEST);

		// pw 입력받는 텍스트 필드
		tfPw2 = new JPasswordField();
		tfPw2.addActionListener(this);
		pPw2.add(tfPw2, BorderLayout.CENTER);
		pInfo.add(pPw2);

		// 이름 입력받는 패널
		pName = new JPanel();
		pName.setLayout(new BorderLayout(10, 0));
		pName.setBackground(Color.white);
		// pw 라벨
		lblName = new JLabel("　  닉네임 : ");
		lblName.setPreferredSize(new Dimension(70, 30));
		pName.add(lblName, BorderLayout.WEST);

		// 이름을 입력받는 텍스트 필드
		tfName = new JTextField();
		tfName.addActionListener(this);
		pName.add(tfName, BorderLayout.CENTER);

		// 중복 체크 버튼
		btnNameCheck = new JButton("중복");
		BtnStyle.setStyle(btnNameCheck, BGCOLOR, 0XFFFFFF, 60, 30);
		btnNameCheck.addActionListener(this);
		pName.add(btnNameCheck, BorderLayout.EAST);

		pInfo.add(pName);

		// 로그인 버튼
		btnSignup = new JButton("회원가입");
		BtnStyle.setStyle(btnSignup, BGCOLOR, 0XFFFFFF, 20, 35);
		btnSignup.setBorderPainted(false);
		btnSignup.addActionListener(this);
		pInfo.add(btnSignup);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		// 텍스트 필드 값 불러오기
		String strId = tfId.getText();
		String pw1 = GetJPwField.getPw(tfPw);
		String pw2 = GetJPwField.getPw(tfPw2);
		String strName = tfName.getText();

		// 현재 시간 가져오기
		Date now = new Date(System.currentTimeMillis());
		// 형식 지정
		SimpleDateFormat simple = new SimpleDateFormat("YYYY-MM-DD hh:mm");

		if (obj == btnCheck) {

			String sql = "SELECT * FROM users WHERE user_id ='" + strId + "'";

			ResultSet rs = DB.getResultSet(sql);
			try {
				// id 입력을 하지 않은 경우
				if (strId.equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {
					if (rs.next()) {
						// 중복된 아이디 있는 경우
						JOptionPane.showMessageDialog(null, "중복된 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
						idCheck = false;
					} else {
						// 중복된 아이디 없는 경우
						JOptionPane.showMessageDialog(null, "사용 가능한 아이디입니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
						tfId.setEditable(false);
						idCheck = true;
					}
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
		else if(obj == btnNameCheck) {


			String sql = "SELECT * FROM users WHERE nickname ='" + strName + "'";

			ResultSet rs = DB.getResultSet(sql);
			try {
				// id 입력을 하지 않은 경우
				if (strName.equals("")) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {
					if (rs.next()) {
						// 중복된 닉네임이 있는 경우
						JOptionPane.showMessageDialog(null, "중복된 닉네임입니다.", "오류", JOptionPane.ERROR_MESSAGE);
						nameCheck = false;
					} else {
						// 중복된 아이디 없는 경우
						JOptionPane.showMessageDialog(null, "사용 가능한 닉네임입니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
						tfName.setEditable(false);
						nameCheck = true;
					}
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}


		}

		else if (obj == btnSignup) { // 회원가입 버튼

			// 모든 정보 입력하지 않은 경우
			if (strId.equals("") || pw1.equals("") || pw2.equals("") || strName.equals("")) {
				JOptionPane.showMessageDialog(null, "모든 정보를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
			} else {
				if (idCheck == false || nameCheck == false) {
					JOptionPane.showMessageDialog(null, "ID 또는 닉네임 중복 체크를 해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {

					if (pw1.equals(pw2)) {
						// insert 작업
						String insertStr = "INSERT INTO users "
								+ "(user_id, nickname, \"password\", kick_count) " + "VALUES('" + strId
								+ "', '" + strName + "', '" + pw1 + "', 0)";
						DB.executeQuery(insertStr);

						dispose();
						login.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "두 비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		}
	}
}
