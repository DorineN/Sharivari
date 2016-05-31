package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;
import sample.ProjectDAO;
import sample.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

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
            mainApp.showCreateTask();
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

    public void setMainApp(Main mainApp) {
        projectName.setText(mainApp.getMyProject().getProjectName());
        this.mainApp = mainApp;

    }


}