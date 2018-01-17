package org.usfirst.frc.team3388.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.CameraServer;

public class CamerasHandler {
	UsbCamera cam1;
	UsbCamera cam2;
	VideoSink server;
	private boolean whichCam = false;
	public CamerasHandler() {
		cam1 = CameraServer.getInstance().startAutomaticCapture(RobotMap.CAM_PORT1);	
		//cam2 = CameraServer.getInstance().startAutomaticCapture(RobotMap.CAM_PORT2);
		server = CameraServer.getInstance().getServer(); 	
	}
	public void switchCamera()
	{
		if(whichCam)
			server.setSource(cam1);
		else
			server.setSource(cam2);
		whichCam = !whichCam;
	}
}
