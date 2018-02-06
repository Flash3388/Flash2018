package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.actions.PIDAction;
import edu.flash3388.flashlib.util.FlashUtil;

public class DrivePIDAction extends Action {

	public final static double MARGIN=2.5;
	public final static double TIME_IN_THRESHOLD=3.0;
	double thresholdStartTime
	,startTime;
	double setpoint;
	public DrivePIDAction(double setpoint) {
		requires(Robot.drive);
		this.setpoint=setpoint;
		Robot.drive.distanceSetPoint.set(setpoint);
	}
	@Override
	protected void initialize() {
		startTime=FlashUtil.secs();
	}
	@Override
	protected void end() {
		Robot.drive.driveTrain.tankDrive(0.0,0.0);
	}

	@Override
	protected void execute() {
		if(Robot.drive.distanceSource.pidGet()<=setpoint+MARGIN &&
				Robot.drive.distanceSource.pidGet()>setpoint-MARGIN)
		{
			if(thresholdStartTime!=0)
				thresholdStartTime=FlashUtil.secs();
			Robot.drive.drive(Robot.drive.distancePID.calculate());
		}
		else
		{
			Robot.drive.drive(Robot.drive.distancePID.calculate());
			thresholdStartTime=0;
		}
		
	}
	
	@Override
	protected boolean isFinished() {
		return FlashUtil.secs()-thresholdStartTime>=TIME_IN_THRESHOLD && thresholdStartTime!=0		;
	}
}
