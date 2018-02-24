
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
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.frc.PDP;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeFRCRobot {

	DrivePIDAction switchPIDDrive;
	ActionGroup frontSwitchAuto;
		
	CamerasHandler camHandler;
	public static DriveSystem drive;
	
	SendableChooser<Integer> autoChooser;
	
	public static Joystick rightController;
	public static Joystick leftController;
	public static XboxController systemController;
	
	public static PoleSystem poleSystem;
	public static RollerGripperSystem rollerGripperSystem;
	public static RollerLiftingSystem liftSystem;
	
	static double startTime;	

	public static boolean closeSwitch;
	@Override
	protected void initRobot() {
		controllersSetup();
		/*
		Compressor c = new Compressor();
		c.stop();
		*/
		
		systemSetup();
		drive = new DriveSystem();
		drive.setup();
		
		camHandler = new CamerasHandler();
		
		ActionHandler.setup();
		buttons();
		autoChooserSetup();
		
	}

	private void buttons() {
		systemController.B.whenPressed(ActionHandler.fullDown);
		//systemController.X.whenPressed(ActionHandler.downLift);backNScale
		systemController.X.whenPressed(ActionHandler.fullScaleLift);
		/*
		systemController.X.whenMultiPressed(action, 2);
		systemController.X.whenMultiPressed(action, 3);
		*/
		systemController.RB.whenPressed(ActionHandler.capture);
		systemController.DPad.Up.whenPressed(ActionHandler.hide);
		systemController.DPad.Down.whenPressed(ActionHandler.downUse);
		//systemController.Y.whenPressed(ActionHandler.shoot);
		systemController.LB.whileHeld(new Action() {
			
			@Override
			protected void execute() {
				rollerGripperSystem.spin();
			}
			
			@Override
			protected void end() {
				rollerGripperSystem.stop();
			}
		});	
		systemController.Y.whenPressed(AutoHandlers.rightScale(true,false,true));
		systemController.RB.whenReleased(ActionHandler.capture);
		systemController.LB.whenPressed(ActionHandler.release);
		
		systemController.Back.whenPressed(new InstantAction() {		
			@Override
			protected void execute() {
				resetSensors();
			}
		});

		systemController.Start.whenPressed(new InstantAction() {		
			@Override
			protected void execute() {
				drive.calibGyro();
			}
		});
	}
	
	@Override
	protected void preInit(RobotInitializer initializer) {
		super.preInit(initializer);
		initializer.initFlashboard= false;	
	}
	private void systemSetup()
	{
		rollerGripperSystem = new RollerGripperSystem();
		rollerGripperSystem.setup();
		
		poleSystem = new PoleSystem();		
		poleSystem.setup();
		liftSystem = new RollerLiftingSystem();
		liftSystem.setup();
	}
	
	private void autoChooserSetup() {
		autoChooser = new SendableChooser<Integer>();
		autoChooser.addDefault("Center switch", 0);
		autoChooser.addDefault("Right Scale", 1);
		autoChooser.addDefault("Left Scale", 2);
		SmartDashboard.putData("auto chooser",autoChooser);
	}
	
	private void controllersSetup() {
		final int BUTTON_COUNT = 4;
		systemController = new XboxController(RobotMap.SYSTEM_CONTROLLER);
	
		rightController = new Joystick(RobotMap.RIGHT_CONTROLLER,BUTTON_COUNT);
		leftController = new Joystick(RobotMap.LEFT_CONTROLLER,BUTTON_COUNT);
	}
	protected void disabledInit() {
		DashHandle.disInit();
		drive.initGyro();
		
	}
	
	@Override
	protected void disabledPeriodic() {
		DashHandle.disPeriodic();	
	}

	@Override
	protected void teleopInit() {
		resetSensors();
		startTime = FlashUtil.secs();
		DashHandle.teleInit();
	}

	@Override
	protected void teleopPeriodic() {
		DashHandle.telePeriodic();
	}
	
	@Override
	protected void autonomousInit() {
		resetSensors();
	
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		boolean rightSwitch = gameData.charAt(0) == 'R';
		boolean rightScale = gameData.charAt(1) == 'R';
		
		Action auto = AutoHandlers.centerSwitch(rightSwitch);
		switch(autoChooser.getSelected())
		{
		case 1:
			auto = AutoHandlers.rightScale(rightScale,rightSwitch,true);
			break;
		case 2:
			auto = AutoHandlers.rightScale(rightScale,rightSwitch,false);
			break;
		}
		auto.start();
		}


	public static void resetSensors() {
		drive.encoder.reset();
		drive.resetGyro();
		drive.initGyro();
		poleSystem.resetStartAngle();
		liftSystem.resetEncoder();

	}

	@Override
	protected void autonomousPeriodic() {
		
	}
}
