package me.riseremi.ui.windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.Getter;
import me.riseremi.main.Main;
import static me.riseremi.main.Main.setUIFont;
import me.riseremi.ui.RButton;
import me.riseremi.ui.RTextField;
import me.riseremi.utils.NameGenerator;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class LoginScreen extends JPanel implements ActionListener {

    private final BufferedImage heroPreview;
    private BufferedImage back;
    private final int NUM_OF_HEROES = 15;
    private final BufferedImage[] heroPreviews = new BufferedImage[NUM_OF_HEROES];
    private final String PREFIX = "/res/sprites/hero";
    private final String POSTFIX = "";
    private final JButton preview;
    private final RButton next, previous;
    @Getter private int previewIndex = 0;
    //
    @Getter private static final JButton hostButton = new RButton("Host", false),
            joinButton = new RButton("Join", false), newNickButton = new RButton("<", false);
    @Getter private static final JTextField nickField = new RTextField(NameGenerator.getName()),
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
            back = ImageIO.read(getClass().getResourceAsStream("/res/back0.png"));
        } catch (IOException ex) {
        }

        for (int i = 0; i < NUM_OF_HEROES; i++) {
            try {
                heroPreviews[i] = ImageIO.read(getClass().getResourceAsStream(PREFIX + i + ".png"));
            } catch (IOException ex) {
            }

        }

        String imageName1 = "next";
        String imageName2 = "previous";

        String imgLocation1 = "/res/buttons/" + imageName1 + ".png";
        String imgLocation2 = "/res/buttons/" + imageName2 + ".png";

        URL imageURL1 = LoginScreen.class.getResource(imgLocation1);
        URL imageURL2 = LoginScreen.class.getResource(imgLocation2);

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
            nickField.setText(NameGenerator.getName());
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

}
