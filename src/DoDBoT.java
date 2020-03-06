import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private DoDBoT(String cca, int ccp) throws IOException {
        super(cca, ccp);
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
        if (parsedMsg.length > 1) { // ignores one/zero-word messages
            String playerName = parsedMsg[0].substring(1, parsedMsg[0].length()-1); // Gets the player name without the
            // square brackets
            if (parsedMsg[1].charAt(0) == '!') {
                if (parsedMsg[1].equals("!begin")) { // available maps: small, medium, large
                    System.out.println("Player " + playerName + " has begin a new game!");
                    try {
                        switch (parsedMsg[2]) {
                            case "small":
                                this.games.add(new DODGame(playerName, "src/small_example_map.txt"));
                                break;
                            case "medium":
                                this.games.add(new DODGame(playerName, "src/medium_example_map.txt"));
                                break;
                            case "large":
                                this.games.add(new DODGame(playerName, "src/large_example_map.txt"));
                                break;
                            default:
                                toServer.writeBytes("[DoDBoT] Sorry, unknown map. Available maps: small, medium, large.\n");
                                break;
                        }
                        toServer.writeBytes("[DoDBoT] Welcome " + playerName + " to the "
                                + this.games.get(this.games.size() - 1).getMapName() + "!\n");
                    }
                    catch (IndexOutOfBoundsException ignored) {
                        toServer.writeBytes("[DoDBoT] No map inputted. Available maps: small, medium, large.\n");
                    }
                }
                else if (this.games.size() != 0) {
                    DODGame game = findGame(playerName);
                    if (game != null) { // checking if there is an actual game tied to a username
                        if (parsedMsg[1].equals("!quit")) { // quitting procedure
                            System.out.println("User " + playerName + " has quit a game.");
                            if (game.hasWon()) { // checking for a victory
                                game.deactivate();
                                toServer.writeBytes("[DoDBoT] Congratulations " + playerName + ", you win!\n");
                            }
                            else {
                                game.deactivate();
                                toServer.writeBytes("[DoDBoT] Unfortunately " + playerName + ", you lose...\n");
                            }
                        }
                        else { // user command procedure
                            System.out.println("User prompted " + parsedMsg[1]);
                            String out;
                            try {
                                out = game.processCommand(parsedMsg[1] + " " + parsedMsg[2]);
                                // processes command. return statement is the message to be sent to server
                                // parsedMsg[2] is used when !move is entered
                            }
                            catch (ArrayIndexOutOfBoundsException ignored) {
                                out = game.processCommand(parsedMsg[1]); // if there is no operand
                            }
                            toServer.writeBytes("[DoDBoT] " + out);
                            out = game.processEnemy();
                            toServer.writeBytes("[DoDBoT] " + out + "\n");
                            if (game.hasBeenCaptured()) {
                                game.deactivate();
                                toServer.writeBytes("[DoDBoT] Oh no! " + playerName + ", you were captured!\n");
                            }
                        }
                    }
                    else {
                        toServer.writeBytes("[DoDBoT] You have no active games, " + playerName + ".\n");
                    }
                }
                else {
                    toServer.writeBytes("[DoDBoT] Invalid command " + parsedMsg[1] + "\n");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String cca = AddressPort.getAddressOrPort(args, "-cca"); // getting the address of the server to connect
        int ccp = Integer.parseInt(AddressPort.getAddressOrPort(args, "-ccp")); //getting the port
        DoDBoT bot = new DoDBoT(cca, ccp);
        String serverOutput; // message received from server
        System.out.println("DoDBoT, ready for adventurers!");
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(bot.connection.getInputStream()));
        //noinspection InfiniteLoopStatement
        while (true) {
            serverOutput = serverReader.readLine();
            if (serverOutput != null) {
                System.out.println(serverOutput);
                bot.analyseMessage(serverOutput);
            }
        }
    }
}
