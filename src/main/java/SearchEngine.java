import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class SearchEngine {
    public String CorpusPath;
    public String StopWordsPath;
    boolean StemmerNeeded;
    public HashMap<String,DocDetailes> Docs;
    public static HashMap<String,HashSet<String>> Terms;





    public SearchEngine(String corpusPath, String stopWordsPath , boolean Steemer) throws IOException {
        CorpusPath = corpusPath;
        StopWordsPath = stopWordsPath;
        StemmerNeeded = Steemer;
        Docs = new HashMap<>();
        ReadFile readFile = new ReadFile();
        Docs = readFile.ReadAllDocs(CorpusPath);
        Parse parse = new Parse();
        Terms = parse.ParseCorpus(Docs,StemmerNeeded,StopWordsPath);
        // Indexer indexer = new Indexer();
        // indexer.CreateIndexer(Terms);
    }

}
