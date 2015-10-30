import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;
import lejos.robotics.localization.OdometryPoseProvider;

public class ASSIGNMENT1
{
	protected UltrasonicSensor us;
	protected DifferentialPilot pilot;
	protected OdometryPoseProvider opp;
	int turnAngle;
	int locX, locY;
	int[][] ourMap;
	int[][] mapCount;
	static Boolean finished;
	
	ASSIGNMENT1()
	{
		pilot = new DifferentialPilot(2.25f, 5.5f, Motor.B, Motor.C);
		opp = new OdometryPoseProvider(pilot); //pose provider now listens to pilot and updates accordingly
		
		TouchSensor leftBump = new TouchSensor(SensorPort.S2);
		TouchSensor rightBump = new TouchSensor(SensorPort.S1);
		us = new UltrasonicSensor(SensorPort.S3);
		
		//colour sensor, could we set an even to fire when it detects a certain colour?
		
		//check if we have a compass as well
		
		ourMap = new int[6][8]; //create our grid map, each grid spot is 25cm by 25cm
		mapCount = new int[6][8]; //this array stores the number of times that grid location has been checked
		turnAngle = 250;
		locX = 1;
		locY = 1;
		
		for(int i = 0; i<6; i++)
		{
			for(int j = 0; j<8; j++)
			{
				ourMap[i][j] = 0;
				mapCount[i][j] = 0;
			}		
		}		
	}	
	    
	public void senseArea()
	{	
		int gridDistance;
		float sensorResult;
		//our heading is 0 at this point!
		
				//check left
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateLeft(gridDistance);
						
				//check ahead of us
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateFowards(gridDistance);
					
				//check to our immediate right
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateRight(gridDistance);
				
				Motor.A.rotate(-1440); //reset our sensor to point left again
		
		//rotate robot to face right
		pilot.rotate(turnAngle);
		
				//check left
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateFowards(gridDistance);
						
				//check ahead of us
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateRight(gridDistance);
					
				//check to our immediate right
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateBack(gridDistance);
				
				Motor.A.rotate(-1440); //reset our sensor to point left again

		//rotate to face backwards	
		pilot.rotate(turnAngle);
	
				//check left
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateRight(gridDistance);
						
				//check ahead of us
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateBack(gridDistance);
					
				//check to our immediate right
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateLeft(gridDistance);
				
				Motor.A.rotate(-1440); //reset our sensor to point left again

		//rotate to face left	
		pilot.rotate(turnAngle);
	
				//check left
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateBack(gridDistance);
						
				//check ahead of us
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateLeft(gridDistance);
					
				//check to our immediate right
				Motor.A.rotate(720);	
				sensorResult = us.getDistance();
				gridDistance = Math.round(sensorResult / 25);
				updateFowards(gridDistance);
				
				Motor.A.rotate(-1440); //reset our sensor to point left again
			
		//rotate back to forwards
		pilot.rotate(turnAngle);
		
		//might be worth trying to do a check here to see if we are really facing forwards
	}
  	
	public void updateFowards(int gridDistance)
	{
		mapCount[locX][locY + gridDistance]++;
		ourMap[locX][locY + gridDistance] = ((++ourMap[locX][locY + gridDistance] + mapCount[locX][locY + gridDistance]) / 2 * mapCount[locX][locY + gridDistance]);
		
		gridDistance--; 
		while(gridDistance > 0)
		{
			mapCount[locX][locY + gridDistance]++;
			ourMap[locX][locY + gridDistance] = ((--ourMap[locX][locY + gridDistance] + mapCount[locX][locY + gridDistance]) / 2 * mapCount[locX][locY + gridDistance]);
			gridDistance--; 
		}
	}
	
	public void updateLeft(int gridDistance)
	{
		mapCount[locX - gridDistance][locY]++;
		ourMap[locX - gridDistance][locY] = ((++ourMap[locX - gridDistance][locY] + mapCount[locX - gridDistance][locY]) / 2 * mapCount[locX - gridDistance][locY]);
		
		while(gridDistance > 0)
		{
			mapCount[locX - gridDistance][locY]++;
			ourMap[locX - gridDistance][locY] = ((--ourMap[locX - gridDistance][locY] + mapCount[locX - gridDistance][locY]) / 2 * mapCount[locX - gridDistance][locY]);
			gridDistance--; 
		}	
	}
	
