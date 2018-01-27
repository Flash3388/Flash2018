package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public final double DEFAULT_SPEED=0.3;
	
	FlashSpeedController rController;
	FlashSpeedController lController;
	
	public RollerLiftingSystem() {
		rController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_LIFT_CONTROLLER);
		lController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_R_LIFT_CONTROLLER);
	}
	@Override
	public void rotate(double speed) {
		rController.set(speed);
		lController.set(speed);

	}
	public void rotate()
	{
		rController.set(DEFAULT_SPEED);
		lController.set(DEFAULT_SPEED);

	}

	@Override
	public void stop() {
		rController.set(0);
		lController.set(0);

	}

}
