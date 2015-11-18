import java.util.ArrayList;

import lejos.nxt.comm.RConsole;

public final class Navigation 
{
    private Navigation() 
	{ // private constructor
    }
	
	//returns a list of neighbouring cells that we think are traversable inside our map, this is usually called by findPath
	private static ArrayList<Node> GetAdjacentWalkableNodes(Node fromNode, int[][] ourMap)
	{
		ArrayList <Node> walkableNodes = new ArrayList<Node>();
			
		for (int x=-1;x<2;x++) {
			for (int y=-1;y<2;y++) {
				
				// not a neighbour, its the current tile
				if ((x == 0) && (y == 0)) {
					continue;
				}
					
				//no diagonal routes, just up, down, left, right considered
				if ((x != 0) && (y != 0)) {
					continue;
				}
				
				if((fromNode.x + x) < 6 && (fromNode.y + y) < 8) //if we are inside our 2d array
				{
					if((fromNode.x + x) >= 0 && (fromNode.y + y) >= 0)
					{
						if(ourMap[fromNode.x + x][fromNode.y + y] == 0) //and we do not think there is an object there
						{
							walkableNodes.add(new Node((fromNode.x + x), (fromNode.y + y))); //add it to nodes to be considered
						}
					}
				}
			}
		}				
		return walkableNodes; //return our candidates for neighbouring nodes to be considered
	}
	
	public static ArrayList<Node> findpath(Node start, Node end, int[][] ourMap) 
	{ 	
		ArrayList <Node> open = new ArrayList<Node>();  //initialize the open list
		ArrayList <Node> close = new ArrayList<Node>(); //initialize the closed list
		start.g = 0;
		start.f = start.g + Math.abs(start.x-end.x) + Math.abs(start.y-end.y); 		// Estimated total cost from start to goal through y.
		open.add(start); //put the starting node on the open list (you can leave its f at zero)	
	
		Node current = start;
        while(!open.isEmpty()) //while the open list is not empty
		{			
			if(open.size() > 100)
			{
				break;
			}				
			//find the node with the least f on the open list, call it "current"
			for(Node i : open) 
			{
				if(i.f <= current.f)
				{
					current = i;
				}
			}

			//if current is the goal, stop the search, make a list of the grid points that make up our route!
			if(current.x == end.x && current.y == end.y)
			{
				ArrayList <Node> steps = new ArrayList<Node>();
				while(current != null)
				{
					steps.add(current);
					current = current.parent;
				}	
				return steps;
			}	
				
			open.remove(current);//pop q off the open list
			close.add(current); //push current on the closed list 
				
			//generate current's 4 successors and set their parents to current
			//for each successor
			ArrayList<Node> nextNodes = GetAdjacentWalkableNodes(current, ourMap);

			for(Node i : nextNodes) 
			{	
				if (close.contains(i)) // Ignore the neighbor which is already evaluated.
				{
					continue;
				}	
														
				double tentative_g_score = current.g + 1; // length of this path.
				
				if (!open.contains(i))
				{
					open.add(i); // Discover a new node
				}						
				else if (tentative_g_score >= i.g) 
				{
					continue;		// This is not a better path.
				}

				// This path is the best until now. Record it!
				i.parent = current;
				i.g = tentative_g_score;
				i.h = Math.abs(end.x - i.x) + Math.abs(end.y - i.y); //distance from goal to successor
				i.f = i.g + i.h;
			}
        }
        RConsole.println("No route found!");
		return null;
	}

}
