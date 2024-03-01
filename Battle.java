import java.util.ArrayList;

/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds the battle system of the application.
 *  It checks if the battle is the final boss battle, processes all battle commands,
 *  and has the structure for the player's turn and enemy's turn.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class Battle
{
    private Enemy enemy;
    private boolean battleWon;
    private Player player;
    private Parser battleParser;
    private Room currentRoom;
    
    private String enemyName;
    /**
     * Constructor for objects of class Battle
     */
    public Battle(Enemy enemy, Player player, Room currentRoom)
    {
        this.enemy = enemy;
        this.player = player;
        battleParser = new Parser();
        battleWon = false;
        this.currentRoom = currentRoom;
        
        // Set enemy's name for easier use.
        enemyName = this.enemy.getNPCName();
    }
    
    /**
     * It starts the battle between the player and the enemy.
     * 
     * @param bossBattle    A boolean used to check if the battle is a boss battle.
     * @return  true if the battle has ended successfully, i.e., the player has
     * defeated the enemy.
     */
    public boolean commence(boolean bossBattle)
    {
        System.out.println();
        System.out.println(bossBattle ?
        "=== BATTLE START ===\nTHIS IS THE FINAL BOSS!" : "=== BATTLE START ===");
        
        while (this.player.getHealth() > 0 && !this.battleWon)
        {
            boolean turnOver = false;
            while (!turnOver)
            {
                System.out.println("Your health: " + this.player.getHealth());
                System.out.println("What would you like to do?");
                printBattleHelp();
                Command command = battleParser.getCommand();
                turnOver = processBattleCommand(command);
            }
            if (!this.enemy.getIsDead())
            {
                enemyTurn();
            } else {
                this.battleWon = true;
            }
        }
        
        // if this.battleWon is not true that means the player has fainted.
        if (!this.battleWon)
        {
            System.out.println("You have fainted!");
            this.player.setHealth(this.player.getMaxHealth());
            return false;
        }
        
        // Otherwise, the player has won the battle and gets rewarded.
        this.enemy.setNPCInteracted();
        System.out.println(this.enemy.getMessage());
        System.out.println("You have succesfully defeated the " + enemyName + "!");
        
        // Reward the player with the NPC items.
        this.enemy.givePlayerNPCItems(this.player, this.currentRoom);
        
        System.out.println("=== BATTLE END ===");
        return true;
    }
    
    /**
     * Command handler for battles
     * @param command   used to call particular commands.
     */
    private boolean processBattleCommand(Command command) 
    {
        boolean turnOver = false;
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...\nYou can use 'help' for battle commands.");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printBattleHelp();
        } else if (commandWord.equals("attack")) {
            attackEnemy();
            turnOver = true;
        } else if (commandWord.equals("inventory")) {
            this.player.getInventory().showInventory();
        
        } else if (commandWord.equals("stats")) {
            this.player.showPlayerStats();
        } else if (commandWord.equals("item")) {
            this.player.itemCommand(command, this.battleParser, this.currentRoom);
        }
        
        return turnOver;
    }
    
    /**
     * Print out all the commands that can be used during battle.
     */
    private void printBattleHelp() 
    {
        System.out.println("Your command words are:");
        battleParser.showBattleCommands();
    }
    
    /**
     * The player attacking the enemy, also determining whether the enemy is dead.
     * the damage formula: e.atk
     * e = enemy
     */
    private void attackEnemy()
    {
        if (this.enemy.getIsDead())
        {
            this.battleWon = true;
            return;
        }
        int playerAttack = this.player.getAttack();
        int enemyHealth = this.enemy.getHealth();
        
        // Damage formula: p.atk
        enemyHealth -= playerAttack;
        
        if (enemyHealth <= 0)
        {
            enemyHealth = 0;
            this.enemy.setIsDead();
        }
        this.enemy.setHealth(enemyHealth);
        
        System.out.println("You have dealt " + playerAttack + " damage against the "
                            + enemyName + "!");
    }

    /**
     * Enemy's turn of attacking the player. It attacks the player with
     * the damage formula:  e.atk - p.def
     * e = enemy, p = player
     */
    private void enemyTurn()
    {
        int playerHealth = this.player.getHealth();
        int playerDefense = this.player.getDefense();
        int enemyAttack = this.enemy.getAttack();

        // Damage formula: e.atk - p.def
        int resultingAttack = enemyAttack - playerDefense;
        
        System.out.println("The " + enemyName + " attacked you!");
        if (resultingAttack <= 0)
        {
             System.out.println("It had no effect!");
        } else {
             playerHealth -= resultingAttack;
             System.out.println("You lost "
                                + resultingAttack + " health!");
        }
        
        if (playerHealth <= 0) {
            playerHealth = 0;
        }
        
        this.player.setHealth(playerHealth);
        System.out.println(this.enemy.getMessage());
    }
}
