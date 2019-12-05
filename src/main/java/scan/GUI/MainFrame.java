package scan.GUI;

import scan.Search.SearchTread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame {

    private JFileChooser fileChooser = new JFileChooser();
    private JTextField search = new JTextField();
    private JLabel textSearch = new JLabel("Введите текст для поиска");
    private File selectedFile;
    private JTextField extension = new JTextField("log");
    private JLabel textExtension = new JLabel("Введите расширение файла");
    private JLabel message = new JLabel();
    private JButton btnSearch= new JButton("Поиск");
    private JButton btnDirectory = new JButton("Выбрать директорию");
    private JButton btnSelectAll = new JButton("Выделить все");
    private JButton btnRemoveSelect = new JButton("Убрать выделения");
    private JButton btnSelectNext = new JButton("Вперед");
    private JButton btnSelectBack = new JButton("Назад");
    private JLabel textDirectory1 = new JLabel("Путь ");
    private JLabel textDirectory2 = new JLabel("Директория не выбрана ");
    private JPanel panelTree = new JPanel();
    private JPanel panelText = new JPanel();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private int width =1000;
    private int height = 800;



    public MainFrame(){

        super();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setMaximumSize(new Dimension(width,500));
        search.setMaximumSize(new Dimension(width,20));
        btnSearch.setMaximumSize(new Dimension(width,20));
        extension.setMaximumSize(new Dimension(width,20));
        btnDirectory.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Устанавка свойства чтобы указывать только директорию
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File ("/var/log" ));
                //Возвращает состояние выбранной диеректори
                int response = fileChooser.showOpenDialog(MainFrame.this);
                switch(response){
                    case JFileChooser.APPROVE_OPTION: {
                        textDirectory2.setText(fileChooser.getSelectedFile().toString());
                        selectedFile = fileChooser.getSelectedFile();
                        break;
                    }
                    case JFileChooser.ERROR_OPTION:{
                        textDirectory2.setText("При выборе директории возникала ошибка");
                        break;
                     }
                }
            }
        }
        );
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile!=null){
                    Thread thread = new SearchTread(search.getText(),selectedFile.toString(),extension.getText(),
                            tabbedPane,width,height,btnSelectAll,btnSelectNext,btnSelectBack,btnRemoveSelect);
                    thread.start();
                }

            }
        });
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .addComponent(message)
                .addGroup(layout.createSequentialGroup()
                                .addComponent(textSearch)
                                .addGap(25)
                                .addComponent(search)
                )
                .addGroup(layout.createSequentialGroup()
                                .addComponent(textExtension)
                                .addGap(10)
                                .addComponent(extension)
                )
                .addGroup(layout.createSequentialGroup()
                                .addComponent(textDirectory1)
                                .addGap(10)
                                .addComponent(textDirectory2)
                                .addComponent(btnDirectory)
                )
                .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSearch)
                )
                .addGroup(layout.createSequentialGroup()
                                .addComponent(tabbedPane)
                )
                .addGroup(layout.createSequentialGroup()
                        .addGap(530)
                        .addComponent(btnSelectAll)
                        .addComponent(btnSelectBack)
                        .addComponent(btnSelectNext)
                        .addComponent(btnRemoveSelect)
                )

        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(message)
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(textSearch)
                                        .addGap(25)
                                        .addComponent(search)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(textExtension)
                                        .addGap(10)
                                        .addComponent(extension)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(textDirectory1)
                                        .addGap(10)
                                        .addComponent(textDirectory2)
                                        .addComponent(btnDirectory)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnSearch)

                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(tabbedPane)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(btnSelectAll)
                                .addComponent(btnSelectBack)
                                .addComponent(btnSelectNext)
                                .addComponent(btnRemoveSelect)
                        )
        );
        setSize(width, height);
    }
}
