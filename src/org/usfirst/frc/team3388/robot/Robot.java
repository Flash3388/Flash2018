
package org.usfirst.frc.team3388.robot;

import java.sql.Time;

import org.usfirst.frc.team3388.actions.CaptureAction;
import org.usfirst.frc.team3388.actions.DrivePIDAction;
import org.usfirst.frc.team3388.actions.LiftAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.PoleLiftingAction;
import org.usfirst.frc.team3388.robot.subsystems.Drive;
import org.usfirst.frc.team3388.robot.subsystems.Pole;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripper;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeFRCRobot {
	/********************
	 * Main Robot class *
	 *******************/
	DrivePIDAction switchPIDDrive;
	ActionGroup frontSwitchAuto;
	
	CamerasHandler camHandler;
	public static Drive drive;
	
	SendableChooser<Action> autoChooser;
	
	Joystick rightController;
	Joystick leftController;
	Joystick systemController;
	
	public static Pole rollerGripperPole;
	public static RollerGripper rollerGripper;
	public static RollerLiftingSystem rollerGripperLifter;
	
	static double startTime;
	
	boolean drivingTrain = true;
	
	public enum Side {LEFT,MIDDLE,RIGHT};
	
	Side side;
	
	@Override
	protected void initRobot() {
		
		/*
		 * drive setup 
		 */
		
		drive = new Drive();
		/*
		 * cam handler
		 */
		
		camHandler = new CamerasHandler();
		/*
		 * auto setup
		 */
		controllersSetup();
		if(!drivingTrain)
		{
			rollerGripperSystemSetup();	
			autoHandlers();
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
	/*Function will setup all of the autonomy programs and the auto chooser
	 * input: None 
	 * output: None
	 */
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

	/*Function will setup all the roller gripper system(pole+lift+roller)
	 */
	private void rollerGripperSystemSetup()
	{
		final double DST_TO_SWITCH = 366.04;
		final double RELEASE_SPEED = -1.0;
		final double seconds=1.0;/*  */
		final double MAX_ANGLE=0.0;
		final double MIN_ANGLE=0.0;
		final double MID_ANGLE=0.0;
		final double DOWN = 75.0;
		final double SWITCH_ANGLE=0.0;
				
		rollerGripperPole = new Pole();//pole setup start
		rollerGripperPole.setDefaultAction(new Action() {
			final double MAX = 0.25;
			final double MIN = -0.25;
			
			@Override
			protected void execute() {
				rollerGripperPole.rotate(Mathf.constrain(systemController.getY(), MIN, MAX));
			}
			
			@Override
			protected void end() {
				//maybe rotate 0
			}
		} );
		
		rollerGripper = new RollerGripper();//roller setup start
		rollerGripperLifter = new RollerLiftingSystem();//lift setup start
		
		LiftAction lift = new LiftAction();
		CaptureAction capture = new CaptureAction();
		PoleAction poleAction = new PoleAction();
				
		TimedAction release = new TimedAction(new InstantAction() {	
			@Override
			protected void execute() {
				rollerGripper.rotate(RELEASE_SPEED);
			}
		}, seconds);//roller setup end
		
		
		InstantAction scaleMinLift = new PoleLiftingAction(MIN_ANGLE);
		InstantAction scaleMidLift = new PoleLiftingAction(MID_ANGLE);
		InstantAction scaleMaxLift = new PoleLiftingAction(MAX_ANGLE);
		InstantAction switchLift = new PoleLiftingAction(SWITCH_ANGLE);
		InstantAction downLift = new PoleLiftingAction(DOWN);//pole setup end
		/*TODO:
		 * add rollergripper rotation in parallel
		 */
		
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
			.addParallel(switchLift)
			.addSequential(release);
	}

	/*Function will setup the controllers
	 */
	private void controllersSetup() {
		final int BUTTON_COUNT = 4;
		if(!drivingTrain)
			systemController = new Joystick(RobotMap.SYSTEM_CONTROLLER, BUTTON_COUNT);
		
		rightController = new Joystick(RobotMap.RIGHT_CONTROLLER,BUTTON_COUNT);
		leftController = new Joystick(RobotMap.LEFT_CONTROLLER,BUTTON_COUNT);

		drive.driveTrain.setDefaultAction(new SystemAction(new Action() {
					@Override
					protected void execute() {
						drive.driveTrain.tankDrive(rightController.getY(),leftController.getY());
					}
					
					@Override
					protected void end() {
						drive.driveTrain.tankDrive(0,0);
					}
				}, drive.driveTrain));		
	}

	protected void disabledInit() {
		DashHandle.disInit();
		getRobotSide();
	}
	
	@Override
	protected void disabledPeriodic() {
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
				&& side==Side.RIGHT) 
				|| (gameData.charAt(0) == 'R' && autoChooser.getSelected().getName().charAt(1)=='w'
				&& side==Side.LEFT))
		{
			chosenAction=switchPIDDrive;
		}
		else if((gameData.charAt(1) == 'L' && autoChooser.getSelected().getName().charAt(1)=='c'
				&& side==Side.RIGHT)||(gameData.charAt(1) == 'R' && autoChooser.getSelected().getName().charAt(1)=='c'
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
