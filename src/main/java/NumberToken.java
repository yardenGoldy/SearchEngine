import java.util.HashMap;
import java.util.List;

public class NumberToken implements IToken {
    private HashMap< String ,  Double > NumberByNumber;
    public NumberToken(){
        this.NumberByNumber = new HashMap<>();
        this.InitiateNumber();
    }

    @Override
    public ParsedResult TryParse(List<String> sentence){
        int size = sentence.size();
        String first = sentence.get(0);
        String second = size > 1 ? sentence.get(1) : "";
        String third = size > 2 ? sentence.get(2) : "";
        StringBuilder result = new StringBuilder();
        Integer index = 1;

        //if the first token is not number
        String firstWithoutComma = first.replaceAll("," , "");
        if(!isNumeric(firstWithoutComma)){ return null; }
        else {
            //its number -> parse real Double
            Double num = Double.parseDouble(firstWithoutComma);
            if (NumberByNumber.containsKey(second)){
               num*= NumberByNumber.get(second);
               index = 2 ;
            }
            else if( second.contains("/") && NumberByNumber.containsKey(third)){
                num*= NumberByNumber.get(third);
                index = 3;
            }
            // return number as is - num without fraction
            if (num < 1000 && !second.contains("/")) {
                return new ParsedResult(true , result.append(firstWithoutComma), 1);
            }
            //return number as is with fraction
            else if (num < 1000 && second.contains("/")) {
                return new ParsedResult(true , result.append(String.format(" %s %s", firstWithoutComma, second)), 2);
            }
            //Thousand
            else if (num >= 1000.0 && num < 1000000.0){
                result.append(FinallyParse (num ,second, 'k' ,NumberByNumber.get("Thousand")));
            }
            //Million
            else if (num >= 1000000.0 && num < 1000000000.0){
                result.append(FinallyParse (num ,second, 'M' ,NumberByNumber.get("Million")));
            }
            //Billion
            else if (num >= 1000000000.0 && num < 1000000000000.0){
                result.append(FinallyParse (num ,second, 'B' ,NumberByNumber.get("Billion")));
            }
            //more than Billion - trillion
            else if (num >= 1000000000000.0){
                result.append(FinallyParse (num ,second, 'B' ,NumberByNumber.get("Billion")));
            }
        }
        return new ParsedResult(true , result, index);
    }

    private StringBuilder FinallyParse(Double num, String token, char suffix, Double div) {
        StringBuilder result = new StringBuilder();
        String divResult = String.valueOf(num/div).replace(".0", "");
        result.append(divResult);
        if (token.contains("/")){
            result.append(" ").append(token);
        }
        result.append(suffix);
        return result;
    }

    public Boolean isNumeric (String s){
        Boolean res;
        res = (s != null && s.matches("[-+]?\\d*\\.?\\d+"));
        return res;
    }

    private void InitiateNumber() {
        NumberByNumber.put("Thousand", 1000.0);
        NumberByNumber.put("Million", 1000000.0);
        NumberByNumber.put("Billion", 1000000000.0);
        NumberByNumber.put("Trillion", 1000000000000.0);
        NumberByNumber.put("thousand", 1000.0);
        NumberByNumber.put("million", 1000000.0);
        NumberByNumber.put("billion", 1000000000.0);
        NumberByNumber.put("trillion", 1000000000000.0);
    }
}