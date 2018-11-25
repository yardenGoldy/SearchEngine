import java.util.HashMap;
import java.util.List;

public class PriceToken extends NumberToken implements IToken {
    private HashMap<String, Double> NumberByNumber;

    public PriceToken() {
        this.NumberByNumber = new HashMap<>();
        this.InitiateNumber();
    }

    @Override
    public ParsedResult TryParse(List<String> sentence) {
        int size = sentence.size();
        String first = sentence.get(0);
        String second = size > 1 ? sentence.get(1) : "";
        String third = size > 2 ? sentence.get(2) : "";
        String fourth = size > 3 ? sentence.get(3) : "";
        StringBuilder result = new StringBuilder();
        String withoutDollar = first;
        Integer index = 1;

        //it's not price
        //todo  : change to equals
        if (first.charAt(0) != '$' || !second.equals("Dollars") || !third.equals("Dollars") || !fourth.equals("Dollars") || !second.equals("dollars") || !third.equals("dollars") || !fourth.equals("dollars")) {
            return null;
        }
        // if there is $ - cut him
        if (first.charAt(0) == '$') {
            withoutDollar = first.substring(1, first.length());
        }
        //if the last char is 'm' so cut him
        else if (first.charAt(withoutDollar.length() - 1) == 'm') {
            withoutDollar = first.substring(0, first.length() - 1);
        }
        String firstWithoutCommaAndDollar = withoutDollar.replaceAll(",", "");

        //if the first token is not number
        if (!isNumeric(firstWithoutCommaAndDollar)) {
            return null;
        }

        //it's number - do parse
        Double numPrice = Double.parseDouble(firstWithoutCommaAndDollar);

        if (numPrice < 1000000) {
            if (second.contains("/") && (third.equals("Dollars") || third.equals("dollars"))) {
                return new ParsedResult(true, result.append(String.format("%s %s Dollars", first, second)), 3);
            }
            // return number as is - num without fraction
            else if (!second.contains("/") && (third.equals("Dollars") || second.equals("Dollars"))) {
                return new ParsedResult(true, result.append(first).append(" Dollars"), 2);
            } else {
                return new ParsedResult(true, result.append(withoutDollar).append(" Dollars"), 1);
            }
        } else if (NumberByNumber.containsKey(second)) {
            numPrice *= NumberByNumber.get(second);
            if (third == "U.S." && (fourth == "Dollars" || fourth == "dollars")) {
                index = 4;
            } else if (third == "Dollars" || third == "dollars") {
                index = 3;
            } else {
                index = 2;
            }
            result.append(FinallyParse(numPrice));
        } else if (numPrice > 1000000 && (second == "Dollars" || second == "dollars")) {
            index = 2;
            result.append(FinallyParse(numPrice));
        } else {
            index = 1;
            result.append(FinallyParse(numPrice));
        }
        //it's number with fraction and dollar - 22 3/4 Dollars

        return new ParsedResult(true, result, index);
    }

    private StringBuilder FinallyParse(Double num) {
        StringBuilder result = new StringBuilder();
        String divResult = String.valueOf(num / 1000000.0).replace(".0", "");
        result.append(divResult).append(" ").append("M").append(" ").append(" Dollars");
        return result;
    }

    public Boolean isNumeric(String s) {
        Boolean res;
        res = (s != null && s.matches("[-+]?\\d*\\.?\\d+"));
        return res;
    }

    private void InitiateNumber() {
        NumberByNumber.put("Thousand", 1000.0);
        NumberByNumber.put("thousand", 1000.0);
        NumberByNumber.put("Million", 1000000.0);
        NumberByNumber.put("million", 1000000.0);
        NumberByNumber.put("m", 1000000.0);
        NumberByNumber.put("bn", 1000000000.0);
        NumberByNumber.put("Billion", 1000000000.0);
        NumberByNumber.put("billion", 1000000000.0);
        NumberByNumber.put("Trillion", 1000000000000.0);
        NumberByNumber.put("trillion", 1000000000000.0);
    }
}