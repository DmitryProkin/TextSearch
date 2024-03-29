package scan.Search;

import scan.GUI.ResultPanel;
import scan.Model.ModelTree;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.List;

public class SearchTread extends Thread {
    private String path;
    private String extension;
    private String search;
    private JTabbedPane tabbedPane;
    private JTextArea textArea = new JTextArea(10, 20);
    private JButton btnSelectAll;
    private JButton btnSelectNext;
    private JButton btnSelectBack;
    private JButton btnRemoveSelect;
    private JTree tree;
    private int width;
    private int height;
    private static int indexCourser;
    private File fileSelect;
    private LineHighlightPainter highlight;
    private Caret caret;

    private HashMap<File, List<Integer>> mapFiles = new HashMap<>();

    public SearchTread(String search, String path, String extension, JTabbedPane tabbedPane, int width, int height,
                       JButton btnSelectAll, JButton btnSelectNext, JButton btnSelectBack, JButton btnRemoveSelect) {
        this.search = search;
        this.path = path;
        this.extension = extension;
        this.tabbedPane = tabbedPane;
        this.width = width;
        this.height = height;
        this.btnSelectAll = btnSelectAll;
        this.btnSelectNext = btnSelectNext;
        this.btnSelectBack = btnSelectBack;
        this.btnRemoveSelect = btnRemoveSelect;

    }

    @Override
    public void start() {
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        tabPanel.add(contentPanel);

        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setViewportView(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        // Создание курсора
        caret = textArea.getCaret();
        caret.setVisible(true);

        try {
            FullTextFinder fullTextFinder = new FullTextFinder();
            //Вызов метода для поиска файлов удовлетворяющие заданным условиям
            mapFiles = fullTextFinder.tree(path, extension, search);
            if (!mapFiles.isEmpty()) {
                ModelTree model = new ModelTree(path, mapFiles);

                tree = new JTree(model);
                JScrollPane scrollPanels = new JScrollPane(tree);
                scrollPanels.setViewportView(tree);
                scrollPanels.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPanels.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPanels.setPreferredSize(new Dimension(250, 500));

                tabPanel.add(scrollPanels, BorderLayout.LINE_START);
                mouseListenerTree();

            }
            else{
                JLabel resultNull = new JLabel("По заданному поиску ничего не найдено! " );
                resultNull.setMaximumSize(new Dimension(200,500));

                tabPanel.add(resultNull, BorderLayout.LINE_START);
                tabPanel.setBackground(Color.WHITE);
            }

            actionListenerBtnSelectAll();
            actionListenerBtnSelectNext();
            actionListenerBtnSelectBack();
            actionListenerRemoveSelect();

        } catch (Exception e) {
            handleException(tabPanel, e);
        }

        tabPanel.add(scrollPane, BorderLayout.LINE_END);
        tabbedPane.add(tabPanel);

        int index = tabbedPane.indexOfComponent(tabPanel);
        String textForPanel ="Данное выражение найдено в следующих файлах:";
        JPanel panel = ResultPanel.getTitle(tabbedPane, tabPanel, textForPanel);
        tabbedPane.setTabComponentAt(index, panel);

    }

    private void mouseListenerTree(){
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                highlight =null;
                indexCourser = 0;
                TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());

                if (treePath != null) {
                    String pathSelect = treePath.getLastPathComponent().toString();
                    System.out.println(pathSelect);
                    fileSelect = new File(pathSelect);
                    try {
                        BufferedReader input = new BufferedReader(new InputStreamReader(
                                new FileInputStream(fileSelect)));
                        textArea.read(input, "READING FILE :-)");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            }
        });
    }
    private void actionListenerBtnSelectAll(){
        btnSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document doc = textArea.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                if (!search.isEmpty() && !mapFiles.isEmpty()) {
                    highlight = new LineHighlightPainter(search, text, textArea, mapFiles, fileSelect);
                    caret.setDot(mapFiles.get(fileSelect).get(0));
                }
            }
        });
    }

    private void actionListenerBtnSelectNext(){
        btnSelectNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indexCourser<mapFiles.get(fileSelect).size()-1) {
                    indexCourser++;
                }

                // Установление каретки
                if (!mapFiles.isEmpty()){
                    int position = mapFiles.get(fileSelect).get(indexCourser);
                    caret.setDot(position);
                }
                else{
                    caret.setDot(indexCourser);
                }
            }
        });
    }

    private void actionListenerBtnSelectBack() {
        btnSelectBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (indexCourser > 0) {
                    indexCourser--;
                }
                // Установление каретки
                if (!mapFiles.isEmpty()){
                    int position = mapFiles.get(fileSelect).get(indexCourser);
                    caret.setDot(position);
                }
                else{
                    caret.setDot(indexCourser);
                }
            }
        });
    }

    private void actionListenerRemoveSelect() {
        btnRemoveSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!search.isEmpty() && !mapFiles.isEmpty()) {
                    indexCourser = 0;
                    caret.setDot(indexCourser);
                    highlight.removeHighlights(textArea);
                }
            }
        });
    }
    private void handleException(JComponent hBox, Exception e) {
        hBox.add(new JLabel("Ошибка:" + e.getMessage()));
    }

}