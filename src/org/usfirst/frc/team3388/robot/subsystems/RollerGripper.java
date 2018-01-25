package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.devices.FlashSpeedController;
import edu.flash3388.flashlib.robot.devices.Ultrasonic;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;


public class RollerGripper extends Subsystem implements Rotatable{
	FlashSpeedController rCaptureController;
	FlashSpeedController lCaptureController;
	FlashSpeedController rLiftController;
	FlashSpeedController lLiftController;

	Ultrasonic sonic;
	ADXRS450_Gyro gyro;
	public static final double DEFAULT_SPEED = 0.5;
	public RollerGripper() {
		sonic = new Ultrasonic(RobotMap.PING, RobotMap.ECHO);
		gyro = new ADXRS450_Gyro();//fill
		rCaptureController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_R_CAPTURE_CONTROLLER);
		lCaptureController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_CAPTURE_CONTROLLER);
		rLiftController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_L_LIFT_CONTROLLER);
		lLiftController = new TalonSpeed(RobotMap.ROLLER_GRIPPER_R_LIFT_CONTROLLER);
	}
	public void rotate(double speed) {
		rCaptureController.set(speed);
		lCaptureController.set(speed);
	}
	public void rotate() {
		rotate(DEFAULT_SPEED);
	}
	public void lift(double speed)
	{
		rLiftController.set(speed);
		lLiftController.set(-speed);
	}
	public void stopCapture() {
		rCaptureController.stop();
		lCaptureController.stop();
	}
	public void stopLift()
	{
		rLiftController.stop();
		lLiftController.stop();
	}
	public double getDist()
	{
		return sonic.get();
	}
	@Override
	public void stop() {
		rCaptureController.stop();
		lCaptureController.stop();
		rLiftController.stop();
		lLiftController.stop();
	}
	public double getAngle()
	{
		return gyro.getAngle();
	}

}
