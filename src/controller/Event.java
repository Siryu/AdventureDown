package controller;

import model.people.PersonStatus;

public class Event
{
	private Name eventName;
	private int priority;
	private PersonStatus eventFor;
	
	public enum Name
	{
		BUYPEON;
	}
	
	public Event()
	{
		
	}
	
	public Event(Name name, int priority, PersonStatus eventFor)
	{
		this.setEventName(name);
		this.setPriority(priority);
		this.setEventFor(eventFor);
	}

	public Name getEventName()
	{
		return eventName;
	}

	public void setEventName(Name eventName)
	{
		this.eventName = eventName;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public PersonStatus getEventFor()
	{
		return eventFor;
	}

	public void setEventFor(PersonStatus eventFor)
	{
		this.eventFor = eventFor;
	}
}

