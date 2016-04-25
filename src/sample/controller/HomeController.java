package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

public class HomeController {
    //Attributes
    private Main mainApp;

    // /Link FXML
    @FXML
    private Button testBtn;

    @FXML
    public void testClic(){
        System.out.println(Main.getMyUser().toString());
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
