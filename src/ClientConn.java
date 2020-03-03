import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class for handling a client connection called by the ChatServer
 */
class ClientConn extends Thread {
    private final Socket connection;
    private LinkedBlockingQueue<String> messages;

    /**
     * Sets the connection and messages for the individual client connection
     *
     * @param s The client socket
     * @param b The messages queue
     */
    ClientConn(Socket s, LinkedBlockingQueue<String> b) {
        this.connection = s;
        this.messages = b;
    }

    /**
     * Sends a message to the connected client
     *
     * @param msg The message itself
     */
    void sendToClient(String msg) throws IOException { // sends message to a client
        DataOutputStream toClient = new DataOutputStream(this.connection.getOutputStream());
        try {
            toClient.writeUTF(msg);
        } catch (NullPointerException e) {
            System.out.printf("Should print %s", msg);
            e.printStackTrace();
        }
    }

    /**
     * The client connection process. Mostly retrieves messages from the client and sends to the server.
     */
    @Override
    public void run() {
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            String clientOutput; // The text received by a client when they input a message
            //noinspection InfiniteLoopStatement
            while (true) {
                clientOutput = clientReader.readLine();
                //System.out.println(clientOutput);
                this.messages.add(clientOutput);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
