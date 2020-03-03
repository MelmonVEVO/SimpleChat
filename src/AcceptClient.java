import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class run by the ChatServer that constantly accepts clients.
 *
 * @author Dylan Drescher
 */
public class AcceptClient extends Thread {
    private LinkedBlockingDeque<ClientConn> clients;
    private ServerSocket serverConnections;
    private LinkedBlockingQueue<String> messages;

    /**
     * Constructor
     *
     * @param clients List of clients, linked to the server
     * @param serverConnections The server socket
     * @param messages List of messages, linked to the server
     */
    AcceptClient(LinkedBlockingDeque<ClientConn> clients, ServerSocket serverConnections, LinkedBlockingQueue<String> messages) {
        this.clients = clients;
        this.serverConnections = serverConnections;
        this.messages = messages;
    }

    /**
     * Function that constantly listens for new client connections and adds them to the clients deque.
     */
    @Override
    public void run() {
    //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Socket clientConnection = serverConnections.accept();
                clients.add(new ClientConn(clientConnection, messages));
                Objects.requireNonNull(clients.peekLast()).start(); //starts the newly created thread
                System.out.println("New connection established: " + clientConnection);
            }
            catch (IOException ignored) {}
        }
    }
}
