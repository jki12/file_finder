import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        var f = new JFrame();
        var c = new GridBagConstraints();

        f.setSize(800, 480);
        f.setResizable(false);
        f.setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;

        var resViewer = new ResultViewer();
        f.add(new Finder(resViewer), c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.1;
        c.weighty = 0.7;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;

        f.add(resViewer, c);

        f.setVisible(true);
    }
}
