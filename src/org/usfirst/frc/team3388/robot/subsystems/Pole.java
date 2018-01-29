package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class Pole extends Subsystem implements Rotatable{

	public static final double DEFAULT_SPEED=0.5;
	private FlashSpeedController controller;
	private final int RANGE=360;
	private final int OFFSET=0;
	private Potentiometer potentiometer;
	public static DoubleSource angle;
	public Pole()
	{
		potentiometer = new AnalogPotentiometer(RobotMap.POLE_POTENTIOMETER);
		angle = new DoubleSource() {
			@Override
			public double get() {
				return Mathf.scale(potentiometer.get(), 0.0, 240.0);
			}
		};
		//controller= new TalonSpeed(RobotMap.ROLLER_GRIPPER_POLE);
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
