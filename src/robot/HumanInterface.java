package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class HumanInterface {
	private Joystick leftStick;
	private Joystick rightStick;
	private Joystick altStick;
	
	private Drive drive;
	private BallFeed feed;
	private Pneumatics piston;
	private BallShoot shoot;
	private uART uart;
	private AutoShootManager autoShoot;
	private boolean driving = false;
	
	private boolean oldPistonButtonValue = false;
	private boolean oldAlignButtonValue = false;
	
	public HumanInterface(Drive drive, BallShoot shoot, AutoShootManager teleShoot){
		
		this.drive = drive;
		this.shoot = shoot;
		
		leftStick = new Joystick(PortMap.leftJoystick);
		rightStick = new Joystick(PortMap.rightJoystick);
		altStick = new Joystick(PortMap.altJoystick);
		feed = new BallFeed();
		piston = new Pneumatics();
		uart = new uART();
		autoShoot = new AutoShootManager(drive, shoot, uart);
	}
	
	public enum BallFeedStates {
		FAST, SLOW, STOPPED, BACKWARDS
	}
	
	public enum Buttons {
		TOGGLE_SHOOT_PLATFORM, 
		SHOOTER_ON_OFF, 
		SHOOTER_BACKWARD,
		FEED_SLOW, 
		FEED_FAST, 
		ALIGN_TO_SHOOT,
		FEED_BACKWARDS,
		AIM_RESULTS_OK,
		AIM_RESULTS_NOT_OK;
	}
	
	public boolean getButtonValue(Buttons button) {
		
		switch(button)
		{
			case TOGGLE_SHOOT_PLATFORM:
				return altStick.getRawButton(2); //B button
			case SHOOTER_ON_OFF:
				return altStick.getRawAxis(2) >= .5; //Left & Right Trigger
			case SHOOTER_BACKWARD:
				return altStick.getRawAxis(3) >= .5;
			case FEED_SLOW:
				return Math.abs(altStick.getRawAxis(0)) - Math.abs(altStick.getRawAxis(1)) >= .5; //Left or right on left joystick
			case FEED_FAST:
				return altStick.getRawAxis(1) - Math.abs(altStick.getRawAxis(0)) >= .5; //Up on left joystick
			case ALIGN_TO_SHOOT:
				return altStick.getRawButton(5) || altStick.getRawButton(6); //left and right bumpers
			case FEED_BACKWARDS:
				return altStick.getRawAxis(1) - Math.abs(altStick.getRawAxis(0)) <= -.5; //down on left joystick
			case AIM_RESULTS_OK:
				return altStick.getRawButton(8); //start button
			case AIM_RESULTS_NOT_OK:
				return altStick.getRawButton(7); //back button
			default:
				return false;
		}
		//You like my magic numbers?^^
	}
	
	public void buttonControls(){
        boolean pistonButton = getButtonValue(Buttons.TOGGLE_SHOOT_PLATFORM);
		if(pistonButton && !oldPistonButtonValue)
		{
			piston.togglePlatformAngle();
		}
        oldPistonButtonValue = pistonButton;
		
		if(getButtonValue(Buttons.SHOOTER_ON_OFF))
		{
			shoot.runForward();
		}
		else if(getButtonValue(Buttons.SHOOTER_BACKWARD)){
			shoot.runBackward();
		}
		else 
		{
			shoot.turnOff();
		}
        
		boolean alignButton = getButtonValue(Buttons.ALIGN_TO_SHOOT);
		if(alignButton && !oldAlignButtonValue)
		{
			oldAlignButtonValue = alignButton;
			boolean firstLoop = true;
			long startTime = System.currentTimeMillis();
			final double timeoutMs = 20 * 1000;
			boolean buttonWasReleased = false;
			
			String data;
			while(true) {
				alignButton = getButtonValue(Buttons.ALIGN_TO_SHOOT);
				
				if (!alignButton && oldAlignButtonValue)
				{
					if (buttonWasReleased)
					{
						DriverStation.reportWarning("Targeting aborted!", false);
						return;
					}
					else
					{
						buttonWasReleased = true;
					}
				}
				
				data = uart.getAimData(firstLoop);
				firstLoop = false;
				
				if(data != null){
					break;
				}
				
				if(System.currentTimeMillis() - startTime >= timeoutMs){
					DriverStation.reportError("Raspberry pi communication failed! You probably don't want to use it again :-(", false);
					return;
				}
				
				oldAlignButtonValue = alignButton;
			}
			
			DriverStation.reportWarning("Distance: " + data.split(" ")[0] + " Angle: " + data.split(" ")[1] + " Is this ok?"
					+ "Press start if yes, Back if no", false);
			while(true){
				if(getButtonValue(Buttons.AIM_RESULTS_OK)){
					break;
				}
				else if(getButtonValue(Buttons.AIM_RESULTS_NOT_OK)){
					DriverStation.reportWarning("Targeting aborted!", false);
					return;
				}
			}
			
			while(!autoShoot.ballShootComputer(data)){
				if(getButtonValue(Buttons.ALIGN_TO_SHOOT)){
					DriverStation.reportWarning("Targeting aborted!", false);
					return;
				}
			}
		}
		oldAlignButtonValue = alignButton;
		
		if(getButtonValue(Buttons.FEED_FAST))
		{
			feed.ballFeed(BallFeedStates.FAST);
			
		}
		else if(getButtonValue(Buttons.FEED_SLOW))
		{
			feed.ballFeed(BallFeedStates.SLOW);
			
		}
		else if(getButtonValue(Buttons.FEED_BACKWARDS)){
			feed.ballFeed(BallFeedStates.BACKWARDS);
			
		}
		else
		{
			feed.ballFeed(BallFeedStates.STOPPED);
		}
	}//end of method
	
	public void joystickControls() {
		drive.humanDrive(leftStick.getY() * .5, rightStick.getY() * .5);
	}//end of method
	public boolean getDriving() {
		return driving;
	}
	
	
}
