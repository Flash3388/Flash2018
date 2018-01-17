package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.systems.Rotatable;

public class LiftSystem extends Subsystem implements Rotatable {

	TalonSpeed controller;
	public LiftSystem() {
		controller = new TalonSpeed(RobotMap.LIFT);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}

	@Override
	public void stop() {
		controller.stop();
	}

}
