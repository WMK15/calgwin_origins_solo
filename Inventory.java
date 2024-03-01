import java.util.ArrayList;
import java.util.Iterator;

/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds the inventory structure of the application.
 *  It contains the methods for adding and removing items from the inventory,
 *  and the constant for the max weight a player can hold.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class Inventory
{
    public final int MAX_WEIGHT = 15;
    
    private ArrayList<Item> invItems;
    private int weight;

    /**
     * Constructor for objects of class Inventory
     */
    public Inventory()
    {
        this.invItems = new ArrayList<Item>();
        this.weight = 0;
    }
    
    /**
     * @return player's inventory weight.
     */
    public int getWeight()
    {
        return this.weight;
    }
    
    /**
     * Set player's inventory weight.
     * 
     * @param weight
     */
    public boolean setWeight(int itemWeight, boolean add)
    {
        if (itemWeight < 1)
        {
            return false;
        }
        
        if (add)
        {
            if (itemWeight > 15) {
                System.out.println("You cannot pickup this item!");
                return false;
            }
            else if (this.weight + itemWeight > MAX_WEIGHT)
            {
                System.out.println("You do not have enough space in your inventory!");
                return false;
            }  
            this.weight += itemWeight;            
        } else
        {
            this.weight -= itemWeight;
        }
        return true;
    }
    
    /**
     * return playerItems   the playerItems ArrayList
     */
    public ArrayList<Item> getItems()
    {
        return this.invItems;
    }

    /**
     * show the items in the player's inventory.
     */
    public void showInventory()
    {
        // Check if the player has any items in their inventory.
        if (this.isEmpty()) {
            System.out.println("You have no items in your inventory!");
            return;
        }
        
        String inventoryMessage = "Your items:\n";
        for (Item item: invItems) {
            inventoryMessage += "- " + item.getName() + ": " + item.getDescription() + "\n";
        }
        
        inventoryMessage += "Inventory Weight:" + this.weight + "/" + MAX_WEIGHT;
        System.out.println(inventoryMessage);
    }
    
    /**
     * Checks if the current weight of the inventory is equal to its max weight.
     * 
     * @return  boolean value insinuating whether the inventory is full or not.
     */ 
    public boolean checkFull()
    {
        if (this.getWeight() == this.MAX_WEIGHT)
        {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return  whether or not if the playerItems ArrayList is empty.
     */
    public boolean isEmpty()
    {
        return this.invItems.isEmpty();
    }
    
    public Item getItem(String itemName)
    {
        return this.invItems
                    .stream()
                    .filter(item -> item.getName().trim().toLowerCase().equals(itemName))
                    .findFirst()
                    .orElse(null);
    }
    
    /**
     * Add the item to invItems.
     * 
     * @param item  Item to be added to invItems.
     */
    public boolean addItem(Item item)
    {
        if (this.checkFull())
        {
            return false;
        }
        
        if (this.setWeight(item.getWeight(), true))
        {
            this.invItems.add(item);
            return true;
        } else 
        {
            return false;
        }
    }
    
    /**
     * Remove the item from invItems.
     * 
     * @param item  Item to be removed from invItems.
     */
    public void removeItem(Item item)
    {
        if (this.setWeight(item.getWeight(), false))
        {
            int itemIndex = this.invItems.indexOf(item);
            this.invItems.remove(itemIndex);
        }
    }
}
