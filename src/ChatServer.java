import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server class for the chat system.
 *
 * @author Dylan Drescher
 */
public class ChatServer {
    private LinkedBlockingDeque<ClientConn> clients = new LinkedBlockingDeque<>(); // List of clients connected to server
    private LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>(); // Queue of messages to be broadcasted

    /**
     * Broadcasts a gotten message to each connected client.
     *
     * @param msg The message itself
     */
    private synchronized void broadcast(String msg) {
        for (ClientConn client : this.clients) {
            try {
                client.sendToClient(msg);
            }
            catch (IOException ignored) {}
        }
    }

    /**
     * Server process and running. Opens on port 14001 by default, and starts threads for accepting clients.
     * Constantly reads messages from the messages queue and broadcasts them.
     *
     * @param args Port number
     */
    public static void main(String[] args) throws IOException {
        int csp = Integer.parseInt(AddressPort.getAddressOrPort(args, "-csp"));
        ChatServer server = new ChatServer();
        ServerSocket serverConnections = new ServerSocket(csp); // making a new server socket for clients to connect to
        AcceptClient acceptor = new AcceptClient(server.clients, serverConnections, server.messages);
        acceptor.start();
        System.out.println("Ready for clients");
        //noinspection InfiniteLoopStatement
        while (true) {
            String broadcastMessage = server.messages.poll();
            //System.out.println(broadcastMessage);
            if (broadcastMessage != null) {
                System.out.println(broadcastMessage);
                server.broadcast(broadcastMessage);
            }
        }
    }
}
