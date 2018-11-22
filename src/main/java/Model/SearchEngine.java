package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SearchEngine {
    public String CorpusPath;
    public String StopWordsPath;
    boolean StemmerNeeded;
    public HashMap<String,DocDetailes> Docs;  //<DocId,Model.DocDetailes>
    public HashMap<String,TermDetailes> Terms;  //<Term,TermDetailes>   //פה אני מחכה לתשובה שלך יובל איך המבנה נתונים שאני מקבל לאינדקסר יראה





    public SearchEngine(String corpusPath, String stopWordsPath , boolean Steemer) throws IOException {
        CorpusPath = corpusPath;
        StopWordsPath = stopWordsPath;
        StemmerNeeded = Steemer;
        Docs = new HashMap<>();
        ReadFile readFile = new ReadFile();
        Docs = readFile.ReadAllDocs(CorpusPath);
        Parse parse = new Parse();
        parse.ParseCorpus(new ArrayList<DocDetailes>(Docs.values()), false, null);
        Indexer indexer = new Model.Indexer();
        indexer.CreateIndexer(Terms);
    }

}
