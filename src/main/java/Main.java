import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    SearchEngine searchEngine;

    {
        try {
            searchEngine = new SearchEngine("/Users/eranedri/IdeaProjects/SearchEngine-Part1/Eran_Corpuse","/Users/eranedri/IdeaProjects/SearchEngine-Part1/stop_words.txt",false);
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
        launch(args);
    }
}






//"/Users/eranedri/IdeaProjects/SearchEngine-Part1/corpus"
//"/Users/eranedri/IdeaProjects/SearchEngine-Part1/Eran_Corpuse"