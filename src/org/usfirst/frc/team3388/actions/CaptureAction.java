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
		System.out.println("end");
		Robot.rollerGripperSystem.stop();
		if(!side)
			Robot.rollerGripperSystem.piston.open();
	}

	@Override
	protected void initialize() {
		super.initialize();
		if(side)
			Robot.rollerGripperSystem.piston.close();
	}
	@Override
	protected void execute() {
		if(side)
			Robot.rollerGripperSystem.rotate(false);
		else
			Robot.rollerGripperSystem.rotate(true);
	}
	
	@Override
	protected void interrupted() {
		end();
	}

}
