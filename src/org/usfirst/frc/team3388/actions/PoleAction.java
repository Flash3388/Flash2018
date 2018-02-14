package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleAction extends Action{

	private double setpoint;
	static final double ERROR = 2.5;
	boolean dir = true;
	public PoleAction(double setpoint) {
		requires(Robot.poleSystem);
		this.setpoint=setpoint;
	}
	
	@Override
	protected void end() {
		Robot.poleSystem.stop();
	}
	@Override
	protected void execute() {
		if(setpoint>Robot.poleSystem.angle.get())
		{
			dir= true;
			Robot.poleSystem.rotate(true);
		}
		else
		{
			dir = false;
			Robot.poleSystem.rotate(false);
		}
	}

	@Override
	protected boolean isFinished() {

		return Mathf.constrained(Robot.poleSystem.angle.get(), setpoint, setpoint + ERROR)|| (Robot.poleSystem.isPressed.get() && !dir);
	}
}
