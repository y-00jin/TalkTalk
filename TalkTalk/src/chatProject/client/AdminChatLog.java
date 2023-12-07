package chatProject.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import chatProject.DB;
import chatProject.utill.BtnStyle;

public class AdminChatLog extends JFrame implements ActionListener {

	final int BGCOLOR = 0x001054;
	private AdminMenu adminMenu;
	private JPanel pSearch;
	private JTextField tfSearch;
	private JButton btnSearch;

	private JLabel lblSearchTitle;
	private JPanel pSearchInput;
	private JTextArea jta;
	private JScrollPane jsp;
	private JPanel pChat;

	public AdminChatLog(AdminMenu adminMenu) {

		this.adminMenu = adminMenu;
		setTitle("관리자 메뉴 - 채팅 기록");
		setSize(600, 800);

		// 검색 생성
		addSearch();
		add(pSearch, BorderLayout.NORTH);

		// 채팅기록 생성
		addChat();
		add(pChat, BorderLayout.CENTER);

		setResizable(false);
		setLocation(adminMenu.getX(), adminMenu.getY() + adminMenu.getHeight());
		setVisible(true);
	}

	// 검색 패널
	private void addSearch() {

		pSearch = new JPanel();
		pSearch.setLayout(new BorderLayout(0, 20));
		pSearch.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 0));
		pSearch.setBackground(Color.white);

		lblSearchTitle = new JLabel("  | 닉네임 검색");
		pSearch.add(lblSearchTitle, BorderLayout.NORTH);

		pSearchInput = new JPanel();
		pSearchInput.setLayout(new FlowLayout(FlowLayout.LEFT));
		pSearchInput.setBackground(Color.white);

		tfSearch = new JTextField();
		tfSearch.addActionListener(this);
		tfSearch.setPreferredSize(new Dimension(250, 30));
		pSearchInput.add(tfSearch);

		btnSearch = new JButton("검색");
		btnSearch.addActionListener(this);
		BtnStyle.setStyle(btnSearch, BGCOLOR, 0XFFFFFF, 100, 30);
		pSearchInput.add(btnSearch);
		pSearch.add(pSearchInput, BorderLayout.CENTER);

	}

	// 채팅 기록 생성
	private void addChat() {

		pChat = new JPanel();
		pChat.setLayout(new BorderLayout());
		pChat.setBorder(BorderFactory.createEmptyBorder(25, 38, 40, 40));
		pChat.setBackground(Color.white);

		// 채팅이 보이는 스크롤 팬 생성
		jta = new JTextArea("", 10, 50);
		jta.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		jta.setEditable(false);

		String join = "select distinct * "
				+ "from chat_log "
				+ "join users "
				+ "on users.user_id = chat_log.user_id "
				+ "order by chat_id desc";

		reChat(join);	// 전체 조회

		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jta.setLineWrap(true); // 자동 줄바꿈

		pChat.add(jsp);
	}

	// 채팅 기록 db에서 불러옴
	private void reChat(String selectStr) {

		try {
			ResultSet rs = DB.getResultSet(selectStr);

			while (rs.next()) {

				String userNick = rs.getString(7);
				String receiveId = rs.getString(5);
				String chatMsg = rs.getString(3);
				String chatTime = rs.getString(4);

				if(receiveId.equals("전체")) {
					String msg = "[ " + receiveId + " ] " + userNick + " : " + chatMsg + " (" + chatTime +" )"+ System.getProperty("line.separator");
					jta.append(msg);
				}
				else {
					String msg = "[ " + userNick + " >> "+ receiveId +" ] " + chatMsg + " (" + chatTime +")"+ System.getProperty("line.separator");
					jta.append(msg);
				}
				jta.setCaretPosition(jta.getDocument().getLength());

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();
		if(obj == btnSearch || obj == tfSearch) {

			jta.setText("");

			if(tfSearch.getText().equals("")) {
				String join = "select distinct * "
						+ "from chat_log "
						+ "join users "
						+ "on users.user_id = chat_log.user_id "
						+ "order by chat_id desc";

				reChat(join);	// 전체 조회
			}else {

				String join = "select distinct * "
					+ "from chat_log "
					+ "join users "
					+ "on users.user_id = chat_log.user_id "
					+ "where nickname like '%"+tfSearch.getText()+"%' "
					+ "order by chat_id desc";

				reChat(join);	// 전체 조회

			}

			tfSearch.setText("");
		}
	}

}
