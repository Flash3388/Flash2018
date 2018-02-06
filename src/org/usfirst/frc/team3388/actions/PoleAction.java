package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{

	private double setpoint;
	static final double ERROR = 10.0;
	public PoleAction(double setpoint) {
		requires(Robot.PoleSystem);
		this.setpoint=setpoint;
	}

	@Override
	protected void end() {
		Robot.PoleSystem.stop();
	}
	@Override
	protected void execute() {
		if(setpoint>Robot.PoleSystem.angle.get())
			Robot.PoleSystem.rotate(true);
		else
			Robot.PoleSystem.rotate(false);
	}

	@Override
	protected boolean isFinished() {

		return Mathf.constrained(Robot.PoleSystem.angle.get(), setpoint, setpoint + ERROR);
	}
}
