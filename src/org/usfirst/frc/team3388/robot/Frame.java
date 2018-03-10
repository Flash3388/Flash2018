package org.usfirst.frc.team3388.robot;

public class Frame{
	public double rightVal;
	public double leftVal;
	public double poleVal;
	
	public Frame(double r,double l,double pole) {
		rightVal = r;
		leftVal = l;
		poleVal = pole;
	}
	
	public Frame(String[] data)
	{
		serialize(data);
	}

	private void serialize(String[] data) {
		rightVal = Double.parseDouble(data[1]);
		leftVal = Double.parseDouble(data[0]);
		poleVal = Double.parseDouble(data[2]);
	}
	
}
