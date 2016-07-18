package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import sample.Main;
import sample.model.PluginsLoader;

import javax.naming.NamingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Loïc on 29/05/2016.
 */
public class PluginController {

    // Attributes
    private Main mainApp;

    private File fileSource;
    private File dest = new File("plugin/");
    private FileChooser fileChooser = new FileChooser();


    private PluginsLoader pluginsLoader;

    @FXML
    private Label labelMsg;
    @FXML
    private Label labelPath;
    @FXML
    private GridPane gridPane;

    public PluginController() {
        // Empty constructor
    }

    public void initialize() throws Exception {
        this.loadPlugList();

    }

    @FXML
    private void handleBtnFile() throws NamingException {
        this.fileSource = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if(this.fileSource!=null){
            String filename = this.fileSource.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            if(extension.equals("jar")){
                labelMsg.setText("");
                labelPath.setText(this.fileSource.getAbsolutePath());
            }else{
                this.fileSource=null;
                labelPath.setText("");
                this.labelMsg.setText("Veuillez sélectionner un fichier .jar");
            }
        }
    }

    @FXML
    private void handleBtnValider() throws NamingException {
        if(this.fileSource!=null){
            try {
                Files.copy(this.fileSource.toPath(), new File (this.dest, this.fileSource.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                labelPath.setText("");
                labelMsg.setText("Plugin ajouté ! Veuillez redémarrer l'application pour la prise en charge du nouveau plugin.");
            } catch (IOException e) {
                labelPath.setText("");
                labelMsg.setText("Erreur lors de la copie du fichier.");
            }

        }else{
            labelPath.setText("");
            labelMsg.setText("Veuillez sélectionner un fichier .jar");
        }
    }

    private void loadPlugList() throws Exception {

        int j = 0;

        Pane[] tPane = new Pane[mainApp.pluginsLoader.files.length];
        Button[] tButton  = new Button[mainApp.pluginsLoader.files.length];
        for (int i = 0 ; i<mainApp.pluginsLoader.files.length; i++){
            tPane[i] = new Pane();
            tButton[i] = new Button("Supprimer");

            tPane[i].getChildren().add(new Label(mainApp.pluginsLoader.files[i]));
            tPane[i].setStyle("-fx-border-color: #e1e5cd; -fx-background-color : white; -fx-padding : 10px; -fx-width:100%;");
            gridPane.add(tPane[i], j, i);
            gridPane.add(tButton[i], j+1, i);

            int finalI = i;

            tButton[i].setOnMouseClicked(event-> deleteMyPlug(event,mainApp.pluginsLoader.files[finalI]));


        }


    }

    public void deleteMyPlug(Event event, String files){
        File myFile = new File("plugin\\"+files);
        myFile.delete();
        try {
            mainApp.pluginsLoader = new PluginsLoader();
            mainApp.pluginsLoader.loadAllStringPlugins();

        } catch (Exception e) {
            e.printStackTrace();
        }
        labelMsg.setText("Plugin supprimé ! Veuillez redémarrer l'application pour la prise en compte de sa suppression.");

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }



}
