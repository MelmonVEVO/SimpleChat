import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class for handling a client connection called by the ChatServer.
 * Constantly reads from the client, and contains a function for sending to the client
 */
class ClientConn extends Thread {
    private final Socket connection;
    private LinkedBlockingQueue<String> messages;

    /**
     * Sets the connection and messages for the individual client connection
     *
     * @param conn The client socket
     * @param msgs The messages queue
     */
    ClientConn(Socket conn, LinkedBlockingQueue<String> msgs) {
        this.connection = conn;
        this.messages = msgs;
    }

    /**
     * Sends a message to the connected client
     *
     * @param msg The message itself
     */
    void sendToClient(String msg) throws IOException { // sends message to a client
        DataOutputStream toClient = new DataOutputStream(this.connection.getOutputStream());
        try {
            toClient.writeBytes(msg + "\n");
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
                this.messages.add(clientOutput);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
