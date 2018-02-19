package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;

public class AutoHandlers {

	public static Action centerSwitch(boolean switchSide)
	{
		Action a = new ActionGroup()
				.addParallel(ActionHandler.shoot)
				.addParallel(ActionHandler.close)
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
	public static Action sideScale(boolean scaleScale)
	{
		Action a = new ActionGroup()
				.addParallel(ActionHandler.close)
				.addSequential(ActionHandler.backNScale)
				.addParallel(ActionHandler.fullDown)
				.addSequential(ActionHandler.centerCaptureRotateR);
		
		return a;
	}

}
