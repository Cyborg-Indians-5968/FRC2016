package org.usfirst.frc.team5968.robot;


public class AutoShootManager {
	private final double idealDistance = 80;
   
    private double initDegrees;
    private double finalDegrees;
    private double distanceToGoal;
    private double distanceToDrive;
    private double toGoalDegrees;

	private Drive drive;	
	private NavXMXP navx;
  
	private AimState aimState = AimState.IDLE;
	
	public AutoShootManager(Drive drive, BallShoot shoot, NavXMXP navX) {
		this.drive = drive;
		//navx = new NavXMXP();
		this.navx = navX;
	}
	
	private enum AimState {
		TURN_1,
		DRIVE,
		TURN_2,
		IDLE
	}
	
	public boolean ballShootComputer(String aimData) 
	{
		// Get output of angle and distance and split
		//Entry point
		if(aimState == AimState.IDLE){
			distanceToGoal = (Double.parseDouble(aimData.substring(0, aimData.indexOf(' ')))) / 25.4; // result in inches
			
			initDegrees = (Double.parseDouble(aimData.substring(aimData.indexOf(' ') + 1))) * (180 / Math.PI); // result in degrees
			distanceToDrive = Math.sqrt(Math.pow(idealDistance, 2) + Math.pow(distanceToGoal, 2) - 2 * idealDistance * distanceToGoal * Math.cos(initDegrees)); 
			finalDegrees = Math.asin((idealDistance * Math.sin(initDegrees)) / distanceToDrive);
			
			toGoalDegrees = -1 * Math.asin((distanceToGoal * Math.sin(initDegrees)) / distanceToDrive);
			
			navx.resetYaw();
			aimState = AimState.TURN_1;
		}
		
		else if(aimState == AimState.TURN_1){
			if(Math.abs(navx.getYaw()) >= Math.abs(finalDegrees)){
				drive.setRaw(0, 0);
				aimState = AimState.DRIVE;
				drive.resetDriveStraight();
			}
			else if (finalDegrees > 0) {
				drive.setRaw(.05, -.05);
			}
			else {
				drive.setRaw(-.05, .05);
			}
		}
		
		else if(aimState == AimState.DRIVE){
			if(Math.abs(drive.getDistance()) >= distanceToDrive){
				drive.setRaw(0, 0);
				aimState = AimState.TURN_2;
				navx.resetYaw();
			}
			else {
				drive.driveStraight(distanceToDrive > 0.0);
			}
		}
		
		else if(aimState == AimState.TURN_2){
			if(Math.abs(navx.getYaw()) >= Math.abs(toGoalDegrees)){
				drive.setRaw(0, 0);
				aimState = AimState.IDLE;
				return true;
			}
			else if(finalDegrees < 0){
				drive.setRaw(.05, -.05);
			}
			else{
				drive.setRaw(-.05, .05);
			}
		}
		
		return false;
	}//end of another method
	
	public boolean driveBack() {
		final double DRIVE_DISTANCE = 80;
	    
	    if (Math.abs(drive.getDistance()) < DRIVE_DISTANCE) {
	        drive.driveStraight(false);
	        return false;
	    }
	    
	    drive.setRaw(0.0, 0.0);
	    return true;
	}
}