package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.LiftAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.actions.SimpleDistanceDrive;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ActionHandler{

	public static TimedAction capture;
	public static TimedAction release;
	
	public static InstantAction close;
	public static InstantAction open;
	
	public static LiftAction downUse;
	public static LiftAction hide;
	public static LiftAction upUse;
	public static LiftAction upUseFirst;
	public static LiftAction upUseSecond;
	public static LiftAction shoot;
	
	public static PoleAction scaleLift;
	public static PoleAction downLift;
	public static PoleAction switchLift;

	
	public static DrivePIDAction centerSwitchDrive;
	public static SimpleDistanceDrive startScaleDrive;
	public static DrivePIDAction captureDrive;
	public static DrivePIDAction returnCaptureDrive;
	public static DrivePIDAction smallStartDrive;
	public static DrivePIDAction secondScaleDrive;
	public static DrivePIDAction toSwitchDrive;
	public static DrivePIDAction firstBackScaleDrive;
	
	public static RotatePIDAction centerRotationR;
	public static RotatePIDAction centerRotationL;
	public static RotatePIDAction centerCaptureRotateR;
	public static RotatePIDAction centerCaptureRotateL;
	public static RotatePIDAction centerToSwitchRotate;
	public static RotatePIDAction scaleRotateR;
	public static RotatePIDAction scaleRotateL;
	
	public static ActionGroup fullDown;
	public static ActionGroup fullDownUse;
	public static ActionGroup backNScale;
	public static ActionGroup fullHide;
	public static ActionGroup fullScaleLift;
	public static ActionGroup switchShoot;

	public static void setup()
	{
		captureSetup();
		liftSetup();
		poleSetup();

		driveSetup();
		rotateSetup();
		combinedSetup();
	}
	private static void liftSetup()
	{
		downUse = new LiftAction(Constants.DOWN_USE_ANGLE,true);
		upUse = new LiftAction(Constants.UP_USE_ANGLE_FLIPPED,true,RollerLiftingSystem.DEFAULT_UP_SPEED, RollerLiftingSystem.DEFAULT_DOWN_SPEED);
		
		upUseFirst = new LiftAction(Constants.UP_USE_ANGLE/2.0,true,RollerLiftingSystem.DEFAULT_UP_SPEED*2.0,RollerLiftingSystem.DEFAULT_UP_SPEED);
		upUseSecond = new LiftAction(Constants.UP_USE_ANGLE,true,RollerLiftingSystem.DEFAULT_DOWN_SPEED,RollerLiftingSystem.DEFAULT_DOWN_SPEED);
		hide = new LiftAction(Constants.HIDE_ANGLE,true);
		shoot = new LiftAction(Constants.SHOOT_ANGLE,true);
	}
	
	private static void captureSetup()
	{
		capture = new TimedAction(new CaptureAction(true) , Constants.CAPTURE_TIME);
		release = new TimedAction(new CaptureAction(false), Constants.CAPTURE_TIME);
		
		open = new InstantAction() {
			@Override
			protected void execute() {
				Robot.rollerGripperSystem.piston.open();
			}
		};
		
		close = new InstantAction() {
			
			@Override
			protected void execute() {
				Robot.rollerGripperSystem.piston.close();
			}
		};
	}
	
	private static void poleSetup()
	{
		scaleLift = new PoleAction(Constants.POLE_SCALE,Constants.STALL_ANGLE);
		downLift = new PoleAction(Constants.DOWN_ANGLE,Constants.STALL_ANGLE);
		switchLift = new PoleAction(Constants.POLE_SWITCH, Constants.STALL_ANGLE);
	}
	
	private static void combinedSetup()
	{
		fullDown = new ActionGroup()
				.addParallel(fullHide)
				.addSequential(downLift)
				.addSequential(fullDownUse);
		
		fullDownUse = new ActionGroup()
				.addSequential(downUse)
				.addSequential(open);
		
		fullHide = new ActionGroup()
				.addSequential(close)
				.addSequential(hide);
		
		fullScaleLift = new ActionGroup()
				.addParallel(hide)
				.addSequential(scaleLift)
				.addSequential(upUse);
		
		switchShoot = new ActionGroup()
				.addSequential(shoot)
				.addSequential(release);
		
		backNScale = new ActionGroup()
				.addSequential(new DrivePIDAction(-20,0.2,10,false))
				.addSequential(startScaleDrive)
				.addParallel(secondScaleDrive)
				.addSequential(fullScaleLift)
				.addSequential(release);
	}
	
	private static void driveSetup()
	{

		toSwitchDrive = new DrivePIDAction(Constants.TO_SWITCH_DRIVE);
		smallStartDrive = new DrivePIDAction(Constants.SMALL_START_DRIVE);
		centerSwitchDrive = new DrivePIDAction(Constants.CENTER_SWITCH_DRIVE,200);
		captureDrive = new DrivePIDAction(Constants.SMALL_CAPTURE_DRIVE,0.2,10,true);
		returnCaptureDrive = new DrivePIDAction(-Constants.SMALL_CAPTURE_DRIVE/2, 0.3, 10,true);
		secondScaleDrive = new DrivePIDAction(-Constants.SCEOND_SCALE_DRIVE, 0.3);
		startScaleDrive = new SimpleDistanceDrive(-Constants.FIRST_SCALE_DRIVE,-0.5);
	}
	
	private static void rotateSetup()
	{
		scaleRotateR = new RotatePIDAction(Constants.SCALE_ROTATE);
		scaleRotateL = new RotatePIDAction(-Constants.SCALE_ROTATE);
		centerRotationR = new RotatePIDAction(Constants.CENTER_ROTATE);
		centerRotationL = new RotatePIDAction(-Constants.CENTER_ROTATE);
		centerCaptureRotateR = new RotatePIDAction(Constants.CENTER_CAPTURE);
		centerCaptureRotateL = new RotatePIDAction(-Constants.CENTER_CAPTURE);
	}
}
