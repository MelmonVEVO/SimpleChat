import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * DoDBoT class for the bot that lets users play Dungeon Of Doom. Extends MatsuriBot. Uses the command prefix "!"
 *
 * @author Dylan Drescher
 */
public class DoDBoT extends MatsuriBot {
    ArrayList<DODGame> games = new ArrayList<>(); // This will contain all active games

    /**
     * Constructor.
     */
    private DoDBoT(String cca, int ccp) throws IOException {
        super(cca, ccp);
    }

    /**
     * Analyses messages that DoDBoT receives in order to check for commands.
     *
     * @param message The message that was posted to the server
     */
    @Override
    protected void analyseMessage(String message) throws IOException {
        DataOutputStream toServer = new DataOutputStream(this.connection.getOutputStream());
        String[] parsedMsg = message.split("\\s+");
        if (parsedMsg[1].charAt(0) == '!') {
            if (parsedMsg[1].equals("!begin")) {
                System.out.println("ye");
                // bot beginning things
            }
            else {
                System.out.println("ye2");
                // bot processing
            }
        }
    }


    public static void main(String[] args) throws IOException {
        DoDBoT bot = new DoDBoT(args[0], Integer.parseInt(args[1]));
    }
}
