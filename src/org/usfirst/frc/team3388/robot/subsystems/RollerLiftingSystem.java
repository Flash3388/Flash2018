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

	public static final double DEFAULT_UP_SPEED=0.6;
	public static final double DEFAULT_DOWN_SPEED=0.3;
	
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
			final double HIDE=2.0;
			final double USE=3.0;
			@Override
			protected void execute() {
				double y = Robot.systemController.LeftStick.getY();
				if(y > MARGIN)
					rotate(true);
				else if(y < -MARGIN)
					rotate(false);
				else if (stall)
				{	
					if(angle.get() > Constants.LIFT_FLIPPED_STALL && Robot.poleSystem.angle.get() > Constants.POLE_FLIPPED_STALL)
					{
						System.out.println("flipped");
						rotate(-STALL);
					}
					else
						rotate(STALL);
				}
				else
					stop();
				SmartDashboard.putNumber("enc lift", angle.get());
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
	}
}
