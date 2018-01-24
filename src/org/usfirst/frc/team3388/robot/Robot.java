
package org.usfirst.frc.team3388.robot;

import java.sql.Time;

import org.usfirst.frc.team3388.actions.Capture;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.actions.PoleAction;
import org.usfirst.frc.team3388.robot.subsystems.Drive;
import org.usfirst.frc.team3388.robot.subsystems.LaunchSystem;
import org.usfirst.frc.team3388.robot.subsystems.LiftSystem;
import org.usfirst.frc.team3388.robot.subsystems.Pole;
import org.usfirst.frc.team3388.robot.subsystems.RollerGripper;

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
	
	Action auto;
		
	TimedAction timedDriveAuto;
	
	CamerasHandler camHandler;
	Drive drive;
	
	SendableChooser<Action> autoChooser;
	SendableChooser<Action> controllerChooser;
	
	Joystick rightController;
	Joystick leftController;
	XboxController controller;
	
	LaunchSystem shoot;
	LiftSystem liftSystem;
	public static Pole rollerGripperPole;
	public static RollerGripper rollerGripper;
	
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
		autoHandlers();
		
		rollerGripperSystemSetup();
	//	controllersSetup();
		controller = new XboxController(RobotMap.XBOX);

		drive.driveTrain.setDefaultAction(new SystemAction(new Action() {
			@Override
			protected void execute() {
				drive.driveTrain.tankDrive(controller.RightStick.getY(), controller.LeftStick.getY()*-1);
			}
			
			@Override
			protected void end() {
				drive.driveTrain.tankDrive(0,0);
			}
		}, drive.driveTrain));

		//controller.B.whenPressed(capture);//captures while pressed

		//liftSetup();
		//shootSetup();
		autoChooser = new SendableChooser<Action>();
		autoChooser.addDefault("auto1", timedDriveAuto);
		//...
		//autoChooser.addDefault("auto2", auto2);
		SmartDashboard.putData(autoChooser);
	}

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
	}

	private void rollerGripperSystemSetup()
	{
		final double RELEASE_SPEED = -1.0;
		final double seconds=1.0;/*  */
		final double MAX_ANGLE=0.0;
		final double MIN_ANGLE=0.0;
		final double MID_ANGLE=0.0;
		final double SWITCH_ANGLE=0.0;
		
		rollerGripperPole = new Pole();
		rollerGripper = new RollerGripper();
		
		Capture capture = new Capture();
		PoleAction poleAction = new PoleAction();
		
		InstantAction minLift = new InstantAction() {
			@Override
			protected void execute() {
				poleAction.setSetpoint(MIN_ANGLE);
				poleAction.start();
			}
		};
		TimedAction release = new TimedAction(new InstantAction() {
			
			@Override
			protected void execute() {
				rollerGripper.rotate(RELEASE_SPEED);
			}
		}, seconds);
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
				poleAction.setSetpoint(0.0);
				poleAction.start();
			}
		};
		
		ActionGroup putMax = new ActionGroup();
		putMax.addSequential(putMax)
			  .addSequential(release);//wait time can be added
		ActionGroup putMid = new ActionGroup();
		putMid.addSequential(putMid)
			  .addSequential(release);
		ActionGroup putMin = new ActionGroup();
		putMin.addSequential(putMin)
			  .addSequential(release);
		ActionGroup putSwitch = new ActionGroup();
		putSwitch.addSequential(putSwitch)
				 .addSequential(release);
		
		controller.A.whenPressed(putSwitch);
		controller.B.whenPressed(putMin);
		controller.X.whenPressed(putMid);
		controller.Y.whenPressed(putMax);
		controller.RB.whileHeld(capture);
		controller.LB.whenPressed(release);
		
	}
	
	private void controllersSetup() {
		System.out.println("Runned");
		/*****************************
		 *      code for controllers  *
		 * ***************************/
		rightController = new Joystick(RobotMap.RIGHT_CONTROLLER);
		leftController = new Joystick(RobotMap.LEFT_CONTROLLER);
		
		Action setJoysticks = new InstantAction() {
			
			@Override
			protected void execute() {
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
		};
		controller = new XboxController(RobotMap.XBOX);
		Action setXbox = new InstantAction() {
			
			@Override
			protected void execute() {
				drive.driveTrain.setDefaultAction(new SystemAction(new Action() {
					@Override
					protected void execute() {
						drive.driveTrain.tankDrive(controller.RightStick.AxisY, controller.LeftStick.AxisY);
					}
					
					@Override
					protected void end() {
						drive.driveTrain.tankDrive(0,0);
					}
				}, drive.driveTrain));
			}

		};
		//Controller chooser
		controllerChooser = new SendableChooser<Action>();
		controllerChooser.addDefault("None", setXbox);
		controllerChooser.addObject("XBOX",setXbox );
		controllerChooser.addDefault("2 joysticks", setJoysticks);
		SmartDashboard.putData("Controller chooser", controllerChooser);
		Action controllers = controllerChooser.getSelected();
		controllers.start();
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
				//val = Mathf.constrain(val, MIN, MAX);
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

	protected void disabledInit() {
		DashHandle.disInit();
	}
	
	@Override
	protected void disabledPeriodic() {
		//System.out.println(String.format("%.2f", p.get()));
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