	public void updateRight(int gridDistance)
	{
		mapCount[locX + gridDistance][locY]++;
		ourMap[locX + gridDistance][locY] = ((++ourMap[locX + gridDistance][locY] + mapCount[locX + gridDistance][locY]) / 2 * mapCount[locX + gridDistance][locY]);
		

		while(gridDistance > 0)
		{
			mapCount[locX + gridDistance][locY]++;
			ourMap[locX + gridDistance][locY] = ((--ourMap[locX + gridDistance][locY] + mapCount[locX + gridDistance][locY]) / 2 * mapCount[locX + gridDistance][locY]);
			gridDistance--;
		}
	}
	
	public void updateBack(int gridDistance)
	{		
		mapCount[locX][locY - gridDistance]++;
		ourMap[locX][locY - gridDistance] = ((++ourMap[locX][locY - gridDistance] + mapCount[locX][locY - gridDistance]) / 2 * mapCount[locX][locY - gridDistance]);
		
		gridDistance--; 
		while(gridDistance > 0)
		{
			mapCount[locX][locY - gridDistance]++;
			ourMap[locX][locY - gridDistance] = ((--ourMap[locX][locY - gridDistance] + mapCount[locX][locY - gridDistance]) / 2 * mapCount[locX][locY - gridDistance]);
			gridDistance--;
		}
	}
	
	//send an x, y location here and we can try to navigate the grid to take the robot to it.
	public void navigateTO()
	{
		//look up grid based navigation
		//http://cgi.csc.liv.ac.uk/~rmw/329/notes/lect12.pdf
		
		//might be worth always turning to the right first, then to the left as it finishes, try to cancel out the offset errors
		//move in our desired x location first, then y.
		
	    LCD.clear();
		LCD.drawString("One day,\nI would like to be\nable to drive somewhere\n:((((((((((((((((", 0, 0);
		Button.waitForAnyPress();
	}
	
	public void calibration()
	{
		LCD.clear();
		LCD.drawString("Set me at 0 degrees and press \nenter to attempt \na turn", 0, 0);
		Button.waitForAnyPress();
	    pilot.rotate(--turnAngle);	
	    LCD.clear();
		LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
		LCD.drawString("Angle is now:" + turnAngle , 0, 5);
		
		Button.waitForAnyPress();	
		int b = Button.readButtons();
		
		while(b != Button.ID_ENTER)
		{
			if(b == Button.ID_RIGHT)
			{
				turnAngle++;
			    LCD.clear();
				LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
				LCD.drawString("Angle is now:" + turnAngle , 0, 5);
			}	
			if(b == Button.ID_LEFT)
			{
				turnAngle--;
			    LCD.clear();
				LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
				LCD.drawString("Angle is now:" + turnAngle, 0, 5);
			}
			if(b == Button.ID_ESCAPE)
			{
				calibration();					
			}
			Button.waitForAnyPress();	
			b = Button.readButtons();
		}
		
		return;
	}
	
	public void updateMap()
	{
	    LCD.clear();
		for(int i = 0; i<6; i++)
		{
			for(int j = 0; j<8; j++)
			{	
				if(j == locY && i == locX)
				{
					LCD.drawString("X", locX + 1, locY);			
				}
				else
				{
					LCD.drawInt(ourMap[i][j], i + i, j);				
				}
			}		
		}	
	}
	
	public void isBlue()
	{
		
	}
	
	public static void main(String[] args)
	{	
		boolean finished = false;
		ASSIGNMENT1 robot = new ASSIGNMENT1();
	  		
		LCD.drawString("Hello!", 0, 0);
		LCD.drawString("Press enter to begin\ncalibration!", 0, 0);
		Button.ENTER.waitForPress();
					
		Motor.A.rotate(-720);	//rotate our sensor to face left
		robot.calibration();
			
	    LCD.clear();
		LCD.drawString("Calibration \nfinished!", 0, 0);
		LCD.drawString("Press enter\nto begin mapping!", 0, 1);
				
		Button.ENTER.waitForPress();
		   
		//after calibration is completed, our robot can choose between two tasks, moving or scanning
			
		while(finished != true)
		{
			robot.updateMap();

			//robot.navigateTO();
			
			//robot.senseArea();
					
			/*			
			if(//some completeness test  )
			{
				finished = true;
			}
			*/
		}			
		System.exit(0);
	}	
}	
