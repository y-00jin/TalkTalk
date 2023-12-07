package chatProject.utill;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;

public class BtnStyle {

	
	/**
	 * 
	 * @param btn : 스타일 적용할 버튼
	 * @param backColor : 배경색
	 * @param fontColor	: 글자색
	 * @param width		: 너비
	 * @param height	: 높이
	 */
	public static void setStyle(JButton btn, int backColor, int fontColor, int width, int height) {
		
		btn.setBackground(new Color(backColor));
		btn.setForeground(new Color(fontColor));
		btn.setPreferredSize(new Dimension(width, height));
	}
}
