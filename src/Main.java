import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        var path = new File("C:\\Users\\sjak0\\Desktop\\algorithm");
        var fileName = "b1697.java";



        var f = new Frame();
        f.setSize(1000, 1000); // tnwjd

        // f.add(new Finder());
        f.add(new Finder(), BorderLayout.BEFORE_FIRST_LINE);
        f.setVisible(true);
    }
}
