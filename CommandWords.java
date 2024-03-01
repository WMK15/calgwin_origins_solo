/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds an enumeration of all command words known to the game.
 *  It is used to recognise commands as they are typed in.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class CommandWords
{
    // a constant array that holds all valid command words
    private static final String[] validCommands = {
        "go", "back", "talk", "inventory", "item", "help", "quit", "stats"
    };
    
    // a constant array that holds all valid commands during battle.
    private static final String[] battleCommands = {
        "help", "attack", "item", "inventory", "stats"
    };
    
    // a constant array that holds all valid subcommands for the item command.
    private static final String[] itemCommands = {
        "pickup", "use", "discard"
    };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        
        for(int i = 0; i < battleCommands.length; i++) {
            if(battleCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll() 
    {
        for(String command: validCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
    
    /**
     * Print all valid battle commands to System.out.
     */
    public void showBattle() 
    {
        for(String command: battleCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
    
    /**
     * Check whether a given String is a valid item command word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isItemCommand(String aString)
    {
        for(int i = 0; i < itemCommands.length; i++) {
            if(itemCommands[i].equals(aString))
                return true;
        }
        return false;
    }
    
    /**
     * Print all valid item commands to System.out.
     */
    public void showItem() 
    {
        for(String command: itemCommands) {
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
