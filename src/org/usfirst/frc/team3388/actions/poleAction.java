package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class poleAction extends Action{
	
	private double setPoint;
	
	public poleAction() {
		requires(Robot.rollerGripperPole);
	}

	@Override
	protected void end() {
		Robot.rollerGripperPole.stop();
	}

	@Override
	protected void execute() {
		Robot.rollerGripperPole.setPoint(setPoint);
		Robot.rollerGripperPole.rotate(Robot.rollerGripperPole.rotatePID.calculate());
	}
	public void setSetpoint(double setpoint) {
		this.setPoint = setPoint;
	}
}
