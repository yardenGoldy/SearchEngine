
import java.io.*;
import java.util.*;
import java.util.HashMap;
import com.sun.deploy.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// read folder folder
public class ReadFile {

    protected File MainPath;
    public static ArrayList<File> SubFilesPath;
    //public HashMap<String, DocDetailes> Docs; //<DocId,DocDetailes>  sent to parser!!
    public StringBuilder stb;
    //todo check -    private static Pattern CityPattern = Pattern.compile("<f p=\"104\">(.*?)</f>");

    public ReadFile(String path){
        this.MainPath = new File(path);
        SubFilesPath = new ArrayList<File>();
        // Docs = new HashMap<>();
    }

    public void ReadCorpus() throws IOException {
        if (MainPath.isDirectory() && MainPath != null) {
            ProccessSubFilesDirectories(MainPath.getAbsolutePath());
        }
    }

    public void ProccessSubFilesDirectories(String path) throws IOException {
        File file = new File(path);
        File[] SubDirectories = file.listFiles();
        for (File tmp : SubDirectories)
            if (tmp.isFile() && tmp != null && !(tmp.toString().equals(MainPath.getAbsolutePath() + "/stop_words.txt"))) {
                SubFilesPath.add(tmp);
            } else if (tmp != null && tmp.isDirectory()) {
                ProccessSubFilesDirectories(tmp.getAbsolutePath());
            }
    }

    public HashMap<String,DocDetailes> ProccessSubFileToDocs(File subdirectory) throws IOException {
        HashMap<String, DocDetailes> Docs = new HashMap<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(subdirectory))) {
            stb = new StringBuilder();
            String line = bfr.readLine();
            while (line != null) {
                stb.append(" " +line);
                line = bfr.readLine();
            }
            String content = stb.toString();
            Document d = Jsoup.parse(content);
            Elements elements = d.getElementsByTag("DOC"); //process doc
            for (Element element : elements) {
                String DocID = element.getElementsByTag("DOCNO").text();
                String DocText = element.getElementsByTag("TEXT").text();
                String DocDate = element.getElementsByTag("DATE1").text();
                String DocTitle = element.getElementsByTag("TI").text();
                String CitySection = element.getElementsByTag("F").toString();
                int Length = DocText.length();
                if(!CitySection.isEmpty()){
                    String[] splited = StringUtils.splitString(CitySection,"[]}{,<>:\"");
                    for(int i=0;i<splited.length;i++){
                        if(splited[i].equals("104")){
                            String[] finale = StringUtils.splitString(splited[i+1]," ");
                            CitySection = finale[1].toUpperCase();
                            break;
                        }
                    }
                }
                Docs.put(DocID, new DocDetailes(DocID, DocText, DocDate, DocTitle, CitySection ,Length));
            }
        }
        return Docs;
    }


    public int GetSubFilesSize() {
        return SubFilesPath.size();
    }

    public File GetSubFilesPath(int i){
        return SubFilesPath.get(i);
    }
}

