/*
 * C195 - Scheduling System
 * By Rebecca Birmingham
 * March 2018
 * Assumptions and Notes: u/p removed for security reasons,   
 * Locale is only needed for login screen, is set back after successfull login
 */
package c195_schedulingsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rbirmi001c
 */
public class C195_SchedulingSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Scheduling System - Login Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
