package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

public class Drive {
	private static CANTalon leftMotorFront;
	private static CANTalon rightMotorFront;
	private static CANTalon leftMotorBack;
	private static CANTalon rightMotorBack;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	public Drive(){

		leftMotorFront = new CANTalon(3);
		rightMotorFront = new CANTalon(5);
		leftMotorBack = new CANTalon(4);
		rightMotorBack = new CANTalon(6);
		/*
		leftMotorFront.changeControlMode(CANTalon.ControlMode.PercentVbus);
		rightMotorFront.changeControlMode(CANTalon.ControlMode.PercentVbus);
		leftMotorBack.changeControlMode(CANTalon.ControlMode.PercentVbus);
		rightMotorBack.changeControlMode(CANTalon.ControlMode.PercentVbus);
		*/
		
		leftEncoder = new Encoder(PortMap.leftEncoderOne, PortMap.leftEncoderTwo, false, EncodingType.k1X);
		rightEncoder = new Encoder(PortMap.rightEncoderOne, PortMap.rightEncoderTwo, false, EncodingType.k1X);
		
		leftEncoder.setDistancePerPulse(.0469);
		rightEncoder.setDistancePerPulse(.0469);
		
	}
    
	public static void testTemperature(CANTalon c) {
		if (c.getTemperature() >= 38.0) {
			System.out.println("TOO HOT, " + c.getDeviceID());
		}
	}
	
	public static void testCurrent(CANTalon c) {
		if (c.getOutputCurrent() >= 30.0) {
			System.out.println("TOO MUCH CURRENT, " + c.getDeviceID());
		}
	}
	
	final double INITIAL_SPEED = 0.5; 
    double leftSpeed = INITIAL_SPEED;
    double rightSpeed = INITIAL_SPEED;
    
    public void resetDriveStraight() {
    	leftSpeed = INITIAL_SPEED;
    	rightSpeed = INITIAL_SPEED;
    	resetDistance();
    }
	
	public void driveStraight(boolean forward)
	{
		final double SPEED_INC = 0.1; 
		final double MIN_SPEED = 0.3; 
		
        double leftDistance = getLeftEncoder();
        double rightDistance = getRightEncoder();
        //DriverStation.reportWarning(String.format("Encoders: %f, %f", leftDistance, rightDistance), false);
        double distanceDiff = Math.abs(leftDistance - rightDistance);
        
        final double distanceThreshold = .1;
        
        if(distanceDiff > distanceThreshold)
        {
        	if(Math.abs(leftDistance) < Math.abs(rightDistance))
        	{
        		//DriverStation.reportWarning("l < r", false);
        		if (leftSpeed < INITIAL_SPEED)
        		{ leftSpeed += SPEED_INC; }
        		else
        		{ rightSpeed -= SPEED_INC; }
        	}
        	else
        	{
        		//DriverStation.reportWarning("l > r", false);
        		if (rightSpeed < INITIAL_SPEED)
        		{ rightSpeed += SPEED_INC; }
        		else
        		{ leftSpeed -= SPEED_INC; }
        	}
        }
        
        // Cap left and right speeds
        leftSpeed = Math.max(MIN_SPEED, leftSpeed);
        rightSpeed = Math.max(MIN_SPEED, rightSpeed);
        
        //DriverStation.reportWarning(String.format("%f , %f", leftSpeed, rightSpeed), false);
        
        if(forward)
        {
        	setRaw(leftSpeed, rightSpeed);	
        }
        else
        {
        	setRaw(-leftSpeed, -rightSpeed);
        }
	}
	
	public void setRaw(double leftSpeed, double rightSpeed){
		setLeft(leftSpeed);
		setRight(-rightSpeed);
	}
	
	public static void setLeft(double speed) {
		leftMotorFront.set(speed);
		leftMotorBack.set(speed);
	}
	
	public static void setRight(double speed) {
		rightMotorFront.set(speed);
		rightMotorBack.set(speed);
	}
	
	public void humanDrive(double leftSpeed, double rightSpeed)
	{
		// Joysticks are reversed from what setRaw expects
		leftSpeed = -leftSpeed;
		rightSpeed = -rightSpeed;
		
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
}
