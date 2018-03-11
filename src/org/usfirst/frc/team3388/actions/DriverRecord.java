package org.usfirst.frc.team3388.actions;

import org.usfirst.frc.team3388.robot.Frame;
import org.usfirst.frc.team3388.robot.Recorder;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.util.FlashUtil;

public class DriverRecord extends Action{

	Recorder rec;
	public DriverRecord(String path) {
		requires(Robot.drive);
		rec = new Recorder();
		rec.loadFile(Recorder.FILE_PATH+path+".csv");
	}
	Thread t;
	@Override
	protected void initialize() {
		Runnable writer = new Runnable() {	
			@Override
			public void run() {
				int curr =0;
				int startTime = FlashUtil.millisInt();
				System.out.println("start");
				while(curr < rec.frames.size())
				{
					Frame f = rec.frames.get(curr);
					Robot.drive.driveTrain.tankDrive(f.rightVal, f.leftVal);
					Robot.poleSystem.rotate(f.poleVal);
					Robot.liftSystem.rotate(f.liftVal);
					Robot.rollerGripperSystem.rotate(f.rotateVal);
					Robot.rollerGripperSystem.piston.use(f.pistonVal);
	
					curr++;
					try {
						Thread.sleep(Recorder.PERIOD - (startTime - FlashUtil.millisInt()));
						startTime = FlashUtil.millisInt();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		
		};
		t = new Thread(writer);
		t.start();
	}
	@Override
	protected void execute() {
		
	}
	@Override
	protected void end() {
		Robot.drive.driveTrain.stop();
		Robot.poleSystem.stop();
		
	}
	@Override
	protected boolean isFinished() {
		return !t.isAlive();
	}
}
