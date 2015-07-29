package model.people;

import view.DungeonDisplay;
import controller.Event;
import model.dungeon.Room;
import controller.DungeonMaster;

public class NPC extends Person
{
	
	DungeonDisplay display;
	
	public NPC(String name, DungeonMaster dm)
	{
		super(name, PersonStatus.NPC, dm);
	}

	@Override
	public void run()
	{
		this.setFloor(0);
		
		for(Room r : dm.getDungeon().getFloor(0).getRooms())
		{
			if(r.getName().equals("050"))
			{
				this.setAtNode(r.getRoomNode());
				this.setLocationX(r.getX());
				this.setLocationY(r.getY());
			}
		}
		this.setInDungeon(true);
		
		while(true)
		{
			Event e = checkForEvent();
			
			if(e.getEventName() != null)
			{
				switch(e.getEventName())
				{
					case BUYPEON:
						buyPeon();
						dm.removeEvent(e);
						break;
					default:
						break;
				}
			}
		}
	}
	
	private void buyPeon()
	{
		for(Room r : dm.getDungeon().getFloor(0).getRooms())
		{
			if(r.getName().equals("051"))
			{
				this.headToPlace(r);
				
				String peonName = dm.buyPeon();
				Adventurer mike = new Adventurer(peonName, dm);
				dm.addPerson(mike);				
				
				for(Room r2 : dm.getDungeon().getFloor(0).getRooms())
				{
					if(r2.getName().equals("050"))
					{
						this.headToPlace(r2);
						break;
					}
				}
				break;
			}
		}
	}
}
