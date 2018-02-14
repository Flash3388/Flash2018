package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.Spark;

public class CaptureAction extends Action{

	double startTime=0;
	double timeCaptured=0;
	boolean side;
	static final double CAPTURING_TIME = 1.9;
	final double CAPTURING_DISTANCE = 10;
	public CaptureAction(boolean side) {
		requires(Robot.rollerGripperSystem);
		this.side=side;
	}
	@Override
	protected void end() {
		Robot.rollerGripperSystem.stop();
		if(side)
			Robot.rollerGripperSystem.piston.close();
		else
			Robot.rollerGripperSystem.piston.open();
	}

	@Override
	protected void execute() {
		if(side)
		{
			Robot.rollerGripperSystem.rotate(false);
			if(Robot.rollerGripperSystem.getDist()<=CAPTURING_DISTANCE)
			{
				if(startTime == 0)
					startTime=FlashUtil.secs();
				timeCaptured = FlashUtil.secs() - startTime;
			}
			else
				startTime=0;
		}
		else
			Robot.rollerGripperSystem.rotate(true);
	}

	@Override
	protected boolean isFinished() {
		return timeCaptured >= CAPTURING_TIME;
	}

}
