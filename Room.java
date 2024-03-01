import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  A "Room" represents one location in the scenery of the game.  It is 
 *  connected to other rooms via exits.  For each existing exit, the room 
 *  stores a reference to the neighboring room. It contains items, NPCs
 *  and/or enemies.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */

public class Room 
{
    private String name;
    private String description;
    private HashMap<String, Room> exits;    // stores exits of this room.
    private ArrayList<Item> items;          // stores an array of items in this room.
    private ArrayList<NPC> NPCs;      // stores an array of NPCs in this room.
    private ArrayList<Enemy> enemies;
    
    private boolean bossRoom;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String name, String description, boolean bossRoom) 
    {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<Item>();
        this.NPCs = new ArrayList<NPC>();
        this.enemies = new ArrayList<Enemy>();
        this.bossRoom = bossRoom;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * @return The name of the room.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return this.description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are in " + description + ".\n"
                            + getRoomNPCsString() + "\n"
                            + getRoomItemsString()
                            + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit + ",";
        }
        return returnString;
    }
    
    /**
     * @return a list of NPCs (normal & enemies) that may be present in the
     *  room (as a String).
     */
    private String getRoomNPCsString()
    {
        String message = "";
        if (this.enemies.size() > 0)
        {
            message = "There are mobs here!";
        }
        else if (this.NPCs.size() == 0) {
            message = "There seems to be no one else here.";
        } else if (this.NPCs.size() >= 1) {
            message = "There's people here! Use 'talk' to speak with them.";
        }
        return message;
    }
    
    /**
     * @return a list of items that may be present in the room (as a String).
     */
    private String getRoomItemsString()
    {
        String message = "Items seen in this room:\n";
        if(this.items.size() == 0) {
            message = "";
        } else {
            for (Item item: this.items) {
                message += "- " + item.getName() + ":\n" + item.getDescription() + "\n";
            };
        }
        
        return message;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    public boolean isBossRoom()
    {
        return this.bossRoom ? true : false;
    }
    
    /**
     * get Items from the Room
     */
    public ArrayList<Item> getRoomItems()
    {
        return this.items;
    }
    
    /**
     * add the item to the room.
     * @param item  Item to be added to items ArrayList.
     */
    public void addRoomItem(Item item)
    {
        this.items.add(item);
    }
    
    /**
     * Remove the specified item from the room.
     * @param item  Item to be removed from the room.
     */
    public void removeRoomItem(Item item)
    {
        Iterator<Item> iterator = this.items.iterator();
        
        while (iterator.hasNext())
        {
            Item itemToRemove = iterator.next();
            if (item.getName().equals(itemToRemove.getName())) {
                iterator.remove();
            }
        }
    }
    
    
    /**
     * @return the NPCs in the current Room.
     */
    public ArrayList<NPC> getRoomNPCs()
    {
        return this.NPCs;
    }
    
    /**
     * add an NPC to this room.
     * @param npc   the NPC object to be added.
     */
    public void addRoomNPC(NPC npc)
    {
        this.NPCs.add(npc);   
    }
    
    /**
     * add an enemy to this room.
     * @param enemy   the enemy object to be added.
     */
    public void addRoomEnemy(Enemy enemy)
    {
        this.enemies.add(enemy);
    }
    
    /**
     * Conduct an event everytime a new room is entered:
     * 
     * @param player    the player that is currently in the room.
     */
    public boolean roomEvent(Player player)
    {
        // Check if the room has enemies. If yes, start conducting battles.
        boolean eventResult = true;
        if (!this.enemies.isEmpty())
        {
            Iterator<Enemy> iterator = this.enemies.iterator();
            
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();
                eventResult = enemy.enemyBattle(player, this);

                if (eventResult) {
                    iterator.remove(); // Removes the current enemy from the enemies list

                    // Remove defeated enemy from the NPCs list if present
                    if (this.NPCs.contains(enemy)) {
                        this.NPCs.remove(enemy);
                    }
                } else {
                    eventResult = false;
                }
            }
        
            // Check if this is a bossRoom and if player has beaten the boss.
            if (this.isBossRoom() && eventResult)
            {
                System.out.println("Congratulations! You were able to get out of the dungeon!");
                System.out.println("You won the game! You can do 'quit' to get out now.");
            }
        }
        
        return eventResult;
    }
}

