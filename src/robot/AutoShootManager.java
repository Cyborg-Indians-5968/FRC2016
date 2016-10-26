package org.usfirst.frc.team5968.robot;


public class AutoShootManager {
	
	private final double DISTANCE_TO_TRAVEL = 7;
	private final double ERR = .01;
	private final double idealDistance = 80;
   
    private double initDegrees;
    private double finalDegrees;
    private double distanceToGoal;
    private double distanceToDrive;
    private double toGoalDegrees;
    private String angleDistance;

	private Drive drive;	
	private NavXMXP ahrs;
	private uART uART;
  
	private AimState aimState = AimState.DONE;
	private AimState driveBackState = AimState.DONE;
	
	public AutoShootManager(Drive drive, BallShoot shoot, uART uART) {
		this.drive = drive;
		ahrs = new NavXMXP();
		this.uART = uART;
	}
	
	private enum AimState {
		TURN_1,
		DRIVE,
		TURN_2,
		DONE
	}
	
	public boolean ballShootComputer(String aimData) 
	{
		// Get output of angle and distance and split
		
		if(aimState == AimState.DONE){
			distanceToGoal = (Double.parseDouble(angleDistance.substring(0, angleDistance.indexOf(' ')))) * (180 / Math.PI); // degrees
			
			initDegrees = (Double.parseDouble(angleDistance.substring(angleDistance.indexOf(' ') + 1))) * 25.4; // inches
			distanceToDrive = Math.sqrt(Math.pow(idealDistance, 2) + Math.pow(distanceToGoal, 2) - 2 * idealDistance * distanceToGoal * Math.cos(initDegrees)); 
			finalDegrees = Math.asin((idealDistance * Math.sin(initDegrees)) / distanceToDrive);
			
			toGoalDegrees = -1 * Math.asin((distanceToGoal * Math.sin(initDegrees)) / distanceToDrive);
			
			ahrs.resetYaw();
			aimState = AimState.TURN_1;
		}
		
		else if(aimState == AimState.TURN_1){
			if(Math.abs(ahrs.getYaw()) >= Math.abs(finalDegrees)){
				drive.setRaw(0, 0);
				aimState = AimState.DRIVE;
				drive.resetDistance();
			}
			else if (finalDegrees > 0) {
				drive.setRaw(.1, -.1);
			}
			else {
				drive.setRaw(-.1, .1);
			}
		}
		
		else if(aimState == AimState.DRIVE){
			if(drive.getDistance() >= distanceToDrive){
				drive.setRaw(0, 0);
				aimState = AimState.TURN_2;
				ahrs.resetYaw();
			}
			else {
				drive.driveStraight(true);
			}
		}
		
		else if(aimState == AimState.TURN_2){
			if(Math.abs(ahrs.getYaw()) >= Math.abs(toGoalDegrees)){
				drive.setRaw(0, 0);
				aimState = AimState.DONE;
				return true;
			}
			else if(finalDegrees < 0){
				drive.setRaw(.1, -.1);
			}
			else{
				drive.setRaw(-.1, .1);
			}
		}
		
		return false;
	}//end of another method
	
	double dist_prev;
	double dist;
	double dir;
	public boolean driveBack() {
		/*if (auto_state == AutoState.IDLE) {
			dist = dir = 0;
			auto_state = AutoState.DRIVE_BACKWARD;
			dist_prev = drive.getDistance();
		} else if (auto_state == AutoState.DONE) return;

		dist += dir * (drive.getDistance() - dist_prev);
		if (dist <= DISTANCE_TO_TRAVEL - ERR) {
			dir = 1;
			drive.driveStraight(true);
		} else if (dist >= DISTANCE_TO_TRAVEL + ERR) {
			dir = -1;
			drive.driveStraight(false);
		} else {
			auto_state = AutoState.DONE;
			drive.setRaw(0, 0);
		}*/
		if(driveBackState == AimState.DONE){
			dist_prev = drive.getDistance(); 
			dist = 0;
			dir = 1;
			driveBackState = AimState.DRIVE;
		}
		else if(driveBackState == AimState.DRIVE){
			dist += dir * (-dist_prev + (dist_prev = drive.getDistance()));
			if (dist <= DISTANCE_TO_TRAVEL - ERR) {
				dir = 1;
				drive.driveStraight(true);
			} else if (dist >= DISTANCE_TO_TRAVEL + ERR) {
				dir = -1;
				drive.driveStraight(false);
			} else {
				drive.setRaw(0, 0);
				return true;
			}
		}
		return false;
	}
}