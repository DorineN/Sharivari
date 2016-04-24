package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;

import java.sql.*;
import java.util.*;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new project *****************
 *************************************************************
 *********** Created by Dorine on 23/04/2016.*****************
 ************************************************************/
public class Controller_Project {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField nameProject;
    @FXML
    private TextField descriptionProject;
    @FXML
    private Date startDateProject;
    @FXML
    private Date realEndDaTeEndProject;
    @FXML
    private Date estimateEndDateProject;

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
        if (isInputValid()) {
            String varName = nameProject.getText();
            String varDesc = descriptionProject.getText();
            int varStart = startDateProject.getDate();
            int varDeadline = realEndDaTeEndProject.getDate();
            int varEnd = estimateEndDateProject.getDate();

            Connection connection = null;
            Statement myStmt;
            ResultSet myRs;


            try {
                connection = new MySQLConnexion("localhost", "root", "sharin").getConnexion();
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }catch(SQLException e){
                e.printStackTrace();
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
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("YEAH FIRST STEP");

            //TODO LINK TO THE APP
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
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
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}