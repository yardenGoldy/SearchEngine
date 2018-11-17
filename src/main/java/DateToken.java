import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class DateToken implements IToken {
    private HashMap<String, String> datesByNumber;

    public DateToken(){
        datesByNumber = new HashMap<>();
        this.InitiateDates();
    }

    @Override
    public ParsedResult TryParse(List<String> sentence) {
        if(sentence.size() < 2)
        {
            return null;
        }

        String first = sentence.get(0);
        String second = sentence.get(1);
        StringBuilder parsedSentence = new StringBuilder();
        Boolean isMatch = false;
        if (isNumeric(first) && datesByNumber.containsKey(second) ){
            int numeric = Integer.parseInt(first);
            if(numeric < 32 && numeric > 0)
            {
                parsedSentence.append(String.format("%s-%d", datesByNumber.get(second), numeric));
                isMatch = true;
            }
        }
        else if (datesByNumber.containsKey(first) && isNumeric(second)){
            int numeric = Integer.parseInt(second);
            if(numeric < 32 && numeric > 0)
            {
                parsedSentence.append(String.format("%s-%d", datesByNumber.get(first), numeric));
                isMatch = true;
            }
            else if(second.length() == 4)
            {
                parsedSentence.append(String.format("%s-%s", second, datesByNumber.get(first)));
                isMatch = true;
            }
        }

        return new ParsedResult(isMatch, parsedSentence, 2);
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("\\d+");
    }

    private void InitiateDates(){
        datesByNumber.put("JAN", "01"); datesByNumber.put("FEB", "02"); datesByNumber.put("MAR", "03"); datesByNumber.put("APR", "04");
        datesByNumber.put("MAY", "05"); datesByNumber.put("JUN", "06"); datesByNumber.put("JUL", "07"); datesByNumber.put("AUG", "08");
        datesByNumber.put("SEP", "09"); datesByNumber.put("OCT", "10"); datesByNumber.put("NOV", "11"); datesByNumber.put("DEC", "12");
        datesByNumber.put("Jan", "01"); datesByNumber.put("Feb", "01"); datesByNumber.put("Mar", "03"); datesByNumber.put("Apr", "04");
        datesByNumber.put("May", "05"); datesByNumber.put("Jun", "06"); datesByNumber.put("Jul", "07"); datesByNumber.put("Aug", "08");
        datesByNumber.put("Sep", "09"); datesByNumber.put("Oct", "10"); datesByNumber.put("Nov", "11"); datesByNumber.put("Dec", "12");
        datesByNumber.put("January", "01"); datesByNumber.put("February", "02"); datesByNumber.put("March", "03"); datesByNumber.put("April", "04");
        datesByNumber.put("June", "06"); datesByNumber.put("July", "07"); datesByNumber.put("August", "08"); datesByNumber.put("September", "09");
        datesByNumber.put("October", "10"); datesByNumber.put("November", "11"); datesByNumber.put("December", "12");
        datesByNumber.put("JANUARY", "01"); datesByNumber.put("FEBUARY", "02"); datesByNumber.put("MARCH", "03"); datesByNumber.put("APRIL", "04");
        datesByNumber.put("JUNE", "06"); datesByNumber.put("JULY", "07"); datesByNumber.put("AUGUST", "08"); datesByNumber.put("SEPTEMBER", "09");
        datesByNumber.put("OCTOBER", "10"); datesByNumber.put("NOVEMBER", "11"); datesByNumber.put("DECEMBER", "12");
    }
}
