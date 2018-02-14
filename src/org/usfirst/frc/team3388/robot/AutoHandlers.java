package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.RotatePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;

public class AutoHandlers {
	
	public AutoHandlers() {
		
	}
	
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
				.addParallel(new PoleAction(Constants.MAX_ANGLE))
				.addSequential(new DrivePIDAction(700.0))
				//.addSequential(new CaptureAction(false))
				.addParallel(new PoleAction(Constants.DOWN))
				.addSequential(new RotatePIDAction(170.0))
				.addSequential(new DrivePIDAction(500.0))
				//.addSequential(new CaptureAction(true))
				.addSequential(new PoleAction(Constants.SWITCH_ANGLE));
		return a;
	}

}
