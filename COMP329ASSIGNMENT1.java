import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;
import lejos.robotics.localization.OdometryPoseProvider;
import java.util.ArrayList;

public class ASSIGNMENT1
{
	protected UltrasonicSensor us;
	protected DifferentialPilot pilot;
	protected OdometryPoseProvider opp;
	int turnAngle, locX, locY, gridSize;
	int[][] ourMap;   //shows how sure we are an object exists at each location
	int[][] mapCount; //how many times we have checked a location
	int[][] routeMap;	
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
		gridSize = 25;
		
		for(int i = 0; i<6; i++)
		{
			for(int j = 0; j<8; j++)
			{
				ourMap[i][j] = 0;
				mapCount[i][j] = 0;
				routeMap[i][j] = 0;
			}		
		}		
	}	
	    
	public void senseArea()
	{	
		float sensorMyLeft, sensorMyForwards, sensorMyRight;
		
		sensorMyLeft = Math.round(us.getDistance() / gridSize);		//check left		
		Motor.A.rotate(720);	
		sensorMyForwards = Math.round(us.getDistance() / gridSize);	//check ahead of us		
		Motor.A.rotate(720);	
		sensorMyRight = Math.round(us.getDistance() / gridSize);	//check  right
			
		Motor.A.rotate(-1440); //reset our sensor to point left again
		
		if(opp.heading == 0)
		{
			updateFowards(sensorMyForwards);
			updateRight(sensorMyRight);
			updateLeft(sensorMyLeft);
		}
		if(opp.heading == turnAngle)
		{
			updateFowards(sensorMyLeft);
			updateBack(sensorMyRight);
			updateRight(sensorMyForwards);
		}
		if(opp.heading == -turnAngle)
		{
			updateFowards(sensorMyRight);
			updateBack(sensorMyLeft);
			updateLeft(sensorMyForwards);
		}
		if(opp.heading == turnAngle * 2)
		{
			updateBack(sensorMyForwards);
			updateRight(sensorMyLeft);
			updateLeft(sensorMyRight);
		}
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
	
	public void findpath(Node start, Node end) 
	{ 	
		ArrayList <Node> open = new ArrayList<Node>();  //initialize the open list
		ArrayList <Node> close = new ArrayList<Node>(); //initialize the closed list
		Node current = new Node();
        open.add(start); 	//put the starting node on the open list (you can leave its f at zero)
		
        while(!open.isEmpty()) //while the open list is not empty
		{			
			current.f = 200;
			//find the node with the least f on the open list, call it "current"
			for(Node i : open) 
			{
				if(i.f <= current.f)
				{
					current = i;
				}
			}

			//if current is the goal, stop the search
			if(current.x == end.x && current.y == end.y)
			{
				return;
			}	
				
			open.remove(current);//pop q off the open list
			close.add(current); //push current on the closed list 
				
			//generate current's 4 successors and set their parents to current
			//for each successor
			ArrayList<Node> nextNodes = GetAdjacentWalkableNodes(current);
			Outer:
			for(Node i : nextNodes) 
			{	
				if (close.contains(i)) // Ignore the neighbor which is already evaluated.
				{
					continue Outer;
				}	
														
				double tentative_g_score = current.g + Math.sqrt(Math.pow(current.x - i.x, 2) + Math.pow(current.y - i.y, 2)); // length of this path.
				
				boolean contained = false;
				for(Node j : open) {
					if(j.x == i.x && j.y == i.y)
					{
						contained = true;
					}
				}
			
				if (contained == false)
				{
					open.add(i); // Discover a new node
				}
						
				else if (tentative_g_score >= i.g) 
				{
					continue Outer;		// This is not a better path.
				}

				// This path is the best until now. Record it!
				i.parent = current;
				i.g = tentative_g_score;
				i.h = Math.sqrt(Math.pow(end.x - i.x, 2) + Math.pow(end.y - i.y, 2)); //distance from goal to successor
				i.f = i.g + i.h;
												
				//just for drawing it out later
				if((i.x < 8) && (i.y < 8))
				{
					if((i.x >= 0) && (i.y >= 0))
					{
						routeMap[i.x][i.y] = i.f;
					}					
				}					
			}
        }		
		return;
	}
	
	//returns a list of neighbouring cells that we think are traversable
	private ArrayList<Node> GetAdjacentWalkableNodes(Node fromNode)
	{
		ArrayList <Node> walkableNodes = new ArrayList<Node>();
		if((fromNode.x - 1) < 8 && fromNode.y < 8)
		{
			if((fromNode.x - 1) >= 0 && fromNode.y >= 0)
			{
				if(ourMap[fromNode.x - 1][fromNode.y] == 0)
				{
					walkableNodes.add(new Node(fromNode.x - 1, fromNode.y));
				}		
			}
		}
		if((fromNode.x + 1) < 8 && fromNode.y < 8)
		{
			if((fromNode.x + 1) >= 0 && fromNode.y >= 0)
			{
				if(ourMap[fromNode.x + 1][fromNode.y] == 0)
				{
					walkableNodes.add(new Node(fromNode.x + 1, fromNode.y));
				}
			}
		}
		if(fromNode.x < 8 && (fromNode.y - 1) < 8)
		{
			if(fromNode.x >= 0 && (fromNode.y - 1) >= 0)
			{
				if(ourMap[fromNode.x][fromNode.y - 1] == 0)
				{
					walkableNodes.add(new Node(fromNode.x, fromNode.y - 1));
				}
			}
		}
		if(fromNode.x < 8 && (fromNode.y + 1) < 8)
		{
			if(fromNode.x >= 0 && (fromNode.y + 1) >= 0)
			{
				if(ourMap[fromNode.x][fromNode.y + 1] == 0)
				{
					walkableNodes.add(new Node(fromNode.x, fromNode.y + 1));
				}
			}
		}	
		
		return walkableNodes;
	}
	
	private void pickGoal()
	{
		
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
