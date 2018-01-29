package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{

	private double setpoint;
	
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
			Robot.rollerGripperPole.rotate();
		else
			Robot.rollerGripperPole.rotate(-Robot.rollerGripperPole.DEFAULT_SPEED);
	}

	@Override
	protected boolean isFinished() {

		return Robot.rollerGripperPole.angle.get()>=setpoint;
	}
}
