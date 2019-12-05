package scan.Search;

import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class LineHighlightPainter {
    String text;
    String searchTxt;
    LineHighlightPainter(String searchTxt, String text, JTextComponent textComp, HashMap<File,
                         List<Integer>> mapFiles, File fileSelect){
        this.text = text;
        this.searchTxt = searchTxt;
        highlight(textComp,searchTxt,mapFiles,fileSelect);
    }
            public void highlight(JTextComponent textComp, String pattern,
                                  HashMap<File, List<Integer>> mapFiles, File fileSelect) {
       //Удалить старые выделения
        removeHighlights(textComp);

        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            for (int pos : mapFiles.get(fileSelect)){
                if (pos>=0){
                    hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                    pos += pattern.length();
                }
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);

    class MyHighlightPainter
            extends DefaultHighlighter.DefaultHighlightPainter {

        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}
