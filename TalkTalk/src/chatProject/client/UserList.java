package chatProject.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import chatProject.utill.BtnStyle;

public class UserList extends JFrame implements ListSelectionListener, ActionListener {

	final int BGCOLOR = 0x001054;

	private JPanel pTitle, pUList, pWhisp;
	private JLabel lblTitle;
	private JList uList;
	private Vector<String> vUserList = new Vector<>();
	private JButton btnWhisp, btnKick;
	private String reciveUser, id;
	private ObjectOutputStream oos;
	private JTextField tfSendMsg;

	private String nickname;

	public UserList(ChatClient chat) {

		this.id = chat.getId();
		this.oos = chat.getOos();
		this.nickname = chat.getNickname();
		setTitle("사용자 목록");
		setSize(400, 550);

		// 타이틀 생성
		addTitle();
		add(pTitle, BorderLayout.NORTH);

		// 사용자 목록
		addUserList();
		add(pUList, BorderLayout.CENTER);

		// 귓속말 버튼
		addWhisper();
		add(pWhisp, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				dispose();
			}

		});

		setLocation(chat.returnFrame().getX() + this.getWidth(), chat.returnFrame().getY()); // 위치 설정
		setResizable(false); // 크기 고정
		setVisible(true);
	}

	// 타이틀 생성
	private void addTitle() {

		pTitle = new JPanel();
		pTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pTitle.setBackground(new Color(BGCOLOR));
		lblTitle = new JLabel("| 사용자 목록");
		lblTitle.setForeground(Color.white);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		pTitle.add(lblTitle);

	}

	// 사용자 목록 생성
	private void addUserList() {

		pUList = new JPanel();
		pUList.setBackground(Color.white);
		pUList.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		uList = new JList<>(vUserList);
		uList.setFont(new Font("맑은고딕", Font.PLAIN, 14));
		uList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		uList.setFixedCellHeight(35);

		uList.setCellRenderer(new DefaultListCellRenderer() {
			public int getHorizontalAlignment() {
				return CENTER;
			}
		});

		uList.addListSelectionListener(this);
		uList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택될 수 있도록

		JScrollPane jspList = new JScrollPane(uList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspList.setBackground(Color.white);
		jspList.setPreferredSize(new Dimension(360, 250));

		pUList.add(jspList);

	}

	// 귓속말 버튼
	private void addWhisper() {

		pWhisp = new JPanel();
		pWhisp.setBackground(Color.white);
		pWhisp.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		pWhisp.setLayout(new GridLayout(3, 1, 20, 20));

		tfSendMsg = new JTextField(10);
		tfSendMsg.setPreferredSize(new Dimension(20, 35));
		tfSendMsg.addActionListener(this);
		pWhisp.add(tfSendMsg);

		btnWhisp = new JButton("귓속말");
		BtnStyle.setStyle(btnWhisp, BGCOLOR, 0XFFFFFF, 20, 35);
		btnWhisp.addActionListener(this);
		pWhisp.add(btnWhisp);

		if(id.equals("admin")) {
			btnKick = new JButton("강퇴");
			BtnStyle.setStyle(btnKick, BGCOLOR, 0XFFFFFF, 20, 35);
			btnKick.addActionListener(this);
			pWhisp.add(btnKick);
		}
		
	}

	public Vector<String> getvUserList() {
		return vUserList;
	}

	public void setvUserList(Vector<String> vUserList) {
		this.vUserList = vUserList;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (!e.getValueIsAdjusting()) { // 이거 없으면 mouse 눌릴때, 뗄때 각각 한번씩 호출되서 총 두번 호출
			reciveUser = uList.getSelectedValue().toString(); // 보내는 사람 저장
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == btnKick) {

			if (uList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(this, "강퇴할 상대를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			}

			try {
				if(reciveUser.equals("관리자")) {
					JOptionPane.showMessageDialog(this, "본인을 강퇴할 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				}
				else {
					oos.writeObject(nickname + "#kick#" + reciveUser); // 보내는 사람 + kick + 받을사람
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		if (obj == btnWhisp || obj == tfSendMsg) {

			if (uList.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(this, "귓속말 상대를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			}

			else if (tfSendMsg.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "메시지를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			} 
			else if(reciveUser.equals(nickname)) {
				JOptionPane.showMessageDialog(this, "본인에게 귓속말을 보낼 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			}
			else {
				try {
					oos.writeObject(nickname + "#Whisp#" + id + "#" + reciveUser + "#" + tfSendMsg.getText()); // 보내는 사람 + Whisp + 받을사람 + 메시지
					tfSendMsg.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}

	}

	public JPanel getpUList() {
		return pUList;
	}

	

}
