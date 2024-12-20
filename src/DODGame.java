import java.util.Random;

/**
 * Contains main game processes
 *
 * Pulled and slightly modified from Dylan Drescher (dgd29, author)'s Dungeon of Doom coursework
 */
class DODGame {

    private DODMap map;
    private DODHumanPlayer player;
    private DODEnemy beast = new DODEnemy();
    private int gold = 0;

    /**
     * Constructor
     *
     * @param name player name that's currently playing
     */
    DODGame(String name, String mapname) {
        player = new DODHumanPlayer(name);
        createMap(mapname);
    }

    /**
     * Returns the active player's name
     *
     * @return the player's name
     */
    String getPlayerName() {
        return this.player.getPlayerName();
    }

    /**
     * Splits and processes a command
     *
     * @param command : player input command
     * @return the message to be sent to the server
     */
    String processCommand(String command) {
        String[] decoded = command.split("\\s+"); // splits command in to list of words for easier parsing
        if (decoded.length == 0) { // in case command inputted was empty
            decoded = new String[]{""};
        }
        switch (decoded[0]) {
            case "!hello":
                return "Gold to win: " + this.map.getGoldRequirement() + "\n";

            case "!gold":
                return "Gold owned: " + this.gold + "\n";

            case "!move":
                try {
                    switch (decoded[1]) { // checking operand
                        case "n":
                            if (map.getTile(player.getPos('y') - 1, player.getPos('x')) != '#') {
                                player.move('N');
                                return "Moved north!\n";
                            }
                            else {
                                return "Obstruction encountered\n";
                            }

                        case "e":
                            if (map.getTile(player.getPos('y'), player.getPos('x') + 1) != '#') {
                                player.move('E');
                                return "Moved east!\n";
                            }
                            else {
                                return "Obstruction encountered\n";
                            }

                        case "s":
                            if (map.getTile(player.getPos('y') + 1, player.getPos('x')) != '#') {
                                player.move('S');
                                return "Moved south!\n";
                            }
                            else {
                                return "Obstruction encountered\n";
                            }

                        case "w":
                            if (map.getTile(player.getPos('y'), player.getPos('x') - 1) != '#') {
                                player.move('W');
                                return "Moved west!\n";
                            }
                            else {
                                return "Obstruction encountered\n";
                            }

                        default:
                            return "Invalid direction\n";

                    }
                }
                catch (IndexOutOfBoundsException x) {
                    return "Invalid direction/Obstruction encountered\n";
                }

            case "!pickup":
                if (this.map.grabGold(this.player.getPos('y'), this.player.getPos('x'))) {
                    // if there is gold in the same tile as the player
                    this.gold++;
                    return "Picked up some gold!\n";
                }
                else {
                    return "No gold to pick up...\n";
                }

            case "!look":
                return this.map.look(this.player.getPos('y'), this.player.getPos('x'),
                        this.beast.getPos('y'), this.beast.getPos('x'));

            default:
                return "Unknown command " + decoded[0] + "\n";
        }
    }

    /**
     * Loads a map from an external file
     *
     * @param filename : filename of the map the game should load
     */
    private void createMap(String filename) {
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
    String getMapName() {
        return this.map.getName();
    }

    /**
     * Prompts the enemy to move around the map using AI. The beast will try to make chase, but if obstructed, will
     * start wandering randomly
     */
    String processEnemy() {
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
            return "The beast wanders...";
        }
        else {
            return "The beast makes chase!";
        }
    }

    /**
     * Checks if the player has been captured by the beast
     *
     * @return : if the player has been captured
     */
    boolean hasBeenCaptured() {
        return player.getPos('x') == beast.getPos('x') && player.getPos('y') == beast.getPos('y');
    }

    /**
     * Checks if the game has been won
     *
     * @return : winning result
     */
    boolean hasWon() {
        return this.map.getGoldRequirement() == this.gold &&
                this.map.getTile(this.player.getPos('y'), this.player.getPos('x')) == 'E';
    }

    /**
     * Deactivates the player
     */
    void deactivate() {
        this.player.deactivate();
    }
}
