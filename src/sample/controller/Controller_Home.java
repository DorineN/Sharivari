package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

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

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
