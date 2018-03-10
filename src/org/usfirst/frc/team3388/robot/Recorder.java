package org.usfirst.frc.team3388.robot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.usfirst.frc.team3388.robot.subsystems.DriveSystem;

public class Recorder {
	
	File file;
	static FileWriter writer;
	static FileReader reader;
	
	public Recorder() throws IOException
	{
	      file = new File("motorInfo.txt");
	      
	      // creates the file
	      file.createNewFile();
	      
	      // creates a FileWriter Object
	      writer = new FileWriter(file); 
	      reader = new FileReader(file);
	}
	
	public static void saveVal(double leftVal,double rightVal) throws IOException
	{
		do
		{
			while(DriveSystem.inRange(leftVal, 0.0) || DriveSystem.inRange(leftVal, 0.0)) {}
			String left = new Double(leftVal).toString();
			String right = new Double(rightVal).toString();
			// Writes the content to the file

			writer.write(left+"\n"+right+"\n");

		} while(DriveSystem.inRange(leftVal, 0.0) || DriveSystem.inRange(leftVal, 0.0));
		writer.write("0.0");
		writer.flush();
		writer.close();
	}
	
	public static Object getVal()
	{
		Object vals = new Object();
		char[] temp = new char[50];
		String left = null;
		String right = null;
		class vals{
			public LinkedList<Double> leftVals;
			public LinkedList<Double> rightVlas;
			
			public vals()
			{
				leftVals = new LinkedList<Double>();
				rightVlas = new LinkedList<Double>();
			}
		}
		try {
			while(vals.)
			//save left
			while(reader.read()!='\n')
				reader.read(temp);
			for(int i=0 ; i<50 ; i++)
				left=(left+temp[i]);
			//clear
			for(int i=0 ; i<50 ; i++)
				temp[i]=0;
			//save right
			while(reader.read()!='\n')
				reader.read(temp);
			for(int i=0 ; i<50 ; i++)
				right=(right+temp[i]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vals;
	}
}
