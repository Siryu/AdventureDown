package engine;

import controller.DungeonMaster;

public class AdventureDown
{
	public static int REFRESHRATE = 30;
	public static int TILESIZE = 64;
	public static int MAPSIZE = 30;

	public static void main(String[] args)
	{
		DungeonMaster dm = new DungeonMaster();
		dm.startProgram();
	}

}
