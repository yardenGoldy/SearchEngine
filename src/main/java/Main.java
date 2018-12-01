import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main extends Application {

    SearchEngine searchEngine;
    {
        try {
            searchEngine = new SearchEngine("/Users/eranedri/IdeaProjects/SearchEngine/corpus","/Users/eranedri/IdeaProjects/SearchEngine/stop_words.txt",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }

    public static HashSet<String> InitiateStopWords(){
        HashSet<String> stopWords = new HashSet<String>();
        File f = new File("./stop_words.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f))){
            String line = bufferedReader.readLine();
            while (line != null ){
                stopWords.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    public static void main(String[] args) {
        //launch(args);
        Boolean isStemmer = true;
        ReadFile rd = new ReadFile();
        try
        {
            HashMap<String,DocDetailes> corpus = rd.ReadAllDocs("C:\\Git\\SearchEngine\\corpus");
            Parse parse = new Parse(InitiateStopWords(), isStemmer);
            parse.ParseCorpus(new ArrayList<DocDetailes>(corpus.values()));
        }
        catch (IOException e)
        {
            System.out.print("kine");
        }
    }
}

// todo - replace all strings to stringbuilder
//todo - to check that docs,dictionary,posting,cache create only once
//todo - to keep encapsulation on every field