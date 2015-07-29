package model.people;

import controller.DungeonMaster;
import model.dungeon.Room;

public class Goblin extends Person
{

	public Goblin(String name, DungeonMaster dm)
	{
		super(name, PersonStatus.GOBLIN, dm);
	}

	@Override
	public void run()
	{
		for(Room r : this.dm.getDungeon().getFloor(0).getRooms())
		{
			if(r.getName().equals("000"))
			{
				this.setLocationX(r.getX());
				this.setLocationY(r.getY());
				this.setAtNode(r.getRoomNode());
				this.setFloor(0);
				this.setInDungeon(false);
			}
		}
		
		this.setInDungeon(true);
		for(Room r : this.dm.getDungeon().getFloor(1).getRooms())
		{
			if(r.getName().equals("101"))
				this.headToPlace(r);
		}
	/*	while(true)
		{
			
		}*/
		
	}
}
