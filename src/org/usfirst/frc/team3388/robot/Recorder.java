package org.usfirst.frc.team3388.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Vector;

public class Recorder {
	
	public Vector<Frame> frames;
	File file;
	public static final String FILE_PATH = "/home/lvuser/recorder/recored.csv";
	public static final int PERIOD = 20;
	
	public Recorder()
	{	
		frames = new Vector<Frame>();
	}
	
	public void addFrame(Frame f)
	{
		frames.addElement(f);
	}
	public void clear()
	{
		frames.clear();
	}
	public void loadFile()
	{
		loadFile(FILE_PATH);	
		System.out.println("file loaded");
	}
	
	public void loadFile(String path)
	{
		clear();
		BufferedReader file;
		try {
			file = new BufferedReader(new FileReader(path));
			for(String line = file.readLine(); line != null; line = file.readLine())
			{
				String[] data = line.split(",");
				frames.add(new Frame(data));
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void toFile()
	{
		toFile(FILE_PATH);
		System.out.println("file saved");
	}
	public void toFile(String path)
	{
		try {
			PrintWriter writer = new PrintWriter(path);
			writer.print("");
			writer.close();
			writer = new PrintWriter(path);
			for(Frame f : frames)
			{
				writer.print(((f.leftVal) + "," + f.rightVal +","+f.poleVal+","+f.liftVal+","
						+f.rotateVal+","+f.pistonVal+"\n"));
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clear();
	}
	
}
