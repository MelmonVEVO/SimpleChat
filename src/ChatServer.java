import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Server class for the chat system.
 */
public class ChatServer {
    private ArrayList<ClientConn> clients = new ArrayList<>();
    private BlockingQueue<String> messages;

    private void broadcast(String msg) throws IOException {
        for (int x = 0; x < this.clients.size()-1; x++) {
            this.clients.get(x).sendToClient(msg);
        }
    }

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
