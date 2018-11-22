package Model;

import java.util.*;
import java.util.HashMap;

public class Indexer {

    public HashMap<String,DictionaryDetailes> Dictionary; //<Term,Model.DictionaryDetailes>
    public HashMap<String,PostingDetailes> Posting; //<DocId,Model.PostingDetailes>
    public HashMap<String,PostingDetailes> Cache; //<DocId,Model.PostingDetailes>
    public HashMap<String,DocDetailes> Docs;       //<DocId,Model.DocDetailes>


    public Indexer(){
        Dictionary = new HashMap<>();
        Posting = new HashMap<>();
        Cache = new HashMap<>();
    }


    public void CreateIndexer(HashMap<String,HashSet<String>> CorpusAfterParse){  // <Term ,HashSet<DocID>>



    }
}
