import java.io.*;
import java.util.*;

/**
 * This class should perform parse on the entire corpus
 * parse doc after doc
 */
public class Parse {
    public boolean isStemmer;
    public static Stemmer stemmer = null; //todo- it's ok that is public static ?
    private HashSet<String> stopWords; //array of all stop word
    private ArrayList<IToken> departments;
    public static ArrayList<Integer> LocationOfCity;
    public static HashMap<String, TermDetailes> resultForIndex; //todo - it's ok that is public static ?
    //public int index;

    /**
     * Constructor
     *
     * @param isStemmer     - a boolean variable that determines whether to perform stemming on the corpus
     * @param stopwordsPath accepts all words defined as stop word
     * @throws FileNotFoundException
     */
    public Parse(boolean isStemmer, String stopwordsPath) throws FileNotFoundException {
        if (isStemmer) {
            stemmer = new Stemmer();
        }
        resultForIndex = new HashMap<String, TermDetailes>();
        this.stopWords = InitiateStopWords(stopwordsPath); //todo- it's good ?
        departments = new ArrayList<IToken>();
        departments.add(new RangeToken());
        departments.add(new PercentageToken());
        departments.add(new DateToken());
        departments.add(new PriceToken());
        departments.add(new NumberToken());
        departments.add(new CapitalLetterToken(stopWords));
        departments.add(new RelevanceToken());
    }

    /**
     * The main function that manages the entire parse process
     *
     * @param DocText - Text of a single document we will parse
     * @param DocId  - Document ID of the text
     * @param DocTitle -The title of the document
     * @param CityOfDoc - The city that appears under the pattern of a city
     * @return Dictionary of term with details about him
     */
    public HashMap<String, TermDetailes> ParseDoc(String DocText, String DocId , String CityOfDoc , String DocTitle ) {
        if (DocText.equals("")) {
            return resultForIndex;
        } else {
            //long timeForShit = System.nanoTime();
            StringBuilder stb = new StringBuilder();
            int textLength = 0;
            int index = 0;// Index that runs on all words in a document
            List<String> textSplitted = Arrays.asList(DocText.split("[\\s\\n\\r]+"));
            //long timeForText = System.nanoTime();
            removeSpecialChars(textSplitted);
            String TextWithoutDelimiter = textSplitted.toString();
            TextWithoutDelimiter = TextWithoutDelimiter.substring(1, TextWithoutDelimiter.length() - 1);
            textSplitted = Arrays.asList(TextWithoutDelimiter.replaceAll(",", "").split("[\\s\\n\\r]+"));
            textLength = textSplitted.size();
            if (textSplitted.get(0).equals("")) {
                textSplitted = textSplitted.subList(1, textSplitted.size());
                textLength--;
            }
//            long endTimeShit = System.nanoTime();
//            if ((endTimeShit - timeForShit) > 10000000)
//            System.out.println("Time for shit is " + ((endTimeShit - timeForShit) / 1000000));
            while (index < textLength) {
                //move 8 words from the document at a time
                List<String> sentenceToCheck = textSplitted.subList(index, index + 8 < textLength ? index + 8 : textLength);
                ParsedResult parsedResult = this.GetSuitableDepartment(sentenceToCheck);
                // if we did not find a suitable department for the word ,
                // We will add it to the dictionary using the dictionary's update function
                if (parsedResult == null) {
                    String currentWork = textSplitted.get(index);
                    UpdateInDictionary(currentWork, DocId, index, CityOfDoc ,DocTitle);
                    index++;
                }
                // Found a parsed department We will add it to the dictionary using the dictionary's update function
                else {
                    UpdateInDictionary(parsedResult.ParsedSentence.toString(), DocId, index , CityOfDoc ,DocTitle);
                    index += parsedResult.Index;
                }
            }
        }
//        long endTime = System.nanoTime();
//        if ((endTime - timeForText) > 10000000)
//            System.out.println("Time for text is " + ((endTime - timeForText) / 1000000));
        //Update the different term number we have collected from the document
        SearchEngine.All_Docs.get(DocId).setNumOfSpecialWords(resultForIndex.size()); //todo- check 1
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
    public void UpdateInDictionary(String wordToAdd, String docId, int index ,String CityOfDoc , String DocTitle ) {
        //If the word is stopWord or '.' - we don't add to dictionary
        if (wordToAdd.equals(".") || stopWords.contains(wordToAdd.toLowerCase())) {
            return;
        }
        String wordToAddBigLetter = wordToAdd.toUpperCase();
        String wordToAddSmallLetter = wordToAdd.toLowerCase();
        // If the word is the city of the document, We'll update its position in the Locations array
        if(!CityOfDoc.equals("")&& wordToAddBigLetter.equals(CityOfDoc)){
            LocationOfCity = new ArrayList<>();
            LocationOfCity.add(index);
        }
        // If the word does not exist in the dictionary , we'll create a new one - new term
        if (!resultForIndex.containsKey(wordToAddBigLetter) && !resultForIndex.containsKey(wordToAddSmallLetter)) {
            TermDetailes term_Detail_Per_Doc = new TermDetailes(docId);
            resultForIndex.put(wordToAdd, term_Detail_Per_Doc);
            //Searches for the index where the word is found, if it does not find -1
            if ( !DocTitle.equals("") &&  DocTitle.toLowerCase().indexOf(wordToAddSmallLetter) != -1 ) {
                term_Detail_Per_Doc.isInTitle = true;
            }
            term_Detail_Per_Doc.UpdateTF();
            resultForIndex.put(wordToAdd, term_Detail_Per_Doc);
        }
        //the word exists in the dictionary
        else {
            wordToAdd = resultForIndex.containsKey(wordToAddBigLetter) ? wordToAdd.toUpperCase() : wordToAdd.toLowerCase();
            resultForIndex.get(wordToAdd).UpdateTF();
        }
        resultForIndex.get(wordToAdd).SetDocId(docId);
//        long endTime = System.nanoTime();
//        if ((endTime - timeForUpdateDict) > 10000000)
//            System.out.println("Time for update dictionary is " + ((endTime - timeForUpdateDict) / 1000000));
    }

    /**
     * A function that removes the characters that are not relevant to the word.
     *
     * @param sentenceToCheck - The function accepts a sentence containing different characters and removes them.
     */
    public void removeSpecialChars(List<String> sentenceToCheck) {
        //long timeForRemoveChars = System.nanoTime();
        int sentenceSize = sentenceToCheck.size();
        // Passing each of the words in the sentence
        for (int i = 0; i < sentenceSize; i++) {
            boolean isRemoved = false;
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
//        long endTime = System.nanoTime();
//        if ((endTime - timeForRemoveChars) > 10000000)
//            System.out.println("Time for remove special chars is " + ((endTime - timeForRemoveChars) / 1000000));
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
//                long endTime = System.nanoTime();
//                if ((endTime - timeForParsing) > 10000000) {
//                    System.out.println("time for parsing is  " + ((endTime - timeForParsing) / 1000000));
//                }
                return result;
            }
        }
//        long endTime = System.nanoTime();
//        if ((endTime - timeForParsing) > 10000000) {
//            System.out.println("time for not found parsing is  " + ((endTime - timeForParsing) / 1000000));
//            }
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