package org.usfirst.frc.team3388.robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashHandle {
	public static void disInit()
	{
		SmartDashboard.putBoolean(DashNames.enabled, false);
		SmartDashboard.putNumber(DashNames.timeLeft, 0.0);
		SmartDashboard.putNumber(DashNames.time, 0.0);
		if(Robot.drivingTrain)
		{
			SmartDashboard.putBoolean(DashNames.navxConnection, Robot.drive.navx.isConnected());
		}
			
	}
	public static void updateAngle()
	{
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
	}
	public static void disPeriodic()
	{
		if(Robot.drivingTrain)
		{
			updateAngle();
		}
	}
	public static void teleInit()
	{
		SmartDashboard.putBoolean(DashNames.enabled, true);
		SmartDashboard.putNumber(DashNames.time, 0.0);
		if(Robot.drivingTrain)
			SmartDashboard.putBoolean(DashNames.navxConnection, Robot.drive.navx.isConnected());
	}
	
	public static void telePeriodic()
	{
		if(Robot.drivingTrain)
		{
			updateAngle();
		}
		updateTime();
	}
	public static void updateTime()
	{
		double time = (FlashUtil.secs() - Robot.startTime);
		SmartDashboard.putNumber(DashNames.time, Mathf.scale(time, 0.0, 100.0));
		SmartDashboard.putNumber(DashNames.timeLeft, 135.0-time);
	}
}
