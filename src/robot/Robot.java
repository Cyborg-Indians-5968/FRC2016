
package org.usfirst.frc.team5968.robot;

//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	private uART uART;
	private Drive drive;
	private AutoShootManager autoShootManager;
	private HumanInterface humanInterface;
	private BallShoot shoot;
	private NavXMXP navX;
	
	private long start;   
	
    public void robotInit() {
    	drive = new Drive();
    	shoot = new BallShoot();
    	this.uART = new uART();
    	navX = new NavXMXP();
    	autoShootManager = new AutoShootManager(drive, shoot, navX);
    	humanInterface = new HumanInterface(drive, shoot, autoShootManager, navX);
    }
    private AutoState autoState;
    public void autonomousInit() {
    	autoState = AutoState.IDLE;
    }

    private enum AutoState {
    	IDLE, DRIVE_FORWARD, DRIVE_BACKWARD, DONE
    }
    
    public void autonomousPeriodic() {
    	
    	if(System.currentTimeMillis() - start <= 5000)
    	{
    		drive.setRaw(.5, .5);
    	}
    	else
    	{
    		drive.setRaw(0.0, 0.0);
    	}
    	final double distanceToTravelInitial = 121 + 12 + 12;
    	final double distanceToTravel = 44 + 12 + 30 + 12;
    		
    	if(autoState == AutoState.IDLE)
    	{
    		autoState = AutoState.DRIVE_FORWARD;
    		drive.resetDriveStraight();
    	}
    	if(autoState == AutoState.DRIVE_FORWARD && drive.getDistance() > distanceToTravelInitial)
    	{
    		drive.resetDriveStraight();
    		autoState = AutoState.DRIVE_BACKWARD;
    	}
    	if(autoState == AutoState.DRIVE_BACKWARD && drive.getDistance() > distanceToTravel)
    	{
    		drive.resetDriveStraight();
    		autoState = AutoState.DONE;
    	}
    	
    	if(autoState == AutoState.DRIVE_FORWARD)
    	{
    		drive.driveStraight(true);
    	}
    	else if(autoState == AutoState.DRIVE_BACKWARD)
    	{
    		drive.driveStraight(false);
    	}
    	else
    	{
    		drive.setRaw(0.0, 0.0);
    	}
    }
    
    public void teleopInit(){
    	
    }
    
    public void teleopPeriodic() {

    	humanInterface.buttonControls();
    	humanInterface.joystickControls();
    }
    
    public void testPeriodic() {
    
        //humanInterface.buttonControls();
    	//humanInterface.joystickControls();
        
    }
}
