package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.wpi.first.wpilibj.SpeedController;

public class RollerGripper extends Subsystem implements Rotatable{
	SpeedController controller;
	public static final double DEFAULT_SPEED = 0.5;
	public RollerGripper() {
		controller = new TalonSpeed(RobotMap.ROLLER_GRIPPER);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	public void rotate() {
		rotate(DEFAULT_SPEED);
	}
	@Override
	public void stop() {
		controller.stopMotor();
	}

}
