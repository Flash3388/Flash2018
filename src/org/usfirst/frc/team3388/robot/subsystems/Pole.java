package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Pole extends Subsystem implements Rotatable{
	/********************
	 * Pole SubSystem   *
	 *******************/
	public static final double DEFAULT_SPEED=0.5;
	private FlashSpeedController controller;
	private final int RANGE=360;
	private final int OFFSET=0;
	public static Potentiometer potentiometer;

	public Pole()
	{
		potentiometer = new AnalogPotentiometer(RobotMap.POLE_POTENTIOMETER,RANGE,OFFSET);
		//potentiometer.setPIDSourceType(pidSource);
		controller= new TalonSpeed(RobotMap.ROLLER_GRIPPER_POLE);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	public void rotate()
	{
		rotate(DEFAULT_SPEED);
	}
	@Override
	public void stop() {
		controller.set(0);
	}

}
