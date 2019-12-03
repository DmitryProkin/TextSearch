package scan;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ResearchFile {
    public List<File> tree(String path, String extension, String search) throws Exception {

        ArrayList<File> arrayList = new ArrayList<File>();

        Stream files = Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .filter(f->f.toString().endsWith("." +extension));

        for (Path file : (Iterable<Path>)files::iterator){
            runSearch( new File(file.toString()),search,arrayList);
        }
        return arrayList;
    }

    private void runSearch(File file,String search, List<File> arrayList ) throws FileNotFoundException {

        Scanner scanner = new Scanner(file);
        //Cоздаем регулярное выражение подстроки
        Pattern p = Pattern.compile(".*" + search + ".*");

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            // Реализует механизм согласования (matching) с РВ
            Matcher m = p.matcher(line);
            if (m.matches()) {
                arrayList.add(file);
                break;
            }

        }

    }
}
