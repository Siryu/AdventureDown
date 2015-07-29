package model.dungeon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import engine.AdventureDown;


/*
 * Room 001 = courtyard, Room 000 is the entrance to the building, 
 * Room ending in 97 is stairs from that floor to higher floor
 * Room ending in 98 is stairs from that floor to lower floor however, it increases to floor 1 from 0 etc.....
 */

public class Dungeon
{
	private int entranceFloor = 0;
	private Floor[] floor;
	
	public Dungeon()
	{
		floor = new Floor[5];
		this.floor[0] = new Floor();
		this.floor[1] = new Floor();
		this.floor[2] = new Floor();
		this.floor[3] = new Floor();
		this.floor[4] = new Floor();
	}

	public void save()
	{
		for(int i = 0; i < floor.length; i++)
		{
			BufferedWriter writer = null;
			File floorFile = new File("Files\\SaveFiles\\floor" + i + ".sav");
            File collisionFile = new File("Files\\SaveFiles\\floor" + i + ".col");
            
	        try
	        {
	            //create a temporary file
	            
	            writer = new BufferedWriter(new FileWriter(floorFile));
	            
	            int[][] tiles = floor[i].getTiles();
	            for(int w = 0; w < AdventureDown.MAPSIZE; w++)
	            {
	            	for(int z = 0; z < AdventureDown.MAPSIZE; z++)
	            	{
	            		writer.write("" + tiles[w][z] + ",");
	            	}
	            	writer.newLine();
	            }
	            
	            writer.write("" + floor[i].getNodes().size());
	            writer.newLine();
	            for(Node n : floor[i].getNodes())
	            {
	            	writer.write(n.getX() + "," + n.getY() + ",");
	            	if(n.getRoomConnection() != null)
	            	{
	            		writer.write(n.getRoomConnection().getName());
	            	}
	            	writer.newLine();
	            }	            
	        } catch (Exception e) {e.printStackTrace();}
	        finally 
	        {
	            try 
	            {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {}
	        }
	        
	        try
			{
				writer = new BufferedWriter(new FileWriter(collisionFile));
				
				int[][] tiles = floor[i].getCollision();
				for(int w = 0; w < AdventureDown.MAPSIZE; w++)
				{
					for(int z = 0; z < AdventureDown.MAPSIZE; z++)
					{
						writer.write("" + tiles[w][z] + ",");
					}
					writer.newLine();
				}
			} catch (IOException e)
			{
				System.err.println("error writing collision layer");
			}
	        finally 
	        {
	            try 
	            {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {}
	        }
	    }
	}
	
	public void load()
	{
		for(int i = 0; i < floor.length; i++)
		{
			BufferedReader reader = null;
			File floorFile = new File("Files\\SaveFiles\\floor" + i + ".sav");
			File collisionFile = new File("Files\\SaveFiles\\floor" + i + ".col");
			
	        try
	        {
	            //create a temporary file
	           
	            // This will output the full path where the file will be written to...
	            System.out.println(floorFile.getCanonicalPath());

	            reader = new BufferedReader(new FileReader(floorFile));
	            int[][] fullyLoadedTiles = new int[AdventureDown.MAPSIZE][AdventureDown.MAPSIZE];
	            int[] loadedTiles = new int[AdventureDown.MAPSIZE];
	            for(int k = 0; k < AdventureDown.MAPSIZE; k++)
	            {
	            	String aLine = reader.readLine();
		            for(int j = 0; j < AdventureDown.MAPSIZE; j++)
		            {
		            	int numberLength = aLine.indexOf(",");
		            	fullyLoadedTiles[k][j] = Integer.parseInt(aLine.substring(0, numberLength));
		            	aLine = aLine.substring(numberLength + 1);
		            }
	            }		
	            
	            floor[i].setTiles(fullyLoadedTiles);
	            
	            int nodeSize = Integer.parseInt(reader.readLine());
	            for(int m = 0; m < nodeSize; m++)
	            {
	            	String aLine = reader.readLine();
	            	int commaIndex = aLine.indexOf(",");
	            	int nodeX = Integer.parseInt(aLine.substring(0, commaIndex));
	            	aLine = aLine.substring(commaIndex + 1);
	            	commaIndex = aLine.indexOf(",");
	            	int nodeY = Integer.parseInt(aLine.substring(0, commaIndex));
	            	aLine = aLine.substring(commaIndex + 1);
	            	if(!aLine.isEmpty())
	            	{
	            		String roomC = aLine;
	            		floor[i].addRoom(roomC, nodeX, nodeY);
	            	}
	            	else
	            	{
	            		floor[i].addNode(new Node(nodeX, nodeY));
	            	}
	            }
	            
	        } catch (Exception e) {e.printStackTrace();}
	        finally 
	        {
	            try 
	            {
	                // Close the reader regardless of what happens...
	                reader.close();
	            } catch (Exception e) {}
	        }
	        
	        try
			{
				reader = new BufferedReader(new FileReader(collisionFile));
				int[][] fullyLoadedCollision = new int[AdventureDown.MAPSIZE][AdventureDown.MAPSIZE];
	            int[] loadedCollision = new int[AdventureDown.MAPSIZE];
	            for(int k = 0; k < AdventureDown.MAPSIZE; k++)
	            {
	            	String aLine = reader.readLine();
		            for(int j = 0; j < AdventureDown.MAPSIZE; j++)
		            {
		            	int numberLength = aLine.indexOf(",");
		            	fullyLoadedCollision[k][j] = Integer.parseInt(aLine.substring(0, numberLength));
		            	aLine = aLine.substring(numberLength + 1);
		            }
	            }		
	            
	            floor[i].setCollision(fullyLoadedCollision);
			}
	        catch (FileNotFoundException e)
			{
				System.err.println("error loading collision layer");
			}
	        catch (IOException e)
			{
	        	System.err.println("error loading collision layer");
			}
	        finally
	        {
	        	try
				{
					reader.close();
				} catch (IOException e)
				{
					System.err.println("error loading collision layer");
				}
	        }
           
	        
	    }
	}
	
	public Floor getFloor(int n)
	{
		return floor[n];
	}

	public void setFloor(Floor[] floor)
	{
		this.floor = floor;
	}

	public int getEntranceFloor()
	{
		return entranceFloor;
	}

	public void setEntranceFloor(int entranceFloor)
	{
		this.entranceFloor = entranceFloor;
	}
}
