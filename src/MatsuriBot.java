import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

/**
 * Class for the MatsuriBot chat bot. Uses the command prefix "/"
 *
 * @author Dylan Drescher
 */
public class MatsuriBot {
    Socket connection;

    /**
     * Constructor.
     *
     * @param cca Address to connect to
     * @param ccp Port to connect to
     */
    MatsuriBot(String cca, int ccp) throws IOException {
        this.connection = new Socket(cca, ccp); // Server socket to connect to
    }

    /**
     * Analyses messages that MatsuriBot receives in order to check for commands.
     *
     * @param message The message that was posted to the server
     */
    protected void analyseMessage(String message) throws IOException {
        DataOutputStream toServer = new DataOutputStream(this.connection.getOutputStream());
        String[] parsedMsg = message.split("\\s+");
        if (parsedMsg[1].charAt(0) == '/') {
            switch (parsedMsg[1]) {
                case "/hello":
                    System.out.println("User prompted a /hello");
                    toServer.writeBytes("[MatsuriBot] Hello! I'm MatsuriBot!\n");
                    break;

                case "/wasshoi":
                    System.out.println("User prompted a /wasshoi");
                    toServer.writeBytes("[MatsuriBot] Wasshoi!\n");
                    break;

                case "/time":
                    LocalTime time = LocalTime.now();
                    System.out.println("User prompted a /time");
                    toServer.writeBytes("[MatsuriBot] The current time is " + time + "!\n");
                    break;

                default:
                    System.out.println("User prompted an unknown command");
                    toServer.writeBytes("[MatsuriBot] Sorry! I don't know what that means!\n");
                    break;
            }
        }
    }

    /**
     * Method for when MatsuriBot is run.
     *
     * @param args Server address and port
     */
    public static void main(String[] args) throws IOException {
        String cca = AddressPort.getAddressOrPort(args, "-cca"); // getting the address of the server to connect
        int ccp = Integer.parseInt(AddressPort.getAddressOrPort(args, "-ccp")); //getting the port
        MatsuriBot bot = new MatsuriBot(cca, ccp);
        String serverOutput; // message received from server
        System.out.println("MatsuriBot running normally~.");
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
