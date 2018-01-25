package org.usfirst.frc.team3388.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.flash3388.flashlib.robot.devices.FlashSpeedController;

public class TalonSpeed implements FlashSpeedController{
	TalonSRX controller;
	WPI_TalonSRX c;
	boolean isInverted= false;
	public TalonSpeed(int devNum) {
		controller = new TalonSRX(devNum);
	}
	@Override
	public void set(double speed) {
		controller.set(ControlMode.PercentOutput, speed);
	}
	@Override
	public double get() {
		return controller.getMotorOutputPercent();
	}
	@Override
	public void setInverted(boolean isInverted) {
		controller.setInverted(isInverted);
		this.isInverted = isInverted;
	}
	@Override
	public boolean isInverted() {
		// TODO Auto-generated method stub
		return this.isInverted;
	}

}
