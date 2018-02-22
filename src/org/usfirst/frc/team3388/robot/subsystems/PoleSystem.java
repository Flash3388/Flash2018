package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.robot.ActionHandler;
import org.usfirst.frc.team3388.robot.Constants;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import com.sun.java.swing.plaf.windows.WindowsBorders.DashedBorder;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.BooleanSource;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleSystem extends Subsystem implements Rotatable{

	public static final double ZERO=0.0;
	public static final double DEFAULT_SPEED=0.5;
	private VictorSP controller;
	private final double UP_SPEED=0.9;
	private final double DOWN_SPEED=-1.0;
	
	public AnalogInput in;
	public DoubleSource angle;

	private DigitalInput d;
	public BooleanSource isDown;
	
	private DigitalInput u;
	public BooleanSource isUp;
	
	double startAngle;
	public PoleSystem()
	{
		
		d = new  DigitalInput(RobotMap.POLE_DOWN_SWITCH);
		isDown = new BooleanSource() {
			
			@Override
			public boolean get() {
				return !d.get();
			}
		};
		u = new DigitalInput(RobotMap.POLE_UP_SWITCH);
		isUp = new BooleanSource() {
			
			@Override
			public boolean get() {
				return u.get();
			}
		};
		setupAngle();
		controller= new VictorSP(RobotMap.POLE);
	}
	private void setupAngle() {
		in = new AnalogInput(RobotMap.POLE_POTENTIOMETER);
		resetStartAngle();
		angle = new DoubleSource() {
			//final double START_ANGLE = 4.232177301;
			final double UNITS = 240.0/5;
			@Override
			public double get() {
				//return (in.getVoltage()*UNITS)-ZERO;
				return (startAngle - in.getVoltage());
			}
		};
	}
	public void resetStartAngle()
	{
		startAngle = in.getVoltage();
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	public void rotate(boolean dir)
	{
		if(dir)
			rotate(UP_SPEED);
		else
			rotate(DOWN_SPEED);
	}
	
	@Override
	public void stop() {
		controller.set(0);
	}
	
	public void setup()
	{
		Robot.systemController.RightStickButton.whenPressed(ActionHandler.fullDown);
	}

}
