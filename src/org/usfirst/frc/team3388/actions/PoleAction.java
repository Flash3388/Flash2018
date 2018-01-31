package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{

	private double setpoint;
	static final double ERROR = 10.0;
	public PoleAction(double setpoint) {
		requires(Robot.rollerGripperPole);
		this.setpoint=setpoint;
	}

	@Override
	protected void end() {
		Robot.rollerGripperPole.stop();
	}
	@Override
	protected void execute() {
		if(setpoint>Robot.rollerGripperPole.angle.get())
			Robot.rollerGripperPole.rotate(true);
		else
			Robot.rollerGripperPole.rotate(false);
	}

	@Override
	protected boolean isFinished() {

		return Mathf.constrained(Robot.rollerGripperPole.angle.get(), setpoint, setpoint + ERROR);
	}
}
