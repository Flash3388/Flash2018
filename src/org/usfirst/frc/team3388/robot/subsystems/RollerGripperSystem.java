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


public class RollerGripperSystem extends Subsystem implements Rotatable{

	private VictorSP rController;
	private VictorSP lController;
	
	public PistonController piston;
	public static DoubleSource angle;
	public static final double DEFAULT_SPEED = 0.7;
	
	private DigitalInput in;
	public BooleanSource isPressed;
	
	public RollerGripperSystem() {
		
		piston = new PistonController(RobotMap.L_CHANNEL, RobotMap.R_CHANNEL);

		rController = new VictorSP(RobotMap.CAPTURE_R);
		
		lController = new VictorSP(RobotMap.CAPTURE_L);
		lController.setInverted(true);
		
		in = new DigitalInput(RobotMap.CAPTURE_SWITCH);
		isPressed = new BooleanSource() {
			
			@Override
			public boolean get() {
				return in.get();
			}
		};
	}
	public void rotate(double speed) {
		rController.set(speed);
		lController.set(speed);
	}
	public void rotate(boolean in) {
		if(in)
			rotate(DEFAULT_SPEED);
		else
			rotate(-DEFAULT_SPEED);
	}
	
	public boolean isClosed()
	{
		return piston.isClosed();
	}
	
	public void setup()
	{
	
		Robot.systemController.RB.whenPressed(ActionHandler.capture);
		Robot.systemController.LB.whenPressed(ActionHandler.release);

		Robot.systemController.A.whenPressed(new InstantAction() {
			
			@Override
			protected void execute() {
				piston.change();
			}
		});
	}
	@Override
	public void stop() {
		rController.set(0.0);
		lController.set(0.0);
	}
}
