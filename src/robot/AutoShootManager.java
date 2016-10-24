package robot;

import org.usfirst.frc.team5968.Robot.*;
import org.usfirst.frc.team5968.robot.HumanInterface.BallFeedStates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import com.kauailabs.navx.frc.AHRS;

public class AutoShootManager {
	
	private final double DISTANCE_TO_TRAVEL = 7;
	private final double ERR = .01;

	private int shootState = 0;
	private int waitTime = 0;
	
    private AHRS ahrs;
   
    private double initDegrees;
    private double finalDegrees;
    private double distanceToGoal;
    private double distanceToDrive;
    private double toGoalDegrees;
    private String angleDistance;

	private final double angle_MAX = 0;
	private final double angle_MIN = 0;
	private final double distance_MIN = 20;
	private final double distance_MAX = 27;
	private double adjDist;
	private Drive drive;
	private BallShoot shoot;
	private BallFeed feeder; //TODO: !!! This will always be null. Right now, the feeder is created by HumanInterface. It should be created in Robot instead and passed into both AutoShootManager and HumanInterface.
	
  
	public AutoShootManager(Drive drive, BallShoot shoot) {
		this.drive = drive;
		this.shoot = shoot;
		this.ahrs = new AHRS(SPI.Port.kMXP);
		
	}
	
	public boolean ballShootComputer(uART uART) 
	{
		// Get output of angle and distance and split
		angleDistance = uART.aimToShoot();
		initDegrees = (Double.parseDouble(angleDistance.substring(0, angleDistance.indexOf(' ')))) * (180 / Math.PI); // degrees
		
		distanceToGoal = (Double.parseDouble(angleDistance.substring(angleDistance.indexOf(' ') + 1))) * 25.4; // inches
		distanceToDrive = Math.sqrt(Math.pow(10, 2) + Math.pow(distanceToGoal, 2) - 2 * 10 * distanceToGoal * Math.cos(initDegrees)); // 10 = distance to goal, change later
		finalDegrees = Math.asin((10 * Math.sin(initDegrees)) / distanceToDrive);
		
		toGoalDegrees = 180 - Math.asin((distanceToGoal * Math.sin(initDegrees)) / distanceToDrive);
		
		ahrs.reset();
		while (ahrs.getAngle() != finalDegrees) {
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
		
		ahrs.reset();
		while (ahrs.getAngle() != toGoalDegrees) {
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