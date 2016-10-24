package robot;

import org.usfirst.frc.team5968.robot.HumanInterface.BallFeedStates;
import org.usfirst.frc.team5968.robot.PortMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class BallFeed {
	
	private SpeedController feedMotor;
	
	public BallFeed() {
		
		feedMotor = new Talon(PortMap.feedMotor);
		
	}
	
	
	public void ballFeed(BallFeedStates state){
		
		if(state == BallFeedStates.FAST)
		{			
			feedMotor.set(-1);
		}
		else if(state == BallFeedStates.SLOW)
		{	
			feedMotor.set(-0.4);	
		}
		else if(state == BallFeedStates.BACKWARDS){
			feedMotor.set(1);
		}
		else
		{
			feedMotor.set(0);
		}
		
		
	}//end of method

}//end of class
