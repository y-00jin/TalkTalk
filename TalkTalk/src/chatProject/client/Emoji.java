package chatProject.client;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

import chatProject.DB;

public class Emoji extends JFrame implements ActionListener {

	final int BGCOLOR = 0x001054;
	private JPanel pEmoji, pTitle;
	
//	private JButton btnEmoji1, btnEmoji2, btnEmoji3, btnEmoji4, btnEmoji5, btnEmoji6;
	private JLabel lblTitle;
	private ArrayList<JButton> arrBtnEmoji;
	
	private ChatClient chat;
	private JPanel pEmojiBtn;
	private JScrollPane jspEmoji;
	
	public Emoji(ChatClient chat) {	

		arrBtnEmoji = new ArrayList<>();
		
		this.chat = chat;
		
		setTitle("EMOJI");
		setSize(420, 350);

		
		addTitle();
		add(pTitle, BorderLayout.NORTH);
		
		// 이모티콘 추가
		addGridEmoji();
		add(jspEmoji, BorderLayout.CENTER);

		setLocation(chat.returnFrame().getX()-this.getWidth(), chat.returnFrame().getY() + this.getHeight());
//		setResizable(false); // 크기 고정
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
	}


	private void addTitle() {
		
		pTitle = new JPanel();
		pTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		pTitle.setBackground(new Color(BGCOLOR));
		lblTitle = new JLabel("| 이모티콘 목록");
		lblTitle.setForeground(Color.white);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
		
		pTitle.add(lblTitle);
		
	}


	private void addGridEmoji() {
		
		// DB에 저장된 이모티콘 목록 가져옴
		String selectStr = "SELECT emoji_text FROM emoji order by emoji_id";
		ResultSet rs = DB.getResultSet(selectStr);
		try {
			while(rs.next()) {
				
				JButton btnArr = new JButton(rs.getString(1));
				btnStyle(btnArr);
			}
			
		} catch (SQLException e) {
		}
		
		pEmoji = new JPanel();
		int height = (arrBtnEmoji.size()/3 ) + 1;
		pEmoji.setLayout(new GridLayout(height, 3, 20, 20));
		
		pEmoji.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		pEmoji.setBackground(Color.white);
		
		
		for(int i=0;i<arrBtnEmoji.size();i++) {
			pEmoji.add(arrBtnEmoji.get(i));
		}
		
		
		jspEmoji = new JScrollPane(pEmoji, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
	}

	public void btnStyle(JButton btn) {
		btn.setPreferredSize(new Dimension(100, 50));
		btn.setBackground(Color.white);
		btn.addActionListener(this);
		arrBtnEmoji.add(btn);		// arraylist에 버튼 추가
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();
		
		for(int i = 0;i<arrBtnEmoji.size();i++) {
			if(obj == arrBtnEmoji.get(i)) {
				JTextField chatInputTf = chat.getTfInput();
				
				chatInputTf.setText(chatInputTf.getText() + " " + arrBtnEmoji.get(i).getText());	// 채팅 입력창에 선택한 이모티콘 추가
			}
		}
	}
}
