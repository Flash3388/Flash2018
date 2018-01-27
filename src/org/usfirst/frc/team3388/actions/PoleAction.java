package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{

	private double setpoint;
	public final double SCOPE=0.5;
	private final double MARGIN=0.5;
	
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
		Robot.rollerGripperPole.rotate();
	}

	@Override
	protected boolean isFinished() {
		return Robot.rollerGripperPole.potentiometer.get()>=setpoint+MARGIN;
	}
}
