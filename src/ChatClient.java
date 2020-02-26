import java.io.*;
import java.net.Socket;

/**
 * Client class for the chat system.
 *
 * @author Dylan Drescher
 */
public class ChatClient {
    private String username;

    /**
     * Gets a message that the user enters.
     *
     * @return Returns the message that the user enters
     */
    private String getMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String msg = "#DONOTSEND";
        System.out.print("> ");
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
     */
    private void changeName() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a username.");
        System.out.print("> ");
        try {
            this.username = reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Something happened. A boring name will be given to you.");
            this.username = "Jeff";
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
        client.changeName();
        String cca = args[0];
        String ccp = args[1];
        Socket connection = new Socket(cca, Integer.parseInt(ccp));
        DataOutputStream toServer = new DataOutputStream(connection.getOutputStream()) {
            @Override
            public void write(int b) throws IOException {

            }
        };
        Receiver receiver = new Receiver(cca, ccp);
        receiver.start();
        System.out.println("Ready for messages.");
        //noinspection InfiniteLoopStatement
        while (true) {
            String toSend = client.getMessage();
            if (!toSend.equals("#DONOTSEND")) { // Message will parse if it's not equal to #DONOTSEND
                toServer.writeChars(toSend + "\n");
            }
        }
    }
}

