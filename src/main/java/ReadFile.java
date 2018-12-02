
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.HashMap;
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
        // File f = new File(subdirectory.get(i).getAbsolutePath());
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
                int Length = DocText.length();
                String CitySection = ""; //todo needed to find a way
//                    if(SearchEngine.City.containsKey(CitySection)){
//                        ProcessCity(CitySection);
//                    }
                //SearchEngine.All_Docs.put(DocID, new DocDetailes(DocID, DocText, DocDate, DocTitle, CitySection));
                Docs.put(DocID, new DocDetailes(DocID, DocText, DocDate, DocTitle, CitySection ,Length));
            }
            //SubFilesCounter++;
            //return DocsPerBlock;
        }
        return Docs;
    }


    public int GetSubFilesSize() {
        return SubFilesPath.size();
    }

    public File GetSubFilesPath(int i){
        return SubFilesPath.get(i);
    }

    //if term not in City database
    public void ProcessCity(String citySection) throws IOException {
        // needed also to trim all the ends to find only the city
        String CityResult = "";
        CityDetailes tmpCityDetailes = CityDetailesAPI(CityResult); // API brings :String cityName, String country, String crrency, String populationSize
        SearchEngine.City.put(CityResult, tmpCityDetailes);
    }

    //https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
    public CityDetailes CityDetailesAPI(String city) throws IOException {
        URL CityDeatailesAPI = new URL("http://getcitydetails.geobytes.com/GetCityDetails?fqcn=" + city);
        BufferedReader Input = new BufferedReader(new InputStreamReader(CityDeatailesAPI.openStream()));
        CityDetailes tmp = new CityDetailes();
        String CityData;
        while ((CityData = Input.readLine()) != null) {
            String CityName = CityData.substring(CityData.indexOf("\"geobytescity\"") + 15, CityData.indexOf("\"geobytescityid\"") - 1);
            String Country = CityData.substring(CityData.indexOf("\"geobytescountry\"") + 18, CityData.indexOf("\"geobytesregionlocationcode\"") - 1);
            String Capital = CityData.substring(CityData.indexOf("\"geobytescapital\"") + 18, CityData.indexOf("\"geobytestimezone\"") - 1);
            String Currency = CityData.substring(CityData.indexOf("\"geobytescurrency\"") + 19, CityData.indexOf("\"geobytescurrencycode\"") - 1);
            String PopulationSize = CityData.substring(CityData.indexOf("\"geobytespopulation\"") + 21, CityData.indexOf("\"geobytesnationalityplural\"") - 1);
            tmp.InitiateCityDetailes(CityName, Country, Capital, Currency, PopulationSize);
        }
        Input.close();
        return tmp;
    }
}

//todo - check to use this function
//    public String ProccessDocCity(String section){
//        StringBuilder tmpCity = new StringBuilder();
//        Matcher CityMatcher = CityPattern.matcher(section);
//        while(CityMatcher.find()){
//            tmpCity.append(CityMatcher.group());
//            tmpCity = new StringBuilder(tmpCity.toString().substring(9));
//            tmpCity = new StringBuilder(tmpCity.toString().substring(0,tmpCity.length()-4));
//            String[] Split = tmpCity.toString().split("\\s+");
//            tmpCity = new StringBuilder( Split[0].toUpperCase());
//        }
//        return tmpCity.toString();
//    }


//                String CitySection = element.getElementsByTag("F").toString();
//                if (CitySection.contains("<f p=\"104\">")) {
//                    CitySection = CitySection.substring(CitySection.indexOf("<f p=\"104\">", CitySection.indexOf("</f>")));
//                    CitySection = CitySection.substring(CitySection.indexOf("\n", CitySection.indexOf("\n")));
//                    CitySection = CitySection.replaceAll("\n", "");
//                    CitySection = CitySection.replace("(", "");
//                    CitySection = CitySection.replace(")", "");
//                    CitySection = CitySection.trim();
//                    CitySection = CitySection.split(" ")[0];
//                    CitySection = CitySection.toUpperCase();
//                    if (!SearchEngine.City.containsKey(CitySection))
//                        ProcessCity(CitySection);
//                    else {
//                        CitySection = "";
//                    }
//                    //todo  check - String DocCity = ProccessDocCity(CitySection);
//                }
