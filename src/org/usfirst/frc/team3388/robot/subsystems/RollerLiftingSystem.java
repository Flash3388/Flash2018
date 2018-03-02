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

	public static final double DEFAULT_UP_SPEED=1.0;
	public static final double DEFAULT_DOWN_SPEED=0.5;
	
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
			rotate(0.8);
		else
			rotate(-0.5);
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
				
				if(!shouldStall())
					stallSpeed = 0.0;				
				if(y > MARGIN && !isOutOfFrame())
					rotate(upSpeed);
				else if(y < -MARGIN)
					rotate(downSpeed);
				else if(Robot.poleSystem.isUp.get() && Robot.liftSystem.angle.get()<=-520)
					rotate(0.65);
				else if(stall)
					rotate(stallSpeed);
				else
					stop();
				
				SmartDashboard.putNumber("enc lift", angle.get());
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
}
