package scan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResultPanel {
    public static JPanel getTitle (final JTabbedPane tabbedPane, final  JPanel panel, String title) {
       JPanel titlePanel = new JPanel( new FlowLayout(FlowLayout.LEFT,0,0));
       //titlePanel.setOpaque(false);
       JLabel titleLabel = new JLabel(title);
       titleLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
       titlePanel.add(titleLabel);
       JButton closeBtn = new JButton("Х");
       closeBtn.setBorder(null);

       closeBtn.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e){
               tabbedPane.remove((panel));
           }
       });
       titlePanel.add(closeBtn);
       return titlePanel;
    }
}
