package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JoyToVictor extends IterativeRobot {
    private Joystick joystick;
    private double joyY = 0;
    private double newJoyY;
    private VictorSP leftVictor = new VictorSP(2);
    private VictorSP rightVictor = new VictorSP(3);
	
    public void getJoystickY() {
    	newJoyY = joystick.getY();
		if (joyY != newJoyY) {
			System.out.println(newJoyY);
			joyY = newJoyY;
		}
		rightVictor.set(joyY);
		leftVictor.set(joyY * -1);
    }
}
