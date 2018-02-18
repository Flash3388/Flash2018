package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;

public class AutoHandlers {
	
	public static Action switchChoose(boolean switchSide)
	{
		Action a = new ActionGroup()
				.addSequential(new DrivePIDAction(50.0))
				.addSequential(new RotatePIDAction(switchSide? 90.0 : -90.0))
				.addSequential(new DrivePIDAction(100.0))
				.addSequential(new RotatePIDAction(switchSide? -90.0 : 90.0))
				.addSequential(new DrivePIDAction(100.0));
		return a;
		
	}
	public static Action scaleChoose(boolean scaleSide)
	{
		
		Action a = new ActionGroup()
				.addSequential(ActionHandler.close)
				.addParallel(ActionHandler.scaleLift);
				/*
				.addSequential(ActionHandler.scaleDrive);
				.addSequential(ActionHandler.open)
				.addParallel(ActionHandler.downLift)
				.addSequential(ActionHandler.scaleToSwitchRotate)
				.addSequential(ActionHandler.scaleToSwitchDrive)
				.addSequential(ActionHandler.capture)
				.addSequential(ActionHandler.switchShoot);
				*/
		return a;
	}
	public static Action centerSwitch(boolean switchSide)
	{
		Action a = new ActionGroup()
				.addSequential(ActionHandler.close)
				.addParallel(ActionHandler.shoot)
				.addSequential(ActionHandler.smallStartDrive)
				.addSequential(switchSide ? ActionHandler.centerRotationR : ActionHandler.centerRotationL)
				.addSequential(ActionHandler.centerSwitchDrive)
				.addSequential(ActionHandler.release)
				.addParallel(ActionHandler.downUse)
				.addSequential(switchSide ? ActionHandler.centerCaptureRotateR : ActionHandler.centerCaptureRotateL)
				.addSequential(ActionHandler.captureDrive)
				.addParallel(ActionHandler.capture)
				.addSequential(ActionHandler.returnCaptureDrive)
				.addParallel(ActionHandler.shoot)
				.addParallel(ActionHandler.switchLift)
				.addSequential(switchSide ? ActionHandler.centerCaptureRotateL : ActionHandler.centerCaptureRotateR)
				.addSequential(ActionHandler.release);
				
		
		return a;
	}

}
