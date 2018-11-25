import java.io.IOException;
import java.util.*;



public class SearchEngine {
    public String CorpusPath;
    public String StopWordsPath;
    public static boolean StemmerNeeded;
    public static int NumOfDocs;
    public static HashMap<String,DocDetailes> Docs;  //<DocId,DocDetailes>
    public HashMap<String,ArrayList<TermDetailes>> Terms;  //<Term,TermDetailes>   //פה אני מחכה לתשובה שלך יובל איך המבנה נתונים שאני מקבל לאינדקסר יראה





    public SearchEngine(String corpusPath, String stopWordsPath , boolean Steemer) throws IOException {
        CorpusPath = corpusPath;
        StopWordsPath = stopWordsPath;
        StemmerNeeded = Steemer;
//        HashMap<String,DocDetailes> Docs = new HashMap<>();
        ReadFile readFile = new ReadFile();
        Docs = readFile.ReadAllDocs(CorpusPath);
//        HashMap<String,TermDetailes> Terms = new HashMap<>();
//        Parse parse = new Parse();
//        Terms = parse.ParseCorpus(Docs,StemmerNeeded,StopWordsPath);
//        parse.ParseCorpus(new ArrayList<DocDetailes>(Docs.values()), false, null);
        Indexer indexer = new Indexer();
        HashMap<String,ArrayList<TermDetailes>> test_terms = new HashMap<>();
        test_terms = IndexerTest();
        indexer.CreateIndexer(test_terms,CorpusPath,StemmerNeeded);
    }


    public HashMap<String,ArrayList<TermDetailes>> IndexerTest(){ // זה סתם עשיתי בשבילי בשביל להתקדם באינדקסר אל תתייחסי
        HashMap<String,ArrayList<TermDetailes>> TestTerms = new HashMap<>();
        TermDetailes t1 = new TermDetailes("1");ArrayList<TermDetailes> T1 = new ArrayList<>();T1.add(t1);TestTerms.put("now",T1);
        TermDetailes t2 = new TermDetailes("1");ArrayList<TermDetailes> T2 = new ArrayList<>();T2.add(t2);TestTerms.put("is",T2);
        TermDetailes t3 = new TermDetailes("1");t3.UpdateTF();ArrayList<TermDetailes> T3 = new ArrayList<>();T3.add(t3);TestTerms.put("the",T3);//needed to add position
        TermDetailes t4 = new TermDetailes("1");ArrayList<TermDetailes> T4 = new ArrayList<>();T4.add(t4);TestTerms.put("time",T4);
        TermDetailes t5 = new TermDetailes("1");ArrayList<TermDetailes> T5 = new ArrayList<>();T5.add(t5);TestTerms.put("for",T5);
        TermDetailes t6 = new TermDetailes("1");ArrayList<TermDetailes> T6 = new ArrayList<>();T6.add(t6);TestTerms.put("all",T6);
        TermDetailes t7 = new TermDetailes("1");ArrayList<TermDetailes> T7 = new ArrayList<>();T7.add(t7);TestTerms.put("good",T7);
        TermDetailes t8 = new TermDetailes("1");ArrayList<TermDetailes> T8 = new ArrayList<>();T8.add(t8);TestTerms.put("men",T8);
        TermDetailes t9 = new TermDetailes("1");t9.UpdateTF();ArrayList<TermDetailes> T9 = new ArrayList<>();T9.add(t9);TestTerms.put("to",T9);//needed to add position
        TermDetailes t10 = new TermDetailes("1");ArrayList<TermDetailes> T10 = new ArrayList<>();T10.add(t10);TestTerms.put("come",T10);
        //TermDetailes t11 = new TermDetailes("1");T9.add(t11);
        //TermDetailes t12 = new TermDetailes("1");T3.add(t12);
        TermDetailes t13 = new TermDetailes("1");ArrayList<TermDetailes> T13 = new ArrayList<>();T13.add(t13);TestTerms.put("aid",T13);
        TermDetailes t14 = new TermDetailes("1");ArrayList<TermDetailes> T14 = new ArrayList<>();T14.add(t14);TestTerms.put("of",T14);
        TermDetailes t15 = new TermDetailes("1");ArrayList<TermDetailes> T15 = new ArrayList<>();T15.add(t15);TestTerms.put("their",T15);
        TermDetailes t16 = new TermDetailes("1");ArrayList<TermDetailes> T16 = new ArrayList<>();T16.add(t16);TestTerms.put("country",T16);
        TermDetailes t17 = new TermDetailes("2");ArrayList<TermDetailes> T17 = new ArrayList<>();T17.add(t17);TestTerms.put("it",T17);
        TermDetailes t18 = new TermDetailes("2");t18.UpdateTF();ArrayList<TermDetailes> T18 = new ArrayList<>();T18.add(t18);TestTerms.put("was",T18);//needed to add position
        TermDetailes t19 = new TermDetailes("2");ArrayList<TermDetailes> T19 = new ArrayList<>();T19.add(t19);TestTerms.put("a",T19);
        TermDetailes t20 = new TermDetailes("2");ArrayList<TermDetailes> T20 = new ArrayList<>();T20.add(t20);TestTerms.put("dark",T20);
        TermDetailes t21 = new TermDetailes("2");ArrayList<TermDetailes> T21 = new ArrayList<>();T21.add(t21);TestTerms.put("and",T21);
        TermDetailes t22 = new TermDetailes("2");ArrayList<TermDetailes> T22 = new ArrayList<>();T22.add(t22);TestTerms.put("stormy",T22);
        TermDetailes t23 = new TermDetailes("2");ArrayList<TermDetailes> T23 = new ArrayList<>();T23.add(t23);TestTerms.put("night",T23);
        TermDetailes t24 = new TermDetailes("2");ArrayList<TermDetailes> T24 = new ArrayList<>();T24.add(t24);TestTerms.put("in",T24);
        TermDetailes t25 = new TermDetailes("2");t25.UpdateTF();T3.add(t25);//needed to add position
        TermDetailes t26 = new TermDetailes("2");T16.add(t26);
        TermDetailes t27 = new TermDetailes("2");ArrayList<TermDetailes> T27 = new ArrayList<>();T27.add(t27);TestTerms.put("manor",T27);
        //TermDetailes t28 = new TermDetailes("2");;T3.add(t28);
        TermDetailes t29 = new TermDetailes("2");T4.add(t29);
        //TermDetailes t30 = new TermDetailes("2");T18.add(t30);
        TermDetailes t31 = new TermDetailes("2");ArrayList<TermDetailes> T31 = new ArrayList<>();T31.add(t31);TestTerms.put("past",T31);
        TermDetailes t32 = new TermDetailes("2");ArrayList<TermDetailes> T32 = new ArrayList<>();T32.add(t32);TestTerms.put("midnight",T32);
        return TestTerms;
    }

}
