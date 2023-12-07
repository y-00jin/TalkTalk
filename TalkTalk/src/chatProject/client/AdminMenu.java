package chatProject.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chatProject.utill.BtnStyle;

public class AdminMenu extends JFrame implements ActionListener {

	private JPanel pTitle;

	final int PointColor = 0xD9E5FF;
	final int BGCOLOR = 0x001054;
	private JLabel imgLabel;

	private JPanel pMenu;

	private JButton btnMembers,btnChatLog;


	public AdminMenu(ChatClient chat) {

		setTitle("톡Talk");
		setSize(400, 300);

		// 타이틀 추가
		addTitle();
		add(pTitle, BorderLayout.NORTH);

		// 메뉴 버튼 추가
		addMenu();
		add(pMenu, BorderLayout.CENTER);


		setLocation(chat.returnFrame().getX()+this.getWidth(), chat.returnFrame().getY());
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
		pTitle.setLayout(new FlowLayout(FlowLayout.CENTER));
		pTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

		imgLabel = new JLabel();
		ImageIcon icon = new ImageIcon(AdminMenu.class.getResource("/chatProject/images/talkLogo.png"));
		Image im = icon.getImage().getScaledInstance(150, 60, Image.SCALE_SMOOTH);
		ImageIcon icon2 = new ImageIcon(im);
		imgLabel.setIcon(icon2);

		pTitle.add(imgLabel);

	}

	// 정보 입력
	private void addMenu() {

		pMenu = new JPanel();
		pMenu.setLayout(new GridLayout(2, 1, 0, 20));
		pMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		pMenu.setBackground(Color.white);

		btnMembers = new JButton("사용자 목록");
		BtnStyle.setStyle(btnMembers, BGCOLOR, 0XFFFFFF, 0, 0);
		btnMembers.addActionListener(this);
		pMenu.add(btnMembers);


		btnChatLog = new JButton("채팅 기록");
		BtnStyle.setStyle(btnChatLog, BGCOLOR, 0XFFFFFF, 0, 0);
		btnChatLog.addActionListener(this);
		pMenu.add(btnChatLog);

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if(obj == btnMembers) {
			new AdminMembers(this);

		}
		else if(obj == btnChatLog) {
			new AdminChatLog(this);

		}
	}
}
