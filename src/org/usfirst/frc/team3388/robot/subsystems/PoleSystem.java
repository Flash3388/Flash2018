package org.usfirst.frc.team3388.robot.subsystems;

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
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleSystem extends Subsystem implements Rotatable{

	public static final double DEFAULT_SPEED=0.5;
	private TalonSpeed controller;
	
	public DoubleSource angle;
	private final double UP_SPEED=0.8;
	private final double DOWN_SPEED=-0.5;
	public AnalogInput in;
	
	public PoleSystem()
	{
		in = new AnalogInput(RobotMap.POLE_POTENTIOMETER);
		angle = new DoubleSource() {
			final double UNITS = 240.0/5.0;
			@Override
			public double get() {
				return in.getVoltage()*UNITS;
			}
		};
		controller= new TalonSpeed(RobotMap.POLE);
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
		this.setDefaultAction(new Action() {
			@Override
			protected void execute() {
				//rollerGripperPole.rotate(0.5*systemController.getY());
				if(Robot.systemController.getY() > 0.2)
					rotate(true);
				else if(Robot.systemController.getY()  < -0.2)
					rotate(false);
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
