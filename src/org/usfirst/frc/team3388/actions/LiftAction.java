package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class LiftAction extends Action{

	double setpoint=0.0;
	static final double ERROR = 2.0;
	public static final double DEFAULT_LIFT_SPEED=0.3;
	
	public LiftAction(double setpoint)
	{
		requires(Robot.liftSystem);
		this.setpoint=setpoint;
	}

	@Override
	protected void end() {
		Robot.rollerGripperSystem.stop();
	}

	@Override
	protected void execute() {
		double angle =Robot.rollerGripperSystem.angle.get(); 
		if(setpoint>angle)
			Robot.liftSystem.liftUp();
		else if(setpoint<angle)
			Robot.liftSystem.liftDown();
	}

	@Override
	protected boolean isFinished() {
		return Mathf.constrained(Robot.rollerGripperSystem.angle.get(), setpoint, setpoint + ERROR);
	}

}
