package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;

public class CaptureAction extends Action{

	double startTime=0;
	double timeCaptured=0;
	static final double CAPTURING_TIME = 1.9;
	final double CAPTURING_DISTANCE = 10;
	public CaptureAction() {
		requires(Robot.rollerGripper);
	}
	@Override
	protected void end() {
		Robot.rollerGripper.stop();
	}

	@Override
	protected void execute() {
		Robot.rollerGripper.rotate();
		if(Robot.rollerGripper.getDist()<=CAPTURING_DISTANCE)
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
		return timeCaptured >= CAPTURING_TIME;
	}

}
