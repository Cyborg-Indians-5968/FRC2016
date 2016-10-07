package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDash {
	
	private Drive drive;
	private HumanInterface humanInterface;
	
	public SmartDash (Drive drive, HumanInterface humanInterface){
		this.drive = drive;
		this.humanInterface = humanInterface;
	}
	
	public void writeToDash() {
		
		SmartDashboard.putNumber("Left Encoder",drive.getLeftEncoder());
		SmartDashboard.putNumber("Right Encoder",drive.getRightEncoder());
		SmartDashboard.putNumber("Gyro", drive.getGyro());
		
		SmartDashboard.putBoolean("Controls Reversed", humanInterface.getControlsReversed());
		SmartDashboard.putBoolean("Alternate Controls Enabled", humanInterface.getAltControlsEnabled());
		//SmartDashboard.putBoolean("Manual Shooting Enabled", humanInterface.getManualShootEnabled());
		SmartDashboard.putBoolean("Aligning to Shoot", humanInterface.getDriving());
		
	}

}
