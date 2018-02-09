package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.devices.Ultrasonic;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;


public class RollerGripperSystem extends Subsystem implements Rotatable{

	FlashSpeedController rController;
	FlashSpeedController lController;

	Ultrasonic sonic;
	ADXRS450_Gyro gyro;
	public static DoubleSource angle;
	public static final double DEFAULT_SPEED = 0.5;
	public TimedAction release;
	public RollerGripperSystem() {
		
		//sonic = new Ultrasonic(RobotMap.PING, RobotMap.ECHO);
		gyro = new ADXRS450_Gyro();//fill
		angle = new DoubleSource() {
			
			@Override
			public double get() {
				return gyro.getAngle();
			}
		};
		rController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_R_CAPTURE_CONTROLLER);
		lController.setInverted(true);
		lController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_CAPTURE_CONTROLLER);
	}
	public void rotate(double speed) {
		rController.set(speed);
		lController.set(speed);
	}
	public void rotate(boolean in) {
		if(in)
			rotate(DEFAULT_SPEED);
		else
			rotate(-DEFAULT_SPEED);
	}
	
	public void setup()
	{
		Robot.systemController.getButton(2).whileHeld(new SystemAction(new Action() {
			@Override
			protected void execute() {
				rotate(true);
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
		
		release = new TimedAction(new InstantAction() {	
			@Override
			protected void execute() {
				rotate(DEFAULT_SPEED);
			}
		}, 0.5);//roller setup end
		
	}
	
	
	
	

	public double getDist()
	{
		return sonic.get();
	}
	@Override
	public void stop() {
		rController.stop();
		lController.stop();
	}
	
	

}
