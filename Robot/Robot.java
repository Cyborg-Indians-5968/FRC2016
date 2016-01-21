
package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Robot extends IterativeRobot {

	TeleopDrive teleopDrive;
	
    public void robotInit() {
    	
    	teleopDrive = new TeleopDrive();
    	
    }
    
    public void autonomousInit() {
    	
    }

   
    public void autonomousPeriodic() {
    	
    }
    
    public void teleopInit(){
    	
    	teleopDrive.driveInit();
    	
    }
    
    public void teleopPeriodic() {
    	
    	teleopDrive.driveBase();
    	
    }
    
    
    public void testPeriodic() {
    
    }
    
}