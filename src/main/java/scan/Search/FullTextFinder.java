package scan.Search;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FullTextFinder {

    private HashMap<File , List<Integer>> map = new HashMap<>();

        //Возвращаемый HashMap содержит в качестве ключа файлы, в которых найдено выражение ,
        // и значения  List<Integer>, список позиций в тексте, в которых обнаружено слово
    public HashMap<File, List<Integer>> tree(String path, String extension, String search) throws Exception {

        ArrayList<File> arrayList = new ArrayList<File>();

        Stream files = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .filter(f->f.toString().endsWith("." +extension));

        for (Path file : (Iterable<Path>)files::iterator){
            runSearch( new File(file.toString()),search,arrayList);
        }
        return map;
    }

    private void runSearch(File file,String search, List<File> arrayList ) throws FileNotFoundException {
        List<Integer> listStartIndex = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        //Cоздаем регулярное выражение подстроки
        Pattern p = Pattern.compile(search);
        String text="";
        while (scanner.hasNextLine()) {
            text += scanner.nextLine()+"\n";
        }
            // Реализует механизм согласования (matching) с РВ
            Matcher m = p.matcher(text);
            while(m.find()) {
                listStartIndex.add(m.start());
            }

            if (!listStartIndex.isEmpty()) {
                map.put(file, listStartIndex);
            }
        }
    }