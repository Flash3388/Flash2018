package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.PIDController;
import edu.flash3388.flashlib.robot.PIDSource;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.devices.IndexEncoder;
import edu.flash3388.flashlib.robot.devices.MultiSpeedController;
import edu.flash3388.flashlib.robot.systems.FlashDrive;
import edu.flash3388.flashlib.robot.systems.FlashDrive.MotorSide;
import edu.flash3388.flashlib.util.beans.DoubleProperty;
import edu.flash3388.flashlib.util.beans.PropertyHandler;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class DriveSystem extends Subsystem {
	
	public static final double RADIUS=10.16;
	public IndexEncoder encoder;
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

	public DriveSystem() {
		
		navxSetup();
		//encoder = new IndexEncoder(RobotMap.DRIVE_ENCODER,RADIUS*2*Math.PI);
		driveTrain = setupDriveTrain();
		driveTrain.setInverted(MotorSide.Left, true);
		
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
		this.setDefaultAction(new SystemAction(new Action() {
			@Override
			protected void execute() {
				driveTrain.tankDrive(Robot.rightController.getY(),Robot.leftController.getY());
			}
				
			@Override
			protected void end() {
				driveTrain.tankDrive(0,0);
			}
		}, driveTrain));
	}

}
