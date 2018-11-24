package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {

    SearchEngine searchEngine;

    {
        try {
            searchEngine = new SearchEngine("/Users/eranedri/IdeaProjects/SearchEngine/MINIcorpus","/Users/eranedri/IdeaProjects/SearchEngine/stop_words.txt",false);
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


    public static void main(String[] args) {
        //launch(args);
//        ReadFile rd = new ReadFile();
//        try
//        {
//            HashMap<String,DocDetailes> yarden = rd.ReadAllDocs("C:\\Git\\SearchEngine\\corpus");
//            Parse parse = new Parse();
//            parse.ParseCorpus(new ArrayList<DocDetailes>(yarden.values()), false, null);
//        }
//        catch (IOException e)
//        {
//            System.out.print("kine");
//        }
    }
}

//        for yuval computer only!!



//      "/Users/eranedri/IdeaProjects/SearchEngine/corpus"
//    "/Users/eranedri/IdeaProjects/SearchEngine/MINIcorpus"

/// to do : 1.replace all strings to stringbuilder
//          2. to check that docs,dictionary,posting,cache create only once
//          3. to keep encapsulation on every field