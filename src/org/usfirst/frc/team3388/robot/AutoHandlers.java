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
				.addParallel(ActionHandler.maxLift)
				.addSequential(ActionHandler.scaleDrive)
				.addSequential(ActionHandler.release)
				.addParallel(ActionHandler.downLift)
				.addSequential(ActionHandler.scaleToSwitchRotate)
				.addSequential(ActionHandler.scaleToSwitchDrive)
				.addSequential(ActionHandler.capture)
				.addSequential(ActionHandler.s)
				
		return a;
	}

}
