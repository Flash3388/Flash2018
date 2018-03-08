package org.usfirst.frc.team3388.robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashHandle {
	private static boolean calibarated = false;
	public void disInit()
	{
		SmartDashboard.putBoolean(DashNames.enabled, false);
		SmartDashboard.putNumber(DashNames.timeLeft, 0.0);
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
		SmartDashboard.putBoolean(DashNames.resetGyro,false);
		SmartDashboard.putBoolean(DashNames.ScalePriority,false);
	}
	public void disPeriodic()
	{
		telePeriodic();
		if(SmartDashboard.getBoolean(DashNames.resetGyro,false))
		{
			if(!calibarated)
			{
				calibarated = true;
				System.out.println("init");
				Robot.drive.calibGyro();
				Robot.resetSensors();		
			}
		}
		else
			calibarated = false;
		
	}
	public void updateAngle()
	{
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
	}
	public void teleInit()
	{
		
		SmartDashboard.putBoolean(DashNames.enabled, true);
		
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());		
	}
	
	public void telePeriodic()
	{
		SmartDashboard.putNumber("enc lift", Robot.liftSystem.angle.get());
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
	//	SmartDashboard.putBoolean(DashNames.pressed, Robot.rollerGripperSystem.isPressed.get());
		
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
		SmartDashboard.putNumber(DashNames.distance, Robot.drive.distanceSource.pidGet());

		
		updateTime();
	}
	public void updateTime()
	{
		double time = (FlashUtil.secs() - Robot.startTime);
		SmartDashboard.putNumber(DashNames.timeLeft, 135.0-time);
	}
	
	public boolean getScalePriority()
	{
		return SmartDashboard.getBoolean(DashNames.ScalePriority,false);
	}
}
