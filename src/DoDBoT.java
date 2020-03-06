import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * DoDBoT class for the bot that lets users play Dungeon Of Doom. Extends MatsuriBot. Uses the command prefix "!"
 *
 * @author Dylan Drescher
 */
public class DoDBoT extends MatsuriBot {
    private ArrayList<DODGame> games = new ArrayList<>(); // This will contain all active games

    /**
     * Constructor.
     */
    private DoDBoT(String csa, int csp) throws IOException {
        super(csa, csp);
    }

    /**
     * Returns the game that corresponds to a specific player
     *
     * @param playerName The playername to get the game from
     * @return The game itself, or if no game exists, returns null
     */
    private DODGame findGame(String playerName) {
        if (this.games.size() != 0) {
            for (DODGame game : this.games) {
                if (game.getPlayerName().equals(playerName)) {
                    return game;
                }
            }
        }
        return null;
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
        String playerName = parsedMsg[0].substring(1, parsedMsg[0].length()-1); // Gets the player name without the
                                                                                // square brackets
        if (parsedMsg[1].charAt(0) == '!') {
            if (parsedMsg[1].equals("!begin")) { // available maps: small, medium, large
                System.out.println("Player" + playerName + " has begin a new game!");
                switch (parsedMsg[2]) {
                    case "small":
                        this.games.add(new DODGame(playerName, "small_example_map.txt"));
                        break;
                    case "medium":
                        this.games.add(new DODGame(playerName, "medium_example_map.txt"));
                        break;
                    case "large":
                        this.games.add(new DODGame(playerName, "large_example_map.txt"));
                        break;
                    default:
                        toServer.writeBytes("[DoDBoT] Sorry, unknown map. Available maps: small, medium, large.");
                        break;
                }
                toServer.writeBytes("[DoDBoT] Welcome " + playerName + " to the "
                        + this.games.get(this.games.size()-1).getMapName() + "!");
            }
            else if (this.games.size() != 0) {
                if (parsedMsg[1].equals("!quit")) {
                    DODGame game = findGame(playerName);
                    assert game != null;
                    if (game.hasWon()) {
                        toServer.writeBytes("[DoDBoT] Congratulations " + game.getPlayerName() + ", you win!");
                    } else {
                        toServer.writeBytes("[DoDBoT] Unfortunately " + game.getPlayerName() + ", you lose...");
                    }
                } else {
                    System.out.println("ye2");
                    // bot processing
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        DoDBoT bot = new DoDBoT(args[0], Integer.parseInt(args[1]));
    }
}
