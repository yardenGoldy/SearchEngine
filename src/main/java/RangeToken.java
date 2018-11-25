import java.util.List;

public class RangeToken extends NumberToken {

    @Override
    public ParsedResult TryParse(List<String> sentence) {
        String first = sentence.get(0);
        StringBuilder result = new StringBuilder();
        Integer index = 0;
        int size = sentence.size();
        //todo : handle numbers
        if (!first.equals("Between") && !first.equals("between")) {
            return null;
        } else if(size > 3) {
            index++;
            ParsedResult firstNumber = super.TryParse(sentence.subList(1, size));
            if(firstNumber.IsMatch && (firstNumber.Index + 1) < size && sentence.get(firstNumber.Index + 1).equals("and"))
            {
                index += firstNumber.Index + 1;
                if((firstNumber.Index + 2) < size)
                {
                    ParsedResult secondNumber = super.TryParse(sentence.subList(firstNumber.Index + 2, size));
                    if(secondNumber.IsMatch)
                    {
                        index += secondNumber.Index;
                        result.append(String.format("%s-%s", firstNumber.ParsedSentence, secondNumber.ParsedSentence));
                    }
                }
            }
        }

        return new ParsedResult(true, result, index);
    }
}
