import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

/**
 * Class for the simple MatsuriBot chatbot.
 *
 * @author Dylan Drescher
 */
public class MatsuriBot {

    /**
     * Analyses messages that MatsuriBot receives in order to check for commands.
     *
     * @param message The message that was posted to the server
     */
    private static String analyseMessage(String message) {
        String[] parsedMsg = message.split("\\s+");
        if (parsedMsg[1].charAt(0) == '/') {
            switch (parsedMsg[1]) {
                case "/hello":
                    // "Hello! I'm MatsuriBot!"
                    System.out.println("User prompted a /hello");
                    return "Hello! I'm MatsuriBot!";

                case "/time":
                    // "The current time is %s" [time]
                    LocalTime time = LocalTime.now();
                    System.out.println("User prompted a /time");
                    return "The current time is " + time + ".";

                default:
                    // "Sorry! I don't know what that means!"
                    System.out.println("User prompted an unknown command");
                    break;
            }
        }
        return "Sorry! I don't know what that means!";
    }

    /**
     * Method for when MatsuriBot is ran.
     *
     * @param args Server address and port
     */
    public static void main(String[] args) throws IOException {
        String cca = args[0];
        String ccp = args[1];
        int port1 = Integer.parseInt(ccp);
        Socket connection = new Socket(cca, port1);
        PrintWriter toServer = new PrintWriter(connection.getOutputStream());
        BufferedReader collect = new BufferedReader(new InputStreamReader(System.in));
        String serverOutput;
        System.out.println("MatsuriBot running normally~.");
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                serverOutput = collect.readLine();
                toServer.printf("[MatsuriBot] %s\n", analyseMessage(serverOutput));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
