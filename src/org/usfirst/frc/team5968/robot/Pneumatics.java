package org.usfirst.frc.team5968.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

public class Pneumatics {
	private Compressor compressor;
	private DoubleSolenoid shootAngle;
	
	public Pneumatics() {
		compressor = new Compressor();
		shootAngle = new DoubleSolenoid(PortMap.shootAngle, PortMap.shootAngle2);
		compressor.setClosedLoopControl(true);
		
	}
	
	public void togglePlatformAngle()
	{
		if(shootAngle.get() == DoubleSolenoid.Value.kForward)
		{
			shootAngle.set(DoubleSolenoid.Value.kReverse);
		}
		else
		{
			shootAngle.set(DoubleSolenoid.Value.kForward);
		}
		System.out.println(shootAngle.get());
	}//end of platformAngle
}
