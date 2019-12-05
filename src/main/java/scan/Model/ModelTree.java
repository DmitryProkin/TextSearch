package scan.Model;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ModelTree implements TreeModel {

    private String root;
    private List<String> nodeList = new ArrayList<>();

    public ModelTree(String root, HashMap<File, List<Integer>> fileList) {

        this.root = root;
        for (File currentFile : fileList.keySet()) {

            String nodePath = root;

            for (String nodeName : currentFile.toString().split("/")) {
                //Cобираем путь файла
                String s = nodeName.replace(root + "\\", "");
                String[] strName = s.split(Pattern.quote("\\"));
                int strNameSize = strName.length;
                if (strNameSize > 1) {
                    for (int i = 0; i < strNameSize - 1; i++) {
                        nodePath += "\\" + strName[i];
                    }
                }
                nodePath += "/" + strName[strNameSize - 1];

                if (!nodeList.contains(nodePath)) {
                    nodeList.add(nodePath);
                }

            }

        }

    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        List<String> listChild = getChildList(parent.toString());
        return listChild.get(index);
    }

    private List<String> getChildList(String parent) {
        ArrayList<String> list = new ArrayList<>();

        for (String currentNode : nodeList) {
            //Начинается ли строка с указанного прифекса ?
            if (currentNode.startsWith(parent) && (currentNode.split("/").length - parent.split("/").length) == 1) {
                list.add(currentNode);
            }
        }
        return list;
    }

    @Override
    public int getChildCount(Object parent) {
        List<String> listChild = getChildList(parent.toString());
        return listChild.size();
    }

    @Override
    public boolean isLeaf(Object node) {
        List<String> listChild = getChildList(node.toString());
        return listChild.size() == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        List<String> listChild = getChildList(parent.toString());
        if (listChild.contains(child.toString())) {
            return 1;
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
