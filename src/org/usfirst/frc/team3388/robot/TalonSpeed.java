package org.usfirst.frc.team3388.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.flash3388.flashlib.robot.devices.FlashSpeedController;

public class TalonSpeed implements FlashSpeedController{
	TalonSRX controller;
	int side = 1;
	public TalonSpeed(int devNum) {
		controller = new TalonSRX(devNum);
	}
	@Override
	public void set(double speed) {
		controller.set(ControlMode.PercentOutput, speed*side);
	}
	@Override
	public double get() {
		return controller.getMotorOutputPercent();
	}
	@Override
	public void setInverted(boolean isInverted) {
		side  = isInverted? -1: 1;
	}
	@Override
	public boolean isInverted() {
		// TODO Auto-generated method stub
		return false;
	}

}
