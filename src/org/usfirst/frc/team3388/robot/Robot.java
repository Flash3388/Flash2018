
package org.usfirst.frc.team3388.robot;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.LiftAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;
import org.usfirst.frc.team3388.robot.subsystems.PoleSystem;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripperSystem;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.devices.IndexEncoder;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.frc.PDP;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.robot.frc.FlashFRCUtil;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
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
	public static Joystick systemController;
	
	public static PoleSystem PoleSystem;
	public static RollerGripperSystem rollerGripperSystem;
	public static RollerLiftingSystem LiftSystem;
	
	static double startTime;
	
	public static boolean drivingTrain = false;
	public static boolean sysTrain = true;
	
	public enum Side {LEFT,MIDDLE,RIGHT};
	
	Side side;
	AHRS navx;
	@Override
	protected void initRobot() {
		SmartDashboard.putNumber("upspeed", 0.8);
		SmartDashboard.putNumber("downspeed", 0.32);
		
		controllersSetup();
		if(sysTrain)
		{
			PoleSystem = new PoleSystem();
			//rollerGripperSystemSetup();	
			PoleSystem.setup();
		}
		if(drivingTrain)
		{
			drive = new DriveSystem();
			drive.setup();
			
			camHandler = new CamerasHandler();
			
			if(sysTrain)
			{
				autoHandlers();
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
		final double seconds = 5;
		Action toDrive = new Action() {
			final double speed = 0.5;		
			@Override
			protected void execute() {
				drive.driveTrain.forward(speed);
			}
			
			@Override
			protected void end() {
				drive.driveTrain.forward(0);
			}
		};
		autoChooser = new SendableChooser<Action>();
		autoChooser.addDefault("drive to switch (cross)", switchPIDDrive);
		autoChooser.addObject("Switch front auto",frontSwitchAuto);
		SmartDashboard.putData(autoChooser);
	}
	private void rollerGripperSystemSetup()
	{ 
		/*TODO:
		 * find all constants
		 */
		final double DST_TO_SWITCH = 366.04;
		final double RELEASE_SPEED = -1.0;
		final double seconds=1.0;/*  */
		final double MAX_ANGLE=0.0;
		final double MIN_ANGLE=0.0;
		final double MID_ANGLE=0.0;
		final double DOWN = 75.0;
		final double SWITCH_ANGLE=0.0;
		final double HIDE_ANGLE=270.0;
		final double USE_ANGLE=90.0;
				
		PoleSystem = new PoleSystem();//pole setup start
		PoleSystem.setup();
		
		rollerGripperSystem = new RollerGripperSystem();//roller setup start
		rollerGripperSystem.setup();
		
		LiftSystem = new RollerLiftingSystem();//lift setup start
		LiftSystem.setup();
		
		LiftAction hide = new LiftAction(HIDE_ANGLE);
		LiftAction use = new LiftAction(USE_ANGLE);
		
		CaptureAction capture = new CaptureAction();
		PoleAction scaleMinLift = new PoleAction(MIN_ANGLE);
		PoleAction scaleMidLift = new PoleAction(MID_ANGLE);
		PoleAction scaleMaxLift = new PoleAction(MAX_ANGLE);
		PoleAction switchLift = new PoleAction(SWITCH_ANGLE);
		PoleAction downLift = new PoleAction(DOWN);

				
		
		ActionGroup scalePutMax = new ActionGroup()
			.addSequential(scaleMaxLift)
			.addParallel(hide)
			.addSequential(use);
		ActionGroup scalePutMid = new ActionGroup()
			.addSequential(scaleMidLift)
			.addParallel(hide)
			.addSequential(use);
		ActionGroup scalePutMin = new ActionGroup()
			.addSequential(scaleMinLift)
			.addParallel(hide)
			.addSequential(use);
		ActionGroup PutSwitch = new ActionGroup()
			.addSequential(switchLift)
			.addParallel(hide)
			.addSequential(use);
		/*
		frontSwitchAuto = new ActionGroup()
			.addSequential(switchPIDDrive = new DrivePIDAction(DST_TO_SWITCH))
			.addParallel(PutSwitch)
			.addSequential(rollerGripperSystem.release);
		*/
		systemController.getButton(1).whileHeld(hide);
		systemController.getButton(1).whenReleased(use);
	}
	
	private void controllersSetup() {
		final int BUTTON_COUNT = 4;
		if(sysTrain)
			systemController = new Joystick(RobotMap.SYSTEM_CONTROLLER, BUTTON_COUNT);
		
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
	}

	@Override
	protected void teleopInit() {
		
		//controller.getRawButton(1);
		//DriverStation.getInstance().getStickButton(0, 1);
		
		startTime = FlashUtil.secs();
		DashHandle.teleInit();
	}

	@Override
	protected void teleopPeriodic() {
		//System.out.println(enc.get());
		DashHandle.telePeriodic();
		
	
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


		Action chosenAction = autoChooser.getSelected();
		
		if((gameData.charAt(0) == 'L' && autoChooser.getSelected().getName().charAt(1)=='w'
				&& side==Side.RIGHT) || (gameData.charAt(0) == 'R' 
				&& autoChooser.getSelected().getName().charAt(1)=='w'
				&& side==Side.LEFT))
		{
			chosenAction=switchPIDDrive;
		}
		else if((gameData.charAt(1) == 'L' && autoChooser.getSelected().getName().charAt(1)=='c'
				&& side==Side.RIGHT)||(gameData.charAt(1) == 'R' 
				&& autoChooser.getSelected().getName().charAt(1)=='c'
				&& side==Side.LEFT))
		{
			chosenAction=switchPIDDrive;
		}
		chosenAction.start();
	}

	@Override
	protected void autonomousPeriodic() {
		
	}
}
