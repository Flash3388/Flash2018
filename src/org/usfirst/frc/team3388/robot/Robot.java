
package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.robot.added.XboxController1;
import org.usfirst.frc.team3388.robot.subsystems.Drive;
import org.usfirst.frc.team3388.robot.subsystems.LaunchSystem;
import org.usfirst.frc.team3388.robot.subsystems.LiftSystem;
import org.usfirst.frc.team3388.robot.subsystems.PistonController;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends IterativeFRCRobot {
	Action auto;
	SendableChooser<Action> autoChooser;
	CamerasHandler camHandler;
	Drive drive;
	LiftSystem liftSystem;
	
	XboxController controller;
	LaunchSystem shoot;
	@Override
	protected void initRobot() {
		/*
		 * drive setup 
		 */
		
		//drive = new Drive();
		
		/*
		 * cam handler
		 */
		
		//camHandler = new CamerasHandler();
		/*
		 * auto setup
		 */
		controller = new XboxController(RobotMap.XBOX);
		liftSetup();
		shootSetup();
		//autoChooser = new SendableChooser<Action>();
		//autoChooser.addDefault("auto1", auto);
		//...
		//autoChooser.addDefault("auto2", auto2);
		 
		 
		
	}

	private void liftSetup() {
		liftSystem = new LiftSystem();
		
		liftSystem.setDefaultAction(new Action() {
			final double MIN = -0.2;
			final double MAX = 0.2;
			
			@Override
			protected void execute() {
				/*	to scale the value use this function:
					Mathf.constrain(value, min, max)
					like so:
					va
					liftSystem.rotate(controller.LeftStick.getYAxis().get());
				 */
				double val = controller.LeftStick.getYAxis().get();
				val = Mathf.constrain(val, MIN, MAX);
				liftSystem.rotate(val);	
			}
			@Override
			protected void end() {
				liftSystem.stop();
			}
		});
	}

	private void shootSetup() {
		shoot = new LaunchSystem(RobotMap.SOLA1,RobotMap.SOLA2,RobotMap.SOLB1,RobotMap.SOLB2);
		//controller.getRawButton(1);
		
		controller.A.whenPressed(new InstantAction() {
			@Override
			protected void execute() {
				if(shoot.isClosed())
				{
					shoot.open();
					System.out.println("open");
				}
				else
				{
					shoot.close();
					System.out.println("close");
				}
				
			}
		});
	}

	@Override
	protected void disabledInit() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void disabledPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void teleopInit() {
		//controller.getRawButton(1);
		//DriverStation.getInstance().getStickButton(0, 1);
	}

	@Override
	protected void teleopPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void autonomousInit() {
	/*	String gameData;
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		// Example for gameData : LRL
		//means that the closet LEFT switch is yours
		//the right scale is yours
		//and the far left switch is yours
		//
		//Other example is : RRL
		
		if(gameData.charAt(0) == 'L')
		{
			//Put left auto code here
		} else {
			//Put right auto code here
		}

		Action chosenAction = autoChooser.getSelected();
		chosenAction.start();
		*/
	}

	@Override
	protected void autonomousPeriodic() {
		// TODO Auto-generated method stub
		
	}


}
