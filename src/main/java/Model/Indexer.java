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



    public void CreateIndexer(HashMap<String,TermDetailes> CorpusAfterParse){







    }

    public HashMap<String,String> AfterParseTest(){ // זה סתם עשיתי בשבילי בשביל להתקדם באינדקסר אל תתייחסי
        HashMap<String,String> TestTerms = new HashMap<>();
        TestTerms.put("now","1");TestTerms.put("is","1");TestTerms.put("the","1");TestTerms.put("time","1");
        TestTerms.put("for","1");TestTerms.put("all","1");TestTerms.put("good","1");TestTerms.put("men","1");
        TestTerms.put("to","1");TestTerms.put("come","1");TestTerms.put("to","1");TestTerms.put("the","1");
        TestTerms.put("aid","1");TestTerms.put("of","1");TestTerms.put("their","1");TestTerms.put("country","1");
        TestTerms.put("it","2");TestTerms.put("was","2");TestTerms.put("a","2");TestTerms.put("dark","2");
        TestTerms.put("and","2");TestTerms.put("stormy","2");TestTerms.put("night","2");TestTerms.put("in","2");
        TestTerms.put("the","2");TestTerms.put("country","2");TestTerms.put("manor","2");TestTerms.put("","2");
        TestTerms.put("the","2");TestTerms.put("time","2");TestTerms.put("was","2");TestTerms.put("past","2");
        TestTerms.put("midnight","2");
        return TestTerms;

    }
}
