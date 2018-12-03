
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SearchEngine {
    public static String CorpusPathIN;
    public static String CorpusPathOUT;
    public static StringBuilder StopWordsPath;
    public static boolean StemmerNeeded;
    private ReadFile readFile;
    private Parse parse;
    private Indexer indexer;
    HashMap<String, DocDetailes> DocsPerBlock;
    HashMap<String, TermDetailes> TermsPerDoc;
    public static HashMap<String, DocDetailes> All_Docs;  //<DocId,Model.DocDetailes>
    public static HashMap<String, CityDetailes> City;       //<City,CityDetailes>


    public SearchEngine(String corpusPathIN, String corpusPathOUT, boolean Steemer) throws IOException {
        CorpusPathIN = corpusPathIN;
        CorpusPathOUT = corpusPathOUT;
        StopWordsPath = new StringBuilder(corpusPathIN + "/stop_words.txt");
        StemmerNeeded = Steemer;
        readFile = new ReadFile(CorpusPathIN);
        parse = new Parse(StemmerNeeded, StopWordsPath.toString());
        indexer = new Indexer(CorpusPathOUT, StemmerNeeded);
        All_Docs = new HashMap<>();
        City = new HashMap<>();
        long StartTime = System.nanoTime();
        System.out.println("Start your Engines!!!");
        // Starts threads
        readFile.ReadCorpus();
        for (int i = 0; i < readFile.GetSubFilesSize(); i++) {
            DocsPerBlock = readFile.ProccessSubFileToDocs(readFile.GetSubFilesPath(i));
            for (Map.Entry<String, DocDetailes> Docid : DocsPerBlock.entrySet()) {
                if(!Docid.getValue().getDocText().isEmpty() && !Docid.getKey().isEmpty()) {
                    if(!City.containsKey(Docid.getValue().getDocCity())) {
                        ProcessCity(Docid.getValue().getDocCity());
                    }
                    All_Docs.put(Docid.getKey(),Docid.getValue());
                    TermsPerDoc = parse.ParseDoc(Docid.getValue().getDocText(), Docid.getKey() , Docid.getValue().getDocCity(),Docid.getValue().getDocTitle()); //update - Positiones
                    indexer.CreateMINI_Posting(TermsPerDoc, Docid.getKey()); // update - tNumOfSpecialWords , DocLength , MaxTermFrequency
                }
            }
        }
        //finish threads

        indexer.ItsTimeForMERGE_All_Postings();
        indexer.ItsTimeToWriteDictionary();
        indexer.ItsTimeFor_FinalDoc();
        long FinishTime = System.nanoTime();
        long TotalTime = FinishTime - StartTime;
        System.out.println("Out of fuel...");
        System.out.println(TotalTime/1000000);
    }


    //if term not in City database
    public void ProcessCity(String city) throws IOException {
        //CityDetailes tmpCityDetailes = CityDetailesAPI(city); // API brings :String cityName, String country, String crrency, String populationSize
        City.put(city, CityDetailesAPI(city));
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
            //todo - parse needed to handle populationsize fix like parser do
            tmp.InitiateCityDetailes(CityName, Country, Capital, Currency, PopulationSize);
        }
        Input.close();
        return tmp;
    }


}
