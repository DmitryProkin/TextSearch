package scan;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

import static javax.swing.BoxLayout.*;


public class SearchTread extends Thread{
    private String path;
    private String extension;
    private String search;
    private JTabbedPane tabbedPane;
    private JTextArea textArea = new JTextArea(10,20);
    private File currentFile = null;
    private boolean isFileLoaded = false;
    private int width;
    private int height;


    private List<File> entryCollection;

    public SearchTread(String search, String path, String extension, JTabbedPane tabbedPane, int width, int height) {
        this.search = search;
        this.path = path;
        this.extension = extension;
        this.tabbedPane = tabbedPane;
        this.width = width;
        this.height = height;
    }
   @Override
    public void start() {
       JPanel tabPanel = new JPanel();
       tabPanel.setLayout(new BorderLayout(10, 10));

       tabPanel.setBorder(BorderFactory.createLineBorder(Color.green));

      // textArea.setPreferredSize(new Dimension(700,500));
       JPanel contentPanel = new JPanel();
       contentPanel.setLayout(new BorderLayout(10, 10));

      // contentPanel.setBackground(Color.white);
       tabPanel.add(contentPanel);
       textArea.setLineWrap(true);
       //textArea.setPreferredSize(new Dimension(700,500));
       JScrollPane scrollPane = new JScrollPane(textArea);
       scrollPane.setViewportView(textArea);
       scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       scrollPane.setPreferredSize(new Dimension(700,500));

       JSeparator separator = new JSeparator();
       separator.setOrientation(SwingConstants.VERTICAL);
       tabPanel.add(separator, BorderLayout.CENTER);
       try {
           ResearchFile researchFile = new ResearchFile();
           entryCollection = researchFile.tree(path, extension, search);

           TreeModl model = new TreeModl(path, entryCollection);
//           File fileRoot = new File(path);
//           DefaultMutableTreeNode root = new DefaultMutableTreeNode(new CreateTree.FileNode(fileRoot));
//           DefaultTreeModel treeModel = new DefaultTreeModel(root);
//
//           JTree tree = new JTree(treeModel);
//           tabPanel.add(tree, BorderLayout.WEST);
//           CreateTree.CreateChildNodes ccn =
//                   new CreateTree.CreateChildNodes(fileRoot, root,tree, entryCollection );
//           new Thread(ccn).start();
           JTree tree = new JTree(model);
          // tree.setPreferredSize(new Dimension(250, 500));
           JScrollPane scrollPanels = new JScrollPane(tree);
           scrollPanels.setViewportView(tree);
           scrollPanels.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
           scrollPanels.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
           scrollPanels.setPreferredSize(new Dimension(250,500));
//           JScrollPane scrollPane = new JScrollPane();
//           scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//           scrollPane.setViewportView(tree);


           tabPanel.add(scrollPanels, BorderLayout.LINE_START);
         //  tabPanel.add(scrollPane);
           //tree.setBorder(BorderFactory.createLineBorder(Color.red));
           tree.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {

                   TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());

                   if (treePath != null) {
                       String pathSelect = treePath.getLastPathComponent().toString();
                       System.out.println(pathSelect);
                    // path = "C:\\Users\\proki\\OneDrive\\Рабочий стол\\Новая папка (2)\\Новая папка\\Юнг, Томас.log";
//                       path.replace("\\", "\\\\");
//                       path.replace("//", "\\\\");
                      /* String str[]= path.split("/");
                       path = str[0]+"\\" + str[1];*/
                      // System.out.println(path);
                       File file = new File(pathSelect);
                       // File file = entryCollection.get(0);
                      //  String path2 = file.getPath();
                 //      System.out.println("path2 " + path2);
                       try {
                           BufferedReader input = new BufferedReader(new InputStreamReader(
                                   new FileInputStream(file)));
                           textArea.read(input, "READING FILE :-)");
                       } catch (Exception ex) {
                           ex.printStackTrace();
                       }

                       tabPanel.add(scrollPane, BorderLayout.LINE_END);
//                       if (!file.exists() && !file.isFile()) {
//                           textArea.setText("");
//                       } else {
//                           readingText(file);
//
//                           JScrollPane scrollPane = new JScrollPane(textArea);
//                           tabPanel.add(textArea, BorderLayout.EAST);
//                       }
                   }


               }
           });

       } catch(Exception e){
           handleException(tabPanel, e);
       }


           tabbedPane.add(tabPanel);
           tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(tabPanel), ResultPanel.getTitle(tabbedPane, tabPanel, "Данное выражение найдено в следующих файлах:"));

   }

   private  void readingText(File file){
       try {
           BufferedReader input = new BufferedReader(new InputStreamReader(
                   new FileInputStream(file)));
           textArea.read(input, "READING FILE :-)");
       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }


    private void handleException(JComponent hBox,Exception e)
    {
        hBox.add(new JLabel("Ошибка:" + e.getMessage()));
    }

    ///Begin another

}