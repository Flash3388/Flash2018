
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

import edu.flash3388.flashlib.flashboard.Flashboard;
import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.devices.AnalogGyro;
import edu.flash3388.flashlib.robot.devices.Gyro;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.frc.PDP;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.AnalogInput;
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
	AHRS navx;
	@Override
	protected void initRobot() {
		SmartDashboard.putNumber("upspeed", 0.8);
		SmartDashboard.putNumber("downspeed", 0.32);
		
		controllersSetup();
		if(sysTrain)
		{
			//rollerGripperSystem = new RollerGripperSystem();
			//rollerGripperSystem.setup();
			
			poleSystem = new PoleSystem();
			poleSystem.setup();
			
			//liftSystem = new RollerLiftingSystem();
			//liftSystem.setup();
			//rollerGripperSystemSetup();
		}
		if(drivingTrain)
		{
			drive = new DriveSystem();
			drive.setup();
			
			camHandler = new CamerasHandler();
			
			if(sysTrain)
			{
				//autoHandlers();
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
				
		poleSystem = new PoleSystem();//pole setup start
		poleSystem.setup();
		
		rollerGripperSystem = new RollerGripperSystem();//roller setup start
		rollerGripperSystem.setup();
		
		liftSystem = new RollerLiftingSystem();//lift setup start
		liftSystem.setup();
		
		PoleAction scaleMinLift = new PoleAction(Constants.MIN_ANGLE);
		PoleAction scaleMidLift = new PoleAction(Constants.MID_ANGLE);
		PoleAction scaleMaxLift = new PoleAction(Constants.MAX_ANGLE);
		PoleAction switchLift = new PoleAction(Constants.SWITCH_ANGLE);
		PoleAction downLift = new PoleAction(Constants.DOWN);

				
		
		ActionGroup scalePutMax = new ActionGroup()
			.addSequential(scaleMaxLift);
		ActionGroup scalePutMid = new ActionGroup()
			.addSequential(scaleMidLift);
		ActionGroup scalePutMin = new ActionGroup()
			.addSequential(scaleMinLift);
		ActionGroup PutSwitch = new ActionGroup()
			.addSequential(switchLift);
		
		frontSwitchAuto = new ActionGroup()
			.addSequential(switchPIDDrive = new DrivePIDAction(DST_TO_SWITCH))
			.addParallel(PutSwitch)
			.addSequential(rollerGripperSystem.release);
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
			if(!drive.inited && SmartDashboard.getBoolean(DashNames.initGyro, false))
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
		systemController.X.whenPressed(AutoHandlers.scaleChoose(true));
	}
	
	
	@Override
	protected void teleopPeriodic() {
		DashHandle.telePeriodic();
		//drive.distancePID.setOutputLimit(-SmartDashboard.getNumber("speed", 0.0)
			//	,SmartDashboard.getNumber("speed", 0.0));
		
	}
	
	@Override
	protected void autonomousInit() {
		String gameData;
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		Action auto = AutoHandlers.switchChoose(gameData.charAt(0) == 'R');
		auto.start();
		/*
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
		*/
	}

	@Override
	protected void autonomousPeriodic() {
		
	}
}
