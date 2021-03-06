/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import modele.Case;
import modele.Plateau;
import modele.Status;

/**
 *
 * @author p1511086
 */
public class viewPlateau implements Observer {

    private Stage primaryStage;
    private Button[][] guiButtons;

    private final Paint background = RadialGradientBuilder.create()
            .stops(new Stop(0d, Color.TURQUOISE), new Stop(1, Color.web("3A5998")))
            .centerX(0.5d).centerY(0.5d).build();

    public viewPlateau(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.guiButtons = null;
    }

    @Override
    public void update(Observable o, Object arg) {
        int cptCaseNonVisible = 0;
        Plateau plateau = (Plateau) o;
        if (this.guiButtons == null) {
            initGrid(plateau.getNbLignes(), plateau.getNbColonnes(), plateau);
        } else {
            for (Map.Entry<String, Case> currentEntry : plateau.getListBoutons().entrySet()) {
                int[] coordonnees = plateau.getPosButton(currentEntry.getKey());
                int i = coordonnees[0], j = coordonnees[1];
                Case c = currentEntry.getValue();

                if (c.getStatus() != Status.VISIBLE) {
                    cptCaseNonVisible++;
                }
                if (c.getStatus() == Status.FLAG) {
                    String style ="-fx-graphic: url('src/drapeau.png');";   
                    this.guiButtons[i][j].setStyle(style);
                } else if (c.getStatus() == Status.HIDE) {
                    String style ="";                     
                    this.guiButtons[i][j].setStyle(style);
                } else if (c.getStatus() == Status.UNDEFINED) {
                    String style ="-fx-graphic: url('src/undefined.png');";   
                    this.guiButtons[i][j].setStyle(style);
                } else if (c.getStatus() == Status.VISIBLE) {

                    if (c.getIsAMine()) {
                        String style ="-fx-graphic: url('src/mine.png');";                     
                        this.guiButtons[i][j].setStyle(style);
                    } else if (c.getNbMinesAround() > 0) {
                        String style ="";
                        if(c.getNbMinesAround() == 1){
                            style = "-fx-color: blue";
                        }
                        else if(c.getNbMinesAround() == 2){
                            style = "-fx-color: green";
                        }
                        else if(c.getNbMinesAround() == 3){
                            style = "-fx-color: red";
                        }
                        else if(c.getNbMinesAround() == 4){
                            style = "-fx-color: orange";
                        }
                        else if(c.getNbMinesAround() == 5){
                            style = "-fx-color: pink";
                        }
                        else{
                            style = "-fx-color: black";
                        }
                        this.guiButtons[i][j].setStyle(style);
                        this.guiButtons[i][j].setText(Integer.toString(c.getNbMinesAround()));
                    } else {
                        String style ="";this.guiButtons[i][j].setStyle(style); 
                        this.guiButtons[i][j].setText("");
                    }
                    this.guiButtons[i][j].setDisable(true);
                }

            }
            if (cptCaseNonVisible == plateau.getNbMines()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Fin du jeu");
                alert.setHeaderText(null);
                alert.setContentText("Bravo, c'est gagné ! :)");

                alert.showAndWait();
            }
            if (plateau.isEnd()){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Fin du jeu");
                alert.setHeaderText(null);
                alert.setContentText("Hélas, c'est perdu ! :(");

                alert.showAndWait();
            }
        }

    }

    private void initGrid(int nbLignes, int nbColonnes, Plateau plateau) {
        GridPane plateauGui = new GridPane();
        plateauGui.setPadding(new Insets(5));
        plateauGui.setHgap(2);
        plateauGui.setVgap(2);
        plateauGui.setAlignment(Pos.CENTER);

        guiButtons = new Button[nbLignes][nbColonnes];
        for (Map.Entry<String, Case> currentEntry : plateau.getListBoutons().entrySet()) {
            Case c = currentEntry.getValue();
            int[] coordonnees = plateau.getPosButton(currentEntry.getKey());
            int i = coordonnees[0], j = coordonnees[1];

            guiButtons[i][j] = new Button("");
            guiButtons[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    Button currentBtn = (Button) e.getSource();
                    //click droit
                    if (e.getButton() == MouseButton.SECONDARY) {
                        plateau.rightClickOn(c);
                    } //click gauche
                    else {
                        plateau.leftClickOn(c);
                    }
                    e.consume();
                }
            });
            plateauGui.add(guiButtons[i][j], j, i);
            guiButtons[i][j].setPrefSize(30, 30);

        }

        Scene scene = new Scene(plateauGui, background);
        this.primaryStage.setTitle("Démineur !");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }
}
