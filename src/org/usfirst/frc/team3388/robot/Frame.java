package org.usfirst.frc.team3388.robot;

public class Frame{
	public double rightVal;
	public double leftVal;
	public double poleVal;
	public double liftVal;
	public double rotateVal;
	public boolean pistonVal;
	
	public Frame(double r,double l,double pole,double lift,double rotate, boolean piston) {
		rightVal = r;
		leftVal = l;
		poleVal = pole;
		liftVal = lift;
		rotateVal = rotate;
		pistonVal = piston;
	}
	
	public Frame(String[] data)
	{
		serialize(data);
	}

	private void serialize(String[] data) {
		rightVal = Double.parseDouble(data[1]);
		leftVal = Double.parseDouble(data[0]);
		poleVal = Double.parseDouble(data[2]);
		liftVal = Double.parseDouble(data[3]);
		rotateVal = Double.parseDouble(data[4]);
		pistonVal = Boolean.parseBoolean(data[5]);
	}
	
}
