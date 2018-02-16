
package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;
import org.usfirst.frc.team3388.robot.subsystems.PoleSystem;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripperSystem;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import com.sun.java.swing.plaf.windows.WindowsBorders.DashedBorder;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.frc.PDP;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeFRCRobot {

	DrivePIDAction switchPIDDrive;
	ActionGroup frontSwitchAuto;
	
	CamerasHandler camHandler;
	public static DriveSystem drive;
	
	SendableChooser<Action> autoChooser;
	
	public static Joystick rightController;
	public static Joystick leftController;
	public static XboxController systemController;
	
	public static PoleSystem poleSystem;
	public static RollerGripperSystem rollerGripperSystem;
	public static RollerLiftingSystem liftSystem;
	
	static double startTime;
	
	public static boolean drivingTrain = true;
	public static boolean sysTrain = true;
	
	public enum Side {LEFT,MIDDLE,RIGHT};
	
	Side side;
	@Override
	protected void initRobot() {
		SmartDashboard.putNumber("upspeed", 0.8);
		SmartDashboard.putNumber("downspeed", 0.32);
		
		controllersSetup();
		if(sysTrain)
		{
			rollerGripperSystem = new RollerGripperSystem();
			rollerGripperSystem.setup();
			
			poleSystem = new PoleSystem();
			poleSystem.setup();
			
			liftSystem = new RollerLiftingSystem();
			liftSystem.setup();
			//rollerGripperSystemSetup();
		}
		if(drivingTrain)
		{
			drive = new DriveSystem();
			drive.setup();
			
			camHandler = new CamerasHandler();
			
			if(sysTrain)
			{
				ActionHandler.setup();
				
			}
		}
		
	}
	private void getRobotSide()
	{
		int getSide = 0;
		SmartDashboard.getNumber("Robot position", getSide);
		switch (getSide)
		{
		case 0:
			side=Side.LEFT;
			break;
		case 1:
			side=Side.MIDDLE;
			break;
		case 2:
			side=Side.RIGHT;
			break;
		}
	}
	
	private void autoHandlers() {
		autoChooser = new SendableChooser<Action>();
		SmartDashboard.putData(autoChooser);
	}
	
	private void controllersSetup() {
		final int BUTTON_COUNT = 4;
		if(sysTrain)
			systemController = new XboxController(RobotMap.SYSTEM_CONTROLLER);
		
		if(drivingTrain)
		{
			rightController = new Joystick(RobotMap.RIGHT_CONTROLLER,BUTTON_COUNT);
			leftController = new Joystick(RobotMap.LEFT_CONTROLLER,BUTTON_COUNT);
		}
	}

	protected void disabledInit() {
		DashHandle.disInit();
	}
	
	@Override
	protected void disabledPeriodic() {
		getRobotSide();
		DashHandle.disPeriodic();
		
		if(drivingTrain)
		{
				drive.initGyro();
		}
	}

	@Override
	protected void teleopInit() {
		if(drivingTrain)
		{
			drive.encoder.reset();
			drive.resetGyro();
		}
		startTime = FlashUtil.secs();
		DashHandle.teleInit();
		//systemController.X.whenPressed(AutoHandlers.scaleChoose(true));
		//SmartDashboard.putNumber("rotate limit",0.4); 
		//SmartDashboard.putNumber("drive limit", 0.4);
	}
	
	
	@Override
	protected void teleopPeriodic() {
		DashHandle.telePeriodic();
		//drive.rotatePID.setOutputLimit(SmartDashboard.getNumber("rotate limit",0.4));
	//	drive.distancePID.setOutputLimit(SmartDashboard.getNumber("drive limit",0.4));

	}
	
	@Override
	protected void autonomousInit() {
		String gameData;
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		Action auto = AutoHandlers.switchChoose(gameData.charAt(0) == 'R');
		auto.start();
		//Action chosenAction = autoChooser.getSelected();
	}

	@Override
	protected void autonomousPeriodic() {
		
	}
}
