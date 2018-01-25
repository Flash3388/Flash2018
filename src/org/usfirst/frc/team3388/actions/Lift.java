package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class Lift extends Action{
	
	final double SCOPE=0.5;
	double desAngle=0;
	public static final double DEFAULT_LIFT_SPEED=0.3;
	
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
		Robot.rollerGripper.lift(DEFAULT_LIFT_SPEED);
	}
	@Override
	protected boolean isFinished() {
		return Robot.rollerGripper.getAngle()==desAngle+SCOPE || Robot.rollerGripper.getAngle()==desAngle-SCOPE;
	}
	public void setAngle(double desAngle)
	{
		this.desAngle=desAngle;
	}

}
