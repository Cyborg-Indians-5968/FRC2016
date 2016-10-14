package org.usfirst.frc.team5968.robot;

import org.usfirst.frc.team5968.robot.HumanInterface.BallFeedStates;

public class AutoShootManager {
	private final double DISTANCE_TO_TRAVEL = 7;
	private final double ERR = .01;

	private int shootState = 0;
	private int waitTime = 0;

	private Drive drive;
	private BallShoot shoot;
	private BallFeed feeder; // TODO: !!! This will always be null. Right now,
								// the feeder is created by HumanInterface. It
								// should be created in Robot instead and passed
								// into both AutoShootManager and
								// HumanInterface.

	public AutoShootManager(Drive drive, BallShoot shoot) {
		this.drive = drive;
		this.shoot = shoot;
	}

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

	public boolean ballShootComputer(double distance, double angle) {

		if (shootState == 0) {
			shoot.turnOnShooter();
			shootState = 1;
		} else if (shootState == 1) {
			waitTime += 20;

			if (waitTime >= 260) {
				shootState = 2;
				waitTime = 0;
			}
		} else if (shootState == 2) {
			feeder.ballFeed(BallFeedStates.FAST);
			shootState = 3;
		} else if (shootState == 3) {
			waitTime += 20;

			if (waitTime >= 1000) {
				shoot.turnOffShooter();
				feeder.ballFeed(BallFeedStates.STOPPED);
				waitTime = 0;
				return true;
			}
		}
		return false;
	}// end of another method
}