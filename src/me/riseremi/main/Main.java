package me.riseremi.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import me.riseremi.controller.Controller;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.json.StringUtils;
import me.riseremi.network.messages.MessageChat;
import me.riseremi.network.messages.MessagePing;
import me.riseremi.network.messages.MessageSetName;
import me.riseremi.ui.windows.LoginScreen;
import org.rising.framework.network.Client;
import org.rising.framework.network.Server;

/**
 *
 * @author remi
 * @version 0.2
 */
public class Main extends JFrame implements ActionListener {

    public static Main game;
    private static JTextField chatField;
    private static JPanel panel;
    private static boolean enabled = false;
    private static JTextArea textArea;
    private static Core_v1 core;
    private final Controller controller = new Controller(false);
    private final Controller chatController = new Controller(true);
//    private static JPanel loginScreen;
    //UI elements for login screen
//    private static JButton server = new RButton("Host"),
//            client = new RButton("Connect");
//    private static JTextField name = new RTextField("Player"),
//            ip = new RTextField("127.0.0.1");
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int MAX_NAME_LENGTH = 32;
    private static LoginScreen loginScreen1;

    public Main(String title) {
        Core_v1.getInstance();
        setTitle(title);
        //setBounds(10, 10, Global.windowWidth, Global.windowHeight);
        setBounds(10, 10, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
//        setUndecorated(true);////////////////////////////////////////
        addKeyListener(controller);

        this.setLayout(new BorderLayout());

        chatField = new JTextField("");
        panel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(Color.BLACK);
        panel.setPreferredSize(new Dimension(0, 100));

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        panel.add(chatField, BorderLayout.SOUTH);
        panel.add(scroll);
        chatField.setEnabled(enabled);
        textArea.setLineWrap(true);
        chatField.addKeyListener(chatController);
        add(panel, BorderLayout.SOUTH);
        panel.setVisible(false);

        //creating login screen
//        loginScreen = new JPanel();
//        loginScreen.setLayout(null);
        int yOffset = 150;

        int frameWidth = getFrames()[0].getWidth();
        setUIFont(new javax.swing.plaf.FontUIResource(MAIN_FONT));
//
//        name.setBounds(frameWidth / 4, 100 + yOffset, frameWidth / 2, 24);
//        ip.setBounds(frameWidth / 4, 130 + yOffset, frameWidth / 2, 24);
//        server.setBounds(frameWidth / 4, 160 + yOffset, frameWidth / 4, 32);
//        client.setBounds(frameWidth / 2, 160 + yOffset, frameWidth / 4, 32);
//
//        loginScreen.add(name);
//        loginScreen.add(ip);
//        loginScreen.add(server);
//        loginScreen.add(client);

        LoginScreen.getServer().addActionListener(this);
        LoginScreen.getClient().addActionListener(this);
        //client.addActionListener(this);
    }

    public static void main(String[] args) throws Exception {
        StringUtils su = new StringUtils();
        su.bleh();
        
        game = new Main("Game");
        core = Core_v1.getInstance();
        loginScreen1 = new LoginScreen();

        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        game.add(loginScreen1, BorderLayout.CENTER);
        loginScreen1.setVisible(true);

        //game.add(loginScreen, BorderLayout.CENTER);
        //loginScreen.setVisible(true);
        core.initBase();
        //core.init();
        //game.add(core);
        game.setVisible(true);

        //only hard only test
        String helpText = "У карт есть радиус использования: у магической — две клетки, у физической — одна. Чтобы использовать карту, нужно подойти к врагу на нужное расстояние и кликнуть на карту в столбике справа. Если по окончании раунда у героя остались AP, в следующем ходу ему начислится одно бонусное AP.";
        JFrame help = new JFrame();
        final JTextArea jTextArea = new JTextArea(helpText);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        help.add(jTextArea, BorderLayout.CENTER);
        help.setSize(new Dimension(320, 240));
        help.setPreferredSize(new Dimension(320, 240));
        help.setLocation(100, 100);
        //help.setVisible(true);
    }

    //if Enter pressed
    public static void toggleChat() {
        enabled = !enabled;
        chatField.setEnabled(enabled);
        if (!enabled) {
            final String msgText = chatField.getText().length() > 256 ? chatField.getText().substring(0, 256) : chatField.getText();
            if (msgText.length() > 0) {
                final Core_v1 core2 = Core_v1.getInstance();
                String[] pieces = msgText.split(" ");

                if (msgText.startsWith("/") && pieces.length >= 2) {
                    int firstSpace = msgText.indexOf(' ');
                    String command = msgText.substring(0, firstSpace).toLowerCase();
                    String msgBody = msgText.substring(firstSpace + 1).replaceAll(" ", "_");
                    msgBody = msgBody.length() >= MAX_NAME_LENGTH ? msgBody.substring(0, MAX_NAME_LENGTH) : msgBody;

                    switch (command) {
                        case "/setname":
                            core2.getPlayer().setName(msgBody);
                            try {
                                //core2.getNetwork().sendData(new NetworkMessage(NetworkMessage.SET_FRIEND_NAME, msgBody));
                                MessageSetName msgSetName = new MessageSetName(msgBody, Core_v1.getInstance().getPlayer().getId());
                                Client.getInstance().send(msgSetName);
                            } catch (IOException ex) {
                            }
                            break;
                        case "/ping":
                            MessagePing msg = new MessagePing(System.currentTimeMillis(), false);
                            try {
                                Server.getInstance().sendToAll(msg);
                            } catch (IOException ex) {
                            }
                            break;
                    }
                } else {
                    try {
                        Client.getInstance().send(new MessageChat(msgText + "\n\r", core.getPlayer().getId()));
                    } catch (IOException ex) {
                    }
                }
            }
            chatField.setText("");
            game.requestFocus();
            //.requestFocus();
        } else {
            chatField.requestFocus();
        }
    }

    public static void addToChat(String msg) {
        textArea.setBackground(Color.GRAY);
        textArea.append(msg);
        textArea.setBackground(Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LoginScreen.getServer()) {
            core.init(loginScreen1.getPreviewIndex(), LoginScreen.getIp().getText(), LoginScreen.getNick().getText(), true);
            game.add(core);
            loginScreen1.setVisible(false);
            panel.setVisible(true);
            game.remove(loginScreen1);
            game.requestFocus();
        }
        if (e.getSource() == LoginScreen.getClient()) {
            core.init(loginScreen1.getPreviewIndex(), LoginScreen.getIp().getText(), LoginScreen.getNick().getText(), false);
            game.add(core);
            loginScreen1.setVisible(false);
            panel.setVisible(true);
            game.remove(loginScreen1);
            game.requestFocus();
        }
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
