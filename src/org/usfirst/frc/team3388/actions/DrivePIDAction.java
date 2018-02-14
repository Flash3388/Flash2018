package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.actions.PIDAction;
import edu.flash3388.flashlib.util.FlashUtil;

public class DrivePIDAction extends Action {

	public final static double MARGIN=2.5;
	public final static int TIME_IN_THRESHOLD=500;
	double thresholdStartTime;
	double setpoint;
	public DrivePIDAction(double setpoint) {
		requires(Robot.drive);
		this.setpoint=setpoint;
		Robot.drive.distanceSetPoint.set(setpoint);
		
	}
	@Override
	protected void initialize() {
		thresholdStartTime=0;
		Robot.drive.encoder.reset();
		Robot.drive.distanceSetPoint.set(setpoint + Robot.drive.distanceSource.pidGet());
		Robot.drive.distancePID.setEnabled(true);
		Robot.drive.distancePID.reset();
	}
	@Override
	protected void end() {
		Robot.drive.driveTrain.tankDrive(0.0,0.0);
		Robot.drive.distancePID.setEnabled(false);
		System.out.println("end");
	}

	@Override
	protected void execute() {
		if(!Robot.drive.distancePID.isEnabled() || inDistanceThreshold())
		{
			if(thresholdStartTime < 1)
				thresholdStartTime= FlashUtil.millisInt();
			
			Robot.drive.drive(-Robot.drive.distancePID.calculate());	
			
		}
		else
		{
			if(thresholdStartTime >= 1)
				thresholdStartTime = 0;
			Robot.drive.drive(-Robot.drive.distancePID.calculate());		
		}
	}
	
	@Override
	protected boolean isFinished() {
		return inDistanceThreshold() && thresholdStartTime > 0 &&FlashUtil.millisInt() - thresholdStartTime >= TIME_IN_THRESHOLD;
	}
	
	public boolean inDistanceThreshold() {
		double current = Robot.drive.distancePID.getPIDSource().pidGet();
		return Mathf.constrained(Robot.drive.distanceSetPoint.get() - current, -MARGIN, MARGIN);
	
	}
}
