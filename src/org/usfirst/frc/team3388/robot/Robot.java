
package org.usfirst.frc.team3388.robot;

import java.sql.Time;

import org.usfirst.frc.team3388.actions.Capture;
import org.usfirst.frc.team3388.actions.Lift;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.robot.subsystems.Drive;
import org.usfirst.frc.team3388.robot.subsystems.Pole;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripper;
import org.usfirst.frc.team3388.robot.subsystems.RollerLiftingSystem;

import edu.flash3388.flashlib.dashboard.controls.ChooserControl;
import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.robot.Action;
import edu.flash3388.flashlib.robot.ActionGroup;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeFRCRobot {
	/********************
	 * Main Robot class *
	 *******************/
	TimedAction timedDriveAuto;
	
	CamerasHandler camHandler;
	Drive drive;
	
	SendableChooser<Action> autoChooser;
	
	Joystick rightController;
	Joystick leftController;
	XboxController controller;
	
	public static Pole rollerGripperPole;
	public static RollerGripper rollerGripper;
	public static RollerLiftingSystem rollerGripperLifter;
	
	static double startTime;
	@Override
	protected void initRobot() {
		
		/*
		 * drive setup 
		 */
		
		drive = new Drive();
		/*
		 * cam handler
		 */
		
		//camHandler = new CamerasHandler();
		/*
		 * auto setup
		 */
		controllersSetup();

		autoHandlers();
		
		rollerGripperSystemSetup();


	}
	/*Function will setup all of the autonomy programs and the auto chooser
	 * input: None 
	 * output: None
	 */
	private void autoHandlers() {
		final double seconds = 0;
		Action toDrive = new Action() {
			final double speed = 0;		
			@Override
			protected void execute() {
				drive.driveTrain.forward(speed);
			}
			
			@Override
			protected void end() {
				drive.driveTrain.forward(0);
			}
		};
		timedDriveAuto = new TimedAction(toDrive, seconds);
		autoChooser = new SendableChooser<Action>();
		autoChooser.addDefault("auto1", timedDriveAuto);
		//...
		//autoChooser.addDefault("auto2", auto2);
		SmartDashboard.putData(autoChooser);
	}
	/*Function will setup all the roller gripper system(pole+lift+roller)
	 * input: None
	 * output: None
	 */
	private void rollerGripperSystemSetup()
	{
		final double RELEASE_SPEED = -1.0;
		final double seconds=1.0;/*  */
		final double MAX_ANGLE=0.0;
		final double MIN_ANGLE=0.0;
		final double MID_ANGLE=0.0;
		final double DOWN = 75.0;
		final double SWITCH_ANGLE=0.0;
		
		int rollerAngle=0;
		
		rollerGripperPole = new Pole();//pole setup start
		rollerGripper = new RollerGripper();//roller setup start
		rollerGripperLifter = new RollerLiftingSystem();//lift setup start
		
		Lift lift = new Lift();
		Capture capture = new Capture();
		PoleAction poleAction = new PoleAction();
		
		rollerGripperLifter.setDefaultAction(lift);//lift setup end
		
		TimedAction release = new TimedAction(new InstantAction() {
			
			@Override
			protected void execute() {
				rollerGripper.rotate(RELEASE_SPEED);
			}
		}, seconds);//roller setup end
		
		InstantAction minLift = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(MIN_ANGLE);
				poleAction.start();
			}
		};
		InstantAction midLift = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(MID_ANGLE);
				poleAction.start();
			}
		};
		InstantAction maxLift = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(MAX_ANGLE);
				poleAction.start();
			}
		};
		InstantAction switchLift = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(SWITCH_ANGLE);
				poleAction.start();
			}
		};
		InstantAction down = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(DOWN);
				poleAction.start();
			}
		};//pole setup end
		
		ActionGroup putMax = new ActionGroup();//Action groups start
		putMax.addSequential(putMax)
			  .addSequential(release)
			  .addSequential(down);
		ActionGroup putMid = new ActionGroup();
		putMid.addSequential(putMid)
			  .addSequential(release)
			  .addSequential(down);
		ActionGroup putMin = new ActionGroup();
		putMin.addSequential(putMin)
			  .addSequential(release)
			  .addSequential(down);
		ActionGroup putSwitch = new ActionGroup();
		putSwitch.addSequential(putSwitch)
				 .addSequential(release)
				 .addSequential(down);//Action groups setup end

		controller.A.whenPressed(putSwitch);//controller binding start
		controller.B.whenPressed(putMin);
		controller.X.whenPressed(putMid);
		controller.Y.whenPressed(putMax);
		controller.RB.whileHeld(capture);
		controller.LB.whenPressed(release);//controller binding end
		
	}
	/*Function will calculate the angle for the roller gripper
	 * input: current angle of the roller , current angle of the pole
	 * output: angle that the roller has to have 
	 */
	public static double calcAngle(double rollerAngle , double poleAngle)
	{
		final double RATIO = 90.0;//the angle where the roller is vertical to the ground
		return Math.cos(poleAngle)*RATIO;
	}
	/*Function will setup the controllers
	 * input: None
	 * output: None
	 */
	private void controllersSetup() {
		
		controller = new XboxController(RobotMap.XBOX);
		
		rightController = new Joystick(RobotMap.RIGHT_CONTROLLER);
		leftController = new Joystick(RobotMap.LEFT_CONTROLLER);

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
		
	}
}
