package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;

public class RotatePIDAction extends Action {
	public final static double MARGIN=1.5;
	public final static int TIME_IN_THRESHOLD=500;
	int thresholdStartTime;
	int start;
	double setpoint;
	public RotatePIDAction(double setpoint) {
		requires(Robot.drive);
		this.setpoint=setpoint;
	}
	@Override
	protected void initialize() {
		thresholdStartTime=0;
		Robot.drive.rotationSetPoint.set(setpoint + Robot.drive.rotationSource.pidGet());
		Robot.drive.encoder.reset();
		Robot.drive.rotatePID.setEnabled(true);
		Robot.drive.rotatePID.reset();
	}
	@Override
	protected void end() {
		Robot.drive.driveTrain.tankDrive(0.0,0.0);
		Robot.drive.distancePID.setEnabled(false);
		System.out.println("end " + (FlashUtil.millisInt()- start));
	}

	@Override
	protected void execute() {
		if(!Robot.drive.rotatePID.isEnabled() || inDistanceThreshold())
		{
			if(thresholdStartTime < 1)
				thresholdStartTime=FlashUtil.millisInt();
			Robot.drive.rotate(-Robot.drive.rotatePID.calculate());	
		}
		else
		{
			if(thresholdStartTime >=1 )
				thresholdStartTime = 0;
			Robot.drive.rotate(-Robot.drive.rotatePID.calculate());		
		}
	}
	
	@Override
	protected boolean isFinished() {
		
		return inDistanceThreshold() && thresholdStartTime > 0 &&FlashUtil.millisInt() - thresholdStartTime >= TIME_IN_THRESHOLD;
	}
	
	public boolean inDistanceThreshold() {
		double current = Robot.drive.rotatePID.getPIDSource().pidGet();
		
		return Mathf.constrained(Robot.drive.rotationSetPoint.get() - current, -MARGIN, MARGIN);
	
	}
}