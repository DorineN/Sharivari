package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new task *****************
 *************************************************************
 *********** Created by Dorine on 24/04/2016.*****************
 ************************************************************/

public class TaskController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField nameTask;
    @FXML
    private TextField descriptionTask;
    @FXML
    private Date estimateStartDateTask;
    @FXML
    private Date realStartDateTask;
    @FXML
    private Date estimateEndDateTask;
    @FXML
    private Date realEndDateTask;

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
            String varName = nameTask.getText();
            String varDesc = descriptionTask.getText();
            int varStart = estimateStartDateTask.getDate();
            int varRStart = realStartDateTask.getDate();
            int varDeadline = estimateEndDateTask.getDate();
            int varEnd = realEndDateTask.getDate();

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
                    String sql = "INSERT INTO task (nameTask, descriptionTask, estimateStartDateTask, realStartDateTask, estimateEndDateTask, realEndDateTask) VALUES ('" + varName + "', '" + varDesc + "', '" + varStart + "', '" + varRStart + "', '" + varDeadline + "', '" + varEnd + "');";
                    myStmt.executeUpdate(sql);

                    //SQL query to display all users
                    myRs = myStmt.executeQuery("SELECT * from task");
                    while (myRs.next()) {
                        System.out.println(myRs.getString("nameTask") + " , " + myRs.getString("descriptionTask") + " , " + myRs.getString("estimateStartDateTask") + " , " + myRs.getString("realStartDateTask"));
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

        if (nameTask.getText() == null || nameTask.getText().length() == 0) {
            errorMessage += "No valid task name !\n";
        }
        if (descriptionTask.getText() == null || descriptionTask.getText().length() == 0) {
            errorMessage += "You must describe your task !\n";
        }
        if (estimateStartDateTask.getTime() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (realStartDateTask.getTime() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEndDateTask.getTime() == 0 || realEndDateTask.getTime() < realStartDateTask.getTime()) {
            errorMessage += "No valid end date !\n";
        }

        if (realEndDateTask.getTime() == 0 || realEndDateTask.getTime() < realStartDateTask.getTime() ) {
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