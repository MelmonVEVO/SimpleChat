import java.io.IOException;

/**
 * DoDBoT class for the bot that lets users play Dungeon Of Doom. Extends MatsuriBot. Uses the command prefix "!"
 *
 * @author Dylan Drescher
 */
public class DoDBoT extends MatsuriBot {

    /**
     * Constructor.
     */
    private DoDBoT(String cca, int ccp) throws IOException {
        super(cca, ccp);
    }

    public static void main(String[] args) throws IOException {
        DoDBoT bot = new DoDBoT(args[0], Integer.parseInt(args[1]));
    }
}
