package controller;

import java.util.ArrayList;

import player.Player;
import view.DungeonDisplay;
import model.people.*;
import model.dungeon.Dungeon;

public class DungeonMaster
{
	public Player player;
	private PersonLibrary people;
	private Dungeon dungeon;
	private EventList events;
	private DungeonDisplay display;
	
	public DungeonMaster()
	{
		events = new EventList();
		player = new Player();
	}
	
	public void startProgram()
	{
		dungeon = new Dungeon();
		dungeon.load();
		//people.load();
		people = new PersonLibrary();
		display = new DungeonDisplay(this);
		NPC me = new NPC("SalesMan", this);
		Goblin hank = new Goblin("hank", this);
		people.add(me);
		me.start();
		people.add(hank);
		hank.start();
	}
	
	// this method will go through the list of Person's and return only those that are on the floor passed in
	public Person[] getPeopleOnFloor(int floor)
	{
		ArrayList<Person> floorPeople = new ArrayList<Person>();
		for(Person person : people)
		{
			if(person.getFloor() == floor)
			{
				floorPeople.add(person);
			}
		}
		return floorPeople.toArray(new Person[floorPeople.size()]);
	}
	
	public void saveDungeon()
	{
		dungeon.save();
	}
	
	public void addPerson(Person p)
	{
		people.addPersonAndSave(p);
	}
	
	// returns all the Person's in people
	public Person[] getPeople()
	{
		Person[] theseGuys = new Person[people.size()];
		for(int i = 0; i < people.size(); i++)
		{
			theseGuys[i] = people.get(i);
		}
		return theseGuys;
	}
	// person is updated with the one that is passed into this function
	public void updatePerson(Person p)
	{
		for(Person person : people)
		{
			if(person.getName().equals(p.getName()))
			{
				person = p;
				people.save();
				break;
			}
		}
	}
	
	public String buyPeon()
	{
		player.setGold(player.getGold() - 500);
		return display.namePeon();
	}
	
	// deletes the person out of the personLibrary who matches the person passed in
	public void deletePerson(Person p)
	{
		people.removePersonAndSave(p);
	}
	
	public Dungeon getDungeon()
	{
		return this.dungeon;
	}

	public Event[] getEvents()
	{
		return this.events.toArray(new Event[events.size()]);
	}
	
	public void addEvent(Event event)
	{
		this.events.add(event);
	}
	
	public void removeEvent(Event event)
	{
		this.events.remove(event);
	}
}
