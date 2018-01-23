package org.usfirst.frc.team3388.actions;

import java.sql.Time;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripper;

import edu.flash3388.flashlib.flashboard.Flashboard;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;

public class Capture extends Action{

	double startTime=0;
	double timeCaptured=0;
	static final double CAPTURING_TIME = 1.9;
	public Capture() {
		requires(Robot.rollerGripper);
	}
	@Override
	protected void end() {
		Robot.rollerGripper.rotate(0);
	}

	@Override
	protected void execute() {
		Robot.rollerGripper.rotate(Robot.rollerGripper.DEFAULT_SPEED);
		if(Robot.rollerGripper.isCaptured())
		{
			if(startTime == 0)
				startTime=FlashUtil.secs();
			timeCaptured = FlashUtil.secs() - startTime;
		}
		else
		{
			startTime=0;
		}
	}
	
	@Override
	protected boolean isFinished() {
		return timeCaptured == CAPTURING_TIME;
	}

}
