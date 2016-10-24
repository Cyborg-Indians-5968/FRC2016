package org.usfirst.frc.team5968.robot;

public final class PortMap {
	
	//ints for pwm ports
	public static final int leftMotor = 0;
	public static final int rightMotor = 1;
	public static final int feedMotor = 2;
	public static final int leftShootMotor = 3;
	public static final int rightShootMotor = 4;
	
	public static final int leftEncoderOne = 0;
	public static final int leftEncoderTwo = 1;
	public static final int rightEncoderOne = 2;
	public static final int rightEncoderTwo = 3;
	public static final int driveSwitch = 4;
	public static final int shootSwitch = 5;
	public static final int modeSwitch1 = 6;
	public static final int modeSwitch2 = 7;
	public static final int modeSwitch3 = 8;
	
	//pneumatics
	public static final int shootAngle = 0;
	public static final int shootAngle2 = 1;
	
	//Joy-sticks
	public static final int leftJoystick = 0;
	public static final int rightJoystick = 1;
	public static final int altJoystick = 2;
	
	
	
	//public static final Gyro gyro;
	
	public static final int countsPerRevolution = 512;
	public static final double distance = 22.22;
}
		
		/*leftMotor = new Victor(0); 
		rightMotor = new Victor(1); //PWM 1
		
		feedMotor = new Talon(2); //PWM 2
		leftShootMotor = new int(3); // PWM 3
		rightShootMotor = new int(4); // PWM 4
		
		port.reset();
		
		leftEncoder = new Encoder(0,1,false,EncodingType.k1X); //Digital IN 0 1
		rightEncoder = new Encoder(2,3,false,EncodingType.k1X); //Digital IN 2 3
		driveSwitch = new DigitalInput(4); //Digital IN 4
		shootSwitch = new DigitalInput(5); //Digital IN 5
		modeSwitch1 = new DigitalInput(6); //Digital IN 6
		modeSwitch2 = new DigitalInput(7); //Digital IN 7
		modeSwitch3 = new DigitalInput(8); //Digital IN 8
		
		
		leftJoystick = new Joystick(0); //USB 0
		rightJoystick = new Joystick(1); //USB 1
		altJoystick = new Joystick(2); // USB 2
		
		compressor = new Compressor(0); //PCM 0
		ballPusher = new Solenoid(1); //PCM 1
		shootAngle = new Solenoid(2); //PCM 2
		
		
		
		countsPerRevolution = 512;*/	
