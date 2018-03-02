package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class LiftAction extends Action{

	private double setpoint=0.0;
	boolean stall=false;
	static final double ERROR = 5.0;
	public static final double DEFAULT_LIFT_SPEED=0.5;
	private double downSpeed;
	private double upSpeed;
	public LiftAction(double setpoint, boolean stall,double downSpeed,double upSpeed)
	{
		requires(Robot.liftSystem);
		this.downSpeed = downSpeed;
		this.upSpeed = upSpeed;
		
		this.setpoint=setpoint;
		this.stall=stall;
	}
	public LiftAction(double setpoint, boolean stall)
	{
		this(setpoint,stall,RollerLiftingSystem.DEFAULT_DOWN_SPEED,RollerLiftingSystem.DEFAULT_UP_SPEED);
	}
	
	@Override
	protected void end() {
		Robot.liftSystem.rotate(upSpeed,true);
		Robot.liftSystem.stall(stall);
	}

	@Override
	protected void execute() {
		double angle = Robot.liftSystem.angle.get(); 
		if(setpoint>angle)
			Robot.liftSystem.rotate(upSpeed,true);
		else if(setpoint<angle)
			Robot.liftSystem.rotate(downSpeed,false);
	}
	@Override
	protected boolean isFinished() {
		return Mathf.constrained(Robot.liftSystem.angle.get(), setpoint, setpoint + ERROR);
	}
	@Override
	protected void interrupted() {
		end();
	}

}
