package org.usfirst.frc.team5968.robot;


public class AutoShootManager {
	
	private final double DISTANCE_TO_TRAVEL = 7;
	private final double ERR = .01;
   
    private double initDegrees;
    private double finalDegrees;
    private double distanceToGoal;
    private double distanceToDrive;
    private double toGoalDegrees;
    private String angleDistance;

	private Drive drive;	
	private NavXMXP ahrs;
  
	public AutoShootManager(Drive drive, BallShoot shoot) {
		this.drive = drive;
		ahrs = new NavXMXP();
	}
	
	public void ballShootComputer(uART uART) 
	{
		// Get output of angle and distance and split
		angleDistance = uART.aimToShoot();
		initDegrees = (Double.parseDouble(angleDistance.substring(0, angleDistance.indexOf(' ')))) * (180 / Math.PI); // degrees
		
		distanceToGoal = (Double.parseDouble(angleDistance.substring(angleDistance.indexOf(' ') + 1))) * 25.4; // inches
		distanceToDrive = Math.sqrt(Math.pow(10, 2) + Math.pow(distanceToGoal, 2) - 2 * 10 * distanceToGoal * Math.cos(initDegrees)); // 10 = distance to goal, change later
		finalDegrees = Math.asin((10 * Math.sin(initDegrees)) / distanceToDrive);
		
		toGoalDegrees = 180 - Math.asin((distanceToGoal * Math.sin(initDegrees)) / distanceToDrive);
	
		ahrs.resetYaw();
		while (ahrs.getYaw() <= finalDegrees) {
			if (finalDegrees > 0) {
				drive.setRaw(.1, -.1);
			}
			else if (finalDegrees < 0) {
				drive.setRaw(-.1, .1);
			}
		}
		drive.resetDistance();
		while(drive.getDistance() != distanceToDrive) {
			drive.setRaw(0.1, 0.1);
		}
		
		ahrs.resetYaw();
		while (ahrs.getYaw() != toGoalDegrees) {
			if (finalDegrees < 0) {
				drive.setRaw(.1, -.1);
			}
			else if (finalDegrees > 0) {
				drive.setRaw(-.1, .1);
			}
		}
	}//end of another method


	public void driveBack() {
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

		// should this be non-blocking?
		double dist_prev = drive.getDistance(), dist = 0, dir = 1;
		while(true) {
			dist += dir * (-dist_prev + (dist_prev = drive.getDistance()));
			if (dist <= DISTANCE_TO_TRAVEL - ERR) {
				dir = 1;
				drive.driveStraight(true);
			} else if (dist >= DISTANCE_TO_TRAVEL + ERR) {
				dir = -1;
				drive.driveStraight(false);
			} else {
				drive.setRaw(0, 0);
				break;
			}
			// stall?
		}
	}
}