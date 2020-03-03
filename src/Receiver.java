import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
     * @param address Server address
     * @param port Server port
     */
    Receiver(String address, int port) {
        try {
            this.connection = new Socket(address, port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
