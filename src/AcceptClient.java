import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class AcceptClient extends Thread {
    private ArrayList<ClientConn> clients;
    private ServerSocket serverConnections;
    private ArrayBlockingQueue<String> messages;

    AcceptClient(ArrayList<ClientConn> clients, ServerSocket serverConnections, ArrayBlockingQueue<String> messages) {
        this.clients = clients;
        this.serverConnections = serverConnections;
        this.messages = messages;
    }

    public void run() {
        while (true) {
            try {
                Socket clientConnection = serverConnections.accept();
                clients.add(new ClientConn(clientConnection, messages));
                clients.get(clients.size() - 1).start(); //starts the newly created thread
                System.out.println("New connection established: " + clientConnection);
            }
            catch (IOException ignored) {}
        }
    }
}
