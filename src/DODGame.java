import java.util.Random;

/**
 * Contains main game processes
 */
public class DODGame {

    private DODMap map;
    private DODHumanPlayer player = new DODHumanPlayer();
    private DODEnemy beast = new DODEnemy();
    private int gold = 0;

    /**
     * Splits and processes a command
     *
     * @param command : player input command
     */
    public void processCommand(String command) {
        String[] decoded = command.split("\\s+"); // splits command in to list of words for easier parsing
        if (decoded.length == 0) { // in case command inputted was empty
            decoded = new String[]{""};
        }
        switch (decoded[0]) {
            case "HELLO":
                System.out.printf("Gold to win: %d\n", this.map.getGoldRequirement());
                break;

            case "GOLD":
                System.out.printf("Gold owned: %d\n", this.gold);
                break;

            case "MOVE":
                try {
                    switch (decoded[1]) { // checking operand
                        case "N":
                            if (map.getTile(player.getPos('y') - 1, player.getPos('x')) != '#') {
                                player.move('N');
                            } else {
                                System.out.println("Obstruction encountered");
                            }
                            break;
                        case "E":
                            if (map.getTile(player.getPos('y'), player.getPos('x') + 1) != '#') {
                                player.move('E');
                            } else {
                                System.out.println("Obstruction encountered");
                            }
                            break;
                        case "S":
                            if (map.getTile(player.getPos('y') + 1, player.getPos('x')) != '#') {
                                player.move('S');
                            } else {
                                System.out.println("Obstruction encountered");
                            }
                            break;
                        case "W":
                            if (map.getTile(player.getPos('y'), player.getPos('x') - 1) != '#') {
                                player.move('W');
                            } else {
                                System.out.println("Obstruction encountered");
                            }
                            break;
                        default:
                            System.out.println("Invalid direction");
                            break;
                    }
                }
                catch (IndexOutOfBoundsException x) {
                    System.out.println("Invalid direction/Obstruction encountered");
                }
                break;

            case "PICKUP":
                if (this.map.grabGold(this.player.getPos('y'), this.player.getPos('x'))) {
                    // if there is gold in the same tile as the plauer
                    this.gold++;
                    System.out.println("Picked up some gold!");
                }
                else {
                    System.out.println("No gold to pick up...");
                }
                break;

            case "LOOK":
                this.map.look(this.player.getPos('y'), this.player.getPos('x'),
                        this.beast.getPos('y'), this.beast.getPos('x'));
                break;

            default:
                System.out.printf("Unknown command %s\n", decoded[0]);
                break;
        }
    }

    /**
     * Loads a map from an external file
     *
     * @param filename : filename of the map the game should load
     */
    public void createMap(String filename) {
        this.map = new DODMap(filename);
        boolean placed = false; // this is for checking whether the player or enemy has been placed in a valid place
        Random rand = new Random();
        while (!placed) { // Placing the player in a random valid position (not on # or G)
            this.player.changePos(rand.nextInt(this.map.lenCol()), rand.nextInt(this.map.lenRow()));
            if (this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) != '#' &&
                    this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) != 'G') {
                placed = true;
            }
        }
        placed = false;
        while (!placed) { // Placing the enemy in a random valid position (not on #, G or B)
            this.beast.changePos(rand.nextInt(this.map.lenCol()), rand.nextInt(this.map.lenRow()));
            if (this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) != '#' &&
                    this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) != 'G' &&
                    (this.player.getPos('y') != this.beast.getPos('y') &&
                            this.player.getPos('x') != this.beast.getPos('x'))) {
                placed = true;
            }
        }
    }

    /**
     * Returns the name of the map
     *
     * @return : name of the map
     */
    public String getMapName() {
        return this.map.getName();
    }

    /**
     * Prompts the enemy to move around the map using AI. The beast will try to make chase, but if obstructed, will
     * start wandering randomly
     */
    public void processEnemy() {
        boolean skipRand = false;
        try {
            /* If the beast can see the player, it will start making chase */
            if (this.player.getPos('y') > this.beast.getPos('y') &&
                    this.map.getTile(this.beast.getPos('y') + 1, this.beast.getPos('x')) != '#') {
                // going south
                this.beast.move('S');
                skipRand = true;
            }
            else if (this.player.getPos('y') < this.beast.getPos('y') &&
                    this.map.getTile(this.beast.getPos('y') - 1, this.beast.getPos('x')) != '#') {
                // going north
                this.beast.move('N');
                skipRand = true;
            }
            else if (this.player.getPos('x') > this.beast.getPos('x') &&
                    this.map.getTile(this.beast.getPos('y'), this.beast.getPos('x') + 1) != '#') {
                // going east
                this.beast.move('E');
                skipRand = true;
            }
            else if (this.player.getPos('x') < this.beast.getPos('x') &&
                    this.map.getTile(this.beast.getPos('y'), this.beast.getPos('x') - 1) != '#') {
                // going west
                this.beast.move('W');
                skipRand = true;
            }
        }
        catch (IndexOutOfBoundsException ignored) {}
        if (!skipRand) { /* if obstructed by anything, the beast will start wandering randomly until it can chase the
            player again */
            boolean moved = false;
            while (!moved) {
                Random rand = new Random();
                int move = rand.nextInt(3);
                switch (move) {
                    case 0: //north
                        if (this.map.getTile(this.beast.getPos('y') - 1, this.beast.getPos('x')) != '#') {
                            this.beast.move('N');
                        }
                        moved = true;
                        break;
                    case 1: //east
                        if (this.map.getTile(this.beast.getPos('y'), this.beast.getPos('x') + 1) != '#') {
                            this.beast.move('E');
                        }
                        moved = true;
                        break;
                    case 2: //south
                        if (this.map.getTile(this.beast.getPos('y') + 1, this.beast.getPos('x')) != '#') {
                            this.beast.move('S');
                        }
                        moved = true;
                        break;
                    case 3: //west
                        if (this.map.getTile(this.beast.getPos('y'), this.beast.getPos('x') - 1) != '#') {
                            this.beast.move('W');
                        }
                        moved = true;
                        break;
                }
            }
            System.out.println("The beast wanders...");
        }
        else {
            System.out.println("The beast makes chase!");
        }
    }

    /**
     * Checks if the player has been captured by the beast
     *
     * @return : if the player has been captured
     */
    public boolean hasBeenCaptured() {
        return player.getPos('x') == beast.getPos('x') && player.getPos('y') == beast.getPos('y');
    }

    /**
     * Checks if the game has been won
     *
     * @return : winning result
     */
    public boolean hasWon() {
        return this.map.getGoldRequirement() == this.gold &&
                this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) == 'E';
    }
}
