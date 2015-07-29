package model.people;

import java.awt.Point;
import java.util.ArrayList;

import model.dungeon.*;
import controller.DungeonMaster;
import controller.Event;


public abstract class Person extends Thread
{
	private String personName;
	private int HP;
	private int maxHP;
	private int speed;
	private int XP;
	private int level;
	private Personality personality;
	private Priority tasks;
	private int locationX;
	private int locationY;
	private int floor;
	private boolean inDungeon;
	private Node atNode;
	private PersonStatus pStatus;
	
	protected DungeonMaster dm;
	
	private static final int UPDATE_RATE = 30;
	private static final int MOVE_SPEED = 3;
	
	public Person(String name, PersonStatus pStatus, DungeonMaster dm)
	{
		this.setInDungeon(false);
		this.setPersonName(name);
		this.setpStatus(pStatus);
		this.dm = dm;
	}

	public abstract void run();
	
	// this will set the path for the Person to follow and set them on their way.
	protected void headToPlace(Room roomName)
	{
		Room room = null;
		for(Room r : dm.getDungeon().getFloor(Integer.parseInt(roomName.getName()) / 100).getRooms())
		{
			if(r.getName().equals(roomName.getName()))
			{
				room = r; 
			}
		}
		this.MoveBetweenFloors(roomName);
		boolean foundPath = false;
		ArrayList<Node> pathway = new ArrayList<Node>();
		ArrayList<Node> connections = new ArrayList<Node>();
		Node lastNode = null;
		Node goodNode = null;
		pathway.add(room.getRoomNode());
		for(Node n : room.getRoomNode().getConnections())
		{
			connections.add(n);
		}
		goodNode = connections.get(0);
		
		while(!foundPath)
		{
			// if there are 2 connections, choose the one you didn't just come from
			if(connections.size() == 2)
			{
				for(Node n : connections)
				{
					if(!n.equals(lastNode))
					{
						goodNode = n;
					}
				}
			}
			
			//with more than 2 connections, will choose the one you didn't just come from +
			//won't choose to go into a room unless it's the final destination. +
			//if there's more than that it will find the closest distance node.
			else if(connections.size() > 2)
			{			
				for(Node n : connections)
				{
					if(!n.equals(lastNode) && (n.getRoomConnection() == null || n.getRoomConnection().equals(this.getAtNode().getRoomConnection())))
					{
						if(goodNode == null)
						{
							goodNode = n;
						}
						else
						{
							Point pPoint = new Point(this.locationX, this.locationY);
							Point nPoint = new Point(n.getX(), n.getY());
							Point gnPoint = new Point(goodNode.getX(), goodNode.getY());
							
							if(pPoint.distance(nPoint) <= pPoint.distance(gnPoint))
							{
								goodNode = n;
							}
						}
					}
				}
			}
			
			connections = goodNode.getConnections();
			lastNode = pathway.get(pathway.size() - 1);
			pathway.add(goodNode);
			if(goodNode.getRoomConnection() != null && goodNode.getRoomConnection().equals(this.getAtNode().getRoomConnection()))
			{
				foundPath = true;
			}
			goodNode = null;
		}
		ArrayList<Node> backwards = new ArrayList<Node>();
		for(int i = pathway.size() - 1; i >= 0; i--)
		{
			backwards.add(pathway.get(i));
		}
		move(backwards);
	}
	
	private void move(ArrayList<Node> pathway)
	{
		for(Node n : pathway)
		{
			this.setAtNode(n);
			this.headToPoint(new Point(n.getX(), n.getY()));
			
		}
		
		for(Person p : dm.getPeople())
		{
			if(p.getPersonName().equals(this.getPersonName()))
			{
				// this is where  you want to set any people to rest once they hit the room.
			}
		}
		
	}

	
	// goes to position incrementally, and exits once Person reaches that Point
	private void headToPoint(Point spot)
	{
		boolean personThere = false;
		while(!personThere)
		{
			if(spot.y > this.getLocationY())
			{
				if(spot.y - this.getLocationY() < 5)
				{
					this.setLocationY(this.getLocationY() + 1);
				}
				else
				{
				this.setLocationY(this.getLocationY() + MOVE_SPEED);
				}
			}
			else if(spot.y < this.getLocationY())
			{
				if(spot.y - this.getLocationY() < 5)
				{
					this.setLocationY(this.getLocationY() - 1);
				}
				else
				{
				this.setLocationY(this.getLocationY() - MOVE_SPEED);
				}
			}
			
			if(spot.x > this.getLocationX())
			{
				if(spot.x - this.getLocationX() < 5)
				{
					this.setLocationX(this.getLocationX() + 1);
				}
				else
				{
				this.setLocationX(this.getLocationX() + MOVE_SPEED);
				}
			}
			else if(spot.x < this.getLocationX())
			{
				if(spot.x - this.getLocationX() < 5)
				{
					this.setLocationX(this.getLocationX() - 1);
				}
				else
				{
				this.setLocationX(this.getLocationX() - MOVE_SPEED);
				}
			}
			
			// used to pause between each movement update
			try 
			{
	            Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
	        } 
			catch (InterruptedException ex) { }
			
			// this determines if the Person is at the spot they designate
			if(((int)this.getLocationY() == (int)spot.y) && ((int)this.getLocationX() == (int)spot.x))
			{
				System.out.println("at spot indicated " + this.getPersonName());
				personThere = true;
			}
		}
	}
	
