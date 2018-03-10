package org.usfirst.frc.team3388.robot.subsystems;

import java.io.IOException;

import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.AutoHandlers;
import org.usfirst.frc.team3388.robot.DashNames;
import org.usfirst.frc.team3388.robot.Frame;
import org.usfirst.frc.team3388.robot.Recorder;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.flashboard.Flashboard;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;

import edu.flash3388.flashlib.robot.devices.MultiSpeedController;
import edu.flash3388.flashlib.robot.systems.FlashDrive;
import edu.flash3388.flashlib.robot.systems.FlashDrive.MotorSide;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveSystem extends Subsystem {
	
	public static final double DRIVE_LIMIT = 0.5;
	public static final double ROTATE_LIMIT = 0.5;
	public static final double WHEEL_RADIUS=10.16;
	public Encoder encoder;
	//public Encoder leftEncoder;

	public FlashDrive driveTrain;
	public PIDController distancePID;
	public PIDController rotatePID;
	public PIDSource distanceSource;
	public PIDSource rotationSource;
	
	public static final double STRAIGHT_DRIVE_KP = 0.3;
	public static final String DIS_NAME= "distanceSetPoint";
	public static final String ROT_NAME= "rotationSetPoint";
	public DoubleProperty distanceSetPoint = PropertyHandler.putNumber(DIS_NAME,0.0);
	public DoubleProperty rotationSetPoint = PropertyHandler.putNumber(ROT_NAME,90.0);
	
	public AnalogGyro gyro;
	
	Action straightDrive;
	public boolean inited = false;
	final boolean tuning = true;
	public DriveSystem() {
		final double PPR=360;
		gyro = new AnalogGyro(RobotMap.GYRO);
		
		encoder= new Encoder(RobotMap.RIGHT_ENCODER_A, RobotMap.RIGHT_ENCODER_B);
		encoder.setDistancePerPulse((2*Math.PI*WHEEL_RADIUS)/PPR);
		
		//leftEncoder= new Encoder(RobotMap.LEFT_ENCODER_A, RobotMap.LEFT_ENCODER_B);
		//leftEncoder.setDistancePerPulse((WHEEL_RADIUS*2.0*Math.PI)/PPR);
		
		driveTrain = setupDriveTrain();
		driveTrain.setInverted(MotorSide.Left, true);
		
		pidsHandler();
		
		if(tuning)
		{	
			//drivePIDTunner();
			//rotationPIDTunner();
		}
	}

	public void initGyro()
	{
		inited = true;
		gyro.initGyro();
		//gyro.calibrate();
	}
	public void calibGyro()
	{
		gyro.calibrate();
	}
	
	
	private void pidsHandler() {
		distanceSource = new PIDSource() {
			@Override
			public double pidGet() {
				return encoder.getDistance();
			}
		};
		
		rotationSource = new PIDSource() {
			@Override
			public double pidGet() {
				return gyro.getAngle();
			}
		};

		distancePID = new PIDController(0.21, 0.0, 0.285, 0.0, distanceSetPoint, distanceSource);
		distancePID.setOutputLimit(-DRIVE_LIMIT, DRIVE_LIMIT);
		rotatePID = new PIDController(0.308, 0.0, 0.628,0.0,rotationSetPoint,rotationSource);
		rotatePID.setOutputLimit(-ROTATE_LIMIT, ROTATE_LIMIT);
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
	
	public void resetGyro()
	{	
		gyro.reset();
	}
	public void resetEncoder()
	{
		encoder.reset();
	}
	public void drive(double speed)
	{	
		final double KP = 0.1;
		final double MARGIN = 1.0;
		//driveTrain.tankDrive(speed, speed);
		double angle = rotationSource.pidGet();
	///	if(DrivePIDAction.inThreshold)
		//	angle = 0;
		driveTrain.arcadeDrive(speed, -angle*KP);
	}
	public void rotate(double speed)
	{
		driveTrain.rotate(speed);
	}
	
	public void setup()
	{
		this.setDefaultAction(new SystemAction(new Action() {
			final double bound = 0.11;
			int time = 0;
			@Override
			protected void initialize() {
				time =FlashUtil.millisInt();
				
			}
			@Override
			protected void execute() {
				double leftVal = Robot.leftController.getY();
				double rightVal = Robot.rightController.getY();
				if(inRange(rightVal, bound))
					rightVal = 0.0;
				if(inRange(leftVal, bound))
					leftVal = 0.0;
				driveTrain.tankDrive(rightVal,leftVal);
				
			}
				
			@Override
			protected void end() {
				driveTrain.tankDrive(0,0);
			}
		}, this));
	}
	
	public static boolean inRange(double val,double bound)
	{
		return(val >= -bound && val <= bound);
	}
	/*
	private void drivePIDTunner()
	{
		Flashboard.putPIDTuner("distance", distancePID.kpProperty(), distancePID.kiProperty()
				, distancePID.kdProperty(), distancePID.kfProperty(), distanceSetPoint
				, distanceSource, 20, 1000);
	}
	private void rotationPIDTunner()
	{
		Flashboard.putPIDTuner("rotation", rotatePID.kpProperty(), rotatePID.kiProperty()
				, rotatePID.kdProperty(), rotatePID.kfProperty(), rotationSetPoint
				, rotationSource, 20, 1000);
	}
	*/
}
