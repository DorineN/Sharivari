package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

import java.io.IOException;

public class Controller_Home {
    //Attributes
    private Main mainApp;

    // /Link FXML
    @FXML
    private Button testBtn;

    @FXML
    private void testClic(){
        System.out.println(mainApp.getMyUser().toString());
    }

    @FXML
    private void linkAccount() {
        try {
            mainApp.showMyAccount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
