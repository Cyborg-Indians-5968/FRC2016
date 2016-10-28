package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;
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
	
	public uART() {
		port = new SerialPort(38400, Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		port.setTimeout(.1);
	}
	
	public String getAimData(boolean firstLoop)
	{	
		if(firstLoop){
			try{
				byte[] b = port.read(port.getBytesReceived());
			}
			catch(RuntimeException ex){}
			port.write("gett".getBytes(StandardCharsets.US_ASCII), 4);
			port.flush();
		}
		
		String result = null;
		try{
			byte[] b = port.read(port.getBytesReceived());
			result = new String(b, StandardCharsets.US_ASCII);
			
			if (result.length() == 0) {
				return null;
			}
		}
		catch(RuntimeException ex){
			//DriverStation.reportError(ex.toString(), true);
		}
			
		return result;
	}
}

