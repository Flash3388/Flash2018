package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.robot.ActionHandler;
import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;
import org.usfirst.frc.team3388.robot.TalonSpeed;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.flash3388.flashlib.util.beans.BooleanSource;
import edu.flash3388.flashlib.util.beans.DoubleSource;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class RollerGripperSystem extends Subsystem implements Rotatable{

	private VictorSP rController;
	private VictorSP lController;
	
	public PistonController piston;
	public static final double CAPTURE_SPEED = -1.0;
	public static final double RELEASE_SPEED = 1.0;
	public static final double SLOW_RELEASE_SPEED = RELEASE_SPEED/2;
	
	private DigitalInput in;
	public BooleanSource isPressed;
	
	public RollerGripperSystem() {
		
		piston = new PistonController(RobotMap.L_CHANNEL, RobotMap.R_CHANNEL);

		rController = new VictorSP(RobotMap.CAPTURE_R);
		
		lController = new VictorSP(RobotMap.CAPTURE_L);
		lController.setInverted(true);
		
	//	in = new DigitalInput(RobotMap.CAPTURE_SWITCH);
		
	}
	public void rotate(double speed) {
		rController.set(speed);
		lController.set(speed);
	}
	public void rotate(boolean in) {
		if(in)
			rotate(-CAPTURE_SPEED);
		else
			rotate(CAPTURE_SPEED);
	}
	
	public boolean isClosed()
	{
		return piston.isClosed();
	}
	
	public void setup()
	{	
		
		Robot.systemController.A.whenPressed(new InstantAction() {
			@Override
			protected void execute() {
				piston.change();
			}
		});
		
		this.setDefaultAction(new SystemAction(new Action() {
			
			@Override
			protected void execute() {
				double r =Robot.systemController.RT.get();
				double l = Robot.systemController.LT.get();
				
				if(r>=0.1)
					rotate(-r);
				else if(l>=0.1)
					rotate(SLOW_RELEASE_SPEED);
				else
					stop();
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
	}
	
	public void spin()
	{
		rController.set(SLOW_RELEASE_SPEED);
		lController.set(-SLOW_RELEASE_SPEED);
	}
	@Override
	public void stop() {
		rController.set(0.0);
		lController.set(0.0);
	}
	public double getCurrent()
	{
		return rController.get();
	}


}
