package me.riseremi.network;

/**
 *
 * @author remi Класс сообщения, которое отправляется по сети. Формально объекты
 * этого класса создаются прямо перед отправкой сообщения на другую сторону,
 * т.е. нет никакого промежуточного места их хранения.
 */
public class NetworkMessage1 {

    private final int type;
    private final String sBody;
    //private final String body[];
    public final static String SEPARATOR = ":";
    public static final int CHAT_MESSAGE = 1;
    public static final int SEND_ITEM = 2;
    public static final int REMOVE_ITEM = 3;
    //public static final int PLAYER_MOVEMENT = 4;
    public static final int ATTACK_TEST = 5;
    public static final int TURN_ENDED = 6;
    //public static final int SET_PLAYER_POSITION = 7;
    public static final int SET_PLAYER_NAME = 8;
    public static final int SET_FRIEND_NAME = 9;
    public static final int PURE_ATTACK_TEST = 10;
    public static final int CREATE_CREEP_TEST = 11;
    public static final int SET_POSITION = 12;
    public static final int SET_PLAYER_ID = 13;
    public static final int CREATE_PLAYER = 14;
    public static final int CREATE_NOT_PLAYER = 15;
    public static final int INIT_NAME_AND_ID = 16;
    public static final int SET_FRIEND_IMGID = 17;

//    public NetworkMessage(int type, String body) {
//        this.type = type;
//        this.body = new String[1];
//        this.body[0] = body;
//    }
//    public NetworkMessage(int type, int... body) {
//        this.type = type;
//        this.body = new String[body.length];
//        for (int i = 0; i < body.length; i++) {
//            if (i != body.length) {
//                this.body[i] = body[i] + SEPARATOR;
//            } else {
//                this.body[i] = "" + body[i];
//            }
//        }
//    }
    public NetworkMessage1(int type, String body) {
        this.type = type;
        this.sBody = body;
    }
//
//    public NetworkMessage(int type, int s, int i1, int i2) {
//        this.type = type;
//        this.body = new String[3];
//        this.body[0] = s + SEPARATOR;
//        this.body[1] = i1 + SEPARATOR;
//        this.body[2] = "" + i2;
//    }
//
//    public NetworkMessage(int type, int s, int i1) {
//        this.type = type;
//        this.body = new String[2];
//        this.body[0] = s + SEPARATOR;
//        this.body[1] = "" + i1;
//    }
//
//    public NetworkMessage(int type, String s, int i1, int i2, int i3) {
//        this.type = type;
//        this.body = new String[4];
//        this.body[0] = s + SEPARATOR;
//        this.body[1] = "" + i1;
//        this.body[2] = SEPARATOR + i2;
//        this.body[3] = SEPARATOR + i3;
//
//    }
//    public NetworkMessage(int type, String s, int... body) {
//        this.type = type;
//        this.body = new String[body.length + 1];
//        this.body[0] = s;
////        if (body.length > 0) {
////            for (int i = 1; i < body.length; i++) {
////                if (i != body.length) {
////                    this.body[i] = body[i] + SEPARATOR;
////                } else {
////                    this.body[i] = "" + body[i];
////                }
////            }
////        }
//        try {
//            System.out.println(this.body[0] + type + "\r\n");
//            System.out.println(this.body[1] + type + "\r\n");
//            System.out.println(this.body[2] + type + "\r\n");
//            System.out.println(this.body[3] + type + "\r\n");
//        } catch (Exception ex) {
//        }
//    }

    public int getType() {
        return type;
    }

    public String getBody() {
        return sBody;
    }

//    public String getBody() {
//        String string = "";
//        for (String body1 : body) {
//            string += body1;
//        }
//        System.out.println(string);
//        return string;
//    }
    public boolean isEmpty() {
        return getBody().length() == 0;
    }
}
