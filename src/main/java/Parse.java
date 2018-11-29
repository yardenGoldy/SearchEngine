import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


//todo- yuval- update for each doc his length without stopwords only verified tokens -
// use: public void setDocLength(int docLength) {DocLength = docLength;}

/**
 * This class should perform parse on the entire corpus
 */
public class Parse {
    private ArrayList<IToken> departments;
    // term -> {docId -> {positions, TF}}
    private HashMap<String, HashMap<String, TermDetailes>> resultForIndex;
    private HashSet<String> stopWords;
    //array of all stop word

    /** constructor
     * @param stopWords - accepts all words defined as stop word
     */
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

    /** The main function that manages the entire parse process
     * @param Docs - gets a list of documents to parse
     * @param isStemmer - a boolean variable that determines whether to perform stemming on the corpus
     * @return - a term dictionary whose value is another dictionary that contains information about the term for documents
     */
    public HashMap<String, HashMap<String, TermDetailes>> ParseCorpus(ArrayList<DocDetailes> Docs, Boolean isStemmer) {
        ArrayList<DocDetailes> result = new ArrayList<>(); // todo- we need result?
        //running on all the documents
        for (int i = 0; i < Docs.size(); i++) {
            System.out.println(Docs.get(i).DocID); // todo - Delete - to test
            String currentText = Docs.get(i).DocText;
            // if we do not recognize text, we will not parse ans continue to another document
            if(currentText.equals("") ){
                continue;
            }
            else {
                List<String> textSplitted = Arrays.asList(currentText.split("\\s+"));
                StringBuilder resultText = new StringBuilder();  // todo- we need resultText?
                int j = 0; // Index that runs on all words in a document
                int textLength = textSplitted.size();
                while (j < textLength) {
                    //move 8 words from the document at a time
                    List<String> sentenceToCheck = textSplitted.subList(j, j + 8 < textLength ? j + 8 : textLength);
                    removeSpecialChars(sentenceToCheck);
                    ParsedResult parsedResult = this.GetSuitableDepartment(sentenceToCheck);
                    // if we did not find a suitable department for the word ,
                    // We will add it to the dictionary using the dictionary's update function
                    if (parsedResult == null) {
                        String currentWork = textSplitted.get(j);
                        UpdateInDictionary(currentWork, (Docs.get(i)).getDocID(), j);
                        resultText.append(currentWork); // todo- we need resultText?
                        j++;
                    }
                    // Found a parsed department We will add it to the dictionary using the dictionary's update function
                    else {
                        resultText = resultText.append(parsedResult.ParsedSentence); // todo- we need resultText?
                        UpdateInDictionary(parsedResult.ParsedSentence.toString(), (Docs.get(i)).getDocID(), j);
                        j += parsedResult.Index;
                    }
                    resultText.append(" "); // todo- we need resultText?
                }
                resultText.deleteCharAt(resultText.length() - 1); // todo- we need resultText?
                DocDetailes parsedDoc = Docs.get(i);
                parsedDoc.setDocText(resultText.toString()); // todo- we need resultText?
                result.add(parsedDoc);  // todo- we need result?
            }
        }
        //todo -Delete before serving
        // print the dictionary - for test
        for (Map.Entry<String, HashMap<String, TermDetailes>> entry : resultForIndex.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (Map.Entry<String, TermDetailes> doc : entry.getValue().entrySet()) {
                System.out.print("<" + doc.getKey() + " , " + doc.getValue().toString() + "> -> ");
            }
            System.out.println();
        }
        return resultForIndex;
    }

