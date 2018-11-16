import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Parse {

    private static HashMap<String,String> Date_DataStructure;
    private static HashSet<String> StopWords;
    public static HashMap<String,HashSet<String>> Terms;  // <Term ,HashSet<DocID> TermInDoc>  sent to indexer!!!
    public Stemmer stemmer;
    public String StopWordsPath;
    public StringBuilder stb;
    protected boolean IsStemmerNeeded;
    protected boolean IsRegNumber;
    protected boolean IsPrecent;
    protected boolean IsAPrice;
    protected boolean IsADate;
    protected boolean IsARange;

    public void Parse(){ }

    public HashMap<String,HashSet<String>> ParseCorpus(HashMap<String,DocDetailes> Docs, boolean stemmerneeded , String stopwordspath) {
        this.StopWordsPath = stopwordspath;
        this.IsStemmerNeeded=stemmerneeded;
        StopWords = new HashSet<>();
        Terms = new HashMap<>();
        Date_DataStructure = new HashMap<>();
        stemmer = new Stemmer();
        stb = new StringBuilder();
        InitiateDates();
        InitiateStopWords();
        // get keySet() into Set
        Set<String> setOfKeySet = Docs.keySet();
        // for-each loop
        for(String DocId : setOfKeySet) {
            String ToParse = Docs.get(DocId).getDocText();
            ParseDoc(ToParse,IsStemmerNeeded);
        }
        return Terms;
    }








    public void ParseDoc(String DocText , boolean stemmerneeded){
        ArrayList<String> DocList = new ArrayList<String>(Arrays.asList(DocText.split("[\\*\\ \\:\\?\\(\\)\\'\\`\\,\\;\\|\\<\\>\\!\\/]")));
        for(int i=0;i<DocList.size();i++){




        }
    }

    public StringBuilder SuspectedAsNumber(String token){

        return stb;
    }

    public StringBuilder SuspectedAsCapital(String token){

        return stb;
    }

    public StringBuilder SuspectedAsPercent(String token){

        return stb;
    }

    public StringBuilder SuspectedAsPrice(String token){

        return stb;
    }

    public StringBuilder SuspectedAsDate(String token){

        return stb;
    }

    public StringBuilder SuspectedAsRange(String token){

        return stb;
    }

    public void StemmDoc(ArrayList<String> Doctext){



    }

    public void InitiateStopWords(){
        File f = new File(this.StopWordsPath);
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
            String line = bufferedReader.readLine();
            while (line != null ){
                StopWords.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void InitiateDates(){
        Date_DataStructure.put("JAN", "01"); Date_DataStructure.put("FEB", "02"); Date_DataStructure.put("MAR", "03"); Date_DataStructure.put("APR", "04");
        Date_DataStructure.put("MAY", "05"); Date_DataStructure.put("JUN", "06"); Date_DataStructure.put("JUL", "07"); Date_DataStructure.put("AUG", "08");
        Date_DataStructure.put("SEP", "09"); Date_DataStructure.put("OCT", "10"); Date_DataStructure.put("NOV", "11"); Date_DataStructure.put("DEC", "12");
        Date_DataStructure.put("Jan", "01"); Date_DataStructure.put("Feb", "01"); Date_DataStructure.put("Mar", "03"); Date_DataStructure.put("Apr", "04");
        Date_DataStructure.put("May", "05"); Date_DataStructure.put("Jun", "06"); Date_DataStructure.put("Jul", "07"); Date_DataStructure.put("Aug", "08");
        Date_DataStructure.put("Sep", "09"); Date_DataStructure.put("Oct", "10"); Date_DataStructure.put("Nov", "11"); Date_DataStructure.put("Dec", "12");
        Date_DataStructure.put("January", "01"); Date_DataStructure.put("February", "02"); Date_DataStructure.put("March", "03"); Date_DataStructure.put("April", "04");
        Date_DataStructure.put("June", "06"); Date_DataStructure.put("July", "07"); Date_DataStructure.put("August", "08"); Date_DataStructure.put("September", "09");
        Date_DataStructure.put("October", "10"); Date_DataStructure.put("November", "11"); Date_DataStructure.put("December", "12");
        Date_DataStructure.put("JANUARY", "01"); Date_DataStructure.put("FEBUARY", "02"); Date_DataStructure.put("MARCH", "03"); Date_DataStructure.put("APRIL", "04");
        Date_DataStructure.put("JUNE", "06"); Date_DataStructure.put("JULY", "07"); Date_DataStructure.put("AUGUST", "08"); Date_DataStructure.put("SEPTEMBER", "09");
        Date_DataStructure.put("OCTOBER", "10"); Date_DataStructure.put("NOVEMBER", "11"); Date_DataStructure.put("DECEMBER", "12");
    }
}
