package org.usfirst.frc.team3388.actions;

import edu.flash3388.flashlib.robot.InstantAction;

public class PoleLiftingAction extends InstantAction{
	private double angle;
	private PoleAction poleAction;
	public PoleLiftingAction(double angle) {
		this.angle = angle;
		poleAction = new PoleAction();
	}
	@Override
	protected void execute() {
		poleAction.setSetpoint(angle);
		poleAction.start();
	}

}
