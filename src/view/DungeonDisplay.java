package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.dungeon.Node;
import model.dungeon.Room;
import model.people.NPC;
import model.people.Person;
import model.people.PersonStatus;
import controller.DungeonMaster;
import controller.Event;
import engine.AdventureDown;

public class DungeonDisplay
{
	private DungeonMaster dm;
	private int displayFloor = 0;
	private boolean isAddingNode = false;
	private boolean isAddingRoom = false;
	private boolean showCollision = false;
	private boolean isEditing = false;
	private boolean addCollision = false;
	private int changeTile;
	private int realMouseX;
	private int realMouseY;
	private int mouseX;
	private int mouseY;
	private int lastClickX;
	private int lastClickY;
	
	BufferedImage grassImage = null;
    BufferedImage dirtImage = null;
    BufferedImage roadImage = null;
    BufferedImage stairsDownImage = null;
    BufferedImage stairsUpImage = null;
	
	JPanel displayPanel;
	JButton[] partyButtons;
	
	public DungeonDisplay(DungeonMaster dm)
	{
		this.dm = dm;
		initDisplay();
		new Timer(1000 / AdventureDown.REFRESHRATE, redrawFloor).start();
	}

	public void initDisplay()
	{
		JFrame mainFrame = new JFrame("DungeonDown");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(1900, 1000));
		
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout());
		JMenu developerMenu = new JMenu("Developer Tools");
		JMenuItem addNodeMenuItem = new JMenuItem("Add Node");
		addNodeMenuItem.addActionListener(new addNodeListener());
		JMenuItem addRoomMenuItem = new JMenuItem("Add Room");
		addRoomMenuItem.addActionListener(new addRoomListener());
		JMenuItem editTilesMenuItem = new JMenuItem("Edit Map");
		editTilesMenuItem.addActionListener(new EditingListener());
		JMenuItem showCollisionMenuItem = new JMenuItem("Show Collision");
		showCollisionMenuItem.addActionListener(new ShowCollisionListener());
		JMenuItem exitEditingMenuItem = new JMenuItem("Exit Editing");
		exitEditingMenuItem.addActionListener(new exitEditingListener());
		developerMenu.add(addNodeMenuItem);
		developerMenu.add(addRoomMenuItem);
		developerMenu.add(editTilesMenuItem);
		developerMenu.add(showCollisionMenuItem);
		developerMenu.add(exitEditingMenuItem);
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveDungeonMenuItem = new JMenuItem("Save Dungeon");
		saveDungeonMenuItem.addActionListener(new SaveDungeonListener());
		JMenuItem savePeopleMenuItem = new JMenuItem("Save People");
		fileMenu.add(saveDungeonMenuItem);
		fileMenu.add(savePeopleMenuItem);
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
		bar.add(developerMenu);
		menuPanel.add(bar);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 900));
		controlPanel.setBackground(Color.DARK_GRAY);
		JButton upButton = new JButton("Up");
		upButton.setPreferredSize(new Dimension(70, 30));
		upButton.addActionListener(new upButtonListener());
		JButton downButton = new JButton("Down");
		downButton.setPreferredSize(new Dimension(70, 30));
		downButton.addActionListener(new downButtonListener());
		JButton hirePeonButton = new JButton("Hire Peon");
		hirePeonButton.setPreferredSize(new Dimension(150, 30));
		hirePeonButton.addActionListener(new HirePeonListener());
		
		controlPanel.add(upButton);
		controlPanel.add(downButton);
		controlPanel.add(hirePeonButton);
		
		File imageGrass = new File("Files\\Tiles\\se_free_grass_texture.jpg");
	    File imageDirt = new File("Files\\Tiles\\se_free_dirt_texture.jpg");
	    File imageRoad = new File("Files\\Tiles\\se_free_road_texture.jpg");
	    File imageStairsDown = new File("Files\\Tiles\\stairsDown.png");
	    File imageStairsUp = new File("Files\\Tiles\\stairsUp.png");
	    
	    try
	    {
	    	grassImage = ImageIO.read(imageGrass);
	    	dirtImage = ImageIO.read(imageDirt);
	    	roadImage = ImageIO.read(imageRoad);
	    	stairsDownImage = ImageIO.read(imageStairsDown);
	    	stairsUpImage = ImageIO.read(imageStairsUp);
	    } catch (IOException e)
	    {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
		
		
		displayPanel = new JPanel()
		{
		    @Override
		    public void paintComponent(Graphics g)
		    {
		       super.paintComponent(g);
		       int[][] tiles = dm.getDungeon().getFloor(displayFloor).getTiles();
		       for(int i = 0; i < AdventureDown.MAPSIZE; i++)
		       {
		    	   for(int j = 0; j < AdventureDown.MAPSIZE; j++)
		    	   {
		    		   if(tiles[j][i] == 0)
		    		   {
		    			   g.drawImage(grassImage, i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE, this);
		    		   }
		    		   else if(tiles[j][i] == 1)
		    		   {
		    			   g.drawImage(dirtImage, i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE, this);
		    		   }
		    		   else if(tiles[j][i] == 2)
		    		   {
		    			   g.drawImage(roadImage, i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE, this);
		    		   }
		    		   else if(tiles[j][i] == 97)
		    		   {
		    			   g.drawImage(stairsDownImage, i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE, this);
		    		   }
		    		   else if(tiles[j][i] == 98)
		    		   {
		    			   g.drawImage(stairsUpImage, i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE, this);
		    		   }
		    	   }
		       }
		       
		       if(dm.getDungeon().getFloor(displayFloor).getNodes() != null && (isAddingNode || isAddingRoom))
		       {
			       for(Node displayNode : dm.getDungeon().getFloor(displayFloor).getNodes())
			       {
			    	   g.setColor(Color.GREEN);
			    	   g.fillOval(displayNode.getX(), displayNode.getY(), 10, 10);
			       }
		       }
		       
		       if(dm.getDungeon().getFloor(displayFloor).getRooms() != null && (isAddingNode || isAddingRoom))
		       {
			       for(Room displayRoom : dm.getDungeon().getFloor(displayFloor).getRooms())
			       {
			    	   g.setColor(Color.BLUE);
			    	   g.fillOval(displayRoom.getX(), displayRoom.getY(), 10, 10);
			       }
		       }
		       
		       if(dm.getPeopleOnFloor(displayFloor) != null)
		       {
			       for(Person p : dm.getPeopleOnFloor(displayFloor))
			       {
			    	   if(p.isInDungeon())
			    	   {
			    		   g.setColor(Color.ORANGE);
			    		   g.fillOval(p.getLocationX(), p.getLocationY(), 32, 32);
			    	   }
			       }
		       }
		       
		       if(showCollision)
		       {
		    	   int[][] col = dm.getDungeon().getFloor(displayFloor).getCollision();
			       for(int i = 0; i < AdventureDown.MAPSIZE; i++)
			       {
			    	   for(int j = 0; j < AdventureDown.MAPSIZE; j++)
			    	   {
			    		   if(col[j][i] == 1)
			    		   {
			    			   g.setColor(Color.RED);
			    			   g.drawRect(i * AdventureDown.TILESIZE, j * AdventureDown.TILESIZE, AdventureDown.TILESIZE, AdventureDown.TILESIZE);
			    		   }
			    	   }
			       }
		       }
		       
		       if(isAddingNode || isAddingRoom)
		       {
			       g.setColor(Color.YELLOW);
			       g.setFont(new Font("Time", Font.BOLD, 24));
			       g.drawString("Editing", 20, displayPanel.getHeight() - 50);
			       g.drawString("Coordinates (" + mouseX + ",  " + mouseY + ")",  20, displayPanel.getHeight() - 20);
		       }
		       
		       else if(isEditing)
		       {
		    	   g.setColor(Color.WHITE);
		    	   g.drawRect((int)(realMouseX / AdventureDown.TILESIZE) * AdventureDown.TILESIZE, (int)(realMouseY / AdventureDown.TILESIZE) * AdventureDown.TILESIZE, 
		    			   AdventureDown.TILESIZE, AdventureDown.TILESIZE);
		       }
		    }
		};
		displayPanel.setPreferredSize(new Dimension(1600, 900));
		displayPanel.setBackground(Color.BLACK);
		displayPanel.addMouseListener(new displayMouseListener());
		displayPanel.addMouseMotionListener(new displayMouseListener());
		
		
		JPanel partyPanel = new JPanel();
		partyPanel.setPreferredSize(new Dimension(300, 900));
		partyPanel.setBackground(Color.DARK_GRAY);
		JButton party1 = new JButton();
		JButton party2 = new JButton();
		JButton party3 = new JButton();
		JButton party97 = new JButton();
		JButton party98 = new JButton();
		JButton collision = new JButton();
		
		partyButtons = new JButton[6];
		
		party1.setPreferredSize(new Dimension(295, 100));
		party1.addActionListener(new PartyButtonListener());
		partyButtons[0] = party1;
		party2.setPreferredSize(new Dimension(295, 100));
		party2.addActionListener(new PartyButtonListener());
		partyButtons[1] = party2;
		party3.setPreferredSize(new Dimension(295, 100));
		party3.addActionListener(new PartyButtonListener());
		partyButtons[2] = party3;
		party97.addActionListener(new PartyButtonListener());
		party97.setPreferredSize(new Dimension(295, 100));
		partyButtons[3] = party97;
		party98.setPreferredSize(new Dimension(295, 100));
		party98.addActionListener(new PartyButtonListener());
		partyButtons[4] = party98;
		collision.setPreferredSize(new Dimension(295, 100));
		collision.addActionListener(new CollisionButtonListener());
		partyButtons[5] = collision;
		
		for(JButton button : partyButtons)
		{
			partyPanel.add(button);
		}
		
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		mainPanel.add(controlPanel, BorderLayout.WEST);
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		mainPanel.add(partyPanel, BorderLayout.EAST);
		mainFrame.getContentPane().add(mainPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	private ActionListener redrawFloor = new ActionListener()
	{
		public void actionPerformed(ActionEvent evt)
		{
			displayPanel.repaint();
		}
	};
	
	private class upButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if(displayFloor > 0)
			{
				displayFloor -= 1;
			}
		}
	}
	
	private class downButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if(displayFloor < 4)
			{
				displayFloor += 1;
			}
		}
	}
	
	private class HirePeonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			dm.addEvent(new Event(Event.Name.BUYPEON, 0, PersonStatus.NPC));
		}
	}
	
	public String namePeon()
	{
		String peonName = JOptionPane.showInputDialog("Peon Name", "Enter the name of the new Peon");
		   
		File peonImageFile = new File("Files\\People\\Peon.jpg");
		BufferedImage peonImage = null;
		try
		{
			peonImage = ImageIO.read(peonImageFile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon peonImageIcon = new ImageIcon(peonImage);
		
		for(JButton b : partyButtons)
	    {
	    	if(b.getActionCommand().equals(""))
	    	{
	    		b.setIcon(peonImageIcon);
	    		b.setActionCommand(peonName);
	    		break;
	    	}
	    }
	    return peonName;
	}
	
	private class EditingListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			isEditing = true;
			isAddingNode = false;
			isAddingRoom = false;
			
			ImageIcon imageForZero = new ImageIcon(grassImage);
			partyButtons[0].setIcon(imageForZero);
			partyButtons[0].setActionCommand("0");
			
			ImageIcon imageForOne = new ImageIcon(dirtImage);
			partyButtons[1].setIcon(imageForOne);
			partyButtons[1].setActionCommand("1");
			
			ImageIcon imageForTwo = new ImageIcon(roadImage);
			partyButtons[2].setIcon(imageForTwo);
			partyButtons[2].setActionCommand("2");
			
			ImageIcon sDownImage = new ImageIcon(stairsDownImage);
			partyButtons[3].setIcon(sDownImage);
			partyButtons[3].setActionCommand("97");
			
			ImageIcon sUpImage = new ImageIcon(stairsUpImage);
			partyButtons[4].setIcon(sUpImage);
			partyButtons[4].setActionCommand("98");
			
			partyButtons[5].setText("Collision: Left Click on, Right Click off");
		}
	}
	
	private class ShowCollisionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			showCollision = !showCollision;
		}
	}
	
	private class CollisionButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			addCollision = !addCollision;
		}
	}
	
	private class PartyButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			if(isEditing)
			{
				if(arg0.getActionCommand() == "0")
				{
					changeTile = 0;
				}
				else if(arg0.getActionCommand() == "1")
				{
					changeTile = 1;
				}
				else if(arg0.getActionCommand() == "2")
				{
					changeTile = 2;
				}
				else if(arg0.getActionCommand() == "97")
				{
					changeTile = 97;
				}
				else if(arg0.getActionCommand() == "98")
				{
					changeTile = 98;
				}
			}
		}
	}
	
	private class addNodeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			isAddingNode = true;
			isAddingRoom = false;
			isEditing = false;
		}
	}
	
	private class addRoomListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			isAddingNode = false;
			isAddingRoom = true;
			isEditing = false;
		}
	}
	
	private class exitEditingListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			isAddingNode = false;
			isAddingRoom = false;
			isEditing = false;
		}
	}
	
	private class SaveDungeonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			dm.saveDungeon();
		}
	}
	
	private class displayMouseListener implements MouseListener, MouseMotionListener
	{

		@Override
		public void mouseClicked(MouseEvent e)
		{
			lastClickX = e.getX();
			lastClickY = e.getY();
			
			if(isAddingNode)
			{
				dm.getDungeon().getFloor(displayFloor).addNode(new Node(e.getX(), e.getY()));
			}	
			
			else if(isAddingRoom)
			{
				String roomName = JOptionPane.showInputDialog("Room Name", "enter the room name or number");
				
				if(roomName != null && roomName.length() > 0)
				{
					dm.getDungeon().getFloor(displayFloor).addRoom(roomName, e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if(addCollision)
			{
				int[][] tempCol = dm.getDungeon().getFloor(displayFloor).getCollision();
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					tempCol[(int)(e.getY() / AdventureDown.TILESIZE)][(int)(e.getX() / AdventureDown.TILESIZE)] = 1;
				}
				else if(e.getButton() == MouseEvent.BUTTON3)
				{
					tempCol[(int)(e.getY() / AdventureDown.TILESIZE)][(int)(e.getX() / AdventureDown.TILESIZE)] = 0;
				}
				dm.getDungeon().getFloor(displayFloor).setCollision(tempCol);
			}
			
			else if(isEditing)
			{
				int[][] tempTiles = dm.getDungeon().getFloor(displayFloor).getTiles();
				tempTiles[(int)(e.getY() / AdventureDown.TILESIZE)][(int)(e.getX() / AdventureDown.TILESIZE)] = changeTile;
				dm.getDungeon().getFloor(displayFloor).setTiles(tempTiles);
				if(changeTile == 98)
				{
					dm.getDungeon().getFloor(displayFloor).addRoom(displayFloor * 100 + "98", 
							(int)((e.getX() / AdventureDown.TILESIZE) * AdventureDown.TILESIZE) + (int)(AdventureDown.TILESIZE * 0.5),
							(int)((e.getY() / AdventureDown.TILESIZE) * AdventureDown.TILESIZE) + (int)(AdventureDown.TILESIZE * 0.5));							
				}
				else if(changeTile == 97)
				{
					dm.getDungeon().getFloor(displayFloor).addRoom(displayFloor * 100 + "97", 
							(int)((e.getX() / AdventureDown.TILESIZE) * AdventureDown.TILESIZE) + (int)(AdventureDown.TILESIZE * 0.5),
							(int)((e.getY() / AdventureDown.TILESIZE) * AdventureDown.TILESIZE) + (int)(AdventureDown.TILESIZE * 0.5));							
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			if(isEditing)
			{
				int[][] tempTiles = dm.getDungeon().getFloor(displayFloor).getTiles();
				tempTiles[(int)(e.getY() / AdventureDown.TILESIZE)][(int)(e.getX() / AdventureDown.TILESIZE)] = changeTile;
				dm.getDungeon().getFloor(displayFloor).setTiles(tempTiles);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			if(isAddingRoom || isAddingNode)
			{
				mouseX = e.getX() - lastClickX;
				mouseY = e.getY() - lastClickY;
			}		
			else if(isEditing)
			{
				realMouseX = e.getX();
				realMouseY = e.getY();
			}
		}
	}
}
