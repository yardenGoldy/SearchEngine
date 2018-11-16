import java.io.*;
import java.util.*;
import javafx.util.Pair;
import java.util.HashMap;

public class Indexer {

    public HashMap<String,DictionaryDetailes> Dictionary; //<Term,DictionaryDetailes>
    public HashMap<String,PostingDetailes> Posting; //<DocId,PostingDetailes>
    public HashMap<String,PostingDetailes> Cache; //<DocId,PostingDetailes>
    public HashMap<String,DocDetailes> Docs;       //<DocId,DocDetailes>


    public Indexer(){
        Dictionary = new HashMap<>();
        Posting = new HashMap<>();
        Cache = new HashMap<>();
    }


    public void CreateIndexer(HashMap<String,HashSet<String>> CorpusAfterParse){  // <Term ,HashSet<DocID>>



    }
}
