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

	public static Action inThresholdRelease;
	
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
	public static DrivePIDAction captureDrive;
	public static DrivePIDAction smallStartDrive;
	
	public static DrivePIDAction backSwitchToScale;
	public static DrivePIDAction returnCaptureDrive;
	public static DrivePIDAction secondScaleDrive;
	public static DrivePIDAction scaleToSwitchDrive;
	public static DrivePIDAction firstBackScaleDrive;
	public static SimpleDistanceDrive startScaleDrive;
	
	public static DrivePIDAction switchDrive;
	public static DrivePIDAction smallCaptureDrive;
	
	
	public static RotatePIDAction centerRotationR;
	public static RotatePIDAction centerRotationL;
	public static RotatePIDAction centerCaptureRotateR;
	public static RotatePIDAction centerCaptureRotateL;
	public static RotatePIDAction centerToSwitchRotate;
	
	public static RotatePIDAction scaleToSwitchRotateR;
	public static RotatePIDAction scaleToSwitchRotateL;
	
	public static RotatePIDAction rotateR90;
	public static RotatePIDAction rotateL90;
	public static RotatePIDAction shootRotateR1;
	public static RotatePIDAction shootRotateR2;
	public static RotatePIDAction shootRotateL1;
	public static RotatePIDAction shootRotateL2;
	
	public static ActionGroup fullDown;
	public static ActionGroup fullDownUse;
	public static ActionGroup backNScale;
	public static ActionGroup fullHide;
	public static ActionGroup fullScaleLift;
	public static ActionGroup switchShoot;
	public static ActionGroup rightSideSwitch;
	public static ActionGroup leftSideSwitch;

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
		
		hide = new LiftAction(Constants.HIDE_ANGLE,true);
		shoot = new LiftAction(Constants.SHOOT_ANGLE,true);
	}
	private static Action inThresholdAct(DrivePIDAction drive,Action act)
	{
		return new Action() {
			
			@Override
			protected void execute() {
			}
			
			@Override
			protected void end() {
				act.start();
			}
			
			@Override
			protected boolean isFinished() {
				return drive.inThreshold;
			}
		};
	}
	private static void captureSetup()
	{
		capture = new TimedAction(new CaptureAction(true) , Constants.CAPTURE_TIME);
		release = new TimedAction(new CaptureAction(false), Constants.RELEASE_TIME);
		 
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
		scaleLift = new PoleAction(Constants.POLE_SCALE*2,Constants.STALL_ANGLE);
		downLift = new PoleAction(Constants.DOWN_ANGLE,Constants.STALL_ANGLE);
		switchLift = new PoleAction(Constants.POLE_SWITCH, Constants.STALL_ANGLE);
	}
	
	private static void combinedSetup()
	{
		
		fullDownUse = new ActionGroup()
				.addParallel(open)
				.addSequential(downUse);
				
		fullHide = new ActionGroup()
				.addSequential(close)
				.addSequential(hide);
		
		fullScaleLift = new ActionGroup()
				.addParallel(hide)
				.addSequential(scaleLift)
				.addSequential(upUse);
		
		fullDown = new ActionGroup()
				.addParallel(fullHide)
				.addSequential(downLift);
				
		
		switchShoot = new ActionGroup()
				.addSequential(shoot)
				.addSequential(release);
		
		backNScale = new ActionGroup()
				.addParallel(fullScaleLift)
				.addSequential(startScaleDrive)
				.addParallel(inThresholdAct(secondScaleDrive, release))
				.addSequential(secondScaleDrive);
		
		//not good
		rightSideSwitch = new ActionGroup()
				.addSequential(capture)
				.addParallel(shoot)
				.addSequential(shootRotateR1)
				.addParallel(release)
				.addParallel(downUse)
				.addSequential(shootRotateL1)
				.addSequential(captureDrive)
				.addSequential(capture)
				.addParallel(shoot)
				.addSequential(shootRotateR2)
				.addSequential(release);
		
		leftSideSwitch = new ActionGroup()
				.addSequential(captureDrive)
				.addSequential(capture)
				.addParallel(shoot)
				.addParallel(release)
				.addParallel(downUse)
				.addSequential(shootRotateR1)
				.addSequential(captureDrive)
				.addSequential(capture)
				.addParallel(shoot)
				.addSequential(shootRotateL2)
				.addSequential(new Action() {
					
					@Override
					protected void execute() {
						Robot.rollerGripperSystem.rotate(0.5);
					}
					
					@Override
					protected void end() {
						Robot.rollerGripperSystem.stop();
					}
				});
	}
			
	private static void driveSetup()
	{
		backSwitchToScale = new DrivePIDAction(-Constants.SCALE_TO_SWITCH_DRIVE,0.3);
		scaleToSwitchDrive = new DrivePIDAction(Constants.SCALE_TO_SWITCH_DRIVE,0.3,10,true);
		smallStartDrive = new DrivePIDAction(Constants.SMALL_START_DRIVE);
		centerSwitchDrive = new DrivePIDAction(Constants.CENTER_SWITCH_DRIVE,200);
		captureDrive = new DrivePIDAction(Constants.SMALL_CAPTURE_DRIVE,0.2,10,true);
		returnCaptureDrive = new DrivePIDAction(-3.0, 0.5, 10,true);
		secondScaleDrive = new DrivePIDAction(-Constants.SCEOND_SCALE_DRIVE, 0.20,65,true);
		startScaleDrive = new SimpleDistanceDrive(-Constants.FIRST_SCALE_DRIVE,-0.65);
		
		switchDrive = new DrivePIDAction(Constants.SWITCH_DRIVE,0.6,70,true);
		smallCaptureDrive = new DrivePIDAction(Constants.SWITCH_CAPTURE_DRIVE,0.2,10, true);
	}
	
	private static void rotateSetup()
	{
		scaleToSwitchRotateR = new RotatePIDAction(Constants.SCALE_TO_SWITCH_ROTATE,0.35);
		scaleToSwitchRotateL = new RotatePIDAction(-Constants.SCALE_TO_SWITCH_ROTATE);
		centerRotationR = new RotatePIDAction(Constants.CENTER_ROTATE);
		centerRotationL = new RotatePIDAction(-Constants.CENTER_ROTATE);
		centerCaptureRotateR = new RotatePIDAction(Constants.CENTER_CAPTURE);
		centerCaptureRotateL = new RotatePIDAction(-Constants.CENTER_CAPTURE);
		
		rotateR90 = new RotatePIDAction(30.0);
		rotateL90 = new RotatePIDAction(-30.0);
		
		shootRotateL1 = new RotatePIDAction(Constants.SHOOT_ROTATE1);
		shootRotateL2 = new RotatePIDAction(Constants.SHOOT_ROTATE2);
		shootRotateR1 = new RotatePIDAction(-Constants.SHOOT_ROTATE1);
		shootRotateR2 = new RotatePIDAction(-Constants.SHOOT_ROTATE2);
	}
}
