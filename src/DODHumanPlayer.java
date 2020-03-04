/**
 * Class for containing player information and methods
 */
public class DODHumanPlayer {

    protected int[] pos;

    /**
     * Constructor
     */
    public DODHumanPlayer() {
        this.pos = new int[]{0, 0};
    }

    /**
     * Gets the position of a specific coordinate
     *
     * @param which : whether to get the x or the y coordinate
     * @return : position
     */
    public int getPos(char which) {
        if (which == 'x') {
            return this.pos[1];
        }
        else if (which == 'y') {
            return this.pos[0];
        }
        return 0;
    }

    /**
     * Changes the position of the character to a specific location
     *
     * @param y : y-coordinate
     * @param x : x-coordinate
     */
    public void changePos(int y, int x) {
        this.pos[0] = y;
        this.pos[1] = x;
    }

    /**
     * Moves the character by one space in a specific direction and tells the player that the character moved
     *
     * @param dir : the direction to move
     */
    public void move(char dir) {
        switch (dir) {
            case 'N':
                this.pos[0]--;
                System.out.println("Moved north!");
                break;
            case 'E':
                this.pos[1]++;
                System.out.println("Moved east!");
                break;
            case 'S':
                this.pos[0]++;
                System.out.println("Moved south!");
                break;
            case 'W':
                this.pos[1]--;
                System.out.println("Moved west!");
                break;
        }
    }
}