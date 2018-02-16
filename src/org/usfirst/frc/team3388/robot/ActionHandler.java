package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.LiftAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;

import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.TimedAction;

public class ActionHandler{

	public static TimedAction capture;
	public static TimedAction release;
	
	private static CaptureAction c;
	private static CaptureAction r;
	
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
	
	public static void setup()
	{
		if(Robot.sysTrain)
		{
			captureSetup();
			liftSetup();
			poleSetup();

			combinedSetup();
				
		}
		if(Robot.drivingTrain)
		{
			driveSetup();
			rotateSetup();
		
		}
	}
	private static void liftSetup()
	{
		use = new LiftAction(Constants.USE_ANGLE);
		hide = new LiftAction(Constants.HIDE_ANGLE);
		shoot = new LiftAction(Constants.SHOOT_ANGLE);
	}
	
	private static void captureSetup()
	{
		c = new CaptureAction(true);
		r = new CaptureAction(false);
		
		capture = new TimedAction(c, Constants.CAPTURE_TIME);
		release = new TimedAction(r, Constants.CAPTURE_TIME);
	}
	
	private static void poleSetup()
	{
		maxLift = new PoleAction(Constants.MAX_ANGLE);
		midLift = new PoleAction(Constants.MID_ANGLE);
		minLift = new PoleAction(Constants.MIN_ANGLE);
		downLift = new PoleAction(Constants.DOWN_ANGLE);
	}
	
	private static void combinedSetup()
	{
		
		max = new ActionGroup()
				.addParallel(hide)
				.addSequential(maxLift)
				.addSequential(use)
				.addSequential(release);
		
		mid = new ActionGroup()
				.addParallel(hide)
				.addSequential(midLift)
				.addSequential(use)
				.addSequential(release);
		
		min = new ActionGroup()
				.addParallel(hide)
				.addSequential(minLift)
				.addSequential(use)
				.addSequential(release);
		
		down = new ActionGroup()
				.addParallel(hide)
				.addSequential(downLift)
				.addSequential(use)
				.addSequential(capture);
		
		switchShoot = new ActionGroup()
				.addParallel(shoot)
				.addSequential(release);
	}
	
	private static void driveSetup()
	{
		scaleDrive = new DrivePIDAction(Constants.SCALE_DRIVE);
		scaleToSwitchDrive = new DrivePIDAction(Constants.SCALE_TO_SWITCH_DRIVE);
	}
	
	private static void rotateSetup()
	{
		scaleToSwitchRotate = new RotatePIDAction(Constants.SCALE_TO_SWITCH_ROTATE);
	}
}
