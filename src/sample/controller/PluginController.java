package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import sample.Main;

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

    @FXML
    private Label labelMsg;
    @FXML
    private Label labelPath;

    public PluginController() {
        // Empty constructor
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
                labelMsg.setText("Plugin ajouté !");
            } catch (IOException e) {
                labelPath.setText("");
                labelMsg.setText("Erreur lors de la copie du fichier.");
            }

        }else{
            labelPath.setText("");
            labelMsg.setText("Veuillez sélectionner un fichier .jar");
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}