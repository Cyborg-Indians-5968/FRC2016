package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Timer;

import java.util.Scanner;

//TODO: Consider using query-response pattern. Methods below send query, and then another method checks for responses.
//TODO: Need to fix life-cycle of responses that causes unnecessary long waits or not long enough waits for responses.
//TODO: Remove this suppress warnings box
@SuppressWarnings("unused")
public class Uart {
	/*private SerialPort port;
	public double angle;
	public double distance;
	
	public Uart() {
        //TODO: !!! I don't know where you got this baud rate, but it is not a typical one. I'd suggest 38400, make sure you update the Pi too. If you are sure we tried with this value and it worked before, then you can leave it.
        //TODO: I say this because some serial hardware is very picky about the baud rates it is capable of.
		port = new SerialPort(5600, Port.kMXP);
	}
*/
	/*public boolean checkEm()
	{
		port.writeString("TopKek");
		port.flush();
		Timer.delay(.25);
		String stringRec = port.readString();
		if (stringRec.equalsIgnoreCase("topLel"))
		{
			System.out.println("We've got 'er captain!");
			return true;
		}
		else
		{
			System.out.println("We've lost 'er captain!");
			return false;
		}
	}//end of checkEm method
	
	public void aquireTarget()
	{
		port.writeString("fire");
		port.flush();
		Timer.delay(.25); //TODO: Investigate if necessary
        
        //TODO: !!! I just double checked the documentation, and this doesn't actually do what I thought it did. Sorry. It reads everything, it doesn't look for nulls like I originally thought. You need to dump a string that is delimited with some character or something.
		String angleString = port.readString();
		String distanceString = port.readString();
		String angleString2 = port.readString();
		//TODO: Sanitize input
		/*if (stringRec.length() != 8)
		{
			angle = 9.11;
			distance = 420.0;
		}*/

			/*angle = Double.parseDouble(angleString);
			distance = Double.parseDouble(distanceString);
			return;
		
		
	}*/


}
