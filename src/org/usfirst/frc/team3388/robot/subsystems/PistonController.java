package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class PistonController  {
	private DoubleSolenoid controller;
	private boolean isClosed;
	
	public PistonController(int port1 , int port2)
	{
		controller= new DoubleSolenoid(RobotMap.PCM_DEV,port1,port2);
		controller.set(Value.kOff);
	}	
	public void use(boolean side) {
		if(side)
		{
			controller.set(Value.kForward );
			isClosed = true;
		}
		if(!side)
		{
			controller.set(Value.kReverse);
			isClosed = false;
		}
	}
	public void close() {
		use(true);
	}
	public void open() {
		use(false);
	}
	public boolean isClosed() {
		return isClosed;
	}
	public void block()
	{
		controller.set(Value.kOff);
		isClosed = !isClosed;
	}
	
}
