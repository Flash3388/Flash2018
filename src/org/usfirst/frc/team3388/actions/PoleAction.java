package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleAction extends Action{

	private double setpoint;
	static final double ERROR = 0.2;
	boolean dir = true;
	double unsoleAngle;
	public PoleAction(double setpoint, double unsoleAngle) {
		requires(Robot.poleSystem);
		this.setpoint=setpoint;
		this.unsoleAngle = unsoleAngle;
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
			dir= true;
			Robot.poleSystem.rotate(true);
			
			if(unsoleAngle <= curr)
			{
				//Robot.liftSystem.stall(false);
			}
		}
		else
		{
			System.out.println("down");
			dir = false;
			Robot.poleSystem.rotate(false);
			
			if(unsoleAngle == curr)
				Robot.liftSystem.stall(false);
		}
		
		
		
	
	}

	@Override
	protected boolean isFinished() {

		System.out.println(Mathf.constrained(Robot.poleSystem.angle.get(), setpoint, setpoint + ERROR));
		return (dir && (Robot.poleSystem.angle.get() >=  setpoint)) || (Robot.poleSystem.isPressed.get() && !dir);
	}
	@Override
	protected void interrupted() {
		end();
	}
}
