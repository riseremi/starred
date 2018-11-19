package me.riseremi.main;

import me.riseremi.controller.Controller;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.json.CardsLoader;
import me.riseremi.network.messages.MessageChat;
import me.riseremi.network.messages.MessageGo;
import me.riseremi.network.messages.MessageSetName;
import me.riseremi.ui.windows.LobbyScreen;
import me.riseremi.ui.windows.LoginScreen;
import org.rising.framework.network.Client;
import org.rising.framework.network.Server;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author riseremi <riseremi at icloud.com>
 * @version 0.2
 */
public class Main extends JFrame implements ActionListener {

    public static final String GAME_TITLE = "Starred Classic";
    public static boolean ENABLE_DEBUG_TOOLS = false;
    public static boolean ENABLE_SERVERLESS_MODE = false;
    public static Main main;
    private static JTextField chatField;
    private static JPanel chatPanel;
    private static boolean enabled = false;
    private static JTextArea textArea;
    private static Core_v1 core;
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int MAX_NAME_LENGTH = 32;
    private static LoginScreen loginScreen;
    private static LobbyScreen lobbyScreen;

    public Main(String title) {
        System.setProperty("sun.java2d.opengl", "True");

        createWindow(this, title);
        createChatUI(this);

        Controller controller = new Controller(false);
        this.addKeyListener(controller);

        LoginScreen.getHostButton().addActionListener(this);
        LoginScreen.getJoinButton().addActionListener(this);
        LobbyScreen.getGoButton().addActionListener(this);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        if (args.length > 0) {
            if (Arrays.asList(args).contains("--debug")) {
                ENABLE_DEBUG_TOOLS = true;
            }

            if (Arrays.asList(args).contains("--serverless")) {
                ENABLE_SERVERLESS_MODE = true;
            }
        }

        CardsLoader cardsLoader = new CardsLoader();
        cardsLoader.loadCards("json/newjson.json");

        main = new Main(GAME_TITLE);
        core = Core_v1.getInstance();
        loginScreen = new LoginScreen();

        main.add(loginScreen, BorderLayout.CENTER);
        loginScreen.setVisible(true);

        core.initBase();
        main.setVisible(true);

        System.out.println("\nStarted in " + (System.currentTimeMillis() - start) + " ms");
    }

    private static void createWindow(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setBounds(10, 10, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
    }

    private static void createChatUI(JFrame frame) {
        chatField = new JTextField("");
        chatPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(Color.BLACK);
        chatPanel.setPreferredSize(new Dimension(0, 100));

        JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatPanel.add(chatField, BorderLayout.SOUTH);
        chatPanel.add(scroll);
        chatField.setEnabled(enabled);
        textArea.setLineWrap(true);
        Controller chatController = new Controller(true);
        chatField.addKeyListener(chatController);
        chatPanel.setVisible(false);

        frame.add(chatPanel, BorderLayout.SOUTH);
        chatPanel.setVisible(true);
    }

    // Enter pressed
    public static void toggleChat() {
        enabled = !enabled;
        chatField.setEnabled(enabled);
        if (!enabled) {
            final String msgText = chatField.getText().length() > 256 ? chatField.getText().substring(0, 256) : chatField.getText();
            if (msgText.length() > 0) {
                final Core_v1 core2 = Core_v1.getInstance();
                String[] pieces = msgText.split(" ");

                if ((msgText.startsWith("/") && pieces.length >= 2)) {
                    int firstSpace = msgText.indexOf(' ');
                    String command = msgText.substring(0, firstSpace).toLowerCase();
                    String msgBody = msgText.substring(firstSpace + 1).replaceAll(" ", "_");
                    msgBody = msgBody.length() >= MAX_NAME_LENGTH ? msgBody.substring(0, MAX_NAME_LENGTH) : msgBody;

                    switch (command) {
                        case "/setname":
                            core2.getPlayer().setName(msgBody);
                            try {
                                MessageSetName msgSetName = new MessageSetName(msgBody, Core_v1.getInstance().getPlayer().getId());
                                Core_v1.getClient().send(msgSetName);
                            } catch (IOException ignored) {
                            }
                            break;
                    }
                } else {
                    try {
                        Core_v1.getClient().send(new MessageChat(msgText + "\n\r", core.getPlayer().getId()));
                    } catch (IOException ignored) {
                    }
                }
            }
            chatField.setText("");
            main.requestFocus();
        } else {
            chatField.requestFocus();
        }
    }

    public static void addToChat(String msg) {
        if (Main.chatField == null) return;

        textArea.setBackground(Color.GRAY);
        textArea.append(msg);
        textArea.setBackground(Color.WHITE);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public static void go() {
        SwingUtilities.invokeLater(() -> {
            lobbyScreen.setVisible(false);
            main.add(core);
            main.requestFocus();
        });
    }

    public static LobbyScreen getLobbyScreen() {
        return Main.lobbyScreen;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LobbyScreen.getGoButton()) {
            try {
                Core_v1.getServer().sendToAll(new MessageGo());
            } catch (Exception ignored) {
            }
        }
        if (e.getSource() == LoginScreen.getHostButton()) initAsServer();
        if (e.getSource() == LoginScreen.getJoinButton()) initAsClient();
    }

    private void init(boolean isServer) {
        String ip = LoginScreen.getIpField().getText();
        String nickname = LoginScreen.getNickField().getText();

        lobbyScreen = new LobbyScreen(isServer);
        core.init(loginScreen.getPreviewIndex(), ip, nickname, isServer);
        loginScreen.setVisible(false);
        main.add(lobbyScreen, BorderLayout.CENTER);
        lobbyScreen.setVisible(true);
    }

    private void initAsServer() {
        init(true);
    }

    private void initAsClient() {
        init(false);
    }

    public static void setUIFont(FontUIResource f) {
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
