import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Class for handling multiple client connections
 */
class ClientConn extends Thread {
    private final Socket connection;
    private BlockingQueue<String> messages;

    ClientConn(Socket s, BlockingQueue<String> b) {
        this.connection = s;
        this.messages = b;
    }

    void sendToClient(String msg) throws IOException { // sends message to a client
        PrintWriter clientWriter = new PrintWriter(this.connection.getOutputStream());
        clientWriter.println(msg);
    }

    @Override
    public void run() { // mostly retrieves messages from the client and sends to the server
        try {
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            String clientOutput; // The text received by a client when they input a message
            //noinspection InfiniteLoopStatement
            while (true) {
                clientOutput = clientReader.readLine();
                System.out.println(clientOutput);
                this.messages.add(clientOutput);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
