package me.riseremi.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;

/**
 *
 * @author remi Клиент, который подключается к серверу.
 */
public class Client1 extends Network1 implements Runnable {

    //private static final String SERVER_IP = "25.183.182.14";
    public synchronized void connect() {
        try {
            final Core_v1 core = Core_v1.getInstance();
            ArrayList<Entity> entities = core.getEntities().getEntities();
            Entity player = entities.get(core.getPlayerId());

            System.out.println("Connecting to " + SERVER_IP + ":" + SERVER_PORT + "...");
            socket = new Socket(SERVER_IP, SERVER_PORT);

            System.out.println("Connected.");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            super.sendData(new NetworkMessage1(NetworkMessage1.INIT_NAME_AND_ID, player.getName() + NetworkMessage1.SEPARATOR + player.getImgId()));

            //super.sendData(new NetworkMessage(NetworkMessage.SET_FRIEND_NAME, core.getPlayer().getName()));
            core.setConnected(true);
        } catch (IOException ex) {
            System.out.println("Cannot connect to server.");
        }
    }

    //этот код выполняется в треде
    @Override
    public void run() {
        connect();
        while (true) {
            try {
                getData();
            } catch (Exception ex) {
            }
        }
    }
}
