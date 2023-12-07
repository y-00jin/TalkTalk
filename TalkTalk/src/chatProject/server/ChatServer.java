package chatProject.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	private ArrayList<ChatServerThread> list;
	private Socket socket;
	
	private ArrayList<String> userId = new ArrayList<>();
	String notice = "공지가 없습니다.";
	
	public ChatServer()throws IOException{
	
		list = new ArrayList<ChatServerThread>();
		ServerSocket serverSocket = new ServerSocket(5000);
		ChatServerThread mst = null;
		boolean isStop = false;
		
		while(!isStop){
			System.out.println("Server ready...");
			socket = serverSocket.accept();
			
			mst = new ChatServerThread(this);
			list.add(mst);
			Thread t = new Thread(mst);
			t.start();
		}
	}
	public ArrayList<ChatServerThread>getList(){
		return list;
	}
	public Socket getSocket()
	{
		return socket;
	}
	public ArrayList<String> getUserList() {
		return userId;
	}
	public void setUserId(ArrayList<String> userId) {
		this.userId = userId;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	

}
