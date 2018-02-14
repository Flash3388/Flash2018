package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleAction extends Action{

	private double setpoint;
	static final double ERROR = 2.5;
	public PoleAction(double setpoint) {
		requires(Robot.poleSystem);
		this.setpoint=setpoint;
		SmartDashboard.putNumber("setpoint angle", 0.0);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		setpoint = SmartDashboard.getNumber("setpoint angle", 0.0);
	}

	@Override
	protected void end() {
		Robot.poleSystem.stop();
	}
	@Override
	protected void execute() {
		if(setpoint>Robot.poleSystem.angle.get())
			Robot.poleSystem.rotate(true);
		else
			Robot.poleSystem.rotate(false);
	}

	@Override
	protected boolean isFinished() {

		return Mathf.constrained(Robot.poleSystem.angle.get(), setpoint, setpoint + ERROR);
	}
}
