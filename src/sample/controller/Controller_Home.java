package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.Main;

import javax.naming.NamingException;

//import javafx
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.modele.User;

/**
 * Created by 9403929M on 23/03/2016.
 */
public class Controller_Home {



    //Attributes
    private Main mainApp;

    // /Link FXML
    @FXML
    private Button testBtn;


    @FXML
    private void testClic(){
        System.out.println(mainApp.myUser.toString());
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    //Attributes

}
