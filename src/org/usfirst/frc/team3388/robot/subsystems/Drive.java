package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.devices.Gyro;
import edu.flash3388.flashlib.robot.devices.MultiSpeedController;
import edu.flash3388.flashlib.robot.frc.FRCSpeedControllers;
import edu.flash3388.flashlib.robot.systems.FlashDrive;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class Drive {
	
	private ADXRS450_Gyro gyro;
	public FlashDrive driveTrain;
	public PIDController distancePID;
	public PIDSource distanceSource;
	public static final String SETPOINT_NAME= "distanceSetPoint";
	public DoubleProperty pidSetPoint = PropertyHandler.putNumber(SETPOINT_NAME, 135.0);
	
	public Drive() {
		
		driveTrain = setupDriveTrain();
		/*
		 * NEED A JOYSTICK SETUP
		 */
		
		/*
		 * NEED A PIDSouce set up
		 */
		distancePID = new PIDController(0.21, 0.0, 0.285, 0.0, pidSetPoint, distanceSource);
		distancePID.setOutputLimit(-1, 1);
		//gyro = new ADXRS450_Gyro();
	}
	private FlashDrive setupDriveTrain() {
		FlashSpeedController frontRight;
		FlashSpeedController frontLeft;
		FlashSpeedController backRight;
		FlashSpeedController backLeft;
		
		frontRight = new TalonSpeed(RobotMap.DRIVE_FRONTRIGHT);
		frontLeft = new TalonSpeed(RobotMap.DRIVE_FRONTLEFT);
		backRight = new TalonSpeed(RobotMap.DRIVE_BACKRIGHT);
		backLeft = new TalonSpeed(RobotMap.DRIVE_BACKLEFT);
		 
		
		return new FlashDrive(
					new MultiSpeedController(frontRight,backRight),
					new MultiSpeedController(frontLeft,backLeft));
	}
}
