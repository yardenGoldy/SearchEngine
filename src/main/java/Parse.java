import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// for yuval :
// 1. update for each doc his length without stopwords only verified tokens -- use: public void setDocLength(int docLength) {DocLength = docLength;}

public class Parse {
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
    public HashMap<String, HashMap<String, TermDetailes>> ParseCorpus(ArrayList<DocDetailes> Docs, Boolean isStemmer) {
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
                    UpdateInDictionary(currentWork, (Docs.get(i)).getDocID(), j);
                    resultText.append(currentWork);
                    j++;
                }
                // Found a parsed department
                else
                {
                    resultText = resultText.append(parsedResult.ParsedSentence);
                    UpdateInDictionary(parsedResult.ParsedSentence.toString(), (Docs.get(i)).getDocID(), j);
                    j += parsedResult.Index;
                }
                resultText.append(" ");
            }

            resultText.deleteCharAt(resultText.length() - 1);

            DocDetailes parsedDoc = Docs.get(i);
            parsedDoc.setDocText(resultText.toString());
            result.add(parsedDoc);
        }
    // print the dictionary
        for (Map.Entry<String,HashMap<String,TermDetailes>> entry:resultForIndex.entrySet()){
            System.out.print(entry.getKey() + ": ");
            for (Map.Entry<String,TermDetailes> doc:entry.getValue().entrySet()){
                System.out.print("<" +  doc.getKey() + " , " + doc.getValue().toString() + "> -> ");
            }
            System.out.println();
        }
        return resultForIndex;
    }


    public void UpdateInDictionary(String wordToAdd, String docId, int index){
        //If the word is stopWord we don't add to dictionary
        String wordToAddBigLetter = wordToAdd.toUpperCase();
        String wordToAddSmallLetter = wordToAdd.toLowerCase();

        if(wordToAdd.equals(".") || stopWords.contains(wordToAdd.toLowerCase()))
        {
            return;
        }
        HashMap<String, TermDetailes> which_document;
        TermDetailes term_Detail_Per_Doc;
        // If the word does not exist in the dictionary , we'll create a new one
        if(!resultForIndex.containsKey(wordToAddBigLetter) && !resultForIndex.containsKey(wordToAddSmallLetter))
        {
            which_document = new HashMap<String, TermDetailes>();
            term_Detail_Per_Doc = new TermDetailes(docId);
            term_Detail_Per_Doc.UpdateTF();
            term_Detail_Per_Doc.Positions.add(index);
            which_document.put(docId, term_Detail_Per_Doc);
            resultForIndex.put(wordToAdd,which_document);
        }
        //the word exists in the dictionary
        else
        {
            wordToAdd = resultForIndex.containsKey(wordToAddBigLetter) ? wordToAdd.toUpperCase() : wordToAdd.toLowerCase();
            //the word exists in the dictionary per docId
            if (resultForIndex.get(wordToAdd).containsKey(docId))
            {

                which_document = resultForIndex.get(wordToAdd);
                term_Detail_Per_Doc = resultForIndex.get(wordToAdd).get(docId);
            }
            //the word exists in the dictionary but for another document
            else {
                //which_document = new HashMap<String, TermDetailes>();
                term_Detail_Per_Doc = new TermDetailes(docId);
            }
            term_Detail_Per_Doc.UpdateTF();
            term_Detail_Per_Doc.Positions.add(index);
            resultForIndex.get(wordToAdd).put(docId,term_Detail_Per_Doc);
        }

        //which_document.put(docId, term_Detail_Per_Doc);

    }

    public void removeSpecialChars(List<String> sentenceToCheck){
        int sentenceSize = sentenceToCheck.size();
        for (int i = 0; i < sentenceSize;i++){
            String current = sentenceToCheck.get(i);
            int wordSize = current.length();
            if(wordSize == 1 && !Character.isDigit(current.charAt(0)) && !(Character.isLetter(current.charAt(0)))){
                sentenceToCheck.set(i, ".");
            }
            else if(wordSize > 2)
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
