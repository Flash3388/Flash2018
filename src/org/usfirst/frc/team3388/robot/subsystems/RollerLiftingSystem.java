package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public final double DEFAULT_SPEED=0.3;
	
	FlashSpeedController controller;
	
	public RollerLiftingSystem() {
		controller = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_LIFT_CONTROLLER);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);

	}
	public void rotate()
	{
		controller.set(DEFAULT_SPEED);
	}

	@Override
	public void stop() {
		controller.stop();

	}

}
