package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.ActionHandler;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.devices.IndexEncoder;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public final double DEFAULT_UP_SPEED=0.3;
	public final double DEFAULT_DOWN_SPEED=0.3;
	
	TalonSpeed controller;
	
	public 	boolean isUsed=false;
	
	private Encoder in;
	public DoubleSource angle;
	final double STALL=0.15;
	private boolean stall = false;
	public RollerLiftingSystem() {
		controller = new TalonSpeed(RobotMap.LIFT_CONTROLLER);
		
		in = new Encoder(RobotMap.LIFT_A, RobotMap.LIFT_B);
		angle = new DoubleSource() {
			
			@Override
			public double get() {
				return 0;
			}
		};		
	}
	@Override
	public void rotate(double speed) {
		controller.set(speed);
	}
	
	public void rotate(boolean dir)
	{
		if(dir)
			rotate(SmartDashboard.getNumber("lift speed", 0.2));
		else
			rotate(-SmartDashboard.getNumber("down speed", 0.2));
	}
	
	public void liftUp()
	{
		rotate(DEFAULT_UP_SPEED);
	}
	
	public void liftDown()
	{
		rotate(DEFAULT_DOWN_SPEED);
	}
	
	@Override
	public void stop() {
		rotate(0.0);
	}
	public void stall(boolean s)
	{
		stall = s;
	}
	public void setup()
	{
		SmartDashboard.putNumber("lift speed", 0.5);
		SmartDashboard.putNumber("down speed", 0.3);
		this.setDefaultAction(new SystemAction(new Action() {
			
			final double MARGIN = 0.2;
			final double HIDE=2.0;
			final double USE=3.0;
			@Override
			protected void execute() {
				double y = Robot.systemController.LeftStick.getY();
				if(Robot.poleSystem.angle.get()>HIDE && Robot.poleSystem.angle.get()<USE && !isUsed)
				{
					ActionHandler.hide.start();
					isUsed=true;
				}
				else if(y > MARGIN)
					rotate(true);
				else if(y < -MARGIN)
					rotate(false);
				else if (Robot.rollerGripperSystem.piston.isClosed() || Robot.poleSystem.isPressed.get())
						rotate(STALL);
				else
					stop();
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
	}
}
