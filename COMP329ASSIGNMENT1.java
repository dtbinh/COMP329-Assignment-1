import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class COMP329ASSIGNMENT1
{
	DifferentialPilot pilot;
  
	COMP329ASSIGNMENT1()
	{
		OdometryPoseProvider opp = new OdometryPoseProvider(pilot); //pose provider now listens to pilot and updates accordingly
				
		TouchSensor leftBump = new TouchSensor(SensorPort.S2);
		TouchSensor rightBump = new TouchSensor(SensorPort.S1);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		
		//colour sensor, could we set an even to fire when it detects a certain colour?
		
		//check if we have a compass as well
		
		ourMap = new int[10][10]; //create our grid map, each grid spot is 25cm by 25cm
		mapCount = new int[10][10]; //this array stores the number of times that grid location has been checked

		for(int i = 0; i<10; i++)
		{
			for(int j = 0; j<10; j++)
			{
				ourMap[i][j] = 0;
				mapCount[i][j] = 0;
			}		
		}
		
		bool finished = false;		
	}	
	    
	public void senseArea()
	{	
		int gridDistance;
		float sensorResult;
		
		//will probably need to add a try catch block incase we go out of range in this matrix's
		
		//check ahead of us
		sensorResult = us.getDistance();
		gridDistance = Math.round(sensorResult / 25);
		mapCount[x,y + gridDistance]++;
		ourMap[x,y + gridDistance] = ((++ourMap[x,y + gridDistance] + mapCount[x,y + gridDistance]) / 2 * mapCount[x,y + gridDistance]);
		
		gridDistance--; 
		for(gridDistance >= 0; gridDistance--)
		{
			mapCount[x,y + gridDistance]++;
			ourMap[x,y + gridDistance] = ((--ourMap[x,y + gridDistance] + mapCount[x,y + gridDistance]) / 2 * mapCount[x,y + gridDistance]);
		}
			
		//check to our immediate right
		pilot.rotate(-90);	
		sensorResult = us.getDistance();
		gridDistance = Math.round(sensorResult / 25);
		mapCount[x + gridDistance,y]++;
		ourMap[x + gridDistance,y] = ((++ourMap[x + gridDistance,y] + mapCount[x + gridDistance,y]) / 2 * mapCount[x + gridDistance,y]);
		
		gridDistance--; 
		for(gridDistance >= 0; gridDistance--)
		{
			mapCount[x + gridDistance,y]++;
			ourMap[x + gridDistance,y] = ((--ourMap[x + gridDistance,y] + mapCount[x + gridDistance,y]) / 2 * mapCount[x + gridDistance,y]);
		}
		
		//check behind us
		pilot.rotate(-90);	
		sensorResult = us.getDistance();
		gridDistance = Math.round(sensorResult / 25);
		mapCount[x,y - gridDistance]++;
		ourMap[x,y - gridDistance] = ((++ourMap[x,y - gridDistance] + mapCount[x,y - gridDistance]) / 2 * mapCount[x,y - gridDistance]);
		
		gridDistance--; 
		for(gridDistance >= 0; gridDistance--)
		{
			mapCount[x,y - gridDistance]++;
			ourMap[x,y - gridDistance] = ((--ourMap[x,y - gridDistance] + mapCount[x,y - gridDistance]) / 2 * mapCount[x,y - gridDistance]);
		}
		
		//check to our immediate left
		pilot.rotate(-90);	
		sensorResult = us.getDistance();
		gridDistance = Math.round(sensorResult / 25);
		mapCount[x - gridDistance,y]++;
		ourMap[x - gridDistance,y] = ((++ourMap[x - gridDistance,y] + mapCount[x - gridDistance,y]) / 2 * mapCount[x - gridDistance,y]);
		
		gridDistance--; 
		for(gridDistance >= 0; gridDistance--)
		{
			mapCount[x - gridDistance,y]++;
			ourMap[x - gridDistance,y] = ((--ourMap[x - gridDistance,y] + mapCount[x - gridDistance,y]) / 2 * mapCount[x - gridDistance,y]);
		}	
		
		pilot.rotate(-90);
	}
  	
	//send an x, y location here and we can try to navigate the grid to take the robot to it.
	public static void navigateTO()
	{
		//look up grid based navigation
		//http://cgi.csc.liv.ac.uk/~rmw/329/notes/lect12.pdf
		
		//might be worth always turning to the right first, then to the left as it finishes, try to cancel out the offset errors
		//move in our desired x location first, then y.
	}
	
	public static void calibration()
	{
		//do some rotations to improve reliability
	}
	
	public static void main(String[] args)
	{		    		
		COMP329ASSIGNMENT1 robot = new COMP329ASSIGNMENT1(new DifferentialPilot(2.25f, 5.5f, Motor.B, Motor.C));
	  
		Button.ENTER.waitForPress();
		   
		robot.calibration();

		Button.ENTER.waitForPress();
		   
		robot.senseArea();
   	   
		while(finished != true)
		{
			robot.navigateTO();
			
			robot.senseArea();
			
			//update map on device, send map to pc for display
			
			
			
			if(//some completeness test  )
			{
				finished = true
			}
		}		
				
		System.exit(0);
	}	
}	
