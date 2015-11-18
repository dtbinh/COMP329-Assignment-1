import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;
import lejos.robotics.localization.OdometryPoseProvider;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Object;
import javax.microedition.lcdui.Graphics;
import lejos.nxt.addon.ColorHTSensor;
import lejos.nxt.comm.RConsole;

//COMP329 ASSIGNMENT ONE
//Group: ROBOT05

//TODO
//check colour sensor port and set it in constructor
//update code to java comment standards
//test that opp.getHeading is always a multiple of turnAngle, it should be

public class ASSIGNMENT1
{
	protected UltrasonicSensor us;
	protected ColorHTSensor cmps;
	protected DifferentialPilot pilot;
	protected OdometryPoseProvider opp;
	int turnAngle, locX, locY, gridSize;
	int[][] ourMap;   //shows how sure we are an object exists at each location
	int[][] mapCount; //how many times we have checked a location
	
	ASSIGNMENT1()
	{
		pilot = new DifferentialPilot(2.25f, 5.5f, Motor.B, Motor.C);
		opp = new OdometryPoseProvider(pilot); //pose provider now listens to pilot and updates accordingly
		TouchSensor leftBump = new TouchSensor(SensorPort.S2);
		TouchSensor rightBump = new TouchSensor(SensorPort.S1);
		us = new UltrasonicSensor(SensorPort.S3);
		cmps = new ColorHTSensor(SensorPort.S3);
				
		ourMap = new int[6][8]; //create our grid map, each grid spot is 25cm by 25cm
		mapCount = new int[6][8]; //this array stores the number of times that grid location has been checked
		locX = 1;
		locY = 1;
		gridSize = 12;
			
		for(int i = 0; i<6; i++)
		{
			for(int j = 0; j<8; j++)
			{
				ourMap[i][j] = 0;
				mapCount[i][j] = 0;
			}		
		}		
	}	
	 
	//check around our robot to see what is around us, update our map accordingly	
	public void senseArea()
	{	
		int sensorMyLeft, sensorMyForwards, sensorMyRight;
					
		sensorMyLeft = Math.round(us.getDistance() / 24);		//check left		
		Motor.A.rotate(650);	
		sensorMyForwards = Math.round(us.getDistance() / 24);	//check ahead of us		
		Motor.A.rotate(650);	
		sensorMyRight = Math.round(us.getDistance() / 24);	//check  right
			
		Motor.A.rotate(-1300); //reset our sensor to point left again
					
		if(isBetween((int)opp.getPose().getHeading(), -1, 1))			
		{
			RConsole.println("I am facing forwards, about to update map");
			updateFowards(sensorMyForwards);
			updateRight(sensorMyRight);
			updateLeft(sensorMyLeft);
		}
		if(isBetween((int)opp.getPose().getHeading(), 89, 91))
		{
			RConsole.println("I am facing right, about to update map");
			updateFowards(sensorMyLeft);
			updateBack(sensorMyRight);
			updateRight(sensorMyForwards);
		}
		if(isBetween((int)opp.getPose().getHeading(), -91, -89))
		{
			RConsole.println("I am facing left, about to update map");
			updateFowards(sensorMyRight);
			updateBack(sensorMyLeft);
			updateLeft(sensorMyForwards);
		}
		if(isBetween((int)opp.getPose().getHeading(), 179, 181))
		{
			RConsole.println("I am facing backwards, about to update map");
			updateBack(sensorMyForwards);
			updateRight(sensorMyLeft);
			updateLeft(sensorMyRight);
		}
	}
	
	//returns true if the value is between min and max
	public static boolean isBetween(int value, int min, int max)
	{
	  return((value > min) && (value < max));
	}
  	
	public void updateFowards(int gridDistance)
	{
		int finalDistance = locY + gridDistance;
		if(finalDistance < 6)
		{
			RConsole.println("i spotted something (" + locX + "," + finalDistance + ")");
			mapCount[locX][finalDistance]++;
			ourMap[locX][finalDistance] = ((++ourMap[locX][finalDistance] + mapCount[locX][finalDistance]) / 2 * mapCount[locX][finalDistance]);
			
			finalDistance--; 
			while(finalDistance > 0)
			{
				mapCount[locX][finalDistance]++;
				ourMap[locX][finalDistance] = ((--ourMap[locX][finalDistance] + mapCount[locX][finalDistance]) / 2 * mapCount[locX][finalDistance]);
				finalDistance--; 
			}	
			updateMap();
		}
	}
	
