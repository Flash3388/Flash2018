package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.ActionHandler;
import org.usfirst.frc.team3388.robot.Constants;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public static final double DEFAULT_UP_SPEED=0.95;
	public static final double DEFAULT_DOWN_SPEED=0.8;
	
	TalonSpeed controller;
	
	
	private Encoder enc;
	public DoubleSource angle;
	final double STALL=0.11;
	private boolean stall = true;
	public RollerLiftingSystem() {
		controller = new TalonSpeed(RobotMap.LIFT_CONTROLLER);
		
		enc = new Encoder(RobotMap.LIFT_A, RobotMap.LIFT_B);
		angle = new DoubleSource() {
			
			@Override
			public double get() {
				return enc.get();
			}
		};		
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	
	public void rotate(boolean dir)
	{
		if(dir)
			rotate(DEFAULT_UP_SPEED);
		else
			rotate(-DEFAULT_DOWN_SPEED);
	}
	@Override
	public void stop() {
		rotate(0.0);
	}
	public void resetEncoder()
	{
		enc.reset();
	}
	public void stall(boolean s)
	{
		stall = s;
	}
	public void setup()
	{	
		this.setDefaultAction(new SystemAction(new Action() {
			
			final double MARGIN = 0.2;
			@Override
			protected void execute() {
				double y = Robot.systemController.LeftStick.getY();
				
				double upSpeed = DEFAULT_UP_SPEED;
				double downSpeed = -DEFAULT_DOWN_SPEED;
				double stallSpeed = STALL;
				if(Robot.poleSystem.angle.get() > Constants.POLE_FLIPPED_STALL || Robot.poleSystem.isUp.get())
				{
					upSpeed *= -1;
					downSpeed *= -1;
					stallSpeed *= -1;
				}
				
				double totalSpeed;
				if(!shouldStall())
					stallSpeed = 0.0;				

				if(y > MARGIN && !isOutOfFrame())
					totalSpeed = upSpeed;
				else if(y < -MARGIN)
				{
					if((Robot.poleSystem.isDown.get() || Robot.poleSystem.angle.get() <= 0.1) && angle.get()<=Constants.DOWN_USE_ANGLE)
						totalSpeed = stallSpeed-0.03;
					else
						totalSpeed = downSpeed;
				}
				else if(Robot.poleSystem.isUp.get() && angle.get() <= Constants.ABOUT_TO_FALL)
					totalSpeed = 0.65;
				else if(stall)
					totalSpeed = stallSpeed;
				else
					totalSpeed = 0.0;
				
				rotate(totalSpeed);
				
			}
			
			private boolean isOutOfFrame()
			{
				if(Robot.liftSystem.angle.get()<=-450 && Robot.poleSystem.isUp.get())
					return true;
				else
					return false;
			}

			private boolean shouldStall() {
				double rollerAngle = angle.get();
				if((Robot.poleSystem.angle.get() > Constants.STALL_ANGLE)&& rollerAngle > -35)
					return false;
				return true;
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
	}
	public double getCurrent()
	{
		return controller.get();
	}
}
