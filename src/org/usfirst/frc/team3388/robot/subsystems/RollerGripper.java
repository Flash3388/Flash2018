package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;

public class RollerGripper extends Subsystem implements Rotatable{
	FlashSpeedController controller;
	DigitalInput rollerGripperSwitch;
	public static final double DEFAULT_SPEED = 0.5;
	public RollerGripper() {
		rollerGripperSwitch = new DigitalInput(RobotMap.ROLLER_GRIPPER_SWITCH);
		controller = new TalonSpeed(RobotMap.ROLLER_GRIPPER);
	}
	public void rotate(double speed) {
		controller.set(speed);
	}
	public void rotate() {
		rotate(DEFAULT_SPEED);
	}
	@Override
	public void stop() {
		controller.stop();
	}
	public boolean isCaptured()
	{
		return rollerGripperSwitch.get();
	}

}