	public void updateLeft(int gridDistance)
	{
		int finalDistance = locX - gridDistance;
		if(finalDistance >= 0)
		{
			RConsole.println("i spotted something at (" + finalDistance + "," + locY + ")");
			mapCount[finalDistance][locY]++;
			ourMap[finalDistance][locY] = ((++ourMap[finalDistance][locY] + mapCount[finalDistance][locY]) / 2 * mapCount[finalDistance][locY]);
			
			finalDistance--; 
			while(finalDistance > 0)
			{
				mapCount[finalDistance][locY]++;
				ourMap[finalDistance][locY] = ((--ourMap[finalDistance][locY] + mapCount[finalDistance][locY]) / 2 * mapCount[finalDistance][locY]);
				finalDistance--; 
			}	
			updateMap();
		}
	}
	
	public void updateRight(int gridDistance)
	{
		int finalDistance = locX + gridDistance;
		if(finalDistance < 6)
		{
			RConsole.println("i spotted something (" + finalDistance + "," + locY + ")");
			mapCount[finalDistance][locY]++;
			ourMap[finalDistance][locY] = ((++ourMap[finalDistance][locY] + mapCount[finalDistance][locY]) / 2 * mapCount[finalDistance][locY]);
			
			finalDistance--; 
			while(finalDistance > 0)
			{
				mapCount[finalDistance][locY]++;
				ourMap[finalDistance][locY] = ((--ourMap[finalDistance][locY] + mapCount[finalDistance][locY]) / 2 * mapCount[finalDistance][locY]);
				finalDistance--;
			}	
			updateMap();
		}
	}
	
	public void updateBack(int gridDistance)
	{		
		int finalDistance = locY - gridDistance;
		if(finalDistance >= 0)
		{
			RConsole.println("i spotted something (" + locX + "," + finalDistance + ")");
			mapCount[locX][finalDistance]++;
			ourMap[locX][finalDistance]= ((++ourMap[locX][finalDistance] + mapCount[locX][finalDistance]) / 2 * mapCount[locX][finalDistance]);
			
			finalDistance--; 
			while(finalDistance > 0)
			{
				mapCount[locX][finalDistance]++;
				ourMap[locX][finalDistance] = ((--ourMap[locX][finalDistance] + mapCount[locX][finalDistance]) / 2 * mapCount[locX][finalDistance]);
				finalDistance--;
			}
			updateMap();
		}
	}
		
	//this method simply updates the map on the robots LCD screen, displaying how certain we are
	//an object exists in each index location, we also show the robot location with an X
	public void updateMap()
	{
	    LCD.clear();
		for(int i = 0; i<6; i++)
		{
			for(int j = 7; j >= 0; j--)
			{
				if(j == locY && i == locX)
				{
					LCD.drawString("X", i + i + 3, j);	
				}
				else
				{
					LCD.drawInt(ourMap[i][j], i + i + 3, j);				
				}
			}		
		}	
	}
	
	//we pick a goal as a random location we havent checked 3 times, and return it
	private Node pickGoal()
	{		
		ArrayList <Node> possibleTargets = new ArrayList<Node>();
		for(int i = 1; i<6; i++)
		{
			for(int j = 7; j>=1; j--)
			{
				if(mapCount[i][j] < 3 && (i != locX && j != locY))
				{
					possibleTargets.add(new Node(i,j)); //if we havent checked it 3 times, add it to a list of possible nodes to check
				}
			}		
		}	
		
		if(possibleTargets.size() != 0)
		{
			Random random = new Random();
			return possibleTargets.get(random.nextInt(possibleTargets.size() - 0 + 1) + 0); //pick one of our possible nodes and select the goal
			
		} else {
			return null;
		}
	}
	
	//check if we are over a colour square, if we are we should check if our current location matches the set
	//location for that colour, if it does not, we need to recreate our route because we are not where we expect
	//returns true = we need to recreate a route, false = we are where we expect ourselves to be
	private boolean isColour()
	{
		int colour = cmps.getColorID();
		int newX, newY;
		switch (colour)
		{
			case 0: newX = 0; //red!
					newY = 0;
					RConsole.println("I am over red!");
					break;
			case 1: newX = 0; //green!
					newY = 0;
					RConsole.println("I am over green!");
					break;
			case 2: newX = 0; //blue!
					newY = 0;
					RConsole.println("I am over blue!");
					break;				
			default: return false;
		}
		if(newX == locX && newY == locY)
		{
			return false; //we are where we thought we are
		} 
		else
		{
			locX = newX;
			locY = newY;
			RConsole.println("I am not where I thought I was, about to pick new route");
			return true;  //we must have ended up somewhere wrong, update location and find new route
		}
	}
	
