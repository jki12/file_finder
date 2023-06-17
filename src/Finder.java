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
    private static final int MAX_PATH = 30;
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

    private StringBuilder sb = new StringBuilder();
    private ArrayList<String> foundFiles = new ArrayList<>();

    public Finder(ResultViewer resultViewer) {
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isValid(depthField.getText())) return; // showing popup.

                depth = Integer.parseInt(depthField.getText());

                search(searchField.getText());
                resultViewer.setResult(foundFiles);

                System.out.println("done : " + foundFiles.size());
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.add(searchPanel, BorderLayout.CENTER);
        this.add(optionPanel, BorderLayout.SOUTH);
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
        if (fileName.equals("") || fileName.length() >= MAX_PATH) {
            return;
        }

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

        String[] list = null;

        try {
            list = file.list();

            if (list == null) return;
        } catch (Exception e) {
            return;
        }

        String fileNameWithoutFileExtensions = removeFileExtension(fileName);

        for (int i = 0; i < list.length; ++i) {
            String path = file.getPath() + "\\" + list[i]; // current path.

            if (strictCheckBox.isSelected() && compare(fileName, list[i], false)) {
                foundFiles.add(path);
            }
            else if (!strictCheckBox.isSelected()) {
                if (hasFileExtension(fileName)) { // just ignore case.
                    if (compare(fileName, list[i], true)) {
                        foundFiles.add(path);
                    }
                }
                else {
                    if (compare(fileNameWithoutFileExtensions, removeFileExtension(list[i]), true)) {
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

    private boolean hasFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');

        return (index != -1);
    }

    private String removeFileExtension(String fileName) {
        if (!hasFileExtension(fileName)) {
            return fileName;
        }

        sb.setLength(0);
        for (int i = 0; i < fileName.lastIndexOf('.'); ++i) {
            sb.append(fileName.charAt(i));
        }

        return sb.toString();
    }
}
