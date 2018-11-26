import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


// for yuval :
// 1. update for each doc his length without stopwords only verified tokens -- use: public void setDocLength(int docLength) {DocLength = docLength;}
// 2. pay attention that we add a field of position of every word in a doc , look the update of TermDetailes
// 3. remember that what i get back from the parse is : HashMap<String,ArrayList<TermDetailes>> Terms;


public class Parse {
    //public static HashMap<String,ArrayList<TermDetailes>> Terms;
    private ArrayList<IToken> departments;
    // term -> {docId -> {positions, TF}}
    private HashMap<String, HashMap<String, TermDetailes>> resultForIndex;
    private HashSet<String> stopWords;
    //array of all stopword

    public Parse(HashSet<String> stopWords) {
        this.stopWords = stopWords;
        resultForIndex = new HashMap<String, HashMap<String, TermDetailes>>();
        departments = new ArrayList<IToken>();
        departments.add(new RangeToken());
        departments.add(new PercentageToken());
        departments.add(new DateToken());
        departments.add(new PriceToken());
        departments.add(new NumberToken());
        departments.add(new CapitalLetterToken(resultForIndex, stopWords));
        departments.add(new RelevanceToken());
    }

    // public HashMap<String,HashSet<String>> ParseCorpus(HashMap<String,DocDetailes> Docs, boolean stemmerneeded , String stopwordspath)
    public ArrayList<DocDetailes> ParseCorpus(ArrayList<DocDetailes> Docs, Boolean isStemmer) {
        ArrayList<DocDetailes> result = new ArrayList<>();
        for (int i = 0; i < Docs.size();i++)
        {
            String currentText = Docs.get(i).DocText;
            List<String> textSplitted = Arrays.asList(currentText.split("\\s+"));
            StringBuilder resultText = new StringBuilder();

            // todo hanle out of range exception
            int j = 0;
            int textLength = textSplitted.size();
            while(j < textLength)
            {
                List<String> sentenceToCheck = textSplitted.subList(j, j+8 < textLength ? j+8 : textLength);
                removeSpecialChars(sentenceToCheck);
                ParsedResult parsedResult = this.GetSuitableDepartment(sentenceToCheck);
                // Not Found
                if(parsedResult == null){
                    String currentWork = textSplitted.get(j);
                    // todo : change text to id and index to real index
                    // String.valueOf(i) -> Docs.get(i).id
                    UpdateInDictionary(currentWork, String.valueOf(i), j);
                    resultText.append(currentWork);
                    j++;
                }
                // Found a parsed department
                else
                {
                    resultText = resultText.append(parsedResult.ParsedSentence);
                    UpdateInDictionary(parsedResult.ParsedSentence.toString(), String.valueOf(i), 6);
                    j += parsedResult.Index;
                }
                resultText.append(" ");
            }

            resultText.deleteCharAt(resultText.length() - 1);

            DocDetailes parsedDoc = Docs.get(i);
            parsedDoc.setDocText(resultText.toString());
            result.add(parsedDoc);
        }

        return result;
    }


    public void UpdateInDictionary(String wordToAdd, String docId, int index){
        if(stopWords.contains(wordToAdd.toLowerCase()))
        {
            return;
        }

        HashMap<String, TermDetailes> WhereIsFound;
        TermDetailes termToUpload;
        if(!resultForIndex.containsKey(wordToAdd))
        {
            WhereIsFound = new HashMap<String, TermDetailes>();
            termToUpload = new TermDetailes(docId);
        }
        else
        {
            WhereIsFound = resultForIndex.get(wordToAdd);
            termToUpload = resultForIndex.get(wordToAdd).get(docId);
        }

        termToUpload.UpdateTF();
        termToUpload.Positions.add(index);
        WhereIsFound.put(docId, termToUpload);
        resultForIndex.put(wordToAdd, WhereIsFound);
    }

    public void removeSpecialChars(List<String> sentenceToCheck){
        int sentenceSize = sentenceToCheck.size();
        for (int i = 0; i < sentenceSize;i++){
            String current = sentenceToCheck.get(i);
            int wordSize = current.length();
            if(wordSize > 2)
            {
                char firstChar = current.charAt(0);
                char lastChar = current.charAt(wordSize - 1);
                if(!(Character.isLetter(firstChar) || Character.isDigit(firstChar) || firstChar == '-' || firstChar == '$'))
                {
                    sentenceToCheck.set(i,sentenceToCheck.get(i).substring(1));
                }

                if(!(Character.isLetter(lastChar) || Character.isDigit(lastChar) || lastChar == '%'))
                {
                    sentenceToCheck.set(i,current.substring(0, wordSize - 1));
                }
            }
        }
    }

    public ParsedResult GetSuitableDepartment(List<String> sentence)
    {
        for (int i = 0; i < departments.size();i++)
        {
            ParsedResult result = departments.get(i).TryParse(sentence);
            if(result != null && result.IsMatch){
                return result;
            }
        }

        return null;
    }
}
