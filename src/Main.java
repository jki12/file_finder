import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        var f = new JFrame();
        var c = new GridBagConstraints();

        f.setSize(960, 640);
        f.setResizable(false);
        f.setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        var resViewer = new ResultViewer();
        var scanFileCountLabel = new JLabel(":)");
        f.add(new Finder(resViewer, scanFileCountLabel), c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.1;
        c.weighty = 0.7;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;

        f.add(resViewer, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.01;
        c.gridy = 2;
        scanFileCountLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        f.add(scanFileCountLabel, c);

        f.setVisible(true);
        // f.dispose();
    }
}
