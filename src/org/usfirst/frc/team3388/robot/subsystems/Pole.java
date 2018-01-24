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
	
	private FlashSpeedController controller;
	public PIDController rotatePID;
	public PIDSource rotateSource;
	public static final String SETPOINT_NAME= "rotateSetPoint";
	public DoubleProperty PIDSetPoint = PropertyHandler.putNumber(SETPOINT_NAME, 0.0);
	public static AnalogPotentiometer potentiometer;

	public Pole()
	{
		potentiometer = new AnalogPotentiometer(RobotMap.POTENTIOMETER);
		//potentiometer.setPIDSourceType(pidSource);
		controller= new TalonSpeed(RobotMap.ROLLER_GRIPPER_POLE);
		rotateSource = new PIDSource() {
			
			@Override
			public double pidGet() {
				return potentiometer.get();
			}
		};
		rotatePID = new PIDController(0.0 , 0.0 , 0.0 , 0.0 , PIDSetPoint,rotateSource);
		rotatePID.setOutputLimit(-1, 1);
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	@Override
	public void stop() {
		controller.set(0);
	}
	public void setPoint(double point)
	{
		PIDSetPoint = PropertyHandler.putNumber(SETPOINT_NAME, point);
	}
}
