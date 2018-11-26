import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CapitalLetterToken implements IToken {

    private HashMap<String, HashMap<String, TermDetailes>> dictForIndex;
    private HashSet<String> stopWords;
    public CapitalLetterToken(HashMap<String, HashMap<String, TermDetailes>> dict, HashSet<String> stopWords)
    {
        this.stopWords = stopWords;
        this.dictForIndex = dict;
    }

    @Override
    public ParsedResult TryParse(List<String> sentence)
    {
        String first = sentence.get(0);

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

        // if the first char is capital letter - change all the char.
        if (!Character.isUpperCase(first.charAt(0)) || this.stopWords.contains(lowerCase)){
            return null;
        }
//
//        //if this stopWord
//        if (stopWord.containsKey(first)){
//            return  null;
//        }

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
