import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
    private JCheckBox ignoreCaseCheckBox;
    private JFileChooser dirChooser;
    private JTextField pathField; // always disabled.
    private JTextField depthField;

    private String findPath = DEFAULT_PATH;
    private int depth = DEFAULT_DEPTH;

    private StringBuilder sb = new StringBuilder();
    private ArrayList<String> foundFiles = new ArrayList<>();

    public Finder() {
        this.setLayout(new BorderLayout());
        this.setSize(300, 300);

        // search options
        optionPanel = new JPanel();
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        pathField = new JTextField(30);
        strictCheckBox = new JCheckBox("strict mode");
        ignoreCaseCheckBox = new JCheckBox("ignore case");

        depthField = new JTextField(String.valueOf(DEFAULT_DEPTH), 30);

        pathField.setText(DEFAULT_PATH);
        pathField.setEnabled(false);

        var btn = new JButton("select path");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirChooser.showDialog(optionPanel, "select");
                findPath = dirChooser.getSelectedFile().getPath();

                pathField.setText(findPath);
            }
        });

        optionPanel.add(pathField);
        optionPanel.add(btn); // click -> open file chooser
        optionPanel.add(strictCheckBox);
        optionPanel.add(ignoreCaseCheckBox);
        optionPanel.add(new JLabel("depth"));
        optionPanel.add(depthField);

        // search field
        searchPanel = new JPanel();
        searchField = new JTextField(10);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isValid(depthField.getText())) return; // showing popup.

                depth = Integer.parseInt(depthField.getText()); // valid data.

                search(searchField.getText());

                System.out.println("done : " + foundFiles.size());
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.add(searchPanel);
        this.add(optionPanel, BorderLayout.AFTER_LAST_LINE);
    }

    public ArrayList<String> getFoundFiles() {
        return foundFiles;
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
    
    private void search(String fileName) { // 찾고자 하는 파일명.
        foundFiles.clear();
        searchButton.setEnabled(false);
        
        try {
            File file = new File(findPath);
            
            dfs(file, fileName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        searchButton.setEnabled(true);
    }

    private boolean compare(final String s1, final String s2, boolean ignoreCase) {
        if (s1.length() != s2.length()) {
            return false;
        }

        for (int i = 0; i < s1.length(); ++i) {
            if (ignoreCase) {
                if ((s1.charAt(i) | ' ') != (s2.charAt(i) | ' ')) {
                    return false;
                }
            }
            else {
                if (s1.charAt(i) != s2.charAt(i)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void dfs(File file, final String fileName, int depth)
    {
        assert (file.isDirectory());

        if (this.depth <= depth) {
            return;
        }

        var list = file.list();

        if (list == null) {
            return;
        }

        for (int i = 0; i < list.length; ++i) {
            String path = file.getPath() + "\\" + list[i]; // current path.

            if (compare(removeFileExtension(list[i]), removeFileExtension(fileName), false)) { // temp code.
                foundFiles.add(path);
            }

            boolean isDir = Files.isDirectory(Paths.get(path));

            if (isDir) {
                dfs(new File(path), fileName, depth + 1);
            }
        }
    }

    private String removeFileExtension(String fileName) { // test..
        int index = fileName.lastIndexOf('.');

        if (index == -1) {
            return fileName;
        }

        sb.setLength(0);
        for (int i = 0; i < index; ++i) {
            sb.append(fileName.charAt(i));
        }

        return sb.toString();
    }
}
