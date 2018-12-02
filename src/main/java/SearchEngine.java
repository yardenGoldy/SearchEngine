
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
                if(!Docid.getValue().getDocText().equals("")) {
                    All_Docs.put(Docid.getKey(),Docid.getValue());
                    TermsPerDoc = parse.ParseDoc(Docid.getValue().getDocText(), Docid.getKey()); //update - Positiones
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


}
