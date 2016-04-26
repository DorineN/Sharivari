package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;
import sample.MySQLConnexion;
import sample.User;
import sample.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

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

    @FXML
    private void handleMenuProject() throws NamingException {
        //GO HOME
        try {
            mainApp.showProject();
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
