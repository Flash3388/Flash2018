package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;

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
	public static Action rightScale(boolean rightScale, boolean rightSwitch,boolean rightSide)
	{
		ActionGroup a = new ActionGroup()
				.addParallel(ActionHandler.close);
				
		if(rightScale == rightSide)
		{
				a.addSequential(ActionHandler.backNScale)
				.addParallel(rightSide ? ActionHandler.scaleToSwitchRotateR : ActionHandler.scaleToSwitchRotateL)
				.addSequential(ActionHandler.fullDown)
				.addParallel(ActionHandler.fullDownUse)
				.addSequential(ActionHandler.scaleToSwitchDrive)
				.addParallel(ActionHandler.returnCaptureDrive)
				.addSequential(ActionHandler.capture);
				if(rightSwitch == rightSide)
				{
					a.addSequential(ActionHandler.shoot)
					.addSequential(ActionHandler.release);	
				}
		}
		else if(rightSwitch == rightSide)
		{
			a.addSequential(sideSwitch(rightSide));
		}
		else
		{
			a.addSequential(ActionHandler.switchDrive);
		}		
				
		return a;
	}
	
	public static Action sideSwitch(boolean right)
	{
		Action a = new ActionGroup()
				.addParallel(ActionHandler.shoot)
				.addSequential(ActionHandler.switchDrive)
				.addSequential(right ? ActionHandler.rotateR90 : ActionHandler.rotateL90)
				.addSequential(right ? ActionHandler.shootRotateL1 : ActionHandler.shootRotateR1)
				.addSequential(ActionHandler.release)
				.addParallel(ActionHandler.downUse)
				.addSequential(right ? ActionHandler.shootRotateR1 : ActionHandler.shootRotateL1)
				.addSequential(right ? ActionHandler.rightSideSwitch : ActionHandler.leftSideSwitch);
		return a;
					
	}
}
