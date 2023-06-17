import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ResultViewer extends JPanel {
    private JList resultViewer;
    private JTextArea previewer; // support only *.txt, java, cpp file.
    private final DefaultListModel model;

    public ResultViewer() {
        this.setLayout(new GridLayout(0, 2));
        model = new DefaultListModel();

        resultViewer = new JList(model);
        previewer = new JTextArea();

        var scrollPane1 = new JScrollPane(resultViewer);
        var scrollPane2 = new JScrollPane(previewer);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        scrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        this.add(scrollPane1);
        this.add(scrollPane2);

        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
    }

    public void setResult(ArrayList<String> res) {
        model.clear();

        for (int i = 0; i < res.size(); ++i) {
            model.add(model.getSize(), res.get(i));
        }
    }

    private void readFile() { // test..
        final String path = ".\\a.txt";
    }
}
