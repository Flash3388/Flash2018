package org.usfirst.frc.team3388.robot.subsystems;

import org.usfirst.frc.team3388.robot.Robot;
import org.usfirst.frc.team3388.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.Subsystem;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.systems.Rotatable;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RollerLiftingSystem extends Subsystem implements Rotatable {

	public final double DEFAULT_UP_SPEED=0.3;
	public final double DEFAULT_DOWN_SPEED=0.3;
	
	TalonSRX rController;
	TalonSRX lController;
	
	public RollerLiftingSystem() {
		rController = new TalonSRX(RobotMap.ROLLER_GRIPPER_L_LIFT_CONTROLLER);
		lController = new TalonSRX(RobotMap.ROLLER_GRIPPER_R_LIFT_CONTROLLER);
		rController.follow(lController);
	}
	@Override
	public void rotate(double speed) {
		lController.set(ControlMode.PercentOutput, speed);
	}
	
	public void rotate(boolean dir)
	{
		lController.set(ControlMode.PercentOutput, SmartDashboard.getNumber("lift speed", 0.2));
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
	
	public void setup()
	{
		SmartDashboard.putNumber("lift speed", 0.5);
		SmartDashboard.putNumber("down speed", 0.3);
		this.setDefaultAction(new SystemAction(new Action() {
			
			final double MARGIN = 0.2;
			@Override
			protected void execute() {
				double y = Robot.systemController.LeftStick.getY();
				if(y > MARGIN)
					rotate(SmartDashboard.getNumber("lift speed", 0.2));
				else if(y < -MARGIN)
					rotate(-SmartDashboard.getNumber("down speed", 0.2));
				else
					stop();
				System.out.println(SmartDashboard.getNumber("lift speed", 0.2));
			}
			
			@Override
			protected void end() {
				stop();
			}
		}, this));
		/*this.setDefaultAction(new Action() {
			final int UP =0;
			final int DOWN =180;		
			@Override
			protected void execute() {
				int val =Robot.systemController.getPOV().get();
				if(val == UP)
					liftUp();
				else if(val == DOWN)
					liftDown();
				else
					stop();
			}
			
			@Override
			protected void end() {
				stop();
			}
		});*/
	}
	public double getRController() {
		return rController.getOutputCurrent();
	}
	public double getLController() {
		return lController.getOutputCurrent();
	}
}
