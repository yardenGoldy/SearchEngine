
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.net.*;


public class Indexer {

    public String CorpusPathOUT;
    public boolean StemmerNeeded;
    public static HashMap<String, DictionaryDetailes> Dictionary; //<Term,DictionaryDetailes>
    public static HashMap<String, ArrayList<TermDetailes>> Posting; //<Term,ArrayList<TermDetailes>>
    public static HashMap<String, ArrayList<TermDetailes>> Cache; //<Term,ArrayList<TermDetailes>>
    public StringBuilder stbOUT;
    public int PostingNumber;
    public int BlockCounter;
    public long PostingSize;
    public int NumOfTermsBeforeStemming;// Dictionary size
    public int NumOfTermsAfterStemming;// Dictionary size
    public int NumOfTerms_Numbers;
    public int NumOfDiffrentCountryInCorpus;//City
    public int NumOfDiffrentCityInCorpus;//City
    public int NumOfDiffrentCityNotCapitalInCorpus;//City


    public Indexer(String corpusPathOUT,Boolean isStemmer) {
        CorpusPathOUT = corpusPathOUT;
        StemmerNeeded = isStemmer;
        Dictionary = new HashMap<>();
        Posting = new HashMap<>();
        Cache = new HashMap<>();
        stbOUT = new StringBuilder();
        PostingNumber = 0;
        PostingSize = 0;
        BlockCounter = 0;
        NumOfTermsBeforeStemming = 0;
        NumOfTermsAfterStemming = 0;
        NumOfTerms_Numbers = 0;
        NumOfDiffrentCountryInCorpus = 0;
        NumOfDiffrentCityInCorpus = 0;
        NumOfDiffrentCityNotCapitalInCorpus = 0;

        if(StemmerNeeded){
            stbOUT.append(CorpusPathOUT + "/Posting_WithStemmer/");
        }
        else
            stbOUT.append(CorpusPathOUT + "/Posting/");

        File folder = new File(stbOUT.toString());
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles!=null) {
            for (int i = 0; i < listOfFiles.length; i++)
                try {
                    Files.deleteIfExists(listOfFiles[i].toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        else{
            folder.mkdir();
        }
    }


    public void CreateMINI_Posting(HashMap<String, TermDetailes> DocAfterParse ,String  Docid) throws IOException {
        int MaxTermFreq = 0;
        for (Map.Entry<String,TermDetailes> term : DocAfterParse.entrySet()) {
            String tmpTerm = term.getKey();
            TermDetailes tmpTermDetailes = term.getValue();
            // if term in Dic
            if (Dictionary.containsKey(tmpTerm)){
                Dictionary.get(tmpTerm).UpdateNumOfDocsTermIN();
                Dictionary.get(tmpTerm).UpdateNumOfTermInCorpus(tmpTermDetailes.getTF());
            }
            // if term not in Dic database
            else{
                DictionaryDetailes tmpDictionaryDetailes = new DictionaryDetailes(tmpTerm);
                tmpDictionaryDetailes.UpdateNumOfTermInCorpus(tmpTermDetailes.getTF());
                Dictionary.put(tmpTerm, tmpDictionaryDetailes);
            }
            // if term in Post database
            if(Posting.containsKey(tmpTerm)){
                Posting.get(tmpTerm).add(tmpTermDetailes);
            }
            // if term not in Post database
            else{
                ArrayList<TermDetailes> tmpPostDetailes = new ArrayList<>();
                tmpPostDetailes.add(tmpTermDetailes);
                Posting.put(tmpTerm,tmpPostDetailes);
            }
            if(SearchEngine.City.containsKey(term)) {
               // SearchEngine.City.get(tmpTerm).AddNewDoc(tmpTermDetailes.getDocId(), tmpTermDetailes.getPositions());
            }
            if(tmpTermDetailes.getTF() > MaxTermFreq){
                MaxTermFreq = tmpTermDetailes.getTF();
            }
        }
        SearchEngine.All_Docs.get(Docid).setMaxTermFrequency(MaxTermFreq);
        if(BlockCounter == 5000) {
            ItsTimeForFLUSH_POSTING();
            Posting.clear();
            BlockCounter = 0;
        }
        BlockCounter++;
    }

    //copy to disk
    public void ItsTimeForFLUSH_POSTING(){
        File tmpPost = new File(stbOUT.toString() + PostingNumber + ".txt");
        ArrayList<String> SortedPost = new ArrayList<>(Posting.keySet());
        Collections.sort(SortedPost);
        PostingNumber++;

        try {
            FileWriter FW = new FileWriter(tmpPost);
            BufferedWriter BW = new BufferedWriter(FW);
            for(String term : SortedPost){
                BW.write(term + ":");
                for(TermDetailes Td : Posting.get(term)){
                    BW.write( "(DocID :" + Td.getDocId() + "," + "TF:" + Td.getTF() +")->");
                }
                BW.newLine();
            }
            BW.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ItsTimeForMERGE_All_Postings() throws IOException {
        File file = new File(stbOUT.toString());
        File[] FilestoMerge = file.listFiles();
        while (FilestoMerge.length > 1) {
            for (int i = 0; i < FilestoMerge.length - 1; i += 2) {
                MERGE_SORT(FilestoMerge[i], FilestoMerge[i + 1]);
            }
            FilestoMerge = file.listFiles();
        }
//todo - how to handele last iteration and not losing tmpmerge
//        if(Posting.size() != 0) {
//            ItsTimeForFLUSH_POSTING();
//            FilestoMerge = file.listFiles();
//            MERGE_SORT(FilestoMerge[0], FilestoMerge[1]);
//        }
        ItsTImeToBoost();
        ItsTimeForSPLIT_Final_Posting_For_Dic();
    }

    public void MERGE_SORT(File F1 ,File F2) throws IOException{
        FileWriter FW = new FileWriter(new File(stbOUT + "./tmpMerge" + ".txt"));
        BufferedReader BR1 = new BufferedReader(new FileReader(F1));
        BufferedReader BR2 = new BufferedReader(new FileReader(F2));
        String S1 = BR1.readLine();
        String S2 = BR2.readLine();
        int Compare;

        while (S1 != null && S2 != null){
            if(S1.length()==0 || S2.length()==0)
                break;
            String t1 = S1.substring(0,S1.indexOf(":"));
            String t2 = S2.substring(0,S2.indexOf(":"));
            Compare = t1.compareTo(t2);

            if(Compare > 0){
                FW.write(S2 + "\n");
                S2 = BR2.readLine();
            }
            else if(Compare < 0){
                FW.write(S1 + "\n");
                S1 = BR1.readLine();
            }
            else{
                StringBuilder stb = new StringBuilder();
                stb.append(S1.substring(S1.indexOf(":")+2));
                stb.append(S2.substring(S2.indexOf(":")+2));
                FW.write(stb.toString() + "\n");
                S1 = BR1.readLine();
                S2 = BR2.readLine();
            }
        }
        while (S1 != null){
            if(S1.length()==0)
                break;
            FW.write(S1 +"\n");
            S1 = BR1.readLine();
        }
        while(S2 != null){
            if(S2.length()==0)
                break;
            FW.write(S2 +"\n");
            S2 = BR2.readLine();
        }
        FW.close();
        BR1.close();
        BR2.close();
        Files.delete(F1.toPath());
        Files.delete(F2.toPath());
    }

    //for showing the dic sorted
    //todo - special func the speed up the diccc
    public void ItsTimeForSPLIT_Final_Posting_For_Dic(){



        //ItsTImeToBoost();
        File Numbers = new File(stbOUT+"/Numbers.txt");
        File A_E = new File(stbOUT+"/A_E.txt");
        File F_J = new File(stbOUT+"/F_J.txt");
        File K_P= new File(stbOUT+"/K_O.txt" );
        File Q_U = new File(stbOUT+"/Q_U.txt");
        File V_Z = new File(stbOUT+"/V_Z.txt");
        int countNumber = 0 ;
        int countA_E = 0 ;
        int countF_J = 0 ;
        int countK_P = 0 ;
        int countQ_U = 0 ;
        int countV_Z = 0 ;

        File Final_Posting =  new File(stbOUT + "/tmpMerge" + ".txt");

        try{
            BufferedReader BR_Final_Posting = new BufferedReader(new FileReader(Final_Posting));
            FileWriter Numbers_FW = new FileWriter(Numbers);
            FileWriter A_E_FW= new FileWriter(A_E);
            FileWriter F_J_FW= new FileWriter(F_J);
            FileWriter K_P_FW= new FileWriter(K_P);
            FileWriter Q_U_FW= new FileWriter(Q_U);
            FileWriter V_Z_FW= new FileWriter(V_Z);

            BufferedWriter Numbers_BW = new BufferedWriter(Numbers_FW);
            BufferedWriter A_E_BW = new BufferedWriter(A_E_FW);
            BufferedWriter F_J_BW = new BufferedWriter(F_J_FW);
            BufferedWriter K_P_BW = new BufferedWriter(K_P_FW);
            BufferedWriter Q_U_BW = new BufferedWriter(Q_U_FW);
            BufferedWriter V_Z_BW = new BufferedWriter(V_Z_FW);

            String S = BR_Final_Posting.readLine();
            StringBuilder stbTerm = new StringBuilder();

            //if(!stbTerm.toString().isEmpty()){
            while(S != null) {
                stbTerm.append(S.substring(0, S.indexOf(":")));
                //A-E
                if (S.charAt(0) >= 'a' && S.charAt(0) <= 'e') {
                    A_E_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countA_E);
                    }
                    A_E_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countA_E++;
                    continue;
                }
                //F-J
                else if (S.charAt(0) >= 'f' && S.charAt(0) <= 'j') {
                    F_J_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countF_J);
                    }
                    F_J_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countF_J++;
                    continue;
                }
                //K-P
                else if (S.charAt(0) >= 'k' && S.charAt(0) <= 'p') {
                    K_P_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countK_P);
                    }
                    K_P_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countK_P++;
                    continue;
                }
                //Q-U
                else if (S.charAt(0) >= 'q' && S.charAt(0) <= 'u') {
                    Q_U_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countQ_U);
                    }
                    Q_U_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countQ_U++;
                    continue;
                }
                //V-Z
                else if (S.charAt(0) >= 'v' && S.charAt(0) <= 'z') {
                    V_Z_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countV_Z);
                    }
                    V_Z_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countV_Z++;
                    continue;
                }
                //Numbers
                else {
                    Numbers_BW.write(S);
                    if(Dictionary.containsKey(S)){
                        Dictionary.get(S).setPointer(countNumber);
                    }
                    Numbers_BW.newLine();
                    S = BR_Final_Posting.readLine();
                    stbTerm.setLength(0);
                    countNumber++;
                    continue;
                }
            }

            BR_Final_Posting.close();
            Files.delete(Final_Posting.toPath());
            Numbers_BW.close();
            A_E_BW.close();
            F_J_BW.close();
            K_P_BW.close();
            Q_U_BW.close();
            V_Z_BW.close();
            NumOfTerms_Numbers = countNumber;
            if(StemmerNeeded) {
                NumOfTermsAfterStemming = countA_E + countF_J + countK_P + countNumber + countQ_U + countV_Z;
            }
            else {
                NumOfTermsBeforeStemming = countA_E + countF_J + countK_P + countNumber + countQ_U + countV_Z;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void ItsTImeToBoost(){
        HashSet<String> Garbage = new HashSet<>();
        for (Map.Entry<String,DictionaryDetailes> term : Dictionary.entrySet()) {
            String tmpTerm = term.getKey();
            if(term.getValue().getNumOfTermInCorpus() < 8 || tmpTerm.equals(" ") || tmpTerm.equals("") || tmpTerm.isEmpty() || tmpTerm.length()<= 1){
                Garbage.add(tmpTerm);
            }
        }
        for(String t : Garbage){
            Dictionary.remove(t);
        }
    }


    // print doc name with max freq of city ,name of the city , location of the city in the doc
    public void PrintDocMaxCityFreq(){

    }

    // print top 10 max freq terms in corpus - ascending order + print top 10 min freq terms in corpus (before stemming) - ascending order
    public void PrintMAX_MIN10_Freq(){

    }

    // show specialwords in zipslaw graph
    public void InitiateZipGraphWith_SpecialWords(){

    }

    // print all the terms of docid : FBIS3-3366 Alphabetic order (before stemming) + freq every term in doc
    public void Print_Terms_SpecialDoc(){

    }

    public void ItsTimeFor_FinalDoc(){

    }

    public void ItsTimeForReset(){

    }

    public void ItsTimeToShowDictionary(){


    }

    public void ItsTimeToWriteDictionary(){


    }
}
