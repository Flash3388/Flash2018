package org.usfirst.frc.team3388.robot;

import edu.flash3388.flashlib.math.Mathf;
import edu.flash3388.flashlib.util.FlashUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashHandle {
	public static void disInit()
	{
		SmartDashboard.putBoolean(DashNames.enabled, false);
		SmartDashboard.putNumber(DashNames.timeLeft, 0.0);
	
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
	}
	public static void disPeriodic()
	{
		SmartDashboard.putBoolean(DashNames.pressed, Robot.rollerGripperSystem.isPressed.get());
	}
	public static void updateAngle()
	{
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
	}
	public static void teleInit()
	{
		
		SmartDashboard.putBoolean(DashNames.enabled, true);
		
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());		
	}
	
	public static void telePeriodic()
	{
		SmartDashboard.putNumber(DashNames.polePotentiometer, Robot.poleSystem.angle.get());
		SmartDashboard.putBoolean(DashNames.pressed, Robot.rollerGripperSystem.isPressed.get());
		
		SmartDashboard.putNumber(DashNames.angle, Robot.drive.rotationSource.pidGet());
		SmartDashboard.putNumber(DashNames.distance, Robot.drive.distanceSource.pidGet());

		
		updateTime();
	}
	public static void updateTime()
	{
		double time = (FlashUtil.secs() - Robot.startTime);
		SmartDashboard.putNumber(DashNames.timeLeft, 135.0-time);
	}
}
