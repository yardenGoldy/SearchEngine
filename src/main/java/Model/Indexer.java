package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class Indexer {

    public HashMap<String, DictionaryDetailes> Dictionary; //<Term,Model.DictionaryDetailes>
    public HashMap<String, ArrayList<PostingDetailes>> Posting; //<DocId,Model.PostingDetailes>
    public HashMap<String, ArrayList<PostingDetailes>> Cache; //<DocId,Model.PostingDetailes>
    public HashMap<String, CityDetailes> City;       //<City,CityDetailes>   //needed API
    public StringBuilder stb;
    public int NumOfTermsBeforeStemming;
    public int NumOfTermsAfterStemming;
    public int NumOfDiffrentCountryInCorpus;
    public int NumOfDiffrentCityInCorpus;
    public int NumOfDiffrentCityNotCapitalInCorpus;
    public long PostingSize;
    public String DocMaxCityFreq;
    // print all the terms of docid : FBIS3-3366 Alphabetic order (before stemming) + freq every term in doc



    public Indexer() {
        Dictionary = new HashMap<>();
        Posting = new HashMap<>();
        Cache = new HashMap<>();
        City = new HashMap<>();
        stb = new StringBuilder();
        NumOfTermsBeforeStemming = 0;
        NumOfTermsAfterStemming = 0;
        NumOfDiffrentCountryInCorpus = 0;
        NumOfDiffrentCityInCorpus = 0;
        NumOfDiffrentCityNotCapitalInCorpus = 0;
        PostingSize = 0;
        DocMaxCityFreq = null;
    }


    public void CreateIndexer(HashMap<String,ArrayList<TermDetailes>> CorpusAfterParse, String CorpusPath, Boolean isStemmer) {
        SortTerms(CorpusAfterParse);














    }

    public void SortTerms(HashMap<String,ArrayList<TermDetailes>> Terms){
        ArrayList<String> sortedKeys = new ArrayList<String>(Terms.keySet());
        Collections.sort(sortedKeys);
    }

    public void InitiatePostingFiles(String MainFolder,Boolean isStemmer){
            try {
                FileWriter PostingFile;
                FileWriter ResultFile;
                if (isStemmer) {
                    PostingFile = new FileWriter(MainFolder);
                    ResultFile = new FileWriter(MainFolder);
                } else {
                    PostingFile = new FileWriter(MainFolder);
                    ResultFile = new FileWriter(MainFolder);
                }
                BufferedWriter OutPosting = new BufferedWriter(PostingFile);
                BufferedWriter OutResult = new BufferedWriter(ResultFile);

                OutPosting.write("eran");
                OutResult.write("edri");







                OutPosting.close();
                OutResult.close();

            } catch (IOException e){}
    }

    // print doc name with max freq of city ,name of the city , location of the city in the doc
    public void PrintDocMaxCityFreq(){

    }

    // print top 10 max freq terms in corpus - ascending order + print top 10 min freq terms in corpus (before stemming) - ascending order
    public void PrintMAX_MIN10_Freq(){

    }

    // show specialwords in zipslaw graph
    public void InitiateZipGraph(){

    }
}
