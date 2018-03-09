package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripperSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;

public class AutoHandlers {

	public static Action centerSwitch(boolean switchSide)
	{
		Action a = new ActionGroup()
				.addParallel(ActionHandler.shoot)
				.addParallel(ActionHandler.close)
				.addSequential(ActionHandler.smallStartDrive)
				.addSequential(switchSide ? ActionHandler.centerRotationR : ActionHandler.centerRotationL)
				.addParallel(new SystemAction(new Action() {
					
					@Override
					protected void execute() {
					}
					
					@Override
					protected void end() {
						ActionHandler.release.start();
					}
					
					@Override
					protected boolean isFinished() {
						return ActionHandler.centerSwitchDrive.inThreshold;
					}
				}))
				.addSequential(ActionHandler.centerSwitchDrive);
				/*
				.addParallel(ActionHandler.downUse)
				.addSequential(switchSide ? ActionHandler.centerCaptureRotateR : ActionHandler.centerCaptureRotateL)
				.addSequential(ActionHandler.captureDrive)
				.addParallel(ActionHandler.capture)
				.addSequential(ActionHandler.returnCaptureDrive)
				.addParallel(ActionHandler.shoot)
				.addParallel(ActionHandler.switchLift)
				.addSequential(switchSide ? ActionHandler.centerCaptureRotateL : ActionHandler.centerCaptureRotateR)
				.addSequential(ActionHandler.release);
		*/
		return a;
	}
	public static Action rightScale(boolean rightScale, boolean rightSwitch,boolean rightSide,boolean scalePriority)
	{
		ActionGroup a = new ActionGroup()
				.addParallel(ActionHandler.close);
		
		if(rightScale == rightSide)
		{
			if(rightSwitch == rightSide)
			{
				if(scalePriority)
				{
					scaleSwitchPart(rightSide, a);
					a.addSequential(ActionHandler.release);	
				}
				else
					a.addSequential(sideSwitch(rightSide));
					
			}
			else if(scalePriority)
			{
				scaleSwitchPart(rightSide, a);
				a.addSequential(ActionHandler.release);	
			}
			else
			{
				a.addSequential(ActionHandler.afterSwitchDrive)
				.addSequential(rightSide ? ActionHandler.rotateL90 : ActionHandler.rotateR90)
				.addSequential(rightSide ? ActionHandler.rightOtherSwitch : ActionHandler.leftOtherSwitch);
			}
		}
		else if(rightSwitch == rightSide)
			a.addSequential(sideSwitch(rightSide));
		else
			a.addSequential(ActionHandler.switchDrive);
				
		return a;
	}
	private static void scaleSwitchPart(boolean rightSide, ActionGroup a) {
		a.addSequential(rightSide ?ActionHandler.backNScaleR :ActionHandler.backNScaleL)
		.addParallel(new DrivePIDAction(30.0, 0.2, 0, false))
		.addSequential(ActionHandler.fullDown)
		.addSequential(rightSide ? ActionHandler.scaleToSwitchRotateR : ActionHandler.scaleToSwitchRotateL)
		.addSequential(ActionHandler.fullDownUse)
		.addParallel(new SystemAction(new Action() {
			
			@Override
			protected void execute() {
				Robot.rollerGripperSystem.rotate(false);
			}
			
			@Override
			protected void end() {	
			}
			
		},Robot.rollerGripperSystem))
		.addSequential(ActionHandler.scaleToSwitchDrive)
		.addParallel(ActionHandler.returnCaptureDrive)
		.addParallel(ActionHandler.capture)
		.addSequential(ActionHandler.shoot);
	}
	
	public static Action sideSwitch(boolean right)
	{
		Action a = new ActionGroup()
				.addSequential(ActionHandler.switchDrive)
				.addSequential(right ? ActionHandler.rotateR90 : ActionHandler.rotateL90)
				.addSequential(new DrivePIDAction(-5.0,0.3))
				.addSequential(ActionHandler.shoot)
				.addSequential(new DrivePIDAction(7.0,0.2,10,false))
				.addSequential(ActionHandler.release)
				.addSequential(ActionHandler.open);
		return a;
					
	}
}
