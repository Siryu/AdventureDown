package model.dungeon;

import java.awt.Point;
import java.util.ArrayList;

import engine.AdventureDown;

public class Floor
{
	private final int PIXEL_SEARCH_RANGE = 100;
	private ArrayList<Node> nodes;
	private ArrayList<Room> rooms;
	private int[][] tiles;
	private int[][] collision;
	
	public Floor()
	{
		tiles = new int[AdventureDown.MAPSIZE][AdventureDown.MAPSIZE];
		collision = new int[AdventureDown.MAPSIZE][AdventureDown.MAPSIZE];
		nodes = new ArrayList<Node>();
		rooms = new ArrayList<Room>();
	}
	
	public void addNode(Node node)
	{
		checkForCloseNodesQ1(node, PIXEL_SEARCH_RANGE);
		/*if(closeNodeQ1 != null)
		{
			closeNodeQ1.addNode(node);
			node.addNode(closeNodeQ1);
		}*/
		checkForCloseNodesQ2(node, PIXEL_SEARCH_RANGE);
		/*if(closeNodeQ2 != null)
		{
			closeNodeQ2.addNode(node);
			node.addNode(closeNodeQ2);
		}*/
		checkForCloseNodesQ3(node, PIXEL_SEARCH_RANGE);
		/*if(closeNodeQ3 != null)
		{
			closeNodeQ3.addNode(node);
			node.addNode(closeNodeQ3);
		}*/
		checkForCloseNodesQ4(node, PIXEL_SEARCH_RANGE);
		/*if(closeNodeQ4 != null)
		{
			closeNodeQ4.addNode(node);
			node.addNode(closeNodeQ4);
		}*/		
		this.nodes.add(node);
		// temp for loop to check connections
		for(Node n : node.getConnections())
			System.out.println("Connection " + n);
	}
	// checks through each quadrant on a 2d map 300px out and will add a node from each quadrant around a freshly placed node
	private void checkForCloseNodesQ1(Node node, int searchRange)
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for(int i = 1; i < searchRange; i++)
		{
			for(int j = 1; j < searchRange; j++)
			{
				for(Node n : this.nodes)
				{
					if(node.getX() + i == n.getX() && node.getY() - j == n.getY())
					{
						nodeList.add(n);
					}
				}
			}
		}
		
		for(Node n : nodeList)
		{
			n.addNode(node);
			node.addNode(n);
		}
		
		//Node node1 = findClosestNode(nodeList, clickX, clickY);
		//return node1;
	}
	
	private void checkForCloseNodesQ2(Node node, int searchRange)
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for(int i = 1; i < searchRange; i++)
		{
			for(int j = 1; j < searchRange; j++)
			{
				for(Node n : this.nodes)
				{
					if(node.getX() + i == n.getX() && node.getY() + j == n.getY())
					{
						nodeList.add(n);
					}
				}
			}
		}
		
		for(Node n : nodeList)
		{
			n.addNode(node);
			node.addNode(n);
		}
		//Node node1 = findClosestNode(nodeList, clickX, clickY);
		//return node1;
	}

	
	private void checkForCloseNodesQ3(Node node, int searchRange)
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for(int i = 1; i < searchRange; i++)
		{
			for(int j = 1; j < searchRange; j++)
			{
				for(Node n : this.nodes)
				{
					if(node.getX() - i == n.getX() && node.getY() + j == n.getY())
					{
						nodeList.add(n);
					}
				}
			}
		}
		
		for(Node n : nodeList)
		{
			n.addNode(node);
			node.addNode(n);
		}
		//Node node1 = findClosestNode(nodeList, clickX, clickY);	
		//return node1;
	}
	
	private void checkForCloseNodesQ4(Node node, int searchRange)
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		
		for(int i = 1; i < searchRange; i++)
		{
			for(int j = 1; j < searchRange; j++)
			{
				for(Node n : this.nodes)
				{
					if(node.getX() - i == n.getX() && node.getY() - j == n.getY())
					{
						nodeList.add(n);
					}
				}
			}
		}
		
		for(Node n : nodeList)
		{
			n.addNode(node);
			node.addNode(n);
		}
		//Node node1 = findClosestNode(nodeList, clickX, clickY);
		//return node1;
	}
	
	// compares the nodes found in the quadrant and only returns the closest one.
	private Node findClosestNode(ArrayList<Node> nodeList, int clickX, int clickY)
	{
		Node node1 = null;
		
		if(nodeList.size() == 1)
		{
			node1 = nodeList.get(0);
		}
		else if(nodeList.size() >= 2)
		{	
			node1 = nodeList.get(0);
			for(Node n : nodeList)
			{
				Point clickPoint = new Point(clickX, clickY);
				Point p = new Point(n.getX(), n.getY());
				Point n1 = new Point(node1.getX(), node1.getY());
				if(clickPoint.distance(n1) <= clickPoint.distance(p))
				{
					node1 = n;
				}
			}
		}
		return node1;
	}
	
	public void addRoom(String roomName, int clickX, int clickY)
	{
		Node roomNode = new Node(clickX, clickY);
		Room room = new Room(roomName, roomNode);
		roomNode.setRoomConnection(room);
		this.addNode(roomNode);
		this.rooms.add(room);
	}
	
	
	public ArrayList<Node> getNodes()
	{
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes)
	{
		this.nodes = nodes;
	}

	public ArrayList<Room> getRooms()
	{
		return this.rooms;
	}

	public int[][] getTiles()
	{
		return tiles;
	}

	public void setTiles(int[][] tiles)
	{
		this.tiles = tiles;
	}
	
	public int[][] getCollision()
	{
		return collision;
	}
	
	public void setCollision(int[][] collision)
	{
		this.collision = collision;
	}
}
