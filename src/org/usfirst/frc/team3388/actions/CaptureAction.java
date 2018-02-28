package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Robot;


import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;

public class CaptureAction extends Action{

	double startTime=0;
	double timeCaptured=0;
	boolean side;
	static final double CAPTURING_TIME = 0.3;
	final double CAPTURING_DISTANCE = 10;
	Timer t;
	private double period;
	public CaptureAction(boolean side,double period) {
		requires(Robot.rollerGripperSystem);
		t = new Timer();
		this.period = period;
		this.side=side;
		this.setTimeout(700);
	}
	@Override
	protected void end() {
		Robot.rollerGripperSystem.stop();
		t.stop();
		
	}

	@Override
	protected boolean isFinished() {
		return period <= t.get();
	}
	@Override
	protected void initialize() {
		super.initialize();
		if(side)
			Robot.rollerGripperSystem.piston.close();
		t.reset();
		t.start();
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
