package org.usfirst.frc.team5968.robot;

import edu.wpi.first.wpilibj.Joystick;

public class HumanInterface {
	private Joystick leftStick;
	private Joystick rightStick;
	private Joystick altStick;
	
	private Drive drive;
	private BallFeed feed;
	private Pneumatics piston;
	private BallShoot shoot;
	
	private boolean controlsReversed = false;
	private boolean altControlsEnabled = false;
	private boolean driving = false;
	
	private boolean oldPistonButtonValue = false;
	private boolean oldReverseButtonValue = false;
	private boolean oldAltControlButtonValue = false;
	private AutoShootManager teleShoot;
	
	public HumanInterface(Drive drive, BallShoot shoot, AutoShootManager teleShoot){
		
		this.drive = drive;
		this.shoot = shoot;
		this.teleShoot = teleShoot;
		
		leftStick = new Joystick(PortMap.leftJoystick);
		rightStick = new Joystick(PortMap.rightJoystick);
		altStick = new Joystick(PortMap.altJoystick);
		feed = new BallFeed();
		piston = new Pneumatics();
	}
	
	public enum BallFeedStates {
		FAST, SLOW, STOPPED, BACKWARDS
	}
	
	public enum Buttons {
		REVERSE_CONTROLS, ALTERNATE_CONTROLS, TOGGLE_MANUAL_SHOOT, TOGGLE_SHOOT_PLATFORM, SHOOT, FEED_SLOW, FEED_FAST, ALIGN_TO_SHOOT,FEED_BACKWARDS
	}
	
	public boolean getButtonValue(Buttons button) {
		
		switch(button)
		{
			case REVERSE_CONTROLS:
				return leftStick.getRawButton(7); //TODO: Consider adding a "|| rightStick.getRawButton(7)" to this so Kyle doesn't have to think about it.
				
			case ALTERNATE_CONTROLS:
				return altStick.getRawButton(8);
					
			case TOGGLE_MANUAL_SHOOT:
				return altStick.getRawButton(12);
				
			case TOGGLE_SHOOT_PLATFORM:
				return altStick.getRawButton(1);
				
			case SHOOT:
				return altStick.getRawButton(4);
				
			case FEED_SLOW:
				return altStick.getRawButton(10);
			
			case FEED_FAST:
				return altStick.getRawButton(11);
			case ALIGN_TO_SHOOT:
				return altStick.getRawButton(9);
			case FEED_BACKWARDS:
				return altStick.getRawButton(3);
			
			default:
				return false;
		}
		
	}
	
	public void buttonControls(){
		
		boolean reverseButton = getButtonValue(Buttons.REVERSE_CONTROLS);
		if(reverseButton && !oldReverseButtonValue)
		{
			controlsReversed = !controlsReversed;
		}
		oldReverseButtonValue = reverseButton;
		
		boolean altButton = getButtonValue(Buttons.ALTERNATE_CONTROLS);
		if (altButton && !oldAltControlButtonValue)
		{
			altControlsEnabled = !altControlsEnabled;
		}
		oldAltControlButtonValue = altButton;
		
        boolean pistonButton = getButtonValue(Buttons.TOGGLE_SHOOT_PLATFORM);
		if(pistonButton && !oldPistonButtonValue)
		{
			piston.togglePlatformAngle();
		}
        oldPistonButtonValue = pistonButton;
		
		if(getButtonValue(Buttons.SHOOT))
		{
			shoot.turnOnShooter();
		}
		else 
		{
			shoot.turnOffShooter();
		}
        
		if(getButtonValue(Buttons.ALIGN_TO_SHOOT) || driving)
		{
			//teleShoot.teleShootComputer();
			
		}
		
		if(getButtonValue(Buttons.FEED_FAST))
		{
			feed.ballFeed(BallFeedStates.FAST);
		}
		else if(getButtonValue(Buttons.FEED_SLOW))
		{
			feed.ballFeed(BallFeedStates.SLOW);
		}
		else
		{
			feed.ballFeed(BallFeedStates.STOPPED);
		}
		
		if(getButtonValue(Buttons.FEED_BACKWARDS)){
			feed.ballFeed(BallFeedStates.BACKWARDS);
		}
	}//end of method
	
	public void joystickControls() {
		if(altControlsEnabled)
		{
			
			if(altStick.getX() > 0)
			{
				drive.humanDrive(altStick.getY() - altStick.getX(), altStick.getY());
			}
			else if(altStick.getX() < 0)
			{
				drive.humanDrive(altStick.getY(), altStick.getY() - altStick.getX());
			}
			else
			{
				drive.humanDrive(altStick.getY(), altStick.getY());
			}
		}
		else
		{
			if(controlsReversed)
			{
				drive.humanDrive(Math.pow(-1.0 * leftStick.getY(), 1), Math.pow(-1.0 * rightStick.getY(), 1));
			}
			else
			{
                //TODO: It is not really appropriate for the human interface class to be aware that the right motor is backwards. This negation should've been donw somewhere in drive.
				drive.humanDrive(Math.pow(leftStick.getY(), 1), Math.pow(rightStick.getY(), 1));
			}
		}
	}//end of method
	
	public boolean getControlsReversed() {
		return controlsReversed;
	}
	public boolean getAltControlsEnabled() {
		return altControlsEnabled;
	}
	public boolean getDriving() {
		return driving;
	}
}
