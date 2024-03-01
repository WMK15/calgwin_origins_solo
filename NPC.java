import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds NPC (Non-playable character) structure. An NPC can be normal
 *  or an enemy (Enemy class is extended off of NPC class) and they may have
 *  dialogue options and/or items to reward the player with.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class NPC
{
    // instance variables - replace the example below with your own
    private String NPCName;
    private ArrayList<Item> NPCItems;
    
    // Eventing variables.
    private boolean interacted;
    private String firstMessage;
    private String secondMessage;
    
    private String[] dialogueOptions;
    private String[] responses;

    /**
     * Constructor for objects of class NPC
     */
    public NPC(String NPCName, String firstMessage, String secondMessage)
    {
        this.NPCName = NPCName;
        this.firstMessage = firstMessage;
        this.secondMessage = secondMessage;
        this.NPCItems = new ArrayList<Item>();
        
        this.interacted = false;
    }
    
    /**
     * @return NPCname The name of the NPC.
     */
    public String getNPCName()
    {
        return this.NPCName;
    }

    /**
     * @return NPCItem The item that the NPC is holding.
     */
    public ArrayList<Item> getNPCItems()
    {        
        return this.NPCItems;
    }
    
    /**
     * add an Item to NPCItems.
     * 
     * @param NPCItem   Item to add to the NPC.
     */
    public void addNPCItem(Item NPCItem)
    {
        this.NPCItems.add(NPCItem);
    }
    
    /**
     * Returns a message depending on if 'interacted' is true or false.
     * 
     * @return NPCMessage The message that the NPC gives when interacted with.
     */
    public String getMessage()
    {
        String message = this.NPCName + ": ";
        
        if (this.interacted)
        {
            message += this.secondMessage;
        } else {
            message += this.firstMessage;
        }
        
        return message;
    }
    
    /**
     * @return the interacted boolean.
     */
    public boolean getNPCInteracted()
    {
        return this.interacted; 
    }
    
    /**
     * Set the interacted boolean of this NPC to true;
     */
    public void setNPCInteracted()
    {
        this.interacted = true;
    }

    /**
     * @param dialogueOptions   The options given to the player to choose from.
     * @param responses         The responses given by the NPC according to the option chosen.
     */
    public void setDialogues(String[] dialogueOptions, String[] responses)
    {
        this.dialogueOptions = dialogueOptions;
        this.responses = responses;
    }
    
    /**
     * Iterates through each element in this.dialogueOptions and prints it out for the player.
     */
    private void showDialogueOptions()
    {
        for (int i = 0; i < this.dialogueOptions.length; i++) {
            System.out.println((i+1) + ". " + this.dialogueOptions[i]);
        }
    }
    
    /**
     * Gets the player's choice of dialogue option (1 or 2).
     * @return the number inputted by the player
     */
    private int getPlayerChoice() {
        Scanner dialogueScanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        while (!dialogueScanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            dialogueScanner.next();
        }
        return dialogueScanner.nextInt() - 1;
    }
    
    /**
     * Starts an NPC event
     * 
     * @param player    The player object.
     */
    public void startNPCEvent(Player player, Room currentRoom)
    {
        // If the player has already completed the event, print the second message.
        if (this.interacted) {
            System.out.println(this.getMessage());
            return;
        }
        
        // Print out intro message.
        System.out.println(this.getMessage());
        
        /* If there is a dialogue to be had, then the player will have
         * to choose correctly to get the item.*/
        if ((this.dialogueOptions != null && this.responses != null)
            && this.dialogueOptions.length == this.responses.length)
        {
            System.out.println("Options (Choose the number of the option): ");
            showDialogueOptions();
            int choice = getPlayerChoice();
            
            if (choice >= 0 && choice < dialogueOptions.length) {
                System.out.println(this.NPCName + ": " + responses[choice]);
                // If the NPC has an item to give, he will always give it if the player
                // chooses the first option.
                
                if (choice == 0 && this.NPCItems.size() > 0) {
                    givePlayerNPCItems(player, currentRoom);
                }
            } else {
                System.out.println("Invalid choice.");
                return;
            }
        /* Give the item to the player. */
        } else if (this.NPCItems.size() > 0) {
            givePlayerNPCItems(player, currentRoom);
        }
        
        this.setNPCInteracted();
    }
    
    /**
     * Give the player all the items that the NPC has.
     * If the player doesn't have space, drop the items in
     * the current room.
     * 
     * @param player    Player to give the items to.
     * @param currentRoom   The room that the NPC is in.
     */
    public void givePlayerNPCItems(Player player, Room currentRoom)
    {
        Inventory playerInv = player.getInventory();
        // Check if the player is able to store the item in their inventory.
        this.NPCItems.forEach(item -> {
            if (playerInv.addItem(item)) {
                // Update player stats if the item is non consumable. 
                if (!item.getIsConsumable())
                {
                    player.setHealth(player.getHealth() + item.getHealthUp());
                    player.setAttack(player.getAttack() + item.getAttackUp());
                    player.setDefense(player.getDefense() + item.getDefenseUp());
                }
            System.out.println("You have stored " + item.getName() + " in your inventory.");
            // Otherwise, drop it on the floor.
            } else {
                System.out.println("You do not have enough space in your inventory to store it!");
                currentRoom.addRoomItem(item);
                System.out.println("You've left the item on the room floor.\nYou can pick it up when you have enough space in your inventory.");
            }
        });
    }
}
