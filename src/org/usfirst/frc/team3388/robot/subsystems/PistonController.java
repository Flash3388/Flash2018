package org.usfirst.frc.team3388.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PistonController {

	private DoubleSolenoid sol;
	
	public PistonController(int fChannel , int rChannel)
	{
		sol= new DoubleSolenoid(fChannel, rChannel);
	}
	public void use(boolean side)
	{
		if(side)
			sol.set(Value.kForward);
		else
			sol.set(Value.kReverse);
	}
	public void close()
	{
		use(true);
	}
	public void open()
	{
		use(false);
	}
	public void change()
	{
		if(sol.get()==Value.kForward)
			close();
		else
			open();
	}
}
