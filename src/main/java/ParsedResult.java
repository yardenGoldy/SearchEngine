public class ParsedResult {
    public Boolean IsMatch;
    public StringBuilder ParsedSentence;
    public int Index;
    public ParsedResult(Boolean isMatch, StringBuilder parsedSentence, int index){
        IsMatch = isMatch;
        ParsedSentence = parsedSentence;
        Index = index;
    }
}

