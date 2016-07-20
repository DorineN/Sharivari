package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import app.Main;
import app.model.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
import java.text.ParseException;

/*************************************************************
 *************** Dialog to create a new project *****************
 *************************************************************
 *********** Created by Dorine on 23/04/2016.*****************
 ************************************************************/
public class ProjectController {

    //Attributes
    private Main mainApp;
    private UserDAO user = null;

    @FXML
    private Label projectName;
    @FXML
    private TextField name;
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
            mainApp.showCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTask() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTchat() throws NamingException {
        //GO TO CHAT
       /* try {
           // mainApp.showTchat();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
            mainApp.showPlugin();
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

    @FXML
    public void showGantt(){
        //GO TO GANTT VIEW
        try{
            mainApp.showGantt();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

    }


}