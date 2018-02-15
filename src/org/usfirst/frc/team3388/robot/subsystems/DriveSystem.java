package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.AutoHandlers;
import org.usfirst.frc.team3388.robot.DashNames;
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
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveSystem extends Subsystem {
	
	public static final double DRIVE_LIMIT = 0.5;
	public static final double ROTATE_LIMIT = 0.4;
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
		//navxSetup();
		encoder= new Encoder(RobotMap.RIGHT_ENCODER_A, RobotMap.RIGHT_ENCODER_B);
		encoder.setDistancePerPulse((2*Math.PI*WHEEL_RADIUS)/PPR);
		
		//leftEncoder= new Encoder(RobotMap.LEFT_ENCODER_A, RobotMap.LEFT_ENCODER_B);
		//leftEncoder.setDistancePerPulse((WHEEL_RADIUS*2.0*Math.PI)/PPR);
		
		driveTrain = setupDriveTrain();
		driveTrain.setInverted(MotorSide.Left, true);
		
		stightDriveHandle();
		pidsHandler();
		
		if(tuning)
		{	
			drivePIDTunner();
			rotationPIDTunner();
			if(distanceSetPoint == null)
				System.out.println("null");
			Robot.rightController.getButton(1).whileHeld(straightDrive);
			
			Robot.rightController.getButton(1).whenPressed(AutoHandlers.switchChoose(true));
		}
	}

	public void initGyro()
	{
		inited = true;
		gyro.initGyro();
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

	private void stightDriveHandle() {
		SmartDashboard.putNumber(DashNames.driveKp, 0.3);
		SmartDashboard.putNumber("speed straight",0.2);
		straightDrive = new SystemAction(new Action() {
			final double kp = 0.0;
			final double speed = 0.2;//later it will be pid calculate
			@Override
			protected void initialize() {
				super.initialize();
				resetGyro();
			}
			@Override
			protected void execute() {
				double val = -SmartDashboard.getNumber(DashNames.driveKp, 0.0);
				driveTrain.arcadeDrive(SmartDashboard.getNumber("speed straight",0.0), rotationSource.pidGet()*val);
			}
			
			@Override
			protected void end() {
				driveTrain.stop();
			}
		}, this);
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
	public void drive(double speed)
	{	
		driveTrain.tankDrive(speed, speed);	
	}
	public void rotate(double speed)
	{
		driveTrain.rotate(speed);
	}
	
	public void setup()
	{
		SmartDashboard.putNumber("rotate", 0.3);
		this.setDefaultAction(new SystemAction(new Action() {
			final double bound = 0.3;
			
			@Override
			protected void execute() {
				double leftVal = Robot.leftController.getY();
				double rightVal = Robot.rightController.getY();
				if(inRange(rightVal, bound))
					rightVal = 0.0;
				if(inRange(leftVal, bound))
					leftVal = 0.0;
				driveTrain.tankDrive(rightVal, leftVal);
			}
				
			@Override
			protected void end() {
				driveTrain.tankDrive(0,0);
			}
		}, this));
	}
	
	private boolean inRange(double val,double bound)
	{
		return(val >= -bound && val <= bound);
	}
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
}
