package org.usfirst.frc.team3388.robot;

public class Frame{
	public double rightVal;
	public double leftVal;
	public Frame(double r,double l) {
		rightVal = r;
		leftVal = l;
	}
	
	public Frame(String[] data)
	{
		serialize(data);
	}

	private void serialize(String[] data) {
		rightVal = Double.parseDouble(data[1]);
		leftVal = Double.parseDouble(data[0]);
	}
	
}
