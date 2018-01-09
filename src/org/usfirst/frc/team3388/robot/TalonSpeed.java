package org.usfirst.frc.team3388.robot;

import javax.sound.midi.ControllerEventListener;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public class TalonSpeed implements SpeedController{
	TalonSRX controller;
	int side = 1;
	public TalonSpeed(int devNum) {
		controller = new TalonSRX(devNum);
	}
	@Override
	public void pidWrite(double output) {
		set(output);
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
	public boolean getInverted() {
		
		return side == -1;
	}

	@Override
	public void disable() {
		stopMotor();	
	}

	@Override
	public void stopMotor() {
		controller.set(ControlMode.PercentOutput, 0.0);
	}

}
