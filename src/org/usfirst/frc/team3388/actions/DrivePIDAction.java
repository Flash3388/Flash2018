package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.actions.PIDAction;
import edu.flash3388.flashlib.util.FlashUtil;

public class DrivePIDAction extends Action {

	public static boolean inThreshold=false;
	public final static double MARGIN=1.5;
	public final static int TIME_IN_THRESHOLD=350;
	double thresholdStartTime;
	double setpoint;
	double speedLimit;
	int timeInThreshold = TIME_IN_THRESHOLD;
	
	public DrivePIDAction(double setpoint,double speedLimit,int timeInThreshold) {
		requires(Robot.drive);
		this.setpoint=setpoint;
		Robot.drive.distanceSetPoint.set(setpoint);
		this.speedLimit = speedLimit;
		this.timeInThreshold = timeInThreshold;
	}

	public DrivePIDAction(double setpoint) {
		this(setpoint,DriveSystem.DRIVE_LIMIT,TIME_IN_THRESHOLD);
	}
	public DrivePIDAction(double setpoint ,double speedLimit) {
		this(setpoint,speedLimit,TIME_IN_THRESHOLD);
	}
	public DrivePIDAction(double setpoint ,int timeInThreshold) {
		this(setpoint,DriveSystem.DRIVE_LIMIT,timeInThreshold);
	}
	
	@Override
	protected void initialize() {
		inThreshold=false;
		thresholdStartTime=0;
		Robot.drive.resetEncoder();
		Robot.drive.resetGyro();
		Robot.drive.distanceSetPoint.set(setpoint + Robot.drive.distanceSource.pidGet());
		Robot.drive.distancePID.setEnabled(true);
		Robot.drive.distancePID.setOutputLimit(-speedLimit,speedLimit);
		Robot.drive.distancePID.reset();
	}
	
	@Override
	protected void end() {
		Robot.drive.driveTrain.tankDrive(0.0,0.0);
		Robot.drive.distancePID.setEnabled(false);
	}

	@Override
	protected void execute() {
		if(!Robot.drive.distancePID.isEnabled() || inDistanceThreshold())
		{
			if(thresholdStartTime < 1)
			{
				thresholdStartTime= FlashUtil.millisInt();
				inThreshold=true;
			}
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
		return inDistanceThreshold() && thresholdStartTime > 0 &&FlashUtil.millisInt() - thresholdStartTime >= timeInThreshold;
	}
	
	public boolean inDistanceThreshold() {
		double current = Robot.drive.distancePID.getPIDSource().pidGet();
		return Mathf.constrained(Robot.drive.distanceSetPoint.get() - current, -MARGIN, MARGIN);
	
	}
	@Override
	protected void interrupted() {
		end();
	}
}
