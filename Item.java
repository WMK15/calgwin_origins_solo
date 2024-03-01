
/**
 *  This class is part of of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  This class holds the Item structure, including its attributes, getters and setters,
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */
public class Item
{
    private String name;
    private String description;
    private int itemWeight;
    private int healthUp;
    private int attackUp;
    private int defenseUp;
    private boolean isConsumable;

    /**
     * Constructor for the objects of class Item
     */
    public Item(String name, String description, int itemWeight, int healthUp, int attackUp, int defenseUp, boolean isConsumable)
    {
        this.name = name;
        this.description = description;
        this.itemWeight = itemWeight;
        this.healthUp = healthUp;
        this.attackUp = attackUp;
        this.defenseUp = defenseUp;
        this.isConsumable = isConsumable;
    }
    
    /**
     * @return  The name of the item
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * @return  The description of the item.
     */
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * @return  The weight of the item
     */
    public int getWeight()
    {
        return this.itemWeight;
    }
    
    /**
     * @return  The healthUp of the item
     */
    public int getHealthUp()
    {
        return this.healthUp;
    }

    /**
     * @return  The attackUp of the item
     */    
    public int getAttackUp()
    {
        return this.attackUp;
    }
    
    /**
     * @return  The defenseUp of the item
     */    
    public int getDefenseUp()
    {
        return this.defenseUp;
    }
    
    /**
     * @return isConsumable   Either true or false. If it is true, then the item can be consumed.
     */
    public boolean getIsConsumable() {
        return this.isConsumable;
    }
}
