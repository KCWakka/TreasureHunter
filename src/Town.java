/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean shovelOrNot;
    private String treasure;
    private boolean treasureOrNot;

    private boolean samurai;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean samurai) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.samurai = samurai;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        shovelOrNot = false;
        treasureOrNot = false;
        int random = (int) (Math.random() * 4);
        if (random == 0) {
            treasure = "Crown";
        } else if (random == 1) {
            treasure = "Trophy";
        } else if (random == 2) {
            treasure = "Gem";
        } else {
            treasure = "dust";
        }
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            if (hunter.hasItemInKit("sword") && item.equals("machete")) {
                item = "sword";
            }
            printMessage = "You used your " + item + " to cross the " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item;
            }
            shovelOrNot = false;
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop!";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance) {
            printMessage = Colors.RED + "You couldn't find any trouble" + Colors.RESET;
        } else {
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("sword")) {
                printMessage = Colors.RED + "The brawler, seeing your sword, has ran away from town" + Colors.RESET;
                printMessage += Colors.RED + "\n You receive " + goldDiff + " gold" + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
                if (Math.random() > noTroubleChance) {
                    printMessage += Colors.RED + "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + " gold." + Colors.RESET;
                    if (hunter.changeGold(-goldDiff)) {
                        System.out.println(printMessage);
                        System.out.println(Colors.CYAN + "Gamer over! The hunter went into debt and can't paid back to the stranger so he got killed!" + Colors.RESET);
                        System.exit(0);
                    }
                }
            }
        }
    }
    public void treasureHunt() {
        if (!treasureOrNot) {
            printMessage = Colors.YELLOW + "You found a " + treasure + "." + Colors.RESET;
            treasureOrNot = true;
            if (!treasure.equals("dust")) {
                hunter.addTreasure(treasure);
                printMessage += Colors.YELLOW + "\nYou put the treasure in your bag!" + Colors.RESET;
                if (hunter.hasTreasure(treasure)) {
                    printMessage += Colors.YELLOW + "You realized that you already have it so you throw it away!" + Colors.RESET;
                }
                if ( (hunter.getTreasure().indexOf("Crown") != -1) && (hunter.getTreasure().indexOf("Trophy") != -1) && (hunter.getTreasure().indexOf("Gem") != -1)) {
                    System.out.println(printMessage);
                    System.out.println(Colors.CYAN + "Congratulations, you have found the last of the three treasures, you win!" + Colors.RESET);
                    System.exit(0);
                }
            } else {
                printMessage += Colors.YELLOW + "\nYou waste your time on a non-valuable treasure, you throw away the dust!" + Colors.RESET;
            }
        } else {
            printMessage = Colors.YELLOW + "You already have search this town for treasure!" + Colors.RESET;
        }
    }
    public void shovel() {
        if (hunter.hasItemInKit("shovel")) {
            if (!shovelOrNot) {
                int random = (int) (Math.random() * 2);
                int goldAmount = (int) (Math.random() * 20) + 1;
                if (random == 0) {
                    printMessage = "You dug up " + Colors.YELLOW + goldAmount +  " gold!" + Colors.RESET;
                    hunter.changeGold(goldAmount);
                } else {
                    printMessage = "You dug but only found dirt";
                }
                shovelOrNot = true;
            } else {
                printMessage = "You already dug for gold in this town.";
            }
        } else {
            printMessage = "You can't dig for gold without a shovel";
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        int rnd = (int) (Math.random() * 13);
        if (rnd < 2) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < 4) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < 6) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < 8) {
            return new Terrain("Desert", "Water");
        } else if (rnd < 10) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}
