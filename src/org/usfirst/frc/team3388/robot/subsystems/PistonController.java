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
		controller.set(side? Value.kForward : Value.kReverse);
	}
	public void close() {
		use(false);
		isClosed=true;
	}
	public void open() {
		use(true);
		isClosed=false;
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
