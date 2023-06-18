import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Finder extends JPanel {
    private static final String DEFAULT_PATH = "C:\\";
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 20;
    private static final int MAX_PATH = 256;
    private static final int DEFAULT_DEPTH = 3;

    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;

    private JPanel optionPanel;
    private JCheckBox strictCheckBox;
    private JFileChooser dirChooser;
    private JTextField pathField; // always disabled.
    private JTextField depthField;
    private final ResultViewer resultViewer;

    private String findPath = DEFAULT_PATH;
    private int depth = DEFAULT_DEPTH;

    private int scanFileCount;
    private ArrayList<String> foundFiles = new ArrayList<>();

    public Finder(ResultViewer resultViewer, JLabel scanFileCountLabel) {
        this.resultViewer = resultViewer;
        this.setLayout(new BorderLayout());

        // search options
        optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout());
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        pathField = new JTextField(30);
        strictCheckBox = new JCheckBox("strict mode");
        strictCheckBox.setToolTipText("If checked, the search will consider file extensions and case sensitivity!");

        depthField = new JTextField(String.valueOf(DEFAULT_DEPTH), 10);

        pathField.setText(DEFAULT_PATH);
        pathField.setEnabled(false);

        var fileChooserOpenButton = new JButton("select path");
        fileChooserOpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirChooser.showDialog(optionPanel, "select");
                findPath = dirChooser.getSelectedFile().getPath();

                pathField.setText(findPath);
            }
        });

        optionPanel.add(pathField);
        optionPanel.add(fileChooserOpenButton);
        optionPanel.add(depthField);
        optionPanel.add(new JLabel("depth"));
        optionPanel.add(strictCheckBox);

        // search field
        searchPanel = new JPanel();
        searchField = new JTextField(40);
        searchButton = new JButton("Search");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) { // enter code : 10.
                    searchHandler(scanFileCountLabel);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchHandler(scanFileCountLabel);
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.CENTER);
        this.add(optionPanel, BorderLayout.SOUTH);
    }

    private void searchHandler(JLabel scanFileCountLabel) {
        if (!isValid(depthField.getText())) {
            JOptionPane.showMessageDialog(null, "Only numbers are allowed, and it should be between 1 and 20");

            return;
        }

        depth = Integer.parseInt(depthField.getText());

        search(searchField.getText());
        resultViewer.setResult(foundFiles);
        scanFileCountLabel.setText(String.format("Search result : %d / %d", foundFiles.size(), scanFileCount));
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
        if (fileName.length() == 0 || fileName.length() >= MAX_PATH) {
            JOptionPane.showMessageDialog(null, "Invalid or emepty value entered");

            return;
        }

        scanFileCount = 0;
        foundFiles.clear();
        searchButton.setEnabled(false);
        
        try {
            File file = new File(findPath);
            
            dfs(file, fileName, 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e.getMessage());

            // e.printStackTrace();
        }
        
        searchButton.setEnabled(true);
    }

    public void dfs(File file, final String fileName, int depth)
    {
        assert (file.isDirectory());

        if (this.depth <= depth) {
            return;
        }

        String[] list;
        try {
            list = file.list();

            if (list == null) return;
        } catch (Exception e) {
            return;
        }

        String fileNameWithoutFileExtensions = Util.removeFileExtension(fileName);

        scanFileCount += list.length;
        for (int i = 0; i < list.length; ++i) {
            String path = file.getPath() + "\\" + list[i]; // current path.

            if (strictCheckBox.isSelected() && Util.compare(fileName, list[i], false)) {
                foundFiles.add(path);
            }
            else if (!strictCheckBox.isSelected()) {
                if (Util.hasFileExtension(fileName)) { // just ignore case.
                    if (Util.compare(fileName, list[i], true)) {
                        foundFiles.add(path);
                    }
                }
                else {
                    if (Util.compare(fileNameWithoutFileExtensions, Util.removeFileExtension(list[i]), true)) {
                        foundFiles.add(path);
                    }
                }
            }

            boolean isDir = Files.isDirectory(Paths.get(path));

            if (isDir) {
                dfs(new File(path), fileName, depth + 1);
            }
        }
    }
}
