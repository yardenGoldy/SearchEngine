import java.util.HashMap;
import java.util.List;

public class CapitalLetterToken implements IToken {

    private HashMap<String, HashMap<String, TermDetailes>> dictForIndex;
    public CapitalLetterToken(HashMap<String, HashMap<String, TermDetailes>> dict)
    {
        dictForIndex = dict;
    }

    @Override
    public ParsedResult TryParse(List<String> sentence)
    {
        return null;
    }
}
