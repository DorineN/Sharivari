package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;
import sample.ProjectDAO;

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

    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker estimateEnd;

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
    public void handleOk() throws ParseException {

        //if (isInputValid()) {
            String varName = name.getText();
            String varDesc = description.getText();

            /**Retrieve values of datepickers **/
            LocalDate date1 = start.getValue();
            LocalDate date2 = estimateEnd.getValue();

            /**Transform date to a specific format**/
            Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
            Date varStart1 = Date.from(instant);
            Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
            Date varEnd1 = Date.from(instant2);

            /**Specific transformed date to string to retrieve it in sql format **/
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String varStart = sdf.format(varStart1);

            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String varEnd = sdf2.format(varEnd1);


            try {
                ProjectDAO user = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
                project.insert(varName, varDesc, varStart, varEnd);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Inscription validée !");
            alert.showAndWait();

            //GO CONNECTION
            try {
                mainApp.showConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*Connection connection = null;
            Statement myStmt;
            ResultSet myRs;


            try {
                connection = new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion();
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
                    String sql = "INSERT INTO project (nameProject, descriptionProjet, startDateProject, estimateEndDateProject) VALUES ('" + varName + "', '" + varDesc + "', '" + varStart + "', '" + varEnd + "');";

                    myStmt.executeUpdate(sql);

                    //SQL query to display all users
                    myRs = myStmt.executeQuery("SELECT * from project");
                    while (myRs.next()) {
                        System.out.println(myRs.getString("nameProject") + " , " + myRs.getString("descriptionProjet") + " , " + myRs.getDate("startDateProject"));
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

            */
        }
    //}

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    /*private boolean isInputValid() {
        String errorMessage = "";

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "No valid project name !\n";
        }
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "You must describe your project !\n";
        }
        if (start.getValue() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEnd.getValue() == 0 || estimateEnd.getValue() < start.getValue() ) {
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


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}