package org.rising.framework.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import me.riseremi.main.Main;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Server {

    public static String SERVER_IP;
    private ServerSocket serverSocket;
    private ArrayList<Connection> clients = new ArrayList<>();
    private int connections;
    private static Server instance;
    // TODO: 11/19/18 Inject a single instance
    private final Protocol protocol = new ClientSeverProtocol();

    public static Server getInstance() {
        if (instance == null) {
            try {
                instance = new Server(1234);
                return instance;
            } catch (IOException ex) {
            }
        }
        return instance;
    }

    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    final Connection connection = new Connection(socket, connections++);
                    clients.add(connection);
                } catch (IOException ex) {
                }
            }
        });
        t.start();
    }

    public void sendToAll(Object message) throws IOException {
        for (int j = 0; j < clients.size(); j++) {
            Connection c = clients.get(j);
            c.send(message);
        }
    }

    public void sendToAllExcludingOne(Object message, int id) throws IOException {
        for (Connection connection : clients) {
            if (connection.getId() != id) {
                connection.send(message);
            }
        }
    }

    public void sendToOne(Object message, int index) throws IOException {
        clients.get(index).send(message);
    }

    class Connection {

        private ObjectInputStream in;
        private ObjectOutputStream out;
        private final int id;

        public Connection(Socket socket, final int id) throws IOException {
            this.id = id;
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Message s = (Message) in.readObject();
                        if (Main.ENABLE_DEBUG_TOOLS) {
                            System.out.println("SERVER RECIEVED: " + s.getType().name());
                        }
                        protocol.processMessageOnServerSide(s, id);
                    } catch (IOException | ClassNotFoundException ex) {
                    }
                }
            });
            t.start();
        }

        public int getId() {
            return id;
        }

        public void send(Object message) throws IOException {
            out.writeObject(message);
        }

    }
}
