import javafx.application.Application;
import javafx.stage.Stage;
import Service.Service;
import repo.Repo;
import UI.GUI;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            var r = new Repo("C:\\Users\\RADU\\IdeaProjects\\Hello\\src\\sud.txt");
            var s = new Service(r);
            var ui = new GUI(s, primaryStage);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
