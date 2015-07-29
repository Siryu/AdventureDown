package model.people;

import model.dungeon.Room;
import controller.DungeonMaster;

public class Adventurer extends Person
{

	public Adventurer(String name, DungeonMaster dm)
	{
		super(name, PersonStatus.ADVENTURER, dm);
	}

	@Override
	public void run()
	{
		for(Room r : this.dm.getDungeon().getFloor(0).getRooms())
		{
			if(r.getName().equals("051"))
			{
				this.setLocationX(r.getX());
				this.setLocationY(r.getY());
				this.setAtNode(r.getRoomNode());
				this.setFloor(0);
				this.setInDungeon(false);
			}
		}
		
		this.setInDungeon(true);
		
		try
		{
			this.sleep(1000 * 3);
		} 
		catch (InterruptedException e)
		{
			System.err.println("Error Sleeping thread " + this.getPersonName());
		}
		
		for(Room r : this.dm.getDungeon().getFloor(1).getRooms())
		{
			if(r.getName().equals("101"))
				this.headToPlace(r);
		}		
	}
}
