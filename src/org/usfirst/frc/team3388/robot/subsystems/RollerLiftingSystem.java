package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public final double DEFAULT_SPEED=0.3;
	public final double DEFAULT_UP_SPEED=0.3;
	public final double DEFAULT_DOWN_SPEED=0.3;
	
	FlashSpeedController controller;
	
	public RollerLiftingSystem() {
		controller = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_LIFT_CONTROLLER);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);

	}
	
	public void liftUp()
	{
		rotate(DEFAULT_UP_SPEED);
	}
	
	public void liftDown()
	{
		rotate(DEFAULT_DOWN_SPEED);
	}
	
	@Override
	public void stop() {
		controller.stop();
	}
	
	public void setup()
	{
		this.setDefaultAction(new Action() {
			final int UP =1;
			final int DOWN =-1;		
			@Override
			protected void execute() {
				int val =Robot.systemController.getPOV().get();
				if(val == UP)
					liftUp();
				else if(val == DOWN)
					liftDown();
				else
					stop();
			}
			
			@Override
			protected void end() {
				stop();
			}
		});
	}
}
