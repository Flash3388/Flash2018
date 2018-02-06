package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;

public class RotatePIDAction extends Action {
	public final static double MARGIN=2.5;
	public final static double TIME_IN_THRESHOLD=3.0;
	double thresholdStartTime
	,startTime;
	double setpoint;
	public RotatePIDAction(double setpoint) {
		requires(Robot.drive);
		this.setpoint=setpoint;
		Robot.drive.rotationSetPoint.set(setpoint);
	}
	@Override
	protected void initialize() {
		startTime=FlashUtil.secs();
	}
	@Override
	protected void end() {
		Robot.drive.driveTrain.rotate(0.0);
	}

	@Override
	protected void execute() {
		if(Robot.drive.rotationSource.pidGet()<=setpoint+MARGIN &&
				Robot.drive.rotationSource.pidGet()>setpoint-MARGIN)
		{
			if(thresholdStartTime!=0)
				thresholdStartTime=FlashUtil.secs();
			Robot.drive.rotate(Robot.drive.rotatePID.calculate());
		}
		else
		{
			Robot.drive.rotate(Robot.drive.rotatePID.calculate());
			thresholdStartTime=0;
		}
	}
	
	@Override
	protected boolean isFinished() {
		return FlashUtil.secs()-thresholdStartTime>=TIME_IN_THRESHOLD && thresholdStartTime!=0		;
	}
}
