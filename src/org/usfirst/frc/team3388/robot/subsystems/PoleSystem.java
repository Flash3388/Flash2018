package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PoleSystem extends Subsystem implements Rotatable{

	public static final double DEFAULT_SPEED=0.5;
	private TalonSpeed controller;
	private final int RANGE=360;
	private final int OFFSET=0;
	private Potentiometer potentiometer;
	public static DoubleSource angle;
	private final double UP_SPEED=1.0;
	private final double DOWN_SPEED=-0.1;
	
	
	public PoleSystem()
	{
		potentiometer = new AnalogPotentiometer(RobotMap.POLE_POTENTIOMETER);
		angle = new DoubleSource() {
			@Override
			public double get() {
				System.out.println("potentiometer "+potentiometer.get());
				
				return potentiometer.get();
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
					rotate(SmartDashboard.getNumber("upspeed", 0.8));
				else if(Robot.systemController.getY()  < -0.2)
					rotate(-SmartDashboard.getNumber("downspeed", 0.5));
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
