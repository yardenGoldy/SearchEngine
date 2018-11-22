package Model;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ReadFile {

    protected File MainPath;
    public ArrayList<File> SubFilesPath;
    public HashMap<String,DocDetailes> Docs; //<DocId,Model.DocDetailes>  sent to parser!!
    public StringBuilder stb;


    public void ReadFile(){ }

    public HashMap<String,DocDetailes> ReadAllDocs(String path) throws IOException {
        this.MainPath= new File(path);
        Docs = new HashMap<>();
        SubFilesPath = new ArrayList<File>();
        if(MainPath.isDirectory() && MainPath != null) {
            ProccessSubFilesDirectories(MainPath.getAbsolutePath());
            ProccessSubFilesToDocs(SubFilesPath);
        }
        return Docs;
    }


    public void ProccessSubFilesDirectories(String path) throws IOException {
        File file = new File(path);
        File [] SubDirectories = file.listFiles();
        for(File tmp : SubDirectories)
            if (tmp.isFile() && tmp != null) {
                SubFilesPath.add(tmp);
            } else if (tmp != null && tmp.isDirectory()) {
                ProccessSubFilesDirectories(tmp.getAbsolutePath());
            }
    }

    public void ProccessSubFilesToDocs(ArrayList<File> subdirectory) throws IOException {
        for (int i = 0; i < subdirectory.size(); i++) {
            File f = new File(subdirectory.get(i).getAbsolutePath());
            try (BufferedReader bfr = new BufferedReader(new FileReader(f))) {
                stb = new StringBuilder();
                String line = bfr.readLine();
                while (line != null) {
                    stb.append(line);
                    line = bfr.readLine();
                }
                String content = stb.toString();
                Document d = Jsoup.parse(content);
                Elements elements = d.getElementsByTag("DOC");
                for (Element element : elements){
                    String DocID = element.getElementsByTag("DOCNO").text();
                    String DocText = element.getElementsByTag("TEXT").text();
                    String DocDate = element.getElementsByTag("DATE1").text();
                    String DocTitle = element.getElementsByTag("TI").text();
                    String DocCity = element.getElementsByTag("F").toString();
                    int DocLength = element.childNodeSize();
                    // needed to handle to find city , and change it ti big letters!!
                    Docs.put(DocID,new DocDetailes(DocText,DocDate,DocTitle,DocCity,DocLength));
                }
            }
        }
    }
}


