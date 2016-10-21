package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

public class BallShoot
{
	private SpeedController leftShootMotor;
	private SpeedController rightShootMotor;
	
	public BallShoot() 
	{
		leftShootMotor = new VictorSP(PortMap.leftShootMotor);
		rightShootMotor = new VictorSP(PortMap.rightShootMotor);
	}
	
	
	//TODO set the timing of the shooting
	public void turnOnShooter() {
	
		leftShootMotor.set(1.0);
		rightShootMotor.set(-1.0);
	}
	public void turnOffShooter() {
		leftShootMotor.set(0);
		rightShootMotor.set(0);		
	}	
}