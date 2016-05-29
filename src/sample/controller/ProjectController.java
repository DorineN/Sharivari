package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
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

    @FXML
    private TextArea nameProject;
    @FXML
    private TextArea descriptionProject;
    @FXML
    private DatePicker startDateProject;
    @FXML
    private DatePicker realEndDaTeEndProject;
    @FXML
    private DatePicker estimateEndDateProject;

    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Empty
    }

    /** Sets the stage of this dialog.*/
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /** Returns true if the user clicked OK, false otherwise. **/
    public boolean isOkClicked() {
        return okClicked;
    }

    /** Called when the user clicks on the button New User*/
    @FXML
    public void handleOk() {

        //if (isInputValid()) {
            String varName = this.nameProject.getText();
            String varDesc = descriptionProject.getText();
            Calendar cal = Calendar.getInstance();
            //cal.setTime(startDateProject);
            int varStart = cal.get(Calendar.DATE);
            //cal.setTime(realEndDaTeEndProject);
            int varDeadline = cal.get(Calendar.DATE);
            //cal.setTime(estimateEndDateProject);
            int varEnd = cal.get(Calendar.DATE);

            Connection connection = null;
            Statement myStmt;
            ResultSet myRs;


            try {
                connection = new MySQLConnexion("localhost", "root", "sharin").getConnexion();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
                System.out.print("Step 1");
            }catch(SQLException e){
                e.printStackTrace();
                System.out.print("Step 2");
            }

            try {
                if(connection != null) {
                    myStmt = connection.createStatement();

                    //SQL query to insert new user
                    String sql = "INSERT INTO project (nameProject, descriptionProject, startDateProject, estimateEndDateProject) VALUES ('" + varName + "', '" + varDesc + "', '" + varStart + "', '" + varDeadline + "');";
                    myStmt.executeUpdate(sql);

                    //SQL query to display all users
                    myRs = myStmt.executeQuery("SELECT * from project");
                    while (myRs.next()) {
                        System.out.println(myRs.getString("nameProject") + " , " + myRs.getString("descriptionProject") + " , " + myRs.getString("descriptionProject") + " , " + myRs.getString("realEndDaTeEndProject"));
                    }

                    myRs.close();
                    myStmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("Step 3");
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("YEAH FIRST STEP");

            //TODO LINK TO THE APP
        }

    @FXML
    public void backHome(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //}

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    /*private boolean isInputValid() {
        String errorMessage = "";

        if (nameProject.getText() == null || nameProject.getText().length() == 0) {
            errorMessage += "No valid project name !\n";
        }
        if (descriptionProject.getText() == null || descriptionProject.getText().length() == 0) {
            errorMessage += "You must describe your project !\n";
        }
        if (startDateProject.getTime() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEndDateProject.getTime() == 0 || estimateEndDateProject.getTime() < startDateProject.getTime() ) {
            errorMessage += "No valid end date !\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }*/

    @FXML
    public void showAccount(){
        try{
            mainApp.showMyAccount();
        }catch(IOException e){
            e.printStackTrace();
        }
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
    @FXML
    private void handleMenuCalendar() throws NamingException {
        //GO HOME
        try {
            mainApp.showCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}