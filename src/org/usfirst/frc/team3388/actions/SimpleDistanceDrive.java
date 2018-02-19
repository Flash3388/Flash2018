package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;

public class SimpleDistanceDrive extends Action{
	double speed;
	double setpoint;
	public SimpleDistanceDrive(double setpoint,double speed){
		requires(Robot.drive);
		
		this.setpoint=setpoint;
		this.speed = speed;
	}
	
	@Override
	protected void end() {
		
	}
	@Override
	protected void initialize() {
		Robot.drive.resetEncoder();
	}
	
	@Override
	protected void execute() {
		Robot.drive.drive(-speed);
	}
	@Override
	protected boolean isFinished() {
		if(speed < 0)
			return Robot.drive.distanceSource.pidGet() <= setpoint;
		else
			return Robot.drive.distanceSource.pidGet() >= setpoint;

	}
}
