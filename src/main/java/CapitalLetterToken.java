import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**\
 * This department is responsible for capital letter type words
 * if the word does not belong to the department, return Null value, Otherwise we will parse according to laws
 */
public class CapitalLetterToken implements IToken {

    private HashMap<String, HashMap<String, TermDetailes>> dictForIndex;
    private HashSet<String> stopWords;

    /**
     * constructor
     * @param dict - dictionary of all the term from the corpus
     * @param stopWords - hash set of all the stop word we need ignore of them
     */
    public CapitalLetterToken(HashMap<String, HashMap<String, TermDetailes>> dict, HashSet<String> stopWords)
    {
        this.stopWords = stopWords;
        this.dictForIndex = dict;
    }

    /**
     * A function that parse the words in the corpus according to the set rules
     * @param sentence - word we need check
     * @return - result after parse
     */
    public ParsedResult TryParse(List<String> sentence)
    {
        String first = sentence.get(0);
        // check if all the char is letter
        for (int i = 0; i < first.length();i++)
        {
            if(!Character.isLetter(first.charAt(i)))
            {
                return null;
            }
        }

        HashMap <String, TermDetailes> newHashMapForTerm;
        String capitalCase = first.toUpperCase(); //all the letter big
        String lowerCase = first.toLowerCase(); //all the letter small
        Integer index = 1;
        StringBuilder result = new StringBuilder();

        // if the first char is not capital letter and the work contain in the dictionary on capital case or the word is stop word
        if ((!Character.isUpperCase(first.charAt(0)) && !dictForIndex.containsKey(capitalCase)) || this.stopWords.contains(lowerCase)){
            return null;
        }
        //check if the word in the dictionary is in capital letters and our word is with lowercase letters
        if (this.dictForIndex.containsKey(capitalCase)&& Character.isLowerCase(first.charAt(0))){
            this.dictForIndex.put(lowerCase, this.dictForIndex.get(capitalCase));
            this.dictForIndex.remove(capitalCase);
            result.append(lowerCase);
        }
        // A new term , add to the hashMap
        else {
            result.append(capitalCase);
        }
        return new ParsedResult(true, result, index);
    }
}
