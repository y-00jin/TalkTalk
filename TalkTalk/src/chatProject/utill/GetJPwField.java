package chatProject.utill;

import javax.swing.JPasswordField;

public class GetJPwField {

	/**
	 * *로 표시된 비밀번호 출력
	 * @param jpf
	 * @return
	 */
	public static String getPw(JPasswordField jpf) {
		
		String pw="";
		char[] arrJpf = jpf.getPassword();

		// secret_pw 배열에 저장된 암호의 자릿수 만큼 for문 돌리면서 cha 에 한 글자씩 저장
		for (char cha : arrJpf) {
			Character.toString(cha); // cha 에 저장된 값 string으로 변환

			// pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자
			pw += (pw.equals("")) ? "" + cha + "" : "" + cha + "";
		}
		
		return pw;
	}
}
