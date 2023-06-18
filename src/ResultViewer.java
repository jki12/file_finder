import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class ResultViewer extends JPanel {
    private static final String[] PREVIEW_SUPPORTED_EXTENSIONS = { "txt", "c", "cpp", "java" };

    private JList resultViewer;
    private JTextArea previewer;
    private final DefaultListModel model;

    public ResultViewer() {
        this.setLayout(new GridLayout(0, 2));
        model = new DefaultListModel();

        resultViewer = new JList(model);
        previewer = new JTextArea();

        resultViewer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultViewer.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (resultViewer.getSelectedValue() == null) {
                        previewer.setText(Util.EMPTY_STRING);
                    }
                    else {
                        String fileName = resultViewer.getSelectedValue().toString();
                        String extension = Util.getExtension(fileName);

                        previewer.setText((isPreviewSupportedExtension(extension)) ? Util.readFile(fileName).replaceAll("\t", "    ") : "Preview not supported");
                    }
                }
            }
        });

        resultViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // 더블 클릭시 파일 열리는 이벤트 추가.
                if (event.getClickCount() == 2) {
                    var index = ((JList) event.getSource()).locationToIndex(event.getPoint());

                    try {
                        Desktop.getDesktop().open(new File(resultViewer.getModel().getElementAt(index).toString()));
                    } catch (Exception e) {
                        // e.printStackTrace();

                        JOptionPane.showMessageDialog(null, "Exception : " + e.getMessage());
                    }
                }
            }
        });

        previewer.setEditable(false);

        var scrollPane1 = new JScrollPane(resultViewer);
        var scrollPane2 = new JScrollPane(previewer);
        scrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        scrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));

        this.add(scrollPane1);
        this.add(scrollPane2);

        this.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
    }

    public void setResult(ArrayList<String> res) {
        model.clear();

        for (int i = 0; i < res.size(); ++i) {
            model.add(model.getSize(), res.get(i));
        }
    }

    private boolean isPreviewSupportedExtension(final String extension) {
        for (var element : PREVIEW_SUPPORTED_EXTENSIONS) {
            if (element.equals(extension)) return true;
        }

        return false;
    }
}
