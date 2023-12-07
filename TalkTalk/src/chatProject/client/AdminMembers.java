package chatProject.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import chatProject.DB;
import chatProject.utill.BtnStyle;

public class AdminMembers extends JFrame implements ActionListener {

	final int BGCOLOR = 0x001054;
	private AdminMenu adminMenu;
	private JPanel pSearch;
	private JTextField tfSearch;
	private JButton btnSearch;
	private DefaultTableModel model;
	private JTable table;
	private JPanel pInfo;
	private JLabel lblTitle;
	private JPanel pTable;
	private Vector<String> returnColumn;
	private JLabel lblSearchTitle;
	private JPanel pSearchInput;

	public AdminMembers(AdminMenu adminMenu) {

		this.adminMenu = adminMenu;
		setTitle("톡Talk");
		setSize(800, 500);

		// 검색 생성
		addSearch();
		add(pSearch, BorderLayout.NORTH);

		// 테이블 생성
		addInfo();
		add(pInfo, BorderLayout.CENTER);

//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(adminMenu.getX(), adminMenu.getY()+adminMenu.getHeight());
		setVisible(true);
	}

	// 검색 패널
	private void addSearch() {


		pSearch = new JPanel();
		pSearch.setLayout(new BorderLayout(0, 20));
		pSearch.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 0));
		pSearch.setBackground(Color.white);

		lblSearchTitle = new JLabel("  | ID or 닉네임 검색");
		pSearch.add(lblSearchTitle, BorderLayout.NORTH);


		pSearchInput = new JPanel();
		pSearchInput.setLayout(new FlowLayout(FlowLayout.LEFT));
		pSearchInput.setBackground(Color.white);

		tfSearch = new JTextField();
		tfSearch.setPreferredSize(new Dimension(300, 30));
		tfSearch.addActionListener(this);
		pSearchInput.add(tfSearch);

		btnSearch = new JButton("검색");
		BtnStyle.setStyle(btnSearch, BGCOLOR, 0XFFFFFF, 100, 30);
		btnSearch.addActionListener(this);
		pSearchInput.add(btnSearch);
		pSearch.add(pSearchInput, BorderLayout.CENTER);

	}

	// 사람 데이터를 가져와 테이블 생성
	private void addInfo() {

		pInfo = new JPanel();
		pInfo.setBorder(BorderFactory.createEmptyBorder(15, 39, 40, 0));
		pInfo.setLayout(new BorderLayout());
		pInfo.setBackground(Color.white);

		lblTitle = new JLabel("| 사용자 목록");
		pInfo.add(lblTitle, BorderLayout.NORTH);


		// 테이블 생성
		pTable = new JPanel();
		pTable.setBackground(Color.white);
		pTable.setLayout(new FlowLayout(FlowLayout.LEFT));
		pTable.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		// 테이블 헤더 생성
		returnColumn = new Vector<String>();
		returnColumn.add("ID");
		returnColumn.add("이름");
		returnColumn.add("강퇴 횟수");
		returnColumn.add("마지막 접속일");

		model = new DefaultTableModel(returnColumn, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		// 테이블 생성
		String strRetable ="select * from users order by last_login desc";
		reTable(strRetable);

		table.getTableHeader().setReorderingAllowed(false); // 테이블 편집X
		table.setFillsViewportHeight(true); // 테이블 배경색
		JTableHeader tableHeader = table.getTableHeader(); // 테이블 헤더 값 가져오기
		tableHeader.setBackground(new Color(BGCOLOR)); // 테이블 헤더의 색 지정
		tableHeader.setForeground(Color.white); // 테이블 헤더의 색 지정

		// 스크롤 팬
		JScrollPane sc = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sc.setPreferredSize(new Dimension(700, 230));
		pTable.add(sc);

		pInfo.add(pTable, BorderLayout.CENTER);


	}

	private void reTable(String strRetable) {
		model.setNumRows(0);

		ResultSet rs = DB.getResultSet(strRetable);
		String[] rsArr = new String[4]; // 값 받아올 배열

		try {


			while(rs.next()) {

				rsArr[0] = rs.getString(1);
				rsArr[1] = rs.getString(2);
				rsArr[2] = rs.getString(4).toString();
				if(rs.getString(5) == null) {
					rsArr[3] = "";
				}else {
					rsArr[3] = rs.getString(5).toString().substring(0, 16);
				}


				model.addRow(rsArr); // 모델에 배열 추가
			}



		} catch (SQLException e) {
			e.printStackTrace();
		}

		table = new JTable(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();


		if(obj == btnSearch || obj == tfSearch) {

			if(tfSearch.getText().equals("")) {
				String strRetable ="select * from users order by last_login desc";
				reTable(strRetable);	// 전체 조회
			}
			else {
				String strRetable ="select * from users where user_id like '%"+tfSearch.getText()+"%' or nickname like '%"+tfSearch.getText()+"%' order by last_login desc ";
				reTable(strRetable);	// 닉네임으로 검색
			}
			tfSearch.setText("");
		}
	}


}
