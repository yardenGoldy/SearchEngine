import java.util.List;

public class RangeToken extends NumberToken {

    @Override
    public ParsedResult TryParse(List<String> sentence) {
        String first = sentence.get(0);
        StringBuilder result = new StringBuilder();
        Integer index = 0;
        if (!first.equals("Between")) {
            return null;
        } else {
            index++;
            ParsedResult firstNumber = super.TryParse(sentence.subList(1, sentence.size()));
            if(firstNumber.IsMatch && sentence.get(firstNumber.Index + 1).equals("and"))
            {
                index += firstNumber.Index + 1;
                ParsedResult secondNumber = super.TryParse(sentence.subList(firstNumber.Index + 2, sentence.size()));
                if(secondNumber.IsMatch)
                {
                    index += secondNumber.Index;
                    result.append(String.format("%s-%s", firstNumber.ParsedSentence, secondNumber.ParsedSentence));
                }
            }
        }

        return new ParsedResult(true, result, index);
    }
}
