import java.io.*;
import java.net.Socket;

/**
 * Client class for the chat system. Sends messages that the user inputs to a server, which it connects to
 * and receives messages from the connected server.
 *
 * @author Dylan Drescher
 */
public class ChatClient {
    private String username;

    /**
     * Constructor
     */
    private ChatClient() {
        this.username = changeName();
    }

    /**
     * Gets a message that the user enters.
     *
     * @return Returns the message that the user enters
     */
    private String getMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String msg = "#DONOTSEND";
        try {
            msg = reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Invalid input.");
            return msg;
        }

        return "[" + this.username + "] " + msg;
    }

    /**
     * Sets the username of a particular instance of the client.
     *
     * @return The name inputted by the user
     */
    private String changeName() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a username.");
        try {
            return reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Something happened. A boring name will be given to you.");
            return "Jeff";
        }
    }

    /**
     * Method that runs when the ChatClient is run.
     *
     * @param args Server address and port
     */
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        /*Pound sign commands:
        * #DONOTSEND - client does nothing
        * #QUIT - terminate the client*/
        String csa = AddressPort.getAddressOrPort(args, "-csa"); // getting the address of the server to connect
        int csp = Integer.parseInt(AddressPort.getAddressOrPort(args, "-csp")); //getting the port
        Socket connection = new Socket(csa, csp);
        DataOutputStream toServer = new DataOutputStream(connection.getOutputStream()); // output messages to server
        Receiver receiver = new Receiver(connection); // get messages from server simultaneously
        receiver.start();
        System.out.println("Ready for message input.");
        //noinspection InfiniteLoopStatement
        while (true) {
            String toSend = client.getMessage();
            if (!toSend.equals("#DONOTSEND")) { // Message will not parse if it's equal to "#DONOTSEND". #DONOTSEND
                                                // is outputted if an exception occurs, or if the user types it in.
                toServer.writeBytes(toSend + "\n");
            }
        }
    }
}

