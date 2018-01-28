package org.usfirst.frc.team3388.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PistonController {

	private DoubleSolenoid sol;
	
	public PistonController(int fChannel , int rChannel)
	{
		sol= new DoubleSolenoid(fChannel, rChannel);
	}
	private void use(boolean side)
	{
		if(side)
			sol.set(Value.kForward);
		else
			sol.set(Value.kReverse);
	}
	private void close()
	{
		use(true);
	}
	private void open()
	{
		use(false);
	}
	private void change()
	{
		if(sol.get()==Value.kForward)
			open();
		else
			close();
	}
}
