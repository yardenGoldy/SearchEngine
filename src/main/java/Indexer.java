import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Indexer {

    public static HashMap<String, DictionaryDetailes> Dictionary; //<Term,DictionaryDetailes>
    public static HashMap<String, ArrayList<TermDetailes>> Posting; //<DocId,PostingDetailes>
    public static HashMap<String, ArrayList<TermDetailes>> Cache; //<DocId,PostingDetailes>
    public static HashMap<String, CityDetailes> City;       //<City,CityDetailes>   //needed API
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

        for (Map.Entry<String, ArrayList<TermDetailes>> key : CorpusAfterParse.entrySet()) {
            int i = 0;
            String tmpTerm = key.getKey();
            ArrayList<TermDetailes> tmpArrayList = key.getValue();
            DictionaryDetailes tmpDictionaryDetailes = new DictionaryDetailes();
            CityDetailes tmpCityDetailes = new CityDetailes();
            tmpDictionaryDetailes.setName(tmpTerm);
            tmpDictionaryDetailes.setNumOfDocsTermIN(tmpArrayList.size());
            //tmpDictionaryDetailes.setPointer();
            if (IsACity(tmpTerm)) {tmpCityDetailes.InitiateCityDetailes("beer sheba","israel","ILS","1000");}
            // API brings :String cityName, String country, String crrency, String populationSize
            while (i < tmpArrayList.size()) {
                tmpDictionaryDetailes.UpdateNumOfTermInCorpus(tmpArrayList.get(i).getTF());
                if (SearchEngine.Docs.get(tmpArrayList.get(i).getDocId()).getMaxTermFrequency() < tmpArrayList.get(i).getTF())
                    SearchEngine.Docs.get(tmpArrayList.get(i).getDocId()).setMaxTermFrequency(tmpArrayList.get(i).getTF());
                if (IsACity(tmpTerm))
                    tmpCityDetailes.AddNewDoc(tmpArrayList.get(i).DocId, tmpArrayList.get(i).getPositions());
                i++;
            }
            Dictionary.put(tmpTerm,tmpDictionaryDetailes);
            Posting.put("test",tmpArrayList);
            City.put(tmpTerm,tmpCityDetailes);
        }
    }

    public Boolean IsACity(String term){

        return false;
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
