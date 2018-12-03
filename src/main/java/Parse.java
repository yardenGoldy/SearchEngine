
import java.io.*;
import java.util.*;

/**
 * This class should perform parse on the entire corpus
 */

// parse doc doc
public class Parse {
    public Boolean isStemmer;
    public static  Stemmer stemmer;
    private HashSet<String> stopWords; //array of all stop word
    private ArrayList<IToken> departments;
    private HashMap<String, TermDetailes> resultForIndex;
    private HashSet<String > DicForCapitalLetterToken; //todo to find a way to delete thid dic
    public int index;


    public Parse(Boolean isStemmer, String stopwordsPath) throws FileNotFoundException {
        if(isStemmer) {
            stemmer = new Stemmer();
        }
        resultForIndex = new HashMap<>();
        this.stopWords = InitiateStopWords(stopwordsPath);
        DicForCapitalLetterToken = new HashSet<>();
        departments = new ArrayList<IToken>();
        departments.add(new RangeToken());
        departments.add(new PercentageToken());
        departments.add(new DateToken());
        departments.add(new PriceToken());
        departments.add(new NumberToken());
        departments.add(new CapitalLetterToken( resultForIndex, stopWords));
        departments.add(new RelevanceToken());
    }

    /**
     * The main function that manages the entire parse process
     *
     * @param - document to parse
     * @param - a boolean variable that determines whether to perform stemming on the corpus
     * @return - a term dictionary whose value is another dictionary that contains information about the term for documents
     */
    public HashMap<String, TermDetailes> ParseDoc(String Doctxt, String Docid ,String DocCity , String DocTitle) { //HashMap<Term,TermDetailes> parsedDoc
        resultForIndex = new HashMap<>();StringBuilder stb = new StringBuilder();int textLength = 0;int index = 0;// Index that runs on all words in a document
        List<String> textSplitted = Arrays.asList(Doctxt.split("[\\s\\n\\r]+"));
        removeSpecialChars(textSplitted);
        String tmp = textSplitted.toString();
        tmp = tmp.substring(1,tmp.length()-1);
        textSplitted = Arrays.asList(tmp.replaceAll(",", "").split("[\\s\\n\\r]+"));
        textLength = textSplitted.size();
        if (textSplitted.get(0).equals("")) {
            textSplitted = textSplitted.subList(1, textSplitted.size());
            textLength--;
        }
        while (index < textLength) {
            //move 8 words from the document at a time
            List<String> sentenceToCheck = textSplitted.subList(index, index + 8 < textLength ? index + 8 : textLength);
            ParsedResult parsedResult = this.GetSuitableDepartment(sentenceToCheck);
            // if we did not find a suitable department for the word ,
            // We will add it to the dictionary using the dictionary's update function
            if (parsedResult == null) {
                String currentWork = textSplitted.get(index);
                UpdateInDictionary(currentWork, Docid, index);
                index++;
            }
            // Found a parsed department We will add it to the dictionary using the dictionary's update function
            else {
                UpdateInDictionary(parsedResult.ParsedSentence.toString(), Docid, index);
                index += parsedResult.Index;
            }
        }
        SearchEngine.All_Docs.get(Docid).setNumOfSpecialWords(resultForIndex.size()); //todo- check 1
        return resultForIndex;
    }

    /**
     * Function in updating words with new values ​​of term.
     * first we checks if the word exists in the dictionary, if it exists we will update her fields
     * otherwise, we'll create a new key for the word in our dictionary.
     *
     * @param wordToAdd - a word passed parse and we check whether it exists in the dictionary or we need to create a new value for her
     * @param docId     - document name, words can appear in different documents
     * @param index     - saves the location of the word in the document
     */
    public void UpdateInDictionary(String wordToAdd, String docId, int index) {
        //If the word is stopWord or '.' - we don't add to dictionary
        if (wordToAdd.equals(".") || stopWords.contains(wordToAdd.toLowerCase())) {
            return;
        }
        String wordToAddBigLetter = wordToAdd.toUpperCase();
        String wordToAddSmallLetter = wordToAdd.toLowerCase();
        // If the word does not exist in the dictionary , we'll create a new one - new term
        if (!resultForIndex.containsKey(wordToAddBigLetter) && !resultForIndex.containsKey(wordToAddSmallLetter)) {
            TermDetailes term_Detail_Per_Doc = new TermDetailes(docId);
            //term_Detail_Per_Doc.AddPosition(index);
            DicForCapitalLetterToken.add(wordToAdd);
            resultForIndex.put(wordToAdd, term_Detail_Per_Doc);
        }
        //the word exists in the dictionary
        else {
            wordToAdd = resultForIndex.containsKey(wordToAddBigLetter) ? wordToAdd.toUpperCase() : wordToAdd.toLowerCase();
            resultForIndex.get(wordToAdd).UpdateTF();
            //resultForIndex.get(wordToAdd).AddPosition(index);
        }
    }

    /**
     * A function that removes the characters that are not relevant to the word.
     *
     * @param sentenceToCheck - The function accepts a sentence containing different characters and removes them.
     */
    //todo - needed to substract more delimiters!!
    public void removeSpecialChars(List<String> sentenceToCheck) {
        int sentenceSize = sentenceToCheck.size();
        // Passing each of the words in the sentence
        for (int i = 0; i < sentenceSize; i++) {
            Boolean isRemoved = false;
            String current = sentenceToCheck.get(i);
            int wordSize = current.length();
            // If the word is length 1 and it is not a number or a letter, we will become a point and remove it from the dictionary later
            if (wordSize == 1 && !Character.isDigit(current.charAt(0)) && !(Character.isLetter(current.charAt(0)))) {
                sentenceToCheck.set(i, "");
            }
            //If the length of the word is longer than 2.
            else if (wordSize > 1) {
                String currentMiddle = current;
                char firstChar = current.charAt(0);
                char lastChar = current.charAt(wordSize - 1);
                //If the first character is not a number, a letter, '-' or '$' - we will remove him.
                if (!(Character.isLetter(firstChar) || Character.isDigit(firstChar) ||
                        (firstChar == '-' && Character.isDigit(current.charAt(1))) || firstChar == '$')) {
                    isRemoved = true;
                    currentMiddle = currentMiddle.substring(1);
                    sentenceToCheck.set(i, sentenceToCheck.get(i).substring(1));
                }
                //If the last character is not a number, a letter or '%' - We will remove him.
                if (!(Character.isLetter(lastChar) || Character.isDigit(lastChar) || lastChar == '%')) {
                    isRemoved = true;
                    currentMiddle = currentMiddle.substring(0, wordSize - 1);
                    sentenceToCheck.set(i, sentenceToCheck.get(i).substring(0, wordSize - 1)); //todo-there is problem here
                }

                if (!isNumeric(current)) {
                    if (isRemoved) {
                        sentenceToCheck.set(i, currentMiddle.replaceAll("[^a-zA-Z0-9\\.\\/']+", " "));
                    } else {
                        currentMiddle = currentMiddle.substring(1, wordSize - 1);
                        sentenceToCheck.set(i, (current.charAt(0) + currentMiddle.replaceAll("[^a-zA-Z0-9\\.\\/']+", " ") + current.charAt(wordSize - 1)));
                    }
                }
            }
        }
    }

    public Boolean isNumeric(String s) {
        Boolean res;
        res = (s != null && s.matches("[-+]?\\d*\\.?\\d+"));
        return res;
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

    public static HashSet<String> InitiateStopWords(String path) throws FileNotFoundException {
        HashSet<String> stopWords = new HashSet<String>();
        File f = new File(path);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                stopWords.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }
}