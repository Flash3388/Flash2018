
package org.usfirst.frc.team3388.robot;

import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.DriverStation;


public class Robot extends IterativeFRCRobot {
	//stse
	Talon a = new Talon(0);
	Action auto;
	SendableChooser<Action> autoChooser;
	@Override
	protected void initRobot() {
		/*
		 * camera server
		 */
		CameraServer.getInstance().startAutomaticCapture();	
		
		
		/*
		 * auto setup
		 */
		autoChooser = new SendableChooser<Action>();
		autoChooser.addDefault("auto1", auto);
		//...
		//autoChooser.addDefault("auto2", auto2);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void teleopPeriodic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void autonomousInit() {
		String gameData;
		
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
	}

	@Override
	protected void autonomousPeriodic() {
		// TODO Auto-generated method stub
		
	}


}
