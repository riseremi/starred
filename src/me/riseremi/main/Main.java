package me.riseremi.main;

import lombok.Getter;
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
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author riseremi <riseremi at icloud.com>
 * @version 0.2
 */
public class Main extends JFrame implements ActionListener {

    public static final String GAME_TITLE = "Starred Classic";
    public static boolean ENABLE_DEBUG_TOOLS;
    public static boolean CARD_DUMP;
    public static Main main;
    private static JTextField chatField;
    private static JPanel panel;
    private static boolean enabled = false;
    private static JTextArea textArea;
    private static Core_v1 core;
    private final Controller controller = new Controller(false);
    private final Controller chatController = new Controller(true);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int MAX_NAME_LENGTH = 32;
    private static LoginScreen loginScreen;
    @Getter
    private static LobbyScreen lobbyScreen;
    private static DefaultCaret caret;

    public Main(String title) {
        setTitle(title);
        setBounds(10, 10, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
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

        caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        panel.add(chatField, BorderLayout.SOUTH);
        panel.add(scroll);
        chatField.setEnabled(enabled);
        textArea.setLineWrap(true);
        chatField.addKeyListener(chatController);
        add(panel, BorderLayout.SOUTH);
        panel.setVisible(false);

        LoginScreen.getHostButton().addActionListener(this);
        LoginScreen.getJoinButton().addActionListener(this);
        LobbyScreen.getGoButton().addActionListener(this);

    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        if (args.length > 0 && "--debug".equals(args[0])) {
            ENABLE_DEBUG_TOOLS = true;
        }

        if (args.length > 0 && "--dump".equals(args[0])) {
            CARD_DUMP = true;
        }

        CardsLoader cardsLoader = new CardsLoader();
        cardsLoader.loadCards("/res/json/newjson.json");

        main = new Main(GAME_TITLE);
        core = Core_v1.getInstance();
        loginScreen = new LoginScreen();

        main.add(loginScreen, BorderLayout.CENTER);
        loginScreen.setVisible(true);

        core.initBase();
        main.setVisible(true);

        System.out.println("\nStarted in " + (System.currentTimeMillis() - start) + " ms");
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
                                Client.getInstance().send(msgSetName);
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
            main.requestFocus();
        } else {
            chatField.requestFocus();
        }
    }

    public static void addToChat(String msg) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LobbyScreen.getGoButton()) {
            try {
                Server.getInstance().sendToAll(new MessageGo());
            } catch (Exception ex) {
            }
        }
        if (e.getSource() == LoginScreen.getHostButton()) {
            lobbyScreen = new LobbyScreen(true);
            core.init(loginScreen.getPreviewIndex(), LoginScreen.getIpField().getText(), LoginScreen.getNickField().getText(), true);
            loginScreen.setVisible(false);
            main.add(lobbyScreen, BorderLayout.CENTER);
            lobbyScreen.setVisible(true);
            panel.setVisible(true);
        }

        if (e.getSource() == LoginScreen.getJoinButton()) {
            lobbyScreen = new LobbyScreen(false);
            core.init(loginScreen.getPreviewIndex(), LoginScreen.getIpField().getText(), LoginScreen.getNickField().getText(), false);
            loginScreen.setVisible(false);
            main.add(lobbyScreen, BorderLayout.CENTER);
            lobbyScreen.setVisible(true);
            panel.setVisible(true);
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
