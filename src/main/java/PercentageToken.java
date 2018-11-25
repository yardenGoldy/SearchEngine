import java.util.List;

public class PercentageToken implements IToken {
    @Override
    public ParsedResult TryParse(List<String> sentence) {
        int size = sentence.size();
        String first = sentence.get(0);
        String second = size > 1 ? sentence.get(1) : "";
        //String third = sentence.get(2); //todo- maybe fraction
        StringBuilder result = new StringBuilder();
        Integer index = 1;
        Boolean isOk = true;
        // last char is percent
        if (first.endsWith("%")){
            index = 1;
            first = first.substring(0,first.length()-1);
        }
        else if (second.equals("percent") || second.equals("percentage")) {
            index = 2;
        }
        else
        {
            isOk = false;
        }
        if(isOk && isNumeric(first)){
            return new ParsedResult(true, result.append(first).append('%'), index);
        }

        return null;
    }

    public Boolean isNumeric(String s) {
        Boolean res;
        res = (s != null && s.matches("[-+]?\\d*\\.?\\d+"));
        return res;
    }
}