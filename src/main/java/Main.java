//   "/Users/eranedri/IdeaProjects/SearchEngine - eran/corpus"
//    "/Users/eranedri/IdeaProjects/SearchEngine - eran/MINIcorpus"
//   "/Users/eranedri/IdeaProjects/SearchEngine - eran/OneFoldercorpus"

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }

// eran path - /Users/eranedri/Documents/GitHub/SearchEngine/Postingcorpus
// eran path for stop word is /stop_words - without a dot!

    public static void main(String[] args) throws IOException {
        //launch(args);
        SearchEngine searchEngine;
        searchEngine = new SearchEngine( "/Users/eranedri/Documents/GitHub/SearchEngine/Completecorpus","/Users/eranedri/Documents/GitHub/SearchEngine/Completecorpus",false);
        System.exit(0);
    }
}



// todo - replace all strings to stringbuilder
//todo - to check that docs,dictionary,posting,cache create only once
//todo - to keep encapsulation on every field

