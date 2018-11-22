package Model;

import java.util.*;
import java.util.HashMap;

public class Indexer {

    public HashMap<String,DictionaryDetailes> Dictionary; //<Term,Model.DictionaryDetailes>
    public HashMap<String,PostingDetailes> Posting; //<DocId,Model.PostingDetailes>
    public HashMap<String,PostingDetailes> Cache; //<DocId,Model.PostingDetailes>
    public HashMap<String,CityDetailes> City;       //<City,CityDetailes>   //needed API
    // print NumOfTermsBeforeStemming;
    // print NumOfTermsAfterStemming;
    // print NumOfDiffrentCountryInCorpus;
    // print NumOfDiffrentCityInCorpus;
    // print NumOfDiffrentCityNotCapitalInCorpus;
    // print doc name with max freq of city ,name of the city , location of the city in the doc
    // print top 10 max freq terms in corpus - ascending order + print top 10 min freq terms in corpus (before stemming) - ascending order
    // show specialwords in zipslaw graph
    // print all the terms of docid : FBIS3-3366 Alphabetic order (before stemming) + freq every term in doc
    // print size of posting in KB


    public Indexer(){
        Dictionary = new HashMap<>();
        Posting = new HashMap<>();
        Cache = new HashMap<>();
        City = new HashMap<>();
    }


    public void CreateIndexer(HashMap<String,HashSet<String>> CorpusAfterParse){  // <Term ,HashSet<DocID>>







    }
}