    /** Function in updating words with new values ​​of term.
     * first we checks if the word exists in the dictionary, if it exists we will update her fields
     * otherwise, we'll create a new key for the word in our dictionary.
     * @param wordToAdd - a word passed parse and we check whether it exists in the dictionary or we need to create a new value for her
     * @param docId - document name, words can appear in different documents
     * @param index - saves the location of the word in the document
     */
    public void UpdateInDictionary(String wordToAdd, String docId, int index) {
        //If the word is stopWord or '.' - we don't add to dictionary
        if (wordToAdd.equals(".") || stopWords.contains(wordToAdd.toLowerCase())) {
            return;
        }
        String wordToAddBigLetter = wordToAdd.toUpperCase();
        String wordToAddSmallLetter = wordToAdd.toLowerCase();
        HashMap<String, TermDetailes> which_document;
        TermDetailes term_Detail_Per_Doc;
        // If the word does not exist in the dictionary , we'll create a new one - new term
        if (!resultForIndex.containsKey(wordToAddBigLetter) && !resultForIndex.containsKey(wordToAddSmallLetter)) {
            which_document = new HashMap<String, TermDetailes>();
            term_Detail_Per_Doc = new TermDetailes(docId);
            term_Detail_Per_Doc.UpdateTF();
            term_Detail_Per_Doc.Positions.add(index);
            which_document.put(docId, term_Detail_Per_Doc);
            resultForIndex.put(wordToAdd, which_document);
        }
        //the word exists in the dictionary
        else {
            wordToAdd = resultForIndex.containsKey(wordToAddBigLetter) ? wordToAdd.toUpperCase() : wordToAdd.toLowerCase();
            //the word exists in the dictionary per docId
            if (resultForIndex.get(wordToAdd).containsKey(docId)) {

                which_document = resultForIndex.get(wordToAdd);
                term_Detail_Per_Doc = resultForIndex.get(wordToAdd).get(docId);
            }
            //the word exists in the dictionary but for another document - We'll create a new entry in the hashSet
            else {
                term_Detail_Per_Doc = new TermDetailes(docId);
            }
            //Update all the values
            term_Detail_Per_Doc.UpdateTF();
            term_Detail_Per_Doc.Positions.add(index);
            resultForIndex.get(wordToAdd).put(docId, term_Detail_Per_Doc);
        }
    }

    /**
     * A function that removes the characters that are not relevant to the word.
     * @param sentenceToCheck - The function accepts a sentence containing different characters and removes them.
     */
    public void removeSpecialChars(List<String> sentenceToCheck) {
        int sentenceSize = sentenceToCheck.size();
        // Passing each of the words in the sentence
        for (int i = 0; i < sentenceSize; i++) {
            String current = sentenceToCheck.get(i);
            int wordSize = current.length();
            // If the word is length 1 and it is not a number or a letter, we will become a point and remove it from the dictionary later
            if (wordSize == 1 && !Character.isDigit(current.charAt(0)) && !(Character.isLetter(current.charAt(0)))) {
                sentenceToCheck.set(i, ".");
            }
            //If the length of the word is longer than 2.
            else if (wordSize > 2) {
                char firstChar = current.charAt(0);
                char lastChar = current.charAt(wordSize - 1);
                //If the first character is not a number, a letter, '-' or '$' - we will remove him.
                if (!(Character.isLetter(firstChar) || Character.isDigit(firstChar) || firstChar == '-' || firstChar == '$')) {
                    sentenceToCheck.set(i, sentenceToCheck.get(i).substring(1));
                }
                //If the last character is not a number, a letter or '%' - We will remove him.
                if (!(Character.isLetter(lastChar) || Character.isDigit(lastChar) || lastChar == '%')) {
                    sentenceToCheck.set(i, sentenceToCheck.get(i).substring(0, wordSize - 1)); //todo-there is problem here
                }
            }
        }
    }

    /**
     * A function that is checked for a particular word or a collection of words for which department they belongs
     *
     * @param sentence - List of words (up to 8 words) sent for review
     * @return If we find a match for the sentence, we will return the result after following the rules.
     * else - we can not find a suitable class, we will return null.
     */
    public ParsedResult GetSuitableDepartment(List<String> sentence) {
        for (int i = 0; i < departments.size(); i++) {
            ParsedResult result = departments.get(i).TryParse(sentence);
            if (result != null && result.IsMatch) {
                return result;
            }
        }

        return null;
    }
}
