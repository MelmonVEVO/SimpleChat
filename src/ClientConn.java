import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Class for handling multiple client connections
 */
class ClientConn extends Thread {
    private final Socket connection;
    private ArrayBlockingQueue<String> messages;

    ClientConn(Socket s, ArrayBlockingQueue<String> b) {
        this.connection = s;
        this.messages = b;
    }

    void sendToClient(String msg) throws IOException { // sends message to a client
        DataOutputStream clientWriter = new DataOutputStream(this.connection.getOutputStream());
        clientWriter.writeUTF(msg);
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
