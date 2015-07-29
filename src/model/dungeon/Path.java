package model.dungeon;


import engine.AdventureDown;
import model.people.Person;

public class Path
{
	private int[][] pathing;
	private int[][] collision;
	
	public Path(Dungeon dungeon, Person person, Room room)
	{
		this.collision = dungeon.getFloor(person.getFloor()).getCollision();
		this.pathing = new int[AdventureDown.MAPSIZE][AdventureDown.MAPSIZE];
		
		int personStartX = (int)(person.getLocationX()/AdventureDown.MAPSIZE);
		int personStartY = (int)(person.getLocationY()/AdventureDown.MAPSIZE);
		pathing[personStartX][personStartY] = 1;
		
		
		//have it set up for stairs here
		int endX = (int)(room.getX()/AdventureDown.MAPSIZE);
		int endY = (int)(room.getY()/AdventureDown.MAPSIZE);
		pathing[endX][endY] = 100;
	}
	
	private void findValue()
	{
		
		int distanceValue = distanceToTarget() * 10;
	}
	
	private int distanceToTarget()
	{
		int distance = 0;
		for(int i = 0; i < pathing.length; i ++)
		{
			for(int j = 0; j < pathing.length; j++)
			{
				if(pathing[i][j] == 1)
				{
					for(int k = 0; k < pathing.length;k ++)
					{
						for(int m = 0; m < pathing.length; m++)
						{
							if(pathing[k][m] == 100)
							{
								int distanceX = Math.abs(i - k);
								int distanceY = Math.abs(j - m);
								distance = distanceX + distanceY;
							}
						}
					}
					break;
				}
			}
		}
		return distance;
	}
}
