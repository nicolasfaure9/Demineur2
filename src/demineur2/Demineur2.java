/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demineur2;

import Vue.viewPlateau;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modele.Plateau;

/**
 *
 * @author p1511086
 */
public class Demineur2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        viewPlateau vplateau = new viewPlateau(primaryStage);
        Plateau plateau = new Plateau(10,10,10);
        plateau.addObserver(vplateau);
        plateau.initPlateau();    
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
