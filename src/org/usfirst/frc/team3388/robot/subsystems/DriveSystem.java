package org.usfirst.frc.team3388.robot.subsystems;

import java.math.RoundingMode;

import org.usfirst.frc.team3388.robot.DashNames;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;

import edu.flash3388.flashlib.math.Mathf;
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
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSystem extends Subsystem {
	
	public static final double WHEEL_RADIUS=10.16;
	public Encoder rightEncoder;
	public Encoder leftEncoder;

	public FlashDrive driveTrain;
	public PIDController distancePID;
	public PIDController rotatePID;
	public PIDSource distanceSource;
	public PIDSource rotationSource;
	
	public static final String DIS_NAME= "distanceSetPoint";
	public static final String ROT_NAME= "rotationSetPoint";
	public DoubleProperty distanceSetPoint = PropertyHandler.putNumber(DIS_NAME,0.0);
	public DoubleProperty rotationSetPoint = PropertyHandler.putNumber(ROT_NAME,0.0);
	
	public AHRS navx; 
	
	WPI_TalonSRX headController;
	
	Action straightDrive;
	public DriveSystem() {
		final double PPR=1440;
		
		navxSetup();
		rightEncoder= new Encoder(RobotMap.RIGHT_ENCODER_A, RobotMap.RIGHT_ENCODER_B);
		rightEncoder.setDistancePerPulse((WHEEL_RADIUS*2.0*Math.PI)/PPR);
		leftEncoder= new Encoder(RobotMap.LEFT_ENCODER_A, RobotMap.LEFT_ENCODER_B);
		leftEncoder.setDistancePerPulse((WHEEL_RADIUS*2.0*Math.PI)/PPR);
		
		driveTrain = setupDriveTrain();
		driveTrain.setInverted(MotorSide.Left, true);
		
		stightDriveHandle();
		/*distanceSource = new PIDSource() {
			
			@Override
			public double pidGet() {
				return encoder.getDistance();
			}
		};
*/
		
		distancePID = new PIDController(0.21, 0.0, 0.285, 0.0, distanceSetPoint, distanceSource);
		distancePID.setOutputLimit(-1, 1);
		rotatePID = new PIDController(0.21, 0.0, 0.285,0.0,rotationSetPoint,rotationSource);
		rotatePID.setOutputLimit(-1, 1);
	}

	private void stightDriveHandle() {
		straightDrive = new SystemAction(new Action() {
			final double kp = 0;
			final double speed = 0.2;//later it will be pid calculate
			@Override
			protected void initialize() {
			
				super.initialize();
				
				SmartDashboard.putNumber(DashNames.driveKp, 0.0);
				navx.reset();
			}
			@Override
			protected void execute() {
				double val = SmartDashboard.getNumber("straight drive kp", 0.0);
				driveTrain.arcadeDrive(speed, navx.getYaw()*val);
			}
			
			@Override
			protected void end() {
				driveTrain.stop();
			}
		}, this);
	}

	private void navxSetup() {
		navx=new AHRS(Port.kUSB, SerialDataType.kProcessedData, (byte)255);
		
		rotationSource = new PIDSource() {
			
			@Override
			public double pidGet() {
				return navx.getYaw();
			}
		};
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
	public void rotate(double speed)
	{
		driveTrain.rotate(speed);
	}
	
	public void setup()
	{
		SmartDashboard.putNumber("rotate",0.0);
		Robot.leftController.getButton(1).whileHeld(new Action() {
			
			@Override
			protected void execute() {
				driveTrain.rotate(SmartDashboard.getNumber("rotate",0.0));
			}
			
			@Override
			protected void end() {
				driveTrain.rotate(0.0);
			}
		});
		this.setDefaultAction(new SystemAction(new Action() {
			final double bound = 0.15;
			@Override
			protected void execute() {
				double leftVal = Robot.leftController.getY();
				double rightVal = Robot.rightController.getY();
				if(Mathf.constrained(leftVal, bound, -bound))
					leftVal = 0;
				if(Mathf.constrained(rightVal, bound, -bound))
					rightVal = 0;
				driveTrain.tankDrive(leftVal, rightVal);
			}
				
			@Override
			protected void end() {
				driveTrain.tankDrive(0,0);
			}
		}, driveTrain));
	}
	
	/*public double getPosition()
	{
		final double PERCITION = 4096.0;
		double val = headController.getActiveTrajectoryPosition()/PERCITION;
	
		return WHEEL_RADIUS*Math.PI*2.0*val;
	}
	*/
}
