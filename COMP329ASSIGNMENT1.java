import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class COMP329ASSIGNMENT1
{
	DifferentialPilot pilot;
	OdometryPoseProvider opp = new OdometryPoseProvider(pilot); //pose provider now listens to pilot and updates accordingly
			
	TouchSensor leftBump = new TouchSensor(SensorPort.S2);
	TouchSensor rightBump = new TouchSensor(SensorPort.S1);
	UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
	
	ourMap = new int[100][100];	

	bool finished = false;
	//create ultrasonic sensor here
	  
	public void senseArea()
	{
		pilot.rotate(-90);
		sensorResult = 
		ourMap[x,y + 1] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = 
		ourMap[x - 1,y] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = 
		ourMap[x,y - 1] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = 
		ourMap[x + 1,y] = probabilityUpdate(sensorResult);					
	}
  
	//could we move this class to be computed on the pc and not on the device?
	public static void probabilityUpdate(int sensorResult)
	{
		//update map visual here also?
	  
	}
			
	public static void main(String[] args)
	{		    
		COMP329ASSIGNMENT1 robot = new COMP329ASSIGNMENT1(new DifferentialPilot(2.25f, 5.5f, Motor.B, Motor.C));
	  
		Button.ENTER.waitForPress();
		   
		//current location is [1,1], should we take heading here too?
		robot.opp.setPose(1, 1, float 0); 
		
		while(finished != true)
		{
			//spin around and test in the 4 nearby grids with the sensor
			robot.senseArea();
							
			//move forward
			//if we hit something with the bumpers, an object is definately there, 
			// back up a bit, turn 90 degrees and attempt to move around object?
			//conversely, if we move through without interuption, there must be nothing in our current location
					   
			//update current location depending on movement made		
			
			//should we check pose as well here and try and correct our heading?
			//opp.getHeading(); could be compared to a starting heading?
			
			if(//some completeness test  )
			{
				finished = true
			}
		}		
		System.exit(0);
	}	
}	
