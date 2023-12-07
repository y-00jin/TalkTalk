package chatProject.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chatProject.DB;

public class ChatServerThread implements Runnable{
    private Socket socket;
    private ChatServer ms;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    String notice = "";
    
    private Date now;
    SimpleDateFormat simple= new SimpleDateFormat("　(a hh:mm)　");
    SimpleDateFormat insertSimple= new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public ChatServerThread(ChatServer ms){
    	
    	
    	this.notice = ms.getNotice();
        this.ms = ms;
    }
    public synchronized void run(){
        boolean isStop = false;
        try{
            socket = ms.getSocket();
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            String message = null;
            
            while(!isStop){
                
            	// 현재 시간 가져오기
            	now = new Date(System.currentTimeMillis());
                
            	// 메시지를 #으로 나눔
                message = (String)ois.readObject();
                String[] str = message.split("#");
                
                /**
                 * 종료
                 * 
                 */
                if(str[1].equals("exit")){
                	
            		String strUserId="";

            		try {
            			
            			String strSelect = "select * from users where nickname ='" + str[0] + "'";
            			ResultSet rs = DB.getResultSet(strSelect);
            			
            			while (rs.next()) {
            				strUserId = rs.getString(1);	// 사용자 id
            			}
            			
            			String strUpdate = "UPDATE users "
            					+ "SET last_login='"+ insertSimple.format(now)+"' "
            					+ "WHERE user_id='"+strUserId+"'";
            			
            			// db 수정
            			DB.executeQuery(strUpdate);
            			
            			
            		} catch (SQLException e) {

            		}
                	
                	ms.getUserList().remove(str[0]);
                	
                    broadCasting(message + "#" + simple.format(now));
                    isStop = true;
                }
                
                /**
                 * 방에 입장 
                 * 
                 */
                else if(str[1].equals("start")) {
                	
                	// 이름 저장
                	ms.getUserList().add(str[0]);
                	
                	// 공지 저장
                	message = message+ simple.format(now);		// 저장된 공지 추가
                	broadCasting(message);
                }
                
                /**
                 * 사용자 목록
                 * 
                 */
                else if(str[1].equals("list")) {
                	
                	ArrayList<String> temp = ms.getUserList();
                	message = message + "#" +temp.size();		// 내id#list#인원수#a1#a2#a3로 메시지를 변경
                	for(int i=0;i<temp.size();i++) {
                		message = message +"#"+temp.get(i);
                	}
                	broadCasting(message);
                }
                
                /**
                 * 공지
                 */
                else if(str[1].equals("notice")) {
                	ms.setNotice(str[0]+" : "+str[2]);	// 보낸사람 : 공지메시지 저장
                	broadCasting(message);
                }
                /**
                 * 귓속말
                 */
                else if(str[1].equals("Whisp")) {
                	
                	
                	message = str[0] + "#Whisp#" + str[3] + "#"+ str[4];	// 보내는 사람 + Whisp + 받을사람 + 메시지
                	
                	
                	String userId = str[2];
                	String receiveId = str[3];
                	
                	String strInsert = "INSERT INTO chat_log "
                			+ "(user_id, receive_id, chat_msg, chat_time) "
                			+ "VALUES('"+userId+"', '"+receiveId +"', '"+str[4]+"', '"+ insertSimple.format(now)+"')";
                	
                	DB.executeQuery(strInsert);
                	
                	
                	broadCasting(message + simple.format(now));
                }
                /**
                 * 강퇴
                 */
                else if(str[1].equals("kick")) {
                	broadCasting(message);
                }
                else{
                	
                	message = str[0] + "#" + str[3];
                	String userId = str[2];
                	String strInsert = "INSERT INTO chat_log "
                			+ "(user_id, receive_id, chat_msg, chat_time) "
                			+ "VALUES('"+userId+"', '전체', '"+str[3]+"', '"+ insertSimple.format(now)+"')";
                	
                	DB.executeQuery(strInsert);
                	
                	broadCasting(message + simple.format(now));
                
                
                }
            }
            ms.getList().remove(this);
            System.out.println(socket.getInetAddress()+ "정상적으로 종료하셨습니다");
            System.out.println("list size : "+ms.getList().size());
        }catch(Exception e){
        	e.printStackTrace();
        	ms.getList().remove(this);
            System.out.println(socket.getInetAddress()+ "비정상적으로 종료하셨습니다");
            System.out.println("list size : "+ms.getList().size());
        }
    }
    public void broadCasting(String message)throws IOException{
        for(ChatServerThread ct : ms.getList()){
           ct.send(message);
        }
    }
    public void send(String message)throws IOException{
    	
        oos.writeObject(message);        
    }
    
   
}
