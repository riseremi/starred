package me.riseremi.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Setter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.CardsArchive;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Creep;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.items.Item;
import me.riseremi.items.TempNameSwitch;
import me.riseremi.main.Main;

/**
 *
 * @author remi Класс сети, в котором происходит обработка всех событий,
 * полученных с другой стороны. Суперкласс для Client и Server.
 * @see Client
 * @see Server
 */
public class Network1 {

    protected static final int SERVER_PORT = 7777;
    @Setter protected static String SERVER_IP = "";

    protected ServerSocket serverSocket;
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;
    private int id, imgId, x, y, power;

    public void sendData(NetworkMessage1 message) {
        if (!message.isEmpty()) {
            try {
                out.writeUTF(message.getType() + "*" + message.getBody());
            } catch (IOException ex) {
            }
        }
    }

    public void getData() throws Exception {
        final Core_v1 core = Core_v1.getInstance();
        final Entity player = core.getEntities().getEntities().get(core.getPlayerId());
        final Friend friend = core.getFriend();
        String msgBody = in.readUTF();

        //1 - первый символ в сообщении обозначает тип сообщения
        //всего типов 10, все указаны в классе NetworkMessage
        int msgType = Integer.parseInt(msgBody.split("\\*")[0]);
        msgBody = msgBody.split("\\*")[1];

        switch (msgType) {
            case NetworkMessage1.SET_PLAYER_NAME:
                player.setName(msgBody);
                break;
            case NetworkMessage1.SET_FRIEND_IMGID:
                player.setImage(Integer.parseInt(msgBody));
                break;
            case NetworkMessage1.SET_FRIEND_NAME:
                System.out.println("input: " + msgBody);
                System.out.println("player_current: " + player.getName());
                System.out.println();
                final boolean isEquals = player.getName().equals(msgBody);

                //у игрока1 имя Игрок
                //приходит имя друга, которое тоже Игрок => у игрока2 уже имя Игрок
                //нужно сменить имя друга на Игрок
                //а имя игрока на Игрок_2
                //и отправить имя_друга Игрок_2
                if (isEquals) {
                    friend.setName(msgBody + "_2");
                    sendData(new NetworkMessage1(NetworkMessage1.SET_PLAYER_NAME, friend.getName()));
                } else {
                    friend.setName(msgBody);
                }
                break;
            case NetworkMessage1.INIT_NAME_AND_ID:
                String parts[] = parseBody(msgBody); //name:imgId

                sendData(new NetworkMessage1(NetworkMessage1.SET_FRIEND_NAME, parts[0]));
                sendData(new NetworkMessage1(NetworkMessage1.SET_FRIEND_IMGID, parts[1]));
                break;
            case NetworkMessage1.CHAT_MESSAGE:
                writeTime();
                System.out.println("DING-DONG! You have recieved a message.");
                Main.addToChat(core.getFriend().getName() + ": " + msgBody);
                break;
            case NetworkMessage1.SEND_ITEM:
                processingItem(msgBody);
                break;
            case NetworkMessage1.REMOVE_ITEM:
                removeItem(msgBody);
                break;
            case NetworkMessage1.PURE_ATTACK_TEST:
                parts = parseBody(msgBody); //id:power

                id = Integer.parseInt(parts[0]);
                power = Integer.parseInt(parts[1]);

                Entity target = core.getEntities().get(id);
                target.decreaseBloodCostHP(power);
                break;
            case NetworkMessage1.TURN_ENDED:
                core.startTurn();
                break;
            case NetworkMessage1.ATTACK_TEST:
                String parts3[] = parseBody(msgBody);

                int userId = Integer.parseInt(parts3[0]);
                int targetId = Integer.parseInt(parts3[1]);
                int cardId = Integer.parseInt(parts3[2]);

                Entity user = core.getEntities().get(userId);
                Entity targetI = core.getEntities().get(targetId);

                BasicCard card2 = CardsArchive.get(cardId);
                card2.applyEffectFromTo(user, targetI);

                System.out.println("tried to use card from " + userId + " to " + targetId);
                break;
            case NetworkMessage1.SET_PLAYER_ID:
                core.setPlayerId(Integer.parseInt(msgBody));
                core.setPlayer((Player) core.getEntities().get(core.getPlayerId()));
                break;
            case NetworkMessage1.SET_POSITION:
                System.out.println(core.getEntities().get(core.getPlayerId()).getName());
                System.out.println(core.getPlayerId());

                parts = parseBody(msgBody); //id:newX:newY

                id = Integer.parseInt(parts[0]);
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);

                System.out.println("setpos " + id);

                synchronized (core.getEntities()) {
                    if (id != core.getPlayerId()) {
                        int addX = x - core.getEntities().get(id).getX();
                        int addY = y - core.getEntities().get(id).getY();
                        core.getEntities().get(id).addToPosition(addX, addY);
                    } else {
                        core.getEntities().get(id).setX(x);
                        core.getEntities().get(id).setY(y);
                    }
                }
                break;
            case NetworkMessage1.CREATE_CREEP_TEST:
                parts = parseBody(msgBody); // name, imgId, id, x, y

                String name = parts[0];
                imgId = Integer.parseInt(parts[1]);
                id = Integer.parseInt(parts[2]);
                x = Integer.parseInt(parts[3]);
                y = Integer.parseInt(parts[4]);

                Entity e = new Creep(name, imgId, id, Entity.Type.CREEP);
                e.setX(x);
                e.setY(y);

                synchronized (core.getEntities()) {
                    core.getEntities().add(e);
                }
                if (id == 1) {
                    core.setPlayerId(1);
                }
                break;
            case NetworkMessage1.CREATE_PLAYER:
                parts = parseBody(msgBody);// name, imgId, id, x, y

                int oldImgId = player.getImgId();

                name = parts[0];
                imgId = Integer.parseInt(parts[1]);
                id = Integer.parseInt(parts[2]);
                x = Integer.parseInt(parts[3]);
                y = Integer.parseInt(parts[4]);

                Player player1 = new Player(name, imgId, id, Entity.Type.PLAYER);
                player1.setX(x);
                player1.setY(y);

                synchronized (core.getEntities()) {
                    core.getEntities().add(player1);
                }
                break;
            case NetworkMessage1.CREATE_NOT_PLAYER:
                parts = msgBody.split(NetworkMessage1.SEPARATOR);

                id = Integer.parseInt(parts[1]);
                x = Integer.parseInt(parts[2]);
                y = Integer.parseInt(parts[3]);

                Friend player2 = new Friend(parts[0], imgId, id, Entity.Type.NOT_PLAYER);
                player2.setX(x);
                player2.setY(y);

                synchronized (core.getEntities()) {
                    core.getEntities().add(player2);
                }
                break;
        }

    }

    private void processingItem(String msgBody) {
        //разбираем сообщение на куски
        String[] pieces = parseBody(msgBody);
        id = Integer.parseInt(pieces[0]);
        x = Integer.parseInt(pieces[1]);
        y = Integer.parseInt(pieces[2]);

        synchronized (Core_v1.getInstance().getWorld().getItems()) {
            Core_v1.getInstance().getWorld().getItems().add(new Item(TempNameSwitch.switchName(id) + " (id: " + id + ")", id, x, y));
        }
    }

    private void removeItem(String msgBody) {
        synchronized (Core_v1.getInstance().getWorld().getItems()) {
            Core_v1.getInstance().getWorld().getItems().remove(Integer.parseInt(msgBody));
        }
    }

    private String[] parseBody(String msgBody) {
        String[] pieces = msgBody.split(NetworkMessage1.SEPARATOR);
        return pieces;
    }

    private void writeTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        System.out.print("[" + dateFormat.format(date) + "] ");
    }
}
