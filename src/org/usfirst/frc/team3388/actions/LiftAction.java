package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class LiftAction extends Action{

	double setpoint=0.0;
	public static final double DEFAULT_LIFT_SPEED=0.3;
	
	public LiftAction(double setpoint)
	{
		requires(Robot.rollerGripperLifter);
		this.setpoint=setpoint;
	}

	@Override
	protected void end() {
		Robot.rollerGripper.stop();
	}

	@Override
	protected void execute() {
		if(setpoint>Robot.rollerGripper.getAngle())
			Robot.rollerGripper.rotate();
		else if(setpoint<Robot.rollerGripper.getAngle())
			Robot.rollerGripper.rotate(-Robot.rollerGripper.DEFAULT_SPEED);
	}

	@Override
	protected boolean isFinished() {
		return Robot.rollerGripper.getAngle()>=setpoint;
	}

}
