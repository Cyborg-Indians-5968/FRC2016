package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.interfaces.Gyro;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Drive {
	
	private long nanotimeOld;
	
	private SpeedController leftMotor;
	private SpeedController rightMotor;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private double leftEncoderOld;
	private double rightEncoderOld;
	
	private static final double diameter = 7.65;
	
	private Gyro gyro;
	
	public Drive(){

		leftMotor = new Victor(PortMap.leftMotor);
		rightMotor = new Victor(PortMap.rightMotor);
		
		leftEncoder = new Encoder(PortMap.leftEncoderOne, PortMap.leftEncoderTwo, false, EncodingType.k1X);
		rightEncoder = new Encoder(PortMap.rightEncoderOne, PortMap.rightEncoderTwo, false, EncodingType.k1X);
		leftEncoder.setDistancePerPulse(.0469);
		rightEncoder.setDistancePerPulse(.0469);
		
		nanotimeOld = System.nanoTime();
		leftEncoderOld = leftEncoder.get();
		rightEncoderOld = rightEncoder.get();
		
		gyro = new ADXRS450_Gyro();
		
	}
    
	
	public void driveStraight(boolean forward)
	{
        double leftSpeed = 0.8;
        double rightSpeed = 0.8;
		
        double leftDistance = getLeftEncoder();
        double rightDistance = getRightEncoder();
        double distanceDiff = Math.abs(leftDistance - rightDistance);
        
        final double distanceThreshold = .1;
        
        if(distanceDiff > distanceThreshold)
        {
        	if(Math.abs(leftDistance) > Math.abs(rightDistance))
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
	}
	
	
	
	public void setRaw(double leftSpeed, double rightSpeed){
		leftMotor.set(leftSpeed);
		rightMotor.set(-rightSpeed);
	}
	public void humanDrive(double leftSpeed, double rightSpeed)
	{
		if(Math.abs(leftSpeed) <= .03)
		{
			//leftSpeed = 0.0;
		}
		
		if(Math.abs(rightSpeed) <= .03)
		{
			//rightSpeed = 0.0;
		}
		
		setRaw(leftSpeed, rightSpeed);
	}
	
	public double getRightEncoder() {
		return rightEncoder.getDistance();
	}
	public double getLeftEncoder() {
		return -leftEncoder.getDistance();
	}
	public double getDistance(){
		return Math.abs((getRightEncoder() + getLeftEncoder()) / 2.0);
	}
	public void resetDistance(){
		leftEncoder.reset();
		rightEncoder.reset();
	}
	public double getGyro() {
		return gyro.getAngle();
	}
}
