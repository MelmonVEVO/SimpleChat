import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class for the Receiver thread that is called by the ChatClient that
 * constantly tries to receive messages from the server.
 *
 * @author Dylan Drescher
 */
class Receiver extends Thread {
    private Socket connection;

    /**
     * Constructor.
     *
     * @param client The client socket to use for receiving messages. This will be the same socket as the client
     *               which is running the Receiver has
     */
    Receiver(Socket client) {
        this.connection = client;
    }

    /**
     * Function that happens when the Receiver thread is started.
     */
    @Override
    public void run() {
        BufferedReader serverReader = null;
        try {
            serverReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        String serverOutput;
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                serverOutput = serverReader.readLine();
                System.out.println(serverOutput);
            }
            catch (IOException ignored) {}
        }
    }
}
