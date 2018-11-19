package org.rising.framework.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class Client {
    private static final boolean DEBUG = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int id;

    public Client(int port, String ip, Protocol protocol) throws IOException {
        Socket s = new Socket(ip, port);

        out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        in = new ObjectInputStream(s.getInputStream());

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Message s1 = (Message) in.readObject();
                    if (DEBUG) {
                        System.out.println("CLIENT RECEIVED: " + s1.getType().name());
                    }
                    protocol.processMessageOnClientSide(s1);
                } catch (IOException | ClassNotFoundException ex) {
                    if (ex instanceof EOFException) {
                        System.out.println("Server has disconnected. The game will be closed.");
                        System.exit(-1);
                    }
                }
            }
        });
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
