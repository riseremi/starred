package me.riseremi.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import lombok.Getter;
import me.riseremi.main.Main;
import static me.riseremi.main.Main.setUIFont;
import me.riseremi.ui.RButton;

/**
 *
 * @author riseremi
 */
public class LobbyScreen extends JPanel implements ActionListener {

    @Getter private static final JButton goButton = new RButton("Go!");

    public LobbyScreen() {
        int yOffset = 150;
        int frameWidth = Main.getFrames()[0].getWidth();
        setUIFont(new javax.swing.plaf.FontUIResource(Main.MAIN_FONT));
        setLayout(null);

        goButton.setBounds(frameWidth / 2, 160 + yOffset, frameWidth / 4, 32);
        
        add(goButton);
        //goButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
