package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Main;
import sample.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
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
        //GO TO MANAGE PROJECT
        try {
            mainApp.showManageProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuCalendar() throws ParseException {
        //GO SEE MONTH CALENDAR
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
        //GO TO CHAT
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuDocument() throws NamingException {
        //GO SEE AND ADD DOCUMENTS
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuPlugin() throws NamingException {
        //GO TO DL PLUGIN
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAccount(){
        //GO TO ACCOUNT MANAGER
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