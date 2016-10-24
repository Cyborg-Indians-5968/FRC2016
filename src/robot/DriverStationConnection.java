package robot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DriverStationConnection {
	
	private Socket connection = null;
	
	public DriverStationConnection(){
		
		try{
			ServerSocket s = new ServerSocket(5805);
			s.setSoTimeout(30);
			connection = s.accept();
		}
		catch(IOException i){
			System.err.println("Can't connect to Image Viewer! Giving up!");
		}
	}
	
	public void sendImage(byte[] b){
		try{
			if(connection != null){
				connection.getOutputStream().write(b);
				connection.getOutputStream().flush();
			}
			else{
				System.err.println("Image Viewer isn't connected!");
			}
		}
		catch(IOException e){}
	}
}
