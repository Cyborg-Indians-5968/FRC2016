
package org.usfirst.frc.team5968.robot;

//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	private uART uART;
	private Drive drive;
	private AutoShootManager autoShootManager;
	private HumanInterface humanInterface;
	private BallShoot shoot;
	
	private long start;   
	
	public Robot() {
		this.uART = new uART();
	}
    public void robotInit() {
    	drive = new Drive();
    	shoot = new BallShoot();
    	autoShootManager = new AutoShootManager(drive, shoot);
    	humanInterface = new HumanInterface(drive, shoot, autoShootManager);
    }
    private AutoState autoState;
    public void autonomousInit() {
    	autoState = AutoState.IDLE;
    }

    private enum AutoState {
    	IDLE, DRIVE_FORWARD1, DRIVE_BACKWARD, DRIVE_FORWARD2, DRIVE_BACKWARD2, DONE
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
    		autoState = AutoState.DRIVE_FORWARD1;
    		drive.resetDistance();
    	}
    	if(autoState == AutoState.DRIVE_FORWARD1 && drive.getDistance() > distanceToTravelInitial)
    	{
    		drive.resetDistance();
    		autoState = AutoState.DRIVE_BACKWARD;
    	}
    	if(autoState == AutoState.DRIVE_BACKWARD && drive.getDistance() > distanceToTravel)
    	{
    		drive.resetDistance();
    		autoState = AutoState.DONE;
    	}
    	if(autoState == AutoState.DRIVE_FORWARD2 && drive.getDistance() > distanceToTravel)
    	{
    		drive.resetDistance();
    		autoState = AutoState.DRIVE_BACKWARD2;
    	}
    	if(autoState == AutoState.DRIVE_BACKWARD2 && drive.getDistance() > distanceToTravel)
    	{
    		drive.resetDistance();
    		autoState = AutoState.DONE;
    	}
    	
    	if(autoState == AutoState.DRIVE_FORWARD1 || autoState == AutoState.DRIVE_FORWARD2)
    	{
    		drive.driveStraight(true);
    	}
    	else if(autoState == AutoState.DRIVE_BACKWARD || autoState == AutoState.DRIVE_BACKWARD2)
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
