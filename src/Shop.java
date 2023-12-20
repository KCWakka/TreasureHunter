import java.util.Scanner;

/**
 * The Shop class controls the cost of the items in the Treasure Hunt game. <p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method. <p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Shop {
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;

    private static final int BOOTS_COST = 8;

    private static final int SHOVEL_Cost = 8;

    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private double markdown;
    private Hunter customer;
    private TreasureHunter samurai;

    /**
     * The Shop constructor takes in a markdown value and leaves customer null until one enters the shop.
     *
     * @param markdown Percentage of markdown for selling items in decimal format.
     */
    public Shop(double markdown) {
        this.markdown = markdown;
        customer = null; // is set in the enter method
        samurai = new TreasureHunter();
    }

    /**
     * Method for entering the shop.
     *
     * @param hunter the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell) {
        customer = hunter;

        if (buyOrSell.equals("b")) {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, true);
            if (samurai.getSamuraiMode() && item.toLowerCase().equals("sword")) {
                System.out.print("It'll cost you " + Colors.YELLOW + cost + " gold. " + Colors.RESET + "Buy it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();

                if (option.equals("y")) {
                    buyItem(item);
                }
            } else {
                if (cost == 0) {
                    System.out.println("We ain't got none of those.");
                } else {
                    System.out.print("It'll cost you " + Colors.YELLOW + cost + " gold. " + Colors.RESET + "Buy it (y/n)? ");
                    String option = SCANNER.nextLine().toLowerCase();

                    if (option.equals("y")) {
                        buyItem(item);
                    }
                }
            }
        } else {
            System.out.println("What're you lookin' to sell? ");
            System.out.print("You currently have the following items: " + customer.getInventory());
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == 0) {
                System.out.println("We don't want none of those.");
            } else {
                System.out.print("It'll get you " + Colors.YELLOW + cost + " gold. " + Colors.RESET + "Sell it (y/n)? ");
                String option = SCANNER.nextLine().toLowerCase();

                if (option.equals("y")) {
                    sellItem(item);
                }
            }
        }
    }

    /**
     * A method that returns a string showing the items available in the shop
     * (all shops sell the same items).
     *
     * @return the string representing the shop's items available for purchase and their prices.
     */
    public String inventory() {
        String str = "Water: " + Colors.GREEN + WATER_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        if (samurai.getSamuraiMode()) {
            str += Colors.RED + "Sword: 0 gold\n" + Colors.RESET;
        }
        str += "Rope: " + Colors.GREEN + ROPE_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        str += "Machete: " + Colors.GREEN + MACHETE_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        str += "Boots: " + Colors.GREEN + BOOTS_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        str += "Shovel: " + Colors.GREEN + SHOVEL_Cost + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        str += "Horse: " + Colors.GREEN + HORSE_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;
        str += "Boat: " + Colors.GREEN + BOAT_COST + Colors.RESET + Colors.YELLOW + " gold\n" + Colors.RESET;

        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     *
     * @param item The item being bought.
     */
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)) {
            System.out.println("Ye' got yerself a " + item + ". Come again soon.");
        } else {
            System.out.println("Hmm, either you don't have enough" + Colors.YELLOW + " gold " + Colors.RESET + "or you've already got one of those!");
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     *
     * @param item The item being sold.
     */
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice)) {
            System.out.println("Pleasure doin' business with you.");
        } else {
            System.out.println("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     *
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying) {
        if (isBuying) {
            return getCostOfItem(item);
        } else {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item) {
        if (item.equals("water")) {
            return WATER_COST;
        } else if (item.equals("rope")) {
            return ROPE_COST;
        } else if (item.equals("machete")) {
            return MACHETE_COST;
        } else if (item.equals("horse")) {
            return HORSE_COST;
        } else if (item.equals("boat")) {
            return BOAT_COST;
        } else if (item.equals("boots")) {
            return BOOTS_COST;
        } else if (item.equals("shovel")) {
            return SHOVEL_Cost;
        } else {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item) {
        int cost = (int) (getCostOfItem(item) * markdown);
        return cost;
    }
}
