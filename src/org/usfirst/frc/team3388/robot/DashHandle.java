package org.usfirst.frc.team3388.robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashHandle {
	public static void disInit()
	{
		SmartDashboard.putBoolean(DashNames.enabled, false);
		SmartDashboard.putNumber(DashNames.timeLeft, 0.0);
		if(Robot.sysTrain)
			SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
	}
	public static void disPeriodic()
	{
		
	}
	public static void updateAngle()
	{
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
	}
	public static void teleInit()
	{
		
		SmartDashboard.putBoolean(DashNames.enabled, true);
		if(Robot.drivingTrain)
		{
		}
		if(Robot.sysTrain)
			SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
			
	}
	
	public static void telePeriodic()
	{
		if(Robot.sysTrain)
			SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
		updateTime();
	}
	public static void updateTime()
	{
		double time = (FlashUtil.secs() - Robot.startTime);
		SmartDashboard.putNumber(DashNames.timeLeft, 135.0-time);
	}
}
