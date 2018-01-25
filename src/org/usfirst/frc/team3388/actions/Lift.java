package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class Lift extends Action{
	/***************
	 * Lift Action *
	 **************/
	final double SCOPE=0.5;
	double desAngle=0;
	public static final double DEFAULT_LIFT_SPEED=0.3;
	
	public Lift()
	{
		requires(Robot.rollerGripperLifter);
	}

	@Override
	protected void end() {
		Robot.rollerGripper.stop();
	}
	/*Function will lift the roller griper until isFinished returns true
	 * input: None
	 * output: None
	 */
	@Override
	protected void execute() {
		Robot.rollerGripper.rotate();
	}
	/*Function will return true if the roller reached the wanted angle
	 * input: None
	 * output: isFinished
	 */
	@Override
	protected boolean isFinished() {
		return Robot.rollerGripper.getAngle()<=desAngle+SCOPE || Robot.rollerGripper.getAngle()>=desAngle-SCOPE;
	}
	public void setAngle(double desAngle)
	{
		this.desAngle=desAngle;
	}

}
