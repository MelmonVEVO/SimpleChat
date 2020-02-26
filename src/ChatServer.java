import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Server class for the chat system.
 */
public class ChatServer {
    private ArrayList<ClientConn> clients = new ArrayList<>();
    private ArrayBlockingQueue<String> messages = new ArrayBlockingQueue<>(99);

    /**
     * Broadcasts a gotten message to each connected client
     *
     * @param msg The message itself
     */
    private void broadcast(String msg) throws IOException {
        for (int x = 0; x < this.clients.size()-1; x++) {
            this.clients.get(x).sendToClient(msg);
        }
    }

    /**
     * Server running
     *
     * @param args Port number
     */
    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        int csp = Integer.parseInt(args[0]);
        ServerSocket serverConnections;
        try {
            serverConnections = new ServerSocket(csp);
        }
        catch (IOException e) {
            serverConnections = new ServerSocket(14001);
        }
        System.out.println("IT WORKS");
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Socket clientConnection = serverConnections.accept();
                server.clients.add(new ClientConn(clientConnection, server.messages));
                server.clients.get(server.clients.size() - 1).start(); //starts the newly created thread
                System.out.println("New connection established: " + clientConnection);
                String broadcastMessage = server.messages.take();
                server.broadcast(broadcastMessage);
            }
            catch (IOException ignored) {}
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
