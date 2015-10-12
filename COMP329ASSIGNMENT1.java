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
	
	ourMap = new int[15][7];	

	bool finished = false;
	//create ultrasonic sensor here
	  
	public void senseArea()
	{
		//if we put probabilityUpdate on the pc could we have the pc work with and update the map in its own time while the robot moves around
		//again? we could use threads, one to sends data to and recieves visual data from probabilityUpdate on the pc, while the robot
		//deals with moving to the next location through another thread if so the routine would maybe be something like
		
		pilot.rotate(-90);
		sensorResult = us.getDistance();
		ourMap[x,y + 1] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = us.getDistance();
		ourMap[x - 1,y] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = us.getDistance();
		ourMap[x,y - 1] = probabilityUpdate(sensorResult);
		
		pilot.rotate(-90);
		sensorResult = us.getDistance();
		ourMap[x + 1,y] = probabilityUpdate(sensorResult);					
	}
  
	//could we move this class to be computed on the pc and not on the device?
	public static void probabilityUpdate(int sensorResult)
	{
		//distance could be the difference in pose 
		
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
			
			
			//move forward, we need to decide how to move, should we just move in a random direction avoiding definitely blocked locations?
			//if we hit something with the bumpers, an object is definitely there, 
			// back up a bit, turn 90 degrees and attempt to move around object?
			//conversely, if we move through without interuption, there must be nothing in our current location so set to 0
				

				
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
