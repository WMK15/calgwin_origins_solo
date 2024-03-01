import java.util.ArrayList;
import java.util.stream.Collectors;
/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds the player structure of the application.
 *  It contains the methods for the item commands, getters and setters for player
 *  stats and item command handler.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class Player
{
    private int pHealth;
    private int pAttack;
    private int pDefense;
    private Inventory inventory;
    
    private int pMaxHealth;

    /**
     * Constructor for objects of class Player
     */
    public Player(int playerHealth, int playerAttack, int playerDefense)
    {
        this.pHealth = playerHealth;
        this.pMaxHealth = playerHealth;
        this.pAttack = playerAttack;
        this.pDefense = playerDefense;
        this.inventory = new Inventory();
    }
    
    /**
     * @return the max health of the player.
     */
    public int getMaxHealth()
    {
        return this.pMaxHealth;
    }
    
    /**
     * @return the current health of the player.
     */
    public int getHealth()
    {
        return this.pHealth;
    }
    
    /**
     * Sets the current health of the player.
     * 
     * @param newHealth the value to be set as the player's new health.
     */
    public void setHealth(int newHealth)
    {
        this.pHealth = newHealth;
    }
    
    /**
     * @return  the current attack of the player.
     */
    public int getAttack()
    {
        return this.pAttack;
    }
    
    /**
     * Sets the current attack of the player.
     * 
     * @param newAttack the value to be set as the player's new attack.
     */
    public void setAttack(int newAttack)
    {
        this.pAttack = newAttack;
    }

    /**
     * @return  the current defense of the player.
     */    
    public int getDefense()
    {
        return this.pDefense;
    }
    
    /**
     * Sets the current defense of the player.
     * 
     * @param newDefense the value to be set as the player's new defense.
     */
    public void setDefense(int newDefense)
    {
        this.pDefense = newDefense;
    }    
    
    /**
     * get the inventory object of the player.
     * 
     * @return  the inventory object.
     */
    public Inventory getInventory()
    {
        return this.inventory;
    }
    
    /**
     * Print out the playre's stats.
     */
    public void showPlayerStats()
    {
        System.out.println("Health: " + this.pHealth + "/" + this.pMaxHealth
                            + "\nAttack: " + this.pAttack
                            + "\nDefense: " + this.pDefense);
    }
    
    /**
     * Method to pick up an item from the floor of the room that the player is in.
     * 
     * @param itemName   Name of the item to pick up
     * @param currentRoom   the current room where the player is in & 
     *                      the item (to be picked up) should exist.
     */
    private void pickUpItem(String itemName, Room currentRoom)
    {
        ArrayList<Item> roomItems = currentRoom.getRoomItems();
        if(itemName == null) {
            // if there is no second word, we don't which item to use.
            System.out.println("Use which item?");
            roomItems.forEach(item -> System.out.println("- " + item.getName()
                                                        + "\n" + item.getDescription()));
            return;
        }
        
        if (this.inventory.checkFull()) 
        {
            System.out.println("Your inventory is full!");
            return;
        }
        
        Item itemToPickUp = roomItems.stream()
                                    .filter(item -> item.getName().toLowerCase().equals(itemName))
                                    .findFirst()
                                    .orElse(null);
        if (itemToPickUp == null)
        {
            System.out.println("That item doesn't exist!");
            return;
        }
        
        if (this.inventory.addItem(itemToPickUp) != true)
        {
            return;
        }
        
        if (!itemToPickUp.getIsConsumable())
        {
            this.pHealth += itemToPickUp.getHealthUp();
            this.pAttack += itemToPickUp.getAttackUp();
            this.pDefense += itemToPickUp.getDefenseUp();
        }
        
        currentRoom.removeRoomItem(itemToPickUp);
        System.out.println(itemToPickUp.getName() + " added to inventory!");
    }

    /**
     * Method to use an item that is in the player's inventory.
     * 
     * @param itemName  The name of the item to used.
     */
    private void useItem(String itemName)
    {
        // Check if the inventory is empty.
        if (this.inventory.isEmpty()) {
            System.out.println("You do not have any items in your inventory.");
            return;
        }
        
        ArrayList<Item> consumableItems = new ArrayList<Item>(this.inventory
                                        .getItems()
                                        .stream()
                                        .filter(item -> item.getIsConsumable())
                                        .collect(Collectors.toList()));
        
        // Check if user has specified an item name.
        if(itemName == null) {
            // if there is no second word, we don't which item to use.
            System.out.println("Use which item?");
            for (Item item: this.inventory.getItems()) {
                if (item.getIsConsumable())
                {
                    System.out.println("- " + item.getName());
                }
            }
            return;
        }

        Item itemToUse = this.inventory.getItem(itemName);
        
        // Check if item exists in inventory.
        if (itemToUse == null)
        {
            System.out.println("This item does not exist in your inventory!");
            return;
        }
        
        // Check if item is consumable.
        if (!itemToUse.getIsConsumable()) {
            System.out.println("This item is not consumable!");
            return;
        }
        
        // Add the stats.
        // Check if the player's health is full.
        
        // Health-only item.
        if (itemToUse.getHealthUp() > 0
        && itemToUse.getAttackUp() == 0
        && itemToUse.getDefenseUp() == 0)
        {
            if (this.pHealth == this.pMaxHealth)
            {
                System.out.println("Your health is full!");
                return;
            } else {
                this.pHealth += itemToUse.getHealthUp();
                if (this.pHealth > this.pMaxHealth)
                {
                    this.pHealth = this.pMaxHealth;
                }
            }
        } else 
        {
            this.pHealth += itemToUse.getHealthUp();
            if (this.pHealth > this.pMaxHealth)
            {
                this.pHealth = this.pMaxHealth;
            }
        }
        
        this.pAttack += itemToUse.getAttackUp();
        this.pDefense += itemToUse.getDefenseUp();
        
        // Remove item from player's inventory.
        this.inventory.removeItem(itemToUse);
        
        // Print out what item the user has used, and update them on their new stats.
        System.out.println("You have used the item, " + itemName + "!");
        System.out.println("Your new stats:\nHealth: " + this.pHealth
                    + "\nAttack: " + this.pAttack
                    + "\nDefense: " + this.pDefense
                    + "\nWeight: " + this.inventory.getWeight() + "/"
                    + this.inventory.MAX_WEIGHT);
        return;
    }
    
    /**
     * Method to discard an item from the player's inventory, i.e.,
     * drop it on the floor of the current room.
     * 
     * @param itemName  The name of the item to discard.
     * @param currentRoom   The room that the player is in.
     */
    private void discardItem(String itemName, Room currentRoom)
    {
        // Check if the inventory is empty.
        if (this.inventory.isEmpty()) {
            System.out.println("You do not have any items in your inventory.");
            return;
        }
        
        // Check if user has specified an item name.
        if(itemName == null) {
            // if there is no second word, we don't which item to use.
            System.out.println("Discard which item?");
            for (Item item: this.inventory.getItems()) {
                System.out.println("- " + item.getName());
            }
            return;
        }
        
        Item itemToDiscard = this.inventory.getItem(itemName);
        
        // Check if item exists in inventory.
        if (itemToDiscard == null)
        {
            System.out.println("This item does not exist in your inventory!");
            return;
        }
        
        currentRoom.addRoomItem(itemToDiscard);
        this.inventory.removeItem(itemToDiscard);
        
        System.out.println("You've dropped " + itemToDiscard.getName() + " on the floor.");
    }
    
    /**
     * Item command handler
     * 
     * @param command   Used to get the subcommand word and item name.
     * @param parser    Used to call a method to check if the subCommand is valid.
     * @param currentRoom   passed to the 'item discard' & 'item pickup' commands.
     */
    public void itemCommand(Command command, Parser parser, Room currentRoom) {
        if (!command.hasSecondWord())
        {
            System.out.println("Specify an item command that you want to call.");
            parser.showItemCommands();
            return;
        }
        
        String subCommandWord = command.getSecondWord().trim().toLowerCase();
        
        if (!parser.isValidItemCommand(subCommandWord))
        {
            System.out.println("This is not a valid item command!");
            return;
        }
        
        String itemName = null;
        if (command.getThirdWord() != null)
        {
            itemName = command.getThirdWord().trim().toLowerCase();            
        }
        
        if (subCommandWord.equals("use")) {
            this.useItem(itemName);
        } else if (subCommandWord.equals("discard")) {
            this.discardItem(itemName, currentRoom);
        } else if (subCommandWord.equals("pickup")) {
            this.pickUpItem(itemName, currentRoom);
        }
    }
}
