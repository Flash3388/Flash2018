package org.usfirst.frc.team3388.robot.subsystems;

import edu.flash3388.flashlib.robot.Subsystem;

public class LaunchSystem extends Subsystem{
	private PistonController piston1;
	private PistonController piston2;
	public LaunchSystem(int a1,int a2,int b1,int b2) {
		piston1 = new PistonController(a1, a2);
		piston2 = new PistonController(b1, b2);
	}
	public void use(boolean side) {
		piston1.use(side);
		piston2.use(side);
	}
	public void close() {
		use(false);
		
	}
	public void open() {
		use(true);
		
	}
	public boolean isClosed() {
		return piston1.isClosed();
	}
	public void block()
	{
		piston1.block();
		piston2.block();
	}
}
