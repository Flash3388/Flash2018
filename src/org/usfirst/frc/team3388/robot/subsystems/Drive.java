package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.devices.Gyro;
import edu.flash3388.flashlib.robot.devices.IndexEncoder;
import edu.flash3388.flashlib.robot.devices.MultiSpeedController;
import edu.flash3388.flashlib.robot.frc.FRCSpeedControllers;
import edu.flash3388.flashlib.robot.systems.FlashDrive;
import edu.flash3388.flashlib.robot.systems.FlashDrive.MotorSide;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class Drive extends Subsystem {
	
	public static final double RADIUS=10.16;
	public IndexEncoder encoder;
	public FlashDrive driveTrain;
	public PIDController distancePID;
	public PIDSource distanceSource;
	public static final String SETPOINT_NAME= "distanceSetPoint";
	public DoubleProperty pidSetPoint = PropertyHandler.putNumber(SETPOINT_NAME,0.0);
	
	public Drive() {

		encoder = new IndexEncoder(RobotMap.DRIVE_ENCODER);
		driveTrain = setupDriveTrain();
		driveTrain.setInverted(MotorSide.Right, true);
		
		distanceSource = new PIDSource() {
			
			@Override
			public double pidGet() {
				return encoder.get()*RADIUS;
			}
		};


		
		distancePID = new PIDController(0.21, 0.0, 0.285, 0.0, pidSetPoint, distanceSource);
		distancePID.setOutputLimit(-1, 1);
	}

	private FlashDrive setupDriveTrain() {
		TalonSpeed frontRight;
		TalonSpeed frontLeft;
		TalonSpeed backRight;
		TalonSpeed backLeft;
		
		frontRight = new TalonSpeed(RobotMap.DRIVE_FRONTRIGHT);
		frontLeft = new TalonSpeed(RobotMap.DRIVE_FRONTLEFT);
		backRight = new TalonSpeed(RobotMap.DRIVE_BACKRIGHT);
		backLeft = new TalonSpeed(RobotMap.DRIVE_BACKLEFT);
		
		return new FlashDrive(
					new MultiSpeedController(frontRight,backRight),
					new MultiSpeedController(frontLeft,backLeft));
	}
	public void drive(double speed)
	{
		driveTrain.tankDrive(speed,speed);
	}
	public void turn(double speed ,boolean right)
	{
		if(right)
			driveTrain.tankDrive(-speed, speed);
		else
			driveTrain.tankDrive(speed, -speed);
	}

}
