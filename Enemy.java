import java.util.ArrayList;

/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds the enemy structure of the application, extended from the NPC
 *  class. It contains the methods for the enemy's battle event, getters and setters
 *  for enemy stats.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */public class Enemy extends NPC
{   
    // Enemy stats
    private int NPCHealth;
    private int NPCAttack;
    private boolean isDead;

    /**
     * Constructor for objects of class Enemy
     */
    public Enemy(String NPCName, String firstMessage, String secondMessage, int NPCHealth, int NPCAttack)
    {
        super(NPCName, firstMessage, secondMessage);
        
        // Instantiating Enemy stats
        this.NPCHealth = NPCHealth;
        this.NPCAttack = NPCAttack;
        this.isDead = false;
    }
    
    public int getHealth()
    {
        return this.NPCHealth;
    }
    
    public void setHealth(int newHealth)
    {
        this.NPCHealth = newHealth;
    }
    
    public int getAttack()
    {
        return this.NPCAttack;
    }
    
    /**
     * Check if the Enemy is dead.
     * 
     * @return  this.dead   The boolean value (true or false)
     */
    public boolean getIsDead()
    {
        return this.isDead;
    }
    
    /**
     * Set the dead boolean of this Enemy to true;
     */
    public void setIsDead()
    {
        this.isDead = true;
    }
    
    /**
     * Conduct a battle.
     * @return boolean value insinuating whether the battle was successful or not.
     */
    public boolean enemyBattle(Player player, Room currentRoom) {
        Battle battle = new Battle(this, player, currentRoom);
        System.out.println("You have encountered a " + this.getNPCName() + "!");
        boolean battleResult = battle.commence(currentRoom.isBossRoom());
        return battleResult;
    }
    
    
}
