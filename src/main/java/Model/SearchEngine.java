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
//        Docs = new HashMap<>();
//        ReadFile readFile = new ReadFile();
//        Docs = readFile.ReadAllDocs(CorpusPath);
//        Parse parse = new Parse();
//        parse.ParseCorpus(new ArrayList<DocDetailes>(Docs.values()), false, null);
        Indexer indexer = new Indexer();
        StringBuilder PathForIndexer = new StringBuilder();
        PathForIndexer.append(CorpusPath);
        PathForIndexer.append("/CorpusOut");
        HashMap<String,TermDetailes> test_terms = new HashMap<>();
        test_terms = IndexerTest();
        indexer.CreateIndexer(test_terms,PathForIndexer);
    }


    public HashMap<String,TermDetailes> IndexerTest(){ // זה סתם עשיתי בשבילי בשביל להתקדם באינדקסר אל תתייחסי
        HashMap<String,TermDetailes> TestTerms = new HashMap<>();
        TermDetailes t1 = new TermDetailes("1",1);TestTerms.put("now",t1);
        TermDetailes t2 = new TermDetailes("1",1);TestTerms.put("is",t2);
        TermDetailes t3 = new TermDetailes("1",1);TestTerms.put("the",t3);
        TermDetailes t4 = new TermDetailes("1",1);TestTerms.put("time",t4);
        TermDetailes t5 = new TermDetailes("1",1);TestTerms.put("for",t5);
        TermDetailes t6 = new TermDetailes("1",1);TestTerms.put("all",t6);
        TermDetailes t7 = new TermDetailes("1",1);TestTerms.put("good",t7);
        TermDetailes t8 = new TermDetailes("1",1);TestTerms.put("men",t8);
        TermDetailes t9 = new TermDetailes("1",1);TestTerms.put("to",t9);
        TermDetailes t10 = new TermDetailes("1",1);TestTerms.put("come",t10);
        TermDetailes t11 = new TermDetailes("1",1);TestTerms.put("to",t11);
        TermDetailes t12 = new TermDetailes("1",1);TestTerms.put("the",t12);
        TermDetailes t13 = new TermDetailes("1",1);TestTerms.put("aid",t13);
        TermDetailes t14 = new TermDetailes("1",1);TestTerms.put("of",t14);
        TermDetailes t15 = new TermDetailes("1",1);TestTerms.put("their",t15);
        TermDetailes t16 = new TermDetailes("1",1);TestTerms.put("country",t16);
        TermDetailes t17 = new TermDetailes("2",1);TestTerms.put("it",t17);
        TermDetailes t18 = new TermDetailes("2",1);TestTerms.put("was",t18);
        TermDetailes t19 = new TermDetailes("2",1);TestTerms.put("a",t19);
        TermDetailes t20 = new TermDetailes("2",1);TestTerms.put("dark",t20);
        TermDetailes t21 = new TermDetailes("2",1);TestTerms.put("and",t21);
        TermDetailes t22 = new TermDetailes("2",1);TestTerms.put("stormy",t22);
        TermDetailes t23 = new TermDetailes("2",1);TestTerms.put("night",t23);
        TermDetailes t24 = new TermDetailes("2",1);TestTerms.put("in",t24);
        TermDetailes t25 = new TermDetailes("2",1);TestTerms.put("the",t25);
        TermDetailes t26 = new TermDetailes("2",1);TestTerms.put("country",t26);
        TermDetailes t27 = new TermDetailes("2",1);TestTerms.put("manor",t27);
        TermDetailes t28 = new TermDetailes("2",1);TestTerms.put("",t28);
        TermDetailes t29 = new TermDetailes("2",1);TestTerms.put("the",t29);
        TermDetailes t30 = new TermDetailes("2",1);TestTerms.put("time",t30);
        TermDetailes t31 = new TermDetailes("2",1);TestTerms.put("was",t31);
        TermDetailes t32 = new TermDetailes("2",1);TestTerms.put("past",t32);
        TermDetailes t33 = new TermDetailes("2",1);TestTerms.put("midnight",t33);
        return TestTerms;
    }

}
