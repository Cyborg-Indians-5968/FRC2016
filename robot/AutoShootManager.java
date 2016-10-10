package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.Robot.*;
import org.usfirst.frc.team5968.robot.HumanInterface.BallFeedStates;

public class AutoShootManager {
	private final double angle_MAX = 0;
	private final double angle_MIN = 0;
	private final double distance_MIN = 20;
	private final double distance_MAX = 27;
	private double adjDist;
	private Drive drive;
	private BallShoot shoot;
	private BallFeed feeder; //TODO: !!! This will always be null. Right now, the feeder is created by HumanInterface. It should be created in Robot instead and passed into both AutoShootManager and HumanInterface.
	
	private int shootState = 0;
	private int waitTime = 0;
	
	public AutoShootManager(Drive drive, BallShoot shoot) {
		this.drive = drive;
		this.shoot = shoot;
	}
	
	public boolean ballShootComputer(double distance, double angle) 
	{
		
		if(shootState == 0){
			shoot.turnOnShooter();
			shootState = 1;
		}
		else if(shootState == 1){
			waitTime +=20;
			
			if(waitTime >= 260){
				shootState = 2;
				waitTime = 0;
			}
		}
		else if(shootState == 2){
			feeder.ballFeed(BallFeedStates.FAST);
			shootState = 3;
		}
		else if(shootState == 3)
		{
			waitTime += 20;
			
			if(waitTime >= 1000)
			{
				shoot.turnOffShooter();
				feeder.ballFeed(BallFeedStates.STOPPED);
				waitTime = 0;
				return true;
			}
		}
		return false;
	}//end of another method
}