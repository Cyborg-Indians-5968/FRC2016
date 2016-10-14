package robot;

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
		double circumference = 7 * 2.54 * Math.PI * 10;
		double distancePerPulse = (Math.PI * circumference)/360.0;
		String angleDist = uART.aimToShoot();
		int indexOfSpace = angleDist.indexOf(" ");
		String angle = angleDist.substring(0,indexOfSpace);
		double dist = Double.parseDouble(angleDist.substring(indexOfSpace + 1)); // In Millimeters
		double angleDeg = (Double.parseDouble(angle) * 180) / Math.PI;
		
		if (angleDeg < 0) {
			while (angleDeg != Drive.getGyro()) {
				Drive.setRaw(-leftSpeed, rightSpeed);
			}
		} 
		else if (angleDeg > 0) {
			while (angleDeg != Drive.getGyro()) {
				Drive.setRaw(leftSpeed, -rightSpeed);
			}
		}
		
		double leftSpeed = 0.8;
        double rightSpeed = 0.8;
        
		leftEncoder.setDistancePerPulse(distancePerPulse);
		rightEncoder.setDistancePerPulse(distancePerPulse);
		double leftDist = Drive.leftEncoder.getDistance();
		double rightDist = Drive.rightEncoder.getDistance();
		
		double distanceDiff = Math.abs(leftDistance - rightDistance);
        
        final double distanceThreshold = .1;
        
        if(distanceDiff > distanceThreshold)
        {
        	if(Math.abs(leftDist) > Math.abs(rightDist))
        	{
        		rightSpeed -= .1;
        	}
        	else
        	{
        		leftSpeed -= .1;
        	}
        }
        if(!forward)
        {
        	leftSpeed *= -1.0;
        	rightSpeed *= -1.0;
        }
		setRaw(leftSpeed, rightSpeed);
		
		
		// uART.aimToShoot() returns "angle distance" (Radians millimeters)
		
	}//end of another method
}