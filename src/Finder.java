import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Finder extends JPanel {
    private static final String DEFAULT_PATH = "C:\\";
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 30;
    private static final int DEFAULT_DEPTH = 3;

    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;

    private JPanel optionPanel;
    private JCheckBox strictCheckBox;
    private JFileChooser dirChooser;
    private JTextField pathField; // always disabled.
    private String currentPath = DEFAULT_PATH;
    private JTextField depthField;
    private int depth = DEFAULT_DEPTH;

    public Finder() {
        this.setLayout(new BorderLayout());
        this.setSize(300, 300);

        // search options
        optionPanel = new JPanel();
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        pathField = new JTextField(30);
        strictCheckBox = new JCheckBox("strict mode");

        depthField = new JTextField(String.valueOf(depth), 30);

        pathField.setText(currentPath);
        pathField.setEnabled(false);

        var btn = new JButton("select path");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirChooser.showDialog(optionPanel, "select");
                currentPath = dirChooser.getSelectedFile().getPath();

                pathField.setText(currentPath);
            }
        });

        optionPanel.add(pathField);
        optionPanel.add(btn); // click -> open file chooser
        optionPanel.add(strictCheckBox);
        optionPanel.add(new JLabel("depth"));
        optionPanel.add(depthField);

        // search field
        searchPanel = new JPanel();
        searchField = new JTextField(10);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("current setting");

                System.out.println(searchField.getText() + " " + strictCheckBox.isSelected() + " ");
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.add(searchPanel);
        this.add(optionPanel, BorderLayout.AFTER_LAST_LINE);
    }

    private boolean isValid(String s) {
        int n;

        try {
            n = Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }

        return (MIN_DEPTH <= n && n <= MAX_DEPTH);
    }
    
    private void search() {
        searchButton.setEnabled(false);
        
        try {
            File file = new File(currentPath);
            
            dfs(file, depth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        searchButton.setEnabled(true);
    }

    private void dfs(File file, int depth)
    {
        assert (file.isDirectory());

        if (this.depth <= depth) {
            return;
        }

        var list = file.list();

        for (int i = 0; i < list.length; ++i) {

            String path = file.getPath() + "\\" + list[i];

            // strict ver, normal ver
            if (.equals(list[i])) {
                foundFiles.add(path);
            }

            boolean isDir = Files.isDirectory(Paths.get(path));

            if (isDir) {
                dfs(new File(path), depth + 1);
            }
        }

        // System.out.println(getFormatter(depth, false, true)); // 가독성 높이려고 넣었음.
    }




}
