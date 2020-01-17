/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rfrfkbstopwatch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rottm
 */
public class RfrfkbStopwatch extends Application {
    private String appName = "RfrfkbStopwatch";
    
    @Override
    public void start(Stage primaryStage) {
        AnalogStopwatch analog = new AnalogStopwatch();
        Scene scene = new Scene(analog.getRootContainer(), analog.getWidth(), analog.getHeight()+245);
        
        primaryStage.setTitle(appName);
        primaryStage.setScene(scene);
        primaryStage.show();
        
//        analog.setTickTimeInSeconds(1.0);
//        analog.start();
//        analog.setTickTimeInSeconds(1.0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
