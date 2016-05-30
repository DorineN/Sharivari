package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.*;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class ProjectController {
    //Attributes
    private Main mainApp;
    private UserDAO user = null;

    @FXML
    private Label projectName;

    /** Menu links */
    @FXML
    public void handleMenuHome() throws ParseException {
        //GO HOME
        try {
            mainApp.showHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuProject() throws ParseException {
        //GO PROJECT CREATION
        try {
            mainApp.showManageProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuCalendar() throws ParseException {
        //GO PROJECT CREATION
        try {
            mainApp.showCreateProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTask() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTchat() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuDocument() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuPlugin() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAccount(){
        try{
            mainApp.showMyAccount();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        projectName.setText(mainApp.getMyProject().getProjectName());
        this.mainApp = mainApp;

    }


}