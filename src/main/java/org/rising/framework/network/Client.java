package org.rising.framework.network;

import me.riseremi.main.Main;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class Client {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static Client instance;
    private int id;

    public static Client getInstance() {
        if (instance == null) {
            try {
                System.out.println("Connecting to " + Server.SERVER_IP + "...");
                instance = new Client(1234, Server.SERVER_IP);
                return instance;
            } catch (IOException ex) {
            }
        }
        return instance;
    }

    private Client(int port, String ip) throws IOException {
        Socket s = new Socket(ip, port);

        out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        in = new ObjectInputStream(s.getInputStream());

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message s = (Message) in.readObject();
                        if (Main.ENABLE_DEBUG_TOOLS) {
                            System.out.println("CLIENT RECEIVED: " + s.getType().name());
                        }
                        Protocol.processMessageOnClientSide(s);
                    } catch (IOException | ClassNotFoundException ex) {
                        if (ex instanceof EOFException) {
                            System.out.println("Server has disconnected. The game will be closed.");
                            System.exit(-1);
                        }
                    }
                }
            }
        };
        t.start();
    }

    public void send(Object message) throws IOException {
        out.writeObject(message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
