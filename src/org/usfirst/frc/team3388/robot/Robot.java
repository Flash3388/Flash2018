
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
import edu.flash3388.flashlib.robot.FlashRobotUtil;
import edu.flash3388.flashlib.robot.InstantAction;
import edu.flash3388.flashlib.robot.SystemAction;
import edu.flash3388.flashlib.robot.TimedAction;
import edu.flash3388.flashlib.robot.frc.FlashFRCUtil;
import edu.flash3388.flashlib.robot.frc.IterativeFRCRobot;
import edu.flash3388.flashlib.robot.frc.PDP;
import edu.flash3388.flashlib.robot.hid.Joystick;
import edu.flash3388.flashlib.robot.hid.XboxController;
import edu.flash3388.flashlib.robot.systems.FlashDrive.MotorSide;
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
	private DashHandle dashHandle;
	public static boolean closeSwitch;
	
	public static Recorder rec;
	public static boolean shouldRec = false;
	@Override
	protected void initRobot() {
		controllersSetup();
		
		rec = new  Recorder();
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
		
		
		dashHandle = new DashHandle();
	}

	private void buttons() {
		FlashRobotUtil.updateHID();
		systemController.B.whenPressed(ActionHandler.fullDown);
		systemController.X.whenPressed(ActionHandler.fullScaleLift);
		
		systemController.DPad.Up.whenPressed(ActionHandler.hide);
		systemController.DPad.Down.whenPressed(ActionHandler.downUse);
		systemController.Y.whenPressed(ActionHandler.shoot);
		systemController.Start.whenPressed(new InstantAction() {
			
			@Override
			protected void execute() {
				cancelActions();
			}
		});
		//systemController.Y.whenPressed(AutoHandlers.rightScale(false,true,false,true));

		Robot.systemController.RB.whileHeld(new SystemAction(new Action() {
			
			@Override
			protected void execute() {
				rollerGripperSystem.rotate(false);
			}
			
			@Override
			protected void end() {
			}
		}));
		
		systemController.RB.whenReleased(ActionHandler.capture);
		systemController.LB.whenPressed(ActionHandler.release);
		
		systemController.Back.whenPressed(new InstantAction() {		
			@Override
			protected void execute() {
				resetSensors();
			}
		});
		
		record();
		writer();
		
		
	}

	private void writer() {
		Runnable writer = new Runnable() {	
			@Override
			public void run() {
				int curr =0;
				while(curr < rec.frames.size())
				{
					Frame f = rec.frames.get(curr);
					drive.driveTrain.tankDrive(f.rightVal, f.leftVal);
					poleSystem.rotate(f.poleVal);
					liftSystem.rotate(f.liftVal);
					rollerGripperSystem.rotate(f.rotateVal);
					rollerGripperSystem.piston.use(f.pistonVal);
					
					curr++;
					try {
						Thread.sleep(Recorder.PERIOD);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		
		};
		leftController.getButton(1).whenPressed(new SystemAction(new Action() {
		
				Thread t;
				@Override
				protected void initialize() {
					rec.loadFile();
					t = new Thread(writer);
					t.start();
				}
				@Override
				protected void execute() {
					
				}
				@Override
				protected void end() {
					drive.driveTrain.stop();
					poleSystem.stop();
				}
				@Override
				protected boolean isFinished() {
					return !t.isAlive();
				}
			},drive,poleSystem));
	}

	private void record() {
		Runnable recoredThread = new Runnable() {
			
			@Override
			public void run() {
				while(shouldRec && !Thread.interrupted())
				{
					double left = drive.driveTrain.getController(MotorSide.Left).get();
					double right = drive.driveTrain.getController(MotorSide.Right).get();
					double pole = poleSystem.getCurrent();
					double lift = liftSystem.getCurrent();
					double rotate = rollerGripperSystem.getCurrent();
					boolean piston = rollerGripperSystem.piston.isClosed();
					System.out.println("aaa");
					rec.addFrame(new Frame(right,left,pole,lift,rotate,piston));
					try {
						Thread.sleep(Recorder.PERIOD);
					} catch (InterruptedException e) {
						System.out.println("interraped");
					}
				}
				rec.toFile();
					
			}
		};
		rightController.getButton(1).whenPressed(new InstantAction() {
			
			@Override
			protected void execute(){
				shouldRec = !shouldRec;
				if(shouldRec)
				{
					new Thread(recoredThread).start();
					
				}
			}
		});
	}

	private void cancelActions() {
		rollerGripperSystem.cancelCurrentAction();
		liftSystem.cancelCurrentAction();
		poleSystem.cancelCurrentAction();
		drive.cancelCurrentAction();
	}
	
	@Override
	protected void preInit(RobotInitializer initializer) {
		super.preInit(initializer);
		initializer.standardLogs = false;		
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
		dashHandle.disInit();
		drive.initGyro();
		
	}
	
	@Override
	protected void disabledPeriodic() {
		dashHandle.disPeriodic();	
		//System.out.println("up "+ poleSystem.isUp.get());
		//System.out.println("down "+poleSystem.isDown.get());
		
	}

	@Override
	protected void teleopInit() {
		//resetSensors();
		startTime = FlashUtil.secs();
		dashHandle.teleInit(); 
		cancelActions();
		
	}

	@Override
	protected void teleopPeriodic() {
		FlashRobotUtil.updateHID();
		dashHandle.telePeriodic();
	}
	
	@Override
	
	protected void autonomousInit() {
		resetSensors();
	
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		boolean rightSwitch = gameData.charAt(0) == 'R';
		boolean rightScale = gameData.charAt(1) == 'R';
		
		Action auto = AutoHandlers.centerSwitch(rightSwitch);
		boolean scalePri = dashHandle.getScalePriority();
		switch(autoChooser.getSelected())
		{
		case 1:
			auto = AutoHandlers.rightScale(rightScale,rightSwitch,true,scalePri);
			break;
		case 2:
			auto = AutoHandlers.rightScale(rightScale,rightSwitch,false,scalePri);
			break;
		}
		
		//Action auto = AutoHandlers.rightScale(false, false, false, true);
		
		auto.start();
	}


	public static void resetSensors() {
		drive.encoder.reset();
		drive.initGyro();
		drive.resetGyro();
		poleSystem.resetStartAngle();
		liftSystem.resetEncoder();

	}

	@Override
	protected void autonomousPeriodic() {
		dashHandle.telePeriodic();
		
	}
}
