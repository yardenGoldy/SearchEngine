import javafx.util.Pair;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Parse {
    private ArrayList<IToken> departments;

    public Parse() {
        departments = new ArrayList<IToken>();
        departments.add(new RangeToken());
        departments.add(new PercentageToken());
        departments.add(new DateToken());
        departments.add(new PriceToken());
        departments.add(new NumberToken());
    }

    public ArrayList<DocDetailes> ParseCorpus(ArrayList<DocDetailes> Docs, Boolean isStemmer, ArrayList<String> stopWords) {
        ArrayList<DocDetailes> result = new ArrayList<>();
        for (int i = 0; i < Docs.size();i++)
        {
            String currentText = Docs.get(i).DocText;
            // Todo handle /n /t
            List<String> textSplitted = Arrays.asList(currentText.split("\\s+"));
            StringBuilder resultText = new StringBuilder();

            // todo hanle out of range exception
            int j = 0;
            int textLength = textSplitted.size();
            while(j < textLength)
            {
                List<String> sentenceToCheck = textSplitted.subList(j, j+6 < textLength ? j+6 : textLength);
                ParsedResult parsedResult = this.GetSuitableDepartment(sentenceToCheck);
                if(parsedResult == null){
                    resultText.append(textSplitted.get(j));
                    j++;
                }
                else
                {
                    resultText = resultText.append(parsedResult.ParsedSentence);
                    j += parsedResult.Index;
                }
                resultText.append(" ");
            }

            resultText.deleteCharAt(resultText.length() - 1);

            DocDetailes parsedDoc = Docs.get(i);
            parsedDoc.setDocText(resultText.toString());
            result.add(parsedDoc);
        }

        return result;
    }

    public ParsedResult GetSuitableDepartment(List<String> sentence)
    {
        for (int i = 0; i < departments.size();i++)
        {
            ParsedResult result = departments.get(i).TryParse(sentence);
            if(result != null && result.IsMatch){
                return result;
            }
        }

        return null;
    }

//    public void InitiateStopWords(){
//        File f = new File(this.StopWordsPath);
//        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
//            String line = bufferedReader.readLine();
//            while (line != null ){
//                StopWords.add(line);
//                line = bufferedReader.readLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
