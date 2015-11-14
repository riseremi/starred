package me.riseremi.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import lombok.Getter;
import me.riseremi.main.Main;
import static me.riseremi.main.Main.setUIFont;
import me.riseremi.ui.RButton;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class LobbyScreen extends JPanel implements ActionListener {

    @Getter private static final JButton goButton = new RButton("Go!", false);
    @Getter private DefaultListModel playersListModel = new DefaultListModel();
    private JList playersList = new JList(playersListModel);

    public LobbyScreen(boolean server) {
        int frameWidth = Main.getFrames()[0].getWidth();
        final JLabel jLabel = new JLabel("Connected players:");

        setUIFont(new javax.swing.plaf.FontUIResource(Main.MAIN_FONT));
        setLayout(null);

        goButton.setBounds(64, 64, frameWidth / 4, 32);
        jLabel.setBounds(64, 64 + 38, frameWidth / 4, 32);
        playersList.setBounds(64, 128, frameWidth / 4, 320);

        goButton.setEnabled(false);
        if (server) {
            add(goButton);
        }

        add(jLabel);
        add(playersList);

    }

    public void setCanGo() {
        goButton.setEnabled(true);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
