package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;

public class PoleAction extends Action{

	boolean dir = true;
	double setpoint;
	double unsoleAngle;
	public PoleAction(double setpoint, double unsoleAngle) {
		requires(Robot.poleSystem);
		this.setpoint=setpoint;
		this.unsoleAngle = unsoleAngle;
	}
	@Override
	protected void initialize() {
		dir = setpoint > Robot.poleSystem.angle.get();
	}
	@Override
	protected void end() {
		Robot.liftSystem.stall(true);
		Robot.poleSystem.stop();
	}
	@Override
	protected void execute() {
		double curr = Robot.poleSystem.angle.get();
		if(setpoint>curr)
		{
			Robot.poleSystem.rotate(true);
			if(unsoleAngle <= curr)
			{
				//Robot.liftSystem.stall(false);
			}
		}
		else
		{	
			Robot.poleSystem.rotate(false);
			
			if(unsoleAngle == curr)
				Robot.liftSystem.stall(false);
		}
	}
	@Override
	protected boolean isFinished() {

		//System.out.println(dir && (Robot.poleSystem.angle.get() >=  setpoint));
		return (dir && Robot.poleSystem.angle.get() >= setpoint) || (!dir && Robot.poleSystem.isDown.get() ) 
						|| (dir && Robot.poleSystem.isUp.get());
	}
	@Override
	protected void interrupted() {
		end();
	}
}
