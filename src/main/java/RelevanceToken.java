import java.util.List;

public class RelevanceToken implements IToken {

    @Override
    public ParsedResult TryParse(List<String> sentence) {
        String first = sentence.get(0);
        int size = first.length();
        Integer index = 1;
        if (size < 3) {
            return null;
        }
        if (first.substring(first.length() - 2).equals("'s")) {
            StringBuilder result = new StringBuilder();
            first = first.substring(0, first.length() - 2);
            return new ParsedResult(true, result.append(first), index);
        }

        return null;
    }
}