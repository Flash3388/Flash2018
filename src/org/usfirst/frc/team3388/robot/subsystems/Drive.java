package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.frc.FRCSpeedControllers;
import edu.flash3388.flashlib.robot.systems.FlashDrive;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.SpeedController;

public class Drive {
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
		
	}
	private FlashDrive setupDriveTrain() {
		SpeedController frontRight;
		SpeedController frontLeft;
		SpeedController backRight;
		SpeedController backLeft;
		
		frontRight = new TalonSpeed(RobotMap.DRIVE_FRONTRIGHT);
		frontLeft = new TalonSpeed(RobotMap.DRIVE_FRONTLEFT);
		backRight = new TalonSpeed(RobotMap.DRIVE_BACKRIGHT);
		backLeft = new TalonSpeed(RobotMap.DRIVE_BACKLEFT);
		
		
	return new FlashDrive(
				new FRCSpeedControllers(frontRight,backRight),
				new FRCSpeedControllers(frontLeft,backLeft));
	}
}
