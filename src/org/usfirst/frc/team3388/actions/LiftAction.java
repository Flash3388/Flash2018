package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class LiftAction extends Action{

	double setpoint=0.0;
	static final double ERROR = 10.0;
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
		if(setpoint>Robot.rollerGripper.angle.get())
			Robot.rollerGripper.rotate();
		else if(setpoint<Robot.rollerGripper.angle.get())
			Robot.rollerGripper.rotate(Robot.rollerGripper.DEFAULT_SPEED,false);
	}

	@Override
	protected boolean isFinished() {
		return Mathf.constrained(Robot.rollerGripper.angle.get(), setpoint, setpoint + ERROR);
	}

}
