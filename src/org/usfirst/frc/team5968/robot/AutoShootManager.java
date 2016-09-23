package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.HumanInterface.BallFeedStates;

public class AutoShootManager {
	private final double angle_MAX = 0;
	private final double angle_MIN = 0;
	private final double distance_MIN = 20;
	private final double distance_MAX = 27;
	private double adjDist;
	//private Uart uart;
	private Drive drive;
	private BallShoot shoot;
	private BallFeed feeder; //TODO: !!! This will always be null. Right now, the feeder is created by HumanInterface. It should be created in Robot instead and passed into both AutoShootManager and HumanInterface.
	
	private int shootState = 0;
	private int waitTime = 0;
	
	public AutoShootManager(Drive drive, BallShoot shoot) {
		this.drive = drive;
		this.shoot = shoot;
		
		//uart = new Uart();
	}
	
	public boolean ballShootComputer() 
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
	/*public boolean angleChecker()
	{
		if ((uart.angle < angle_MAX) && (uart.angle > angle_MIN))
		{
			System.out.println("Nice angle");
			return true;
		}
		else
		{
			//scooter.scootAngle();
			System.out.println("Angle function not available yet");
			return true;
		}
		
	}
	public void distanceChecker() {
		if ((uart.distance > distance_MIN) && (uart.distance < distance_MAX))
		{
			return; 
		}
		else
		{
			if (uart.distance < distance_MIN)
			{
				adjDist = Math.abs(uart.distance - distance_MIN);
				drive.scootUp(adjDist);
				return;		
			}
			else
			{
				drive.scootBack(adjDist);
				return;
			}
		}
	}//end of Distance Checker
	public void teleShootComputer() 
	{
		
		if(uart.checkEm())
		{
			uart.aquireTarget();;
			if (!angleChecker())
			{
				System.out.print("She won't make it captain! Try again!");
			}
			else
			{
				distanceChecker();
			}
			
	}		*/

}
