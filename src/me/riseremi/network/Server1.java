package me.riseremi.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.network.messages.CreateCreepMessage;
import me.riseremi.network.messages.CreateNonPlayerMessage;
import me.riseremi.network.messages.CreatePlayerMessage;

/**
 *
 * @author remi Сервер, который ждёт подключения клиента. По сути это точно
 * такой же клиент, никакая обработка данных тут не производятся.
 */
public class Server1 extends Network1 implements Runnable {

    public synchronized void init() {
        try {
            final Core_v1 core = Core_v1.getInstance();

            System.out.println("Starting server...");
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started.");
            System.out.println("Note: You cannot move while client not connected.");

            socket = serverSocket.accept();

            //Main.getCore().getFriend().setPaint(true);
            System.out.println("Connection from: " + socket.getInetAddress());
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            //read friend name and icon id
            try {
                getData();
            } catch (Exception ex) {
            }
//            super.sendData(new NetworkMessage(NetworkMessage.SET_FRIEND_NAME, core.getPlayer().getName()));
            super.sendData(new NetworkMessage1(NetworkMessage1.INIT_NAME_AND_ID, core.getPlayer().getName() + NetworkMessage1.SEPARATOR + core.getPlayer().getImgId()));

            for (Entity e : core.getEntities().getEntities()) {
                final String name = e.getName();
                final int id = e.getId();
                final int x = e.getX();
                final int y = e.getY();

                switch (e.getType()) {
                    case CREEP:
//                        super.sendData(new CreateCreepMessage(name, e.getImgId(), id, x, y));
                        break;
                    case PLAYER:
//                        super.sendData(new CreatePlayerMessage(name, e.getImgId(), id, x, y));
                        break;
                    case NOT_PLAYER:
//                        super.sendData(new CreateNonPlayerMessage(name, e.getImgId(), id, x, y));
                        break;
                }
//                if (e.getId() == 1) {
//                    super.sendData(new CreatePlayerMessage(e.getName(), e.getId(), e.getX(), e.getY()));
//                } else {
//                    super.sendData(new CreateCreepMessage(e.getName(), e.getId(), e.getX(), e.getY()));
//
//                }

                //System.out.println(e.getId());
            }
            //core.setPlayerId(0);
            super.sendData(new NetworkMessage1(NetworkMessage1.SET_PLAYER_ID, "1"));

//             for (int i = core.getEntities().size(); i > 0; i--) {
//                Entity e = core.getEntities().get(i);
//                super.sendData(new NetworkMessage(NetworkMessage.CREATE_CREEP_TEST, e.getName() + ":" + e.getX() + ":" + e.getY()));
//            }
            //super.sendData(null);
            core.setConnected(true);
        } catch (IOException ex) {
            System.out.println("Cannot start server.");
        }

    }

    @Override
    public void run() {
        init();
        while (true) {
            try {
                getData();
            } catch (Exception ex) {
            }
        }
    }
}
