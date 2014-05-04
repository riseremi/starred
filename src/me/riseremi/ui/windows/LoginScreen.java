package me.riseremi.ui.windows;

import java.awt.Color;
import java.awt.Graphics;
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

/**
 *
 * @author Remi
 */
public class LoginScreen extends JPanel implements ActionListener {

    private BufferedImage heroPreview;
    private BufferedImage back;
    private final int NUM_OF_HEROES = 4;
    private BufferedImage[] heroPreviews = new BufferedImage[NUM_OF_HEROES];
    private final String PREFIX = "/res/sprites/hero";
    private final String POSTFIX = "";
    private JButton preview;
    private RButton next, previous;
    @Getter private int previewIndex = 0;
    //
    @Getter private static JButton server = new RButton("Host"),
            client = new RButton("Connect");
    @Getter private static JTextField nick = new RTextField("Player"),
            ip = new RTextField("127.0.0.1");

    public LoginScreen() {
        int yOffset = 150;

        int frameWidth = Main.getFrames()[0].getWidth();
        setUIFont(new javax.swing.plaf.FontUIResource(Main.MAIN_FONT));
        //
        nick.setBounds(frameWidth / 4, 100 + yOffset, frameWidth / 2, 24);
        ip.setBounds(frameWidth / 4, 130 + yOffset, frameWidth / 2, 24);
        server.setBounds(frameWidth / 4, 160 + yOffset, frameWidth / 4, 32);
        client.setBounds(frameWidth / 2, 160 + yOffset, frameWidth / 4, 32);

        add(nick);
        add(ip);
        add(server);
        add(client);

        server.addActionListener(this);
        client.addActionListener(this);

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

        next = new RButton("", false);
        next.addActionListener(this);
        next.setIcon(new ImageIcon(imageURL1, "Next"));

        previous = new RButton("", false);
        previous.addActionListener(this);
        previous.setIcon(new ImageIcon(imageURL2, "Previous"));

        preview = new RButton("", false);
        preview.setIcon(new ImageIcon(heroPreviews[0]));

        final JLabel jLabel = new JLabel("Select your icon");
        jLabel.setForeground(Color.WHITE);
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

        for (int i = 0; i < 1024; i += 64) {
            for (int j = 0; j < 768; j += 64) {
                g.drawImage(back.getScaledInstance(64, 64, 3), i, j, null);
            }

        }

//        
//        Random rnd = new Random();
//        g.setColor(Color.LIGHT_GRAY);
//        for (int i = 0; i < 512; i++) {
//            g.drawLine(rnd.nextInt(getWidth()), rnd.nextInt(getHeight()), rnd.nextInt(getWidth()), rnd.nextInt(getHeight()));
//        }
//        g.setColor(Color.LIGHT_GRAY.brighter());
//        for (int i = 0; i < 1024; i++) {
//            g.drawLine(rnd.nextInt(getWidth()), rnd.nextInt(getHeight()), rnd.nextInt(getWidth()), rnd.nextInt(getHeight()));
//        }
//
//        int frameWidth = Main.getFrames()[0].getWidth();
//        g.setColor(getBackground());
//        g.fillRect(frameWidth / 5, 150, (int) (frameWidth - frameWidth / 2.5F), 260);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
