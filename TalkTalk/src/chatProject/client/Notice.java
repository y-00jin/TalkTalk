package chatProject.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;

import chatProject.DB;
import chatProject.utill.BtnStyle;

public class Notice extends JFrame implements ActionListener {

	private JPanel pInfo, pTitle;
	private JTextField tfNotice;
	private Label lblId;
	private JButton btnAdd;
	private JLabel lblTitle;

	private ObjectOutputStream oos;
	private String id;
	private ChatClient chat;
	final int BGCOLOR = 0x001054;

	public Notice(ChatClient chat) {

		this.oos = chat.getOos();
		this.id = chat.getId();

		this.chat = chat;
		setTitle("공지");
		setSize(400, 200);

		// 타이틀 추가
		addTitle();
		add(pTitle, BorderLayout.NORTH); // 정보를 입력 받는 패널 추가

		// 정보 입력
		addInput();
		add(pInfo, BorderLayout.CENTER); // 정보를 입력 받는 패널 추가

		setLocation(chat.returnFrame().getX() + this.getWidth(), chat.returnFrame().getY());
		setResizable(false); // 크기 고정
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	// 타이틀 추가
	private void addTitle() {

		pTitle = new JPanel();
		pTitle.setBackground(Color.white);
		pTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pTitle.setBorder(BorderFactory.createEmptyBorder(30, 15, 0, 0));
		lblTitle = new JLabel("| 공지를 입력하세요");
		pTitle.add(lblTitle);

	}

	// 정보 입력
	private void addInput() {

		// 아이디를 입력 받는 패널 생성
		pInfo = new JPanel();
		pInfo.setBackground(Color.white); // 색 변경
		pInfo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		pInfo.setLayout(new FlowLayout(1, 10, 0));

		// 입력받는 텍스트 필드
		tfNotice = new JTextField();
		tfNotice.setPreferredSize(new Dimension(240, 30));
		tfNotice.addActionListener(this);

		pInfo.add(tfNotice);

		// 등록 버튼
		btnAdd = new JButton("등록");
		BtnStyle.setStyle(btnAdd, BGCOLOR, 0XFFFFFF, 100, 30);
		btnAdd.setBorderPainted(false);
		btnAdd.addActionListener(this);
		pInfo.add(btnAdd);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == tfNotice || obj == btnAdd) {
			if (tfNotice.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "공지를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			} else {

				String strInsert = "INSERT INTO \"notice\" (user_id, notice_msg) "
				+ "VALUES('" + id + "', '"+tfNotice.getText() + "')";
				
				// db에 저장
				DB.executeQuery(strInsert);

				try {
					oos.writeObject(chat.getNickname() +"#notice#"+tfNotice.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				

				tfNotice.setText("");
				chat.noticeReset();	// 공지 리셋
				dispose();
				
			}
		}
	}
}
