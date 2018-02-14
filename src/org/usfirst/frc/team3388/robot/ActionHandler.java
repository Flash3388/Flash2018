package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.LiftAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;

import edu.flash3388.flashlib.robot.ActionGroup;

public class ActionHandler{

	public static CaptureAction capture;
	public static CaptureAction release;
	
	public static LiftAction use;
	public static LiftAction hide;
	public static LiftAction shoot;
	
	public static PoleAction maxLift;
	public static PoleAction midLift;
	public static PoleAction minLift;
	public static PoleAction downLift;
	
	public static ActionGroup max;
	public static ActionGroup mid;
	public static ActionGroup min;
	public static ActionGroup down;
	public static ActionGroup switchShoot;
	
	public static DrivePIDAction scaleDrive;
	public static DrivePIDAction scaleToSwitchDrive;
	
	public static RotatePIDAction scaleToSwitchRotate;
	
	public ActionHandler()
	{
		captureSetup();
		liftSetup();
		poleSetup();
		
		driveSetup();
		rotateSetup();
		
		combinedSetup();
	}
	
	private void liftSetup()
	{
		use = new LiftAction(Constants.USE_ANGLE);
		hide = new LiftAction(Constants.HIDE_ANGLE);
		shoot = new LiftAction(Constants.SHOOT_ANGLE);
	}
	
	private void captureSetup()
	{
		capture = new CaptureAction(true);
		release = new CaptureAction(false);
	}
	
	private void poleSetup()
	{
		maxLift = new PoleAction(Constants.MAX_ANGLE);
		midLift = new PoleAction(Constants.MID_ANGLE);
		minLift = new PoleAction(Constants.MIN_ANGLE);
		downLift = new PoleAction(Constants.DOWN_ANGLE);
	}
	
	private void combinedSetup()
	{
		max.addParallel(hide)
				.addSequential(maxLift)
				.addSequential(use)
				.addSequential(release);
		
		mid.addParallel(hide)
				.addSequential(midLift)
				.addSequential(use)
				.addSequential(release);
		
		min.addParallel(hide)
				.addSequential(minLift)
				.addSequential(use)
				.addSequential(release);
		
		down.addParallel(hide)
				.addSequential(downLift)
				.addSequential(use)
				.addSequential(capture);
		
		switchShoot.addParallel(shoot)
				.addSequential(downLift)
				.addSequential(release);
	}
	
	private void driveSetup()
	{
		scaleDrive = new DrivePIDAction(Constants.SCALE_DRIVE);
		scaleToSwitchDrive = new DrivePIDAction(Constants.SCALE_TO_SWITCH_DRIVE);
	}
	
	private void rotateSetup()
	{
		scaleToSwitchRotate = new RotatePIDAction(Constants.SCALE_TO_SWITCH_ROTATE);
	}
}
