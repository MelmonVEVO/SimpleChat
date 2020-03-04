/**
 * Class for containing enemy info and methods
 */
public class DODEnemy extends DODHumanPlayer {

    /**
     * Constructor
     */
    public DODEnemy() {
        super();
    }

    /**
     * Moves the enemy in a specific direction without announcing the direction to the player
     *
     * @param dir : the direction to move
     */
    @Override
    public void move(char dir) {
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
