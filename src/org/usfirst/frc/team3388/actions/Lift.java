package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class Lift extends Action{
	
	public Lift()
	{
		requires(Robot.rollerGripper);
	}

	@Override
	protected void end() {
		Robot.rollerGripper.stopLift();
	}

	@Override
	protected void execute() {
		Robot.rollerGripper.lift(Robot.rollerGripper.DEFAULT_SPEED);
	}

}
