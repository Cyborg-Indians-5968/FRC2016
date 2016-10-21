package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Timer;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

//TODO: Consider using query-response pattern. Methods below send query, and then another method checks for responses.
//TODO: Need to fix life-cycle of responses that causes unnecessary long waits or not long enough waits for responses.
//TODO: Remove this suppress warnings box

public class uART {
	private SerialPort port;
	public double angle;
	public double distance;
	
	private boolean connected;
	
	public uART() {
		port = new SerialPort(38400, Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		port.setTimeout(2);
		
		String received = new String(SerialCommunicate("ping", 10), StandardCharsets.US_ASCII);
		
		if(received.equals("here")){
			System.out.println("Raspberry Pi Found!");
			connected = true;
		}
		else if(received.equals("")){
			System.out.println("Raspberry Pi Not Found!");
			connected = false;
		}
		else{
			System.out.println("Raspberry Pi Found, but connection may be bad");
			connected = true;
		}
	}
	
	public String aimToShoot()
	{
		String aimData = new String(SerialCommunicate("gett", 5), StandardCharsets.US_ASCII);
		return aimData;
	}
	
	public byte[] takePicture()
	{
		if(connected){
			byte[] image = SerialCommunicate("pict", 5);
			return image;
		}
		else{
			System.err.println("Can't take picture! Raspberry pi not connected!");
			return null;
		}
	}
	
	private byte[] SerialCommunicate(String toSend, int timeoutSeconds)
	{
		int loops = 0;
		while(true){
			port.write(toSend.getBytes(StandardCharsets.US_ASCII), 4);
			port.flush();
			
			Timer.delay(.25);
			
			if(port.getBytesReceived() > 0){
				byte[] b = port.read(port.getBytesReceived());
			}
			else if(loops >= timeoutSeconds * 4){
				return null;
			}
			loops++;
		}
	}
}