	private void MoveBetweenFloors(Room room)
	{		
		while(this.getFloor() != Integer.parseInt(room.getName()) / 100)
		{
			if(Integer.parseInt(room.getName()) / 100 > this.getFloor())
			{
				String stairsUp = "" + ((this.getFloor() * 100) + 97);
				if(Integer.parseInt(stairsUp) / 100 == 0)
				{
					stairsUp = "097"; 
				}

				for(Room r : dm.getDungeon().getFloor(this.getFloor()).getRooms())
				{
					if(r.getName().equals(stairsUp))
					{
						this.headToPlace(r);
						this.setFloor(this.getFloor() + 1);
						stairsUp = "" + ((this.getFloor() * 100) + 98);
						for(Room r2 : dm.getDungeon().getFloor(this.getFloor()).getRooms())
						{
							if(r2.getName().equals(stairsUp))
							{
								this.setLocationX(r2.getX());
								this.setLocationY(r2.getY());
								this.setAtNode(r2.getRoomNode());
							}
						}
					}
				}
			}
			else if(Integer.parseInt(room.getName()) / 100 < this.getFloor())
			{
				String stairsDown = "" + ((this.getFloor() * 100) + 98);
				for(Room r : dm.getDungeon().getFloor(this.getFloor()).getRooms())
				{
					if(r.getName().equals(stairsDown))
					{
						this.headToPlace(r);
						this.setFloor(this.getFloor() - 1);
						stairsDown = "" + ((this.getFloor() * 100) + 97);
						if(Integer.parseInt(stairsDown) / 100 == 0)
						{
							stairsDown = "098"; 
						}
						for(Room r2 : dm.getDungeon().getFloor(this.getFloor()).getRooms())
						{
							if(r2.getName().equals(stairsDown))
							{
								this.setLocationX(r2.getX());
								this.setLocationY(r2.getY());
								this.setAtNode(r2.getRoomNode());
							}
						}
					}
				}
			}
		}
	}
	
	protected Event checkForEvent()
	{
		Event returnEvent = new Event();
		
		if(dm.getEvents().length > 0)
		{
			for(Event e : dm.getEvents())
			{
				if(e.getEventFor() == this.pStatus)
				{
					if(returnEvent.getEventName() == null)
					{
						returnEvent = e;
					}
					else if(e.getPriority() > returnEvent.getPriority())
					{
						returnEvent = e;
					}
				}
			}
		}
		return returnEvent;
	}

	public String getPersonName()
	{
		return personName;
	}

	public void setPersonName(String name)
	{
		this.personName = name;
	}

	public int getLocationX()
	{
		return locationX;
	}

	public void setLocationX(int locationX)
	{
		this.locationX = locationX;
	}

	public int getLocationY()
	{
		return locationY;
	}

	public void setLocationY(int locationY)
	{
		this.locationY = locationY;
	}

	public int getFloor()
	{
		return floor;
	}

	public void setFloor(int floor)
	{
		this.floor = floor;
	}

	public boolean isInDungeon()
	{
		return inDungeon;
	}

	public void setInDungeon(boolean inDungeon)
	{
		this.inDungeon = inDungeon;
	}

	public Node getAtNode()
	{
		return atNode;
	}

	public void setAtNode(Node atNode)
	{
		this.atNode = atNode;
	}
	
	private void setpStatus(PersonStatus pStatus)
	{
		this.pStatus = pStatus;
	}

	public PersonStatus getpStatus()
	{
		return pStatus;
	}

	public int getHP()
	{
		return HP;
	}

	public void setHP(int hP)
	{
		HP = hP;
	}

	public int getMaxHP()
	{
		return maxHP;
	}

	public void setMaxHP(int maxHP)
	{
		this.maxHP = maxHP;
	}

	public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public int getXP()
	{
		return XP;
	}

	public void setXP(int xP)
	{
		XP = xP;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public Personality getPersonality()
	{
		return personality;
	}

	public void setPersonality(Personality personality)
	{
		this.personality = personality;
	}

	public Priority getTasks()
	{
		return tasks;
	}

	public void setTasks(Priority tasks)
	{
		this.tasks = tasks;
	}
}
