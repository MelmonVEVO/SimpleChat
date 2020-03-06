/**
 * Class for containing player information and methods
 *
 * Pulled from Dylan Drescher (dgd29, author)'s Dungeon of Doom coursework
 */
class DODHumanPlayer {

    private int[] pos;
    private String playerName;

    /**
     * Constructor
     */
    DODHumanPlayer(String name) {
        this.pos = new int[]{0, 0};
        this.playerName = name;
    }

    /**
     * Gets the position of a specific coordinate
     *
     * @param which : whether to get the x or the y coordinate
     * @return : position
     */
    int getPos(char which) {
        if (which == 'x') {
            return this.pos[1];
        }
        else if (which == 'y') {
            return this.pos[0];
        }
        return 0;
    }

    /**
     * Gets the active player's name
     *
     * @return player name
     */
    String getPlayerName() {
        return this.playerName;
    }

    void deactivate() {
        this.playerName = "#DEACTIVATED";
    }

    /**
     * Changes the position of the character to a specific location
     *
     * @param y : y-coordinate
     * @param x : x-coordinate
     */
    void changePos(int y, int x) {
        this.pos[0] = y;
        this.pos[1] = x;
    }

    /**
     * Moves the character by one space in a specific direction and tells the player that the character moved
     *
     * @param dir : the direction to move
     */
    void move(char dir) {
        switch (dir) {
            case 'N':
                this.pos[0]--;
                break;
            case 'E':
                this.pos[1]++;
                break;
            case 'S':
                this.pos[0]++;
                break;
            case 'W':
                this.pos[1]--;
                break;
        }
    }
}