	//move through the list of nodes that makes up our route from current location to goal
	//at each step turn to face the right direction and check we can move to the next node
	private boolean letsMove(ArrayList <Node> steps, ASSIGNMENT1 robot)
	{
		//reverse our array, so we go from start goal as we move up array
	    for(int i = 0, j = steps.size() - 1; i < j; i++) {
	    	steps.add(i, steps.remove(j));
	    }
	    
	    /* Prints out the route to follow for debugging
	    for(int i = 0, j = steps.size() - 1; i < j; i++) {
	    	RConsole.println(steps.get(i).x + "," + steps.get(i).y);
	    }
	    */
	
		int desiredAngle;

	    for(int i = 1, j = steps.size() - 1; i < j; i++) 
	    {		    		    
			RConsole.println("Our next step is" + steps.get(i).x + "," + steps.get(i).y);
			//check in what direction the next node is compared to current
			//compare locX, locY with nextStep.x and nextStep.y
			if(locX == steps.get(i).x) //if we are on the same X
			{
				if(locY < steps.get(i).y) //and y is bigger we need to go forwards
				{
					desiredAngle = 0;
				} else {				
					desiredAngle = 180;  //we need to be at 180 degrees
				}
			} else {
				if(locX < steps.get(i).x)
				{
					desiredAngle = 90; //we need to be at 90 degrees
				} else { 
					desiredAngle =  270; //we need to be at -90 degrees
				}
			}				
			
			//turn to face the right direction if we are not already by subtracting our new angle from our old 
			pilot.rotate(((desiredAngle - opp.getPose().getHeading()) / 90) * turnAngle);	
			
			//use sensor to see if we can move forwards 25cm
			Motor.A.rotate(650);	
			int distance = us.getDistance();
			Motor.A.rotate(-650);
			
			opp.setPose(new Pose(1, 1, desiredAngle));	//we will keep pose as our desired angle so our map updates in the correct direction
			
			if(distance < 30)
			{
				ourMap[steps.get(i).x][steps.get(i).y] = 0;
				mapCount[steps.get(i).x][steps.get(i).y]++;
			    return false;
			} else {
				//if we can, move into the next grid spot, update location, remove this node from steps
				pilot.travel(gridSize); 
				locX = steps.get(i).x;
				locY = steps.get(i).y;
				ourMap[locX][locY] = 0; //because we entered this grid we are pretty certain nothing occupies it
				mapCount[locX][locY]++;		
			}		
			updateMap();
			robot.senseArea(); //lets sense the area around this spot
			
		}
		return true;
	}
	
	public static void main(String[] args)
	{	
		RConsole.open(); //open the console so we can print to our pc over bluetooth
		Motor.A.setSpeed(Motor.A.getMaxSpeed());

		boolean finished = false;
		ASSIGNMENT1 robot = new ASSIGNMENT1();
	  		
		LCD.drawString("Hello!", 0, 0);
		LCD.drawString("Press enter to \nbegin calibration!", 0, 0);
		Button.ENTER.waitForPress();
					
		Motor.A.rotate(-650);	//rotate our sensor to face left
		robot.turnAngle = Calibration.Calibrate(robot.pilot); //calibrate our turning angle so we can reliably turn 90 degrees in future
		robot.opp.setPose(new Pose(1, 1, 0)); //set heading to 0 so we know 0 is the starting direction
		   
		//after calibration is completed, our robot can choose between two tasks, moving or scanning
			
		while(finished != true)
		{
			robot.updateMap(); //update our map on the robot
			
			//send our updated map over bluetooth to our computer!
			Node goal = robot.pickGoal(); //pick a goal and create an end node for it
			
			if(goal == null) //we must have checked all locations at least three times, and are finished
			{
				finished = true; 
			}
						
			RConsole.println("Goal = (" + goal.x + "," + goal.y + ")");
			
		    //lets find the path to our chosen destination		
			ArrayList <Node> steps = Navigation.findpath(new Node(robot.locX, robot.locY), goal, robot.ourMap); 

			if(!robot.letsMove(steps, robot)) //lets try and move towards our goal!
			{
				//if letsMove returns false we must have run into an object or realised we were in the wrong 
				//location, lets just restart our behaviour and formulate a new route with our updated info
				RConsole.println("Lets move failed!");
				continue; 
			}				
			
			//we have hopefully arrived at our destination, lets sense the area
			robot.senseArea();					
		}
		Motor.A.rotate(650);	
		RConsole.println("Finished!");
		Button.ENTER.waitForPress();
		System.exit(0);		
	}	
}	
