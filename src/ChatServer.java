import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server class for the chat system.
 */
public class ChatServer {
    private LinkedBlockingDeque<ClientConn> clients = new LinkedBlockingDeque<>();
    private LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>();

    /**
     * Broadcasts a gotten message to each connected client
     *
     * @param msg The message itself
     */
    private synchronized void broadcast(String msg) throws IOException {
        for (ClientConn client : this.clients) {
            client.sendToClient(msg);
        }
    }

    /**
     * Server running
     *
     * @param args Port number
     */
    public static void main(String[] args) throws IOException {
        int csp;
        ChatServer server = new ChatServer();
        try {
            csp = Integer.parseInt(args[0]); // port to establish to
        }
        catch (ArrayIndexOutOfBoundsException e) {
            csp = 14001; // default port
        }
        ServerSocket serverConnections = new ServerSocket(csp); // making a new server socket for clients to connect to
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
