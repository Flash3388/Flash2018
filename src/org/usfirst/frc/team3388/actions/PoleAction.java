package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{
	/***************
	 * Pole Action *
	 **************/
	private double setPoint;
	public final double SCOPE=0.5;
	private double max;
	private double min;
	
	public PoleAction() {
		requires(Robot.rollerGripperPole);
	}

	@Override
	protected void end() {
		Robot.rollerGripperPole.stop();
	}
/*Function will set the point for PID and rotate it until isFinished returns true
 * input: None
 * output: None
 */
	@Override
	protected void execute() {
		Robot.rollerGripperPole.setPoint(setPoint);
		Robot.rollerGripperPole.rotate(Robot.rollerGripperPole.rotatePID.calculate());
	}
	/*Function will change the setpoint , which later be used as the PID set point
	 * input: setpoint
	 * output: None
	 */
	public void setSetpoint(double setPoint) {
		this.setPoint = setPoint;
		max = setPoint+SCOPE;
		min = setPoint-SCOPE;
	}
	/*Function will return true when the pole will reach the setpoint
	 * input: None
	 * output: isFinished
	 */
	@Override
	protected boolean isFinished() {
		return Robot.rollerGripperPole.potentiometer.get()<=max 
				&& Robot.rollerGripperPole.potentiometer.get()>=min;
	}
}
