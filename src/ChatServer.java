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
    private synchronized void broadcast(String msg) throws IOException {
        for (int x = 0; x < this.clients.size(); x++) {
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
        int csp = Integer.parseInt(args[0]); // port to connect to
        ServerSocket serverConnections; // making a new server socket for clients to connect to
        try {
            serverConnections = new ServerSocket(csp);
        }
        catch (IOException e) {
            serverConnections = new ServerSocket(14001);
        }
        AcceptClient acceptor = new AcceptClient(server.clients, serverConnections, server.messages);
        acceptor.start();
        System.out.println("Ready for clients");
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                String broadcastMessage = server.messages.poll();
                //System.out.println(broadcastMessage);
                if (broadcastMessage != null) {
                    server.broadcast(broadcastMessage);
                }
            }
            catch (IOException ignored) {}
        }
    }
}
