package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.interfaces.Gyro;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class TalonDrive {
	
	private long nanotimeOld;
	
	private CANTalon leftMotorFront;
	private CANTalon rightMotorFront;
	private CANTalon leftMotorBack;
	private CANTalon rightMotorBack;
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private double leftEncoderOld;
	private double rightEncoderOld;
	
	private static final double diameter = 7.65;
	
	private Gyro gyro;
	
	public TalonDrive(){

		leftMotorFront = new CANTalon(0);
		rightMotorFront = new CANTalon(1);
		leftMotorBack = new CANTalon(2);
		rightMotorBack = new CANTalon(3);
		
		leftMotorFront.changeControlMode(CANTalon.ControlMode.Speed);
		rightMotorFront.changeControlMode(CANTalon.ControlMode.Speed);
		leftMotorBack.changeControlMode(CANTalon.ControlMode.Speed);
		rightMotorBack.changeControlMode(CANTalon.ControlMode.Speed);
		
		leftEncoder = new Encoder(0, 1, false, EncodingType.k1X);
		rightEncoder = new Encoder(2, 3, false, EncodingType.k1X);
		
		leftEncoder.setDistancePerPulse(.0469);
		rightEncoder.setDistancePerPulse(.0469);
		
		nanotimeOld = System.nanoTime();
		leftEncoderOld = leftEncoder.get();
		rightEncoderOld = rightEncoder.get();
		
		gyro = new ADXRS450_Gyro();
		
	}
    
	public static void testTemperature(CANTalon c) {
		if (c.getTemperature() >= 93.3) {
			System.out.println("TOO HOT, " + c.getDeviceID()); // Fix later
		}
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