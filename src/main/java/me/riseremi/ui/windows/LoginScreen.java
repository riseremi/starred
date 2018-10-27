package me.riseremi.ui.windows;

import me.riseremi.main.Main;
import me.riseremi.ui.RButton;
import me.riseremi.ui.RTextField;
import me.riseremi.utils.NameGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static me.riseremi.main.Main.setUIFont;
import static me.riseremi.utils.ResourceUtilsKt.loadImage;
import static me.riseremi.utils.ResourceUtilsKt.loadUrl;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class LoginScreen extends JPanel implements ActionListener {

    private final BufferedImage heroPreview;
    private BufferedImage back;
    private final int NUM_OF_HEROES = 15;
    private final BufferedImage[] heroPreviews = new BufferedImage[NUM_OF_HEROES];
    private final String PREFIX = "sprites/hero";
    private final String POSTFIX = "";
    private final JButton preview;
    private final RButton next, previous;
    private int previewIndex = 0;
    //
    private static final JButton hostButton = new RButton("Host", false),
            joinButton = new RButton("Join", false), newNickButton = new RButton("<", false);
    private static final JTextField nickField = new RTextField(NameGenerator.INSTANCE.getName()),
            ipField = new RTextField("127.0.0.1");

    //Colors
    private static final Color BACKGROUND_MAIN = new Color(217, 219, 218);
    private static final Color HEADER = new Color(66, 163, 244);
    private static final Color FORM_BACKGROUND = Color.WHITE;

    public LoginScreen() {
        int yOffset = 150;

        //kekeke
        nickField.setEditable(false);

        int frameWidth = Main.getFrames()[0].getWidth();
        setUIFont(new javax.swing.plaf.FontUIResource(Main.MAIN_FONT));
        //
        nickField.setBounds(frameWidth / 4, 100 + yOffset, frameWidth / 2 - 32, 32);
        newNickButton.setBounds(frameWidth / 4 + nickField.getWidth(), 100 + yOffset, 32, 32);

        ipField.setBounds(frameWidth / 4, 100 + nickField.getHeight() + yOffset,
                frameWidth / 2, 32);

        hostButton.setBounds(frameWidth / 4, ipField.getY() + ipField.getHeight(),
                frameWidth / 4, 32);
        joinButton.setBounds(frameWidth / 2, ipField.getY() + ipField.getHeight(),
                frameWidth / 4, 32);

        add(nickField);
        add(newNickButton);
        add(ipField);
        add(hostButton);
        add(joinButton);

        hostButton.addActionListener(this);
        joinButton.addActionListener(this);
        newNickButton.addActionListener(this);

        //
        heroPreview = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        try {
            back = loadImage("back0.png");
        } catch (IOException ex) {
        }

        for (int i = 0; i < NUM_OF_HEROES; i++) {
            try {
                heroPreviews[i] = loadImage(PREFIX + i + ".png");
            } catch (IOException ex) {
            }

        }

        String imageName1 = "next";
        String imageName2 = "previous";

        String imgLocation1 = "buttons/" + imageName1 + ".png";
        String imgLocation2 = "buttons/" + imageName2 + ".png";

        URL imageURL1 = loadUrl(imgLocation1);
        URL imageURL2 = loadUrl(imgLocation2);

        next = new RButton("", true);
        next.addActionListener(this);
        next.setIcon(new ImageIcon(imageURL1, "Next"));

        previous = new RButton("", true);
        previous.addActionListener(this);
        previous.setIcon(new ImageIcon(imageURL2, "Previous"));

        preview = new RButton("", true);
        preview.setIcon(new ImageIcon(heroPreviews[0]));

        final JLabel jLabel = new JLabel("Select your icon");
        //jLabel.setForeground(Color.WHITE);
        jLabel.setFont(Main.MAIN_FONT);
        jLabel.setBounds(frameWidth / 4, 160, 128, 32);
        add(jLabel);

        next.setBounds(frameWidth / 4 + 48, 190, 16, 32);
        preview.setBounds(frameWidth / 4 + 16, 190, 32, 32);
        previous.setBounds(frameWidth / 4, 190, 16, 32);

        setLayout(null);

        add(preview);
        add(next);
        add(previous);

    }

    public static JButton getHostButton() {
        return LoginScreen.hostButton;
    }

    public static JButton getJoinButton() {
        return LoginScreen.joinButton;
    }

    public static JButton getNewNickButton() {
        return LoginScreen.newNickButton;
    }

    public static JTextField getNickField() {
        return LoginScreen.nickField;
    }

    public static JTextField getIpField() {
        return LoginScreen.ipField;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int width = Main.getWindows()[0].getWidth();
        final int height = Main.getWindows()[0].getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(BACKGROUND_MAIN);
        g2.fillRect(0, 0, width, height);

        g2.setColor(HEADER);
        g2.fillRect(120, 0, width - 240, 130);

        g2.setColor(Color.WHITE);
        g2.drawString("Starred", 480, 60);

        g2.setColor(FORM_BACKGROUND);
        g2.fillRect(120, 130, width - 240, 320);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newNickButton) {
            nickField.setText(NameGenerator.INSTANCE.getName());
        }
        if (e.getSource() == next) {
            previewIndex = previewIndex == NUM_OF_HEROES - 1 ? 0 : previewIndex + 1;

            preview.setIcon(new ImageIcon(heroPreviews[previewIndex]));
        }

        if (e.getSource() == previous) {
            previewIndex = previewIndex == 0 ? NUM_OF_HEROES - 1 : previewIndex - 1;

            preview.setIcon(new ImageIcon(heroPreviews[previewIndex]));
        }
    }

    public int getPreviewIndex() {
        return this.previewIndex;
    }
}
