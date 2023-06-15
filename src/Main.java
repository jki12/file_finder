import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        var f = new Frame();
        f.setSize(1000, 1000); // tnwjd

        // f.add(new Finder());
        f.add(new Finder(), BorderLayout.BEFORE_FIRST_LINE);
        f.setVisible(true);
    }
}
