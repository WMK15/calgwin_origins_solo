import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Iterator;
import java.util.Stack;

/**
 *  This class is the main class of the "Calgwin Origins: Solo" application. 
 *  "Calgwin Origins: Solo" is a text-based dungeon crawler game, where the user
 *  has to find their way to the dungeon exit to win the game. The user can go
 *  into different rooms, pick up items & use items (or discard them), fight enemies,
 *  and talk to NPCs!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *  
 *  Base project made by Michael Kölling and David J. Barnes. 
 *  Final version built by Waseef Mohammad Khan, K-Number: K23080255
 * 
 * @author  Waseef Mohammad Khan (K23080255)
 * @version 2023.12.06
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Stack<Room> previousRooms; // LIFO is best for back command.
    private ArrayList<Room> GameRooms; // Used for the teleporter room task.
    
    // Any time the player faints, they get teleported to this room.
    private Room startingRoom; 

    private Player player;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        this.createRooms();
        this.parser = new Parser();
        this.previousRooms = new Stack<Room>();
        this.player = new Player(100, 5, 3); // Create a player for the game.
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room mainRoom, teleporterRoom, treasureVault, trainingGrounds, armoury, cellar, alchemyLab, bossEntrance, bossRoom;

        // create the rooms
        mainRoom = new Room("Main Room", "the main dungeon hall", false);
        treasureVault = new Room("Treasure Vault", "a room with a big, shiny unlocked chest", false);
        teleporterRoom = new Room("Teleporter Room", "a room with a giant floating crystal", false);
        trainingGrounds = new Room("Training Grounds", "a room that looks like a training ground", false);
        armoury = new Room("Armoury", "a room that looks like an armoury", false);
        cellar = new Room("Cellar", "a cellar room", false);
        alchemyLab = new Room("Alchemy Lab", "a lab that looks destroyed and looted", false);
        bossEntrance = new Room("Boss Entrance", "a small corridor with a very spooky steel door\nat the end. There seems to be some noise behind the door", false);
        bossRoom = new Room("Boss Room", "a huge hall with a massive dragon", true);

        ///=== MAIN ROOM ===///
        mainRoom.setExit("north", cellar);
        mainRoom.setExit("east", treasureVault);
        mainRoom.setExit("west", armoury);
        mainRoom.setExit("northwest", alchemyLab);
        mainRoom.setExit("southwest", trainingGrounds);
        
        // MAIN ROOM ITEMS
        mainRoom.addRoomItem(new Item("Apple", "A ripe apple that replenishes 20 health.",
                            1, 20, 0, 0, true));

        // MAIN ROOM NPCs
        NPC Hiro = new NPC("Hiro",
                "Hi, I'm Hiro, please help that poor lady being attacked by that\nslime!",
                "I'll see you around.");
        Hiro.addNPCItem(new Item("Wooden Sword", "A wooden sword that does an additional 15 damage.",
                            2, 0, 15, 0, false));
                            
        String[] HiroDialogueOptions = {"yes", "no"};
        String[] HiroResponses = {"Thank you! Take this wooden sword, it'll help you!",
                                "Ugh, she's not gonna survive!"};
                                
        Hiro.setDialogues(HiroDialogueOptions, HiroResponses);
        
        mainRoom.addRoomNPC(Hiro);

        
        ///=== TREASURE VAULT ===///
        treasureVault.setExit("north", teleporterRoom);
        treasureVault.setExit("west", mainRoom);
        
        // TREASURE VAULT ITEMS
        treasureVault.addRoomItem(new Item("Iron Sword",
                            "A finely crafted sword made of iron. Does 25 damage.",
                            4, 0, 25, 0, false));
                            
        treasureVault.addRoomItem(new Item("Golden Apple",
        "An apple that seems to be dowsed in magic gold.\nReplenishes 50 health and increases user's attack by 5.",
        4, 50, 5, 0, true));
        
        treasureVault.addRoomItem(new Item("Chest", "A chest that may have items in it.", 20, 0, 0, 0, false));
    
                            
        ///=== TRAINING GROUNDS ===///
        trainingGrounds.setExit("east", mainRoom);
        trainingGrounds.setExit("west", armoury);
        
        // TRAINING GROUNDS ENEMIES
        Enemy WitheredZombieTR = new Enemy("Withered Zombie", "Uhhhhggghhh!",
                                                "Eeeeeee~",  50, 15);
        WitheredZombieTR.addNPCItem(new Item("Tattered Cloth",
                                    "A worn piece of cloth. Grants +3 defense.",
                                    1, 0, 0, 3, false));
        WitheredZombieTR.addNPCItem(new Item("Brittle Bone Fragment",
                                    "Fragments of brittle bones, grants +3 attack\nwhen consumed.",
                                    2, 0, 3, 0, true));
                                    
        trainingGrounds.addRoomEnemy(WitheredZombieTR);
        
        
        ///=== ARMOURY ===///                           
        armoury.setExit("south", trainingGrounds);
        armoury.setExit("east", mainRoom);
        
        // ARMOURY ENEMIES
        Enemy armouredSkeleton = new Enemy("Armoured Skeleton", "Clank-Clank... Clank-Clank...",
        "Rattling Clink... Thud... *Silence*", 80, 22);
        armouredSkeleton.addNPCItem(new Item("Iron Chestplate",
                            "A well-built iron chestplate that gives the user\n10 defense.",
                            5, 0, 0, 10, false));
                            
        armoury.addRoomEnemy(armouredSkeleton);

        
        ///=== CELLAR ===///
        cellar.setExit("south", mainRoom);
        
        // CELLAR NPCS
        NPC oldLady = new NPC("Old Lady",
                "Thank you so much for saving me! You must be hurt, here\ntake this healing potion!",
                "I hope you find your way out!");
        oldLady.addNPCItem(new Item("Healing Potion", "A potion that grants the user +20 health points\nand +3 defense points.",
                            3, 30, 0, 3, true));
        cellar.addRoomNPC(oldLady);
                       
        // CELLAR ENEMIES
        Enemy Slime = new Enemy("Slime", "*Plopping noise*",
                                                "*Violent hiss as the Slime dies*",  30, 10);
        Slime.addNPCItem(new Item("Slimy Residue",
                                    "Offers a small 10-point health recovery, a meager\nreward obtained from slimes.",
                                    1, 10, 0, 0, true));
        cellar.addRoomEnemy(Slime);
        
        // CELLAR ITEMS
        cellar.addRoomItem(new Item("Table", "Just a worn-down table.", 20, 0, 0, 0, false));
        cellar.addRoomItem(new Item("Broken Chair", "A broken chair.", 20, 0, 0, 0, false));
        
        
        ///=== ALCHEMY LAB ===///
        alchemyLab.setExit("south", mainRoom);
        alchemyLab.setExit("west", bossEntrance);
        
        // ALCHEMY LAB ITEMS
       alchemyLab.addRoomItem(new Item("testTube", "A broken test tube with nothing in it.",
                                                                        20, 0, 0, 0, false));
        
        // ALCHEMY LAB ENEMIES
        Enemy WitheredZombieAL = new Enemy("Withered Zombie", "Uhhhhggghhh!",
                                                "Eeeeeee~",  50, 15);
        WitheredZombieAL.addNPCItem(new Item("Tattered Cloth",
                                    "A worn piece of cloth. Grants +3 defense.",
                                    1, 0, 0, 3, false));
        WitheredZombieAL.addNPCItem(new Item("Brittle Bone Fragment",
                                    "Fragments of brittle bones, grants +3 attack\nwhen consumed.",
                                    2, 0, 3, 0, true));
                                    
        alchemyLab.addRoomEnemy(WitheredZombieAL);
        
        Enemy armouredSkeletonAL = new Enemy("Armoured Skeleton", "Clank-Clank... Clank-Clank...",
        "Rattling Clink... Thud... *Silence*", 80, 22);
        armouredSkeletonAL.addNPCItem(new Item("Iron Chestplate",
                            "A well-built iron chestplate that gives the user\n10 defense.",
                            5, 0, 0, 10, false));
                            
        alchemyLab.addRoomEnemy(armouredSkeletonAL);

        
        ///=== BOSS ENTRANCE ===///
        bossEntrance.setExit("east", alchemyLab);
        bossEntrance.setExit("west", bossRoom);
        
        // BOSS ENTRANCE ITEMS
        bossEntrance.addRoomItem(new Item("Scroll of Fortitude",
                                        "Boosts defense by 10 points",
                                        1, 0, 0, 10, true));
                                        
        bossEntrance.addRoomItem(new Item("Potion of Vitality",
                                        "Restores 100 health points",
                                        2, 100, 0, 0, true));
        
        bossEntrance.addRoomItem(new Item("Fire Essence Crystal",
                                        "Enhances attack by 15 pounts",
                                        2, 0, 15, 0, true));

                                        
        /* The boss room will have no exit, when the player enters the room they must
         * battle the boss. If they lose, they get teleported back to the mainRoom. */
        /// === BOSS ROOM NPC === ///
        Enemy ZullDragon = new Enemy("Zuul the Dragon", "ROOOOOOAAAAARRRR!",
        "Rrraaauughhh... *fades into silence*", 500, 60);
        
        ZullDragon.addNPCItem(new Item("Dragon Scale",
                                "A sharp object that can be used as a lethal weapon",
                                10, 0, 20, 0, false));
        
        bossRoom.addRoomEnemy(ZullDragon);
        
        /* Teleporter Room not added because we don't want the player to teleport to the
         * same room. Boss Room not added because that would be unfair to the player. */
        GameRooms = new ArrayList<Room>(Arrays.asList(mainRoom,
                treasureVault,
                trainingGrounds,
                armoury,
                cellar,
                alchemyLab,
                bossEntrance));

        startingRoom = mainRoom;
        currentRoom = mainRoom;  // start game in the main dungeon hall.
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand(); 
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message and event for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are Ray, and have been locked in an unknown dungeon in the world of Calgwin.");
        System.out.println("You need to find your way out, and you've been told that there exists\nan entity 'Zuul' on the other end of this dungeon.");
        System.out.println("You must find this entity and defeat it as it guards the only other\nexit out of this dungeon.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println("Maybe go talk to the person.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...\nYou can use 'help' for commands.");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("back")) {
            goBack();
        }
        else if (commandWord.equals("inventory")) {
            this.player.getInventory().showInventory();
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("talk")) {
            this.talk(command);
        }
        else if (commandWord.equals("stats")) {
            this.player.showPlayerStats();
        } else if (commandWord.equals("item")) {
            this.player.itemCommand(command, this.parser, this.currentRoom);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print the main commands, and the item subcommands.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("Your item commands are: ");
        parser.showItemCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRooms.push(currentRoom);
            currentRoom = nextRoom;

            // Check if the current room is the teleporter room.
            if (currentRoom.getName().equals("Teleporter Room")) {
                Random random = new Random();
                int randomIndex = random.nextInt(GameRooms.size());
                Room randomRoom = GameRooms.get(randomIndex);
                currentRoom = randomRoom;
                System.out.println("What's this, you have been teleported to a random room!");
            }
            if (!currentRoom.roomEvent(this.player))
            {
                currentRoom = startingRoom;
                System.out.println("You have been teleported back to where it all began...");
            }
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Go back to the previous room.
     */
    private void goBack()
    {
        if (this.previousRooms.isEmpty())
        {
            System.out.println("You haven't visited any rooms before this one!");
            return; 
        }

        Room prevRoom = (Room) previousRooms.pop();
        currentRoom = prevRoom;
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Talk to the NPCS. Some NPCs have dialogue options where you might get rewarded
     * for the right option chosen. Some NPCs may or may not give you items.
     * @param command   Used to get the name (secondWord of command) of the NPC
     */
    private void talk(Command command) {
        ArrayList<NPC> roomNPCs = currentRoom.getRoomNPCs();
        if (roomNPCs.isEmpty())
        {
            System.out.println("There is no one in this room!");
            return;
        }
        
        if(!command.hasSecondWord()) {
            System.out.println("Talk to whom?");
            roomNPCs.forEach(npc -> 
                System.out.println("- " + npc.getNPCName())
            );
            return;
        }
        
        String wordMerge = command.getSecondWord().trim();
        // Merge everything after 'talk' as one string.
        if (command.hasThirdWord())
        {
            wordMerge += " " + command.getThirdWord().trim();
        }
        
        String NPCName = wordMerge.trim().toLowerCase();
        
        NPC chosenNPC = roomNPCs.stream()
                        .filter(npc -> npc.getNPCName().toLowerCase().equals(NPCName))
                        .findFirst()
                        .orElse(null);
                        
        if (chosenNPC == null)
        {
            System.out.println("There's no one in this room with that name!");
            return;
        }
        
        chosenNPC.startNPCEvent(this.player, this.currentRoom);
    }
}
