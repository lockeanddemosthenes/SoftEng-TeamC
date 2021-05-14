package edu.wpi.teamC;

import edu.wpi.teamC.views.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
public class Capp extends Application {

    private static Stage primaryStage;
    //TODO: make this work with guest/employee/admin default screens
    public static String prevPage = "/edu/wpi/teamC/fxml/Login.fxml";
    public static String userType = "Guest";

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @Override
    public void init() throws IOException {
        String[] str = new String[1];
        Translate.main(str);
        System.out.println("Starting Up");
        SetupDB.main(str);
    }

    @Override
    public void start(Stage primaryStage) {
        Capp.primaryStage = primaryStage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamC/fxml/StartUp.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static Stage getPrimaryStage(){
        return primaryStage;
    }


    @Override
    public void stop() {
        System.out.println("Shutting Down");
    }
}
