package sample.controller;
import sample.Main;
import sample.modele.Bdd;
import sample.modele.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

/*************************************************************
 *************** Dialog to create a new user *****************
 *************************************************************
 *********** Created by Dorine on 28/03/2016.*****************
 ************************************************************/
public class Controller_Subscribe {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField name;
    @FXML
    private TextField firstname;
    @FXML
    private TextField username;
    @FXML
    private TextField enterprise;
    @FXML
    private TextField mail;
    @FXML
    private TextField phone;
    @FXML
    private TextField password;



    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
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
            String varFirstname = firstname.getText();
            String varName = name.getText();
            String varUsername = username.getText();
            double varPhone = Integer.parseInt(phone.getText());
            String varEnterprise = enterprise.getText();
            String varMail = mail.getText();
            String varPassword = password.getText();

            Connection con;

            try {
                Bdd bdd = new Bdd();

                Connection connection = bdd.getConnexion();
                Statement myStmt = connection.createStatement();

                //SQL query to insert new user
                String sql = "INSERT INTO user (lastNameUser, firstNameUser, loginUser, mailUser, typeUser, pwdUser) VALUES ('"+varName+"', '"+varFirstname+"', '"+varUsername+"', '"+varMail+"','basic', '"+varPassword+"');";
                myStmt.executeUpdate(sql);

                //SQL query to display all users
                ResultSet myRs = myStmt.executeQuery("SELECT * from user");
                while(myRs.next()){
                    System.out.println(myRs.getString("lastNameUser") + " , " + myRs.getString("firstNameUser") + " , " + myRs.getString("loginUser")+ " , " + myRs.getString("mailUser")+ " , " + myRs.getString("pwdUser"));
                }
            } catch (SQLException e) {
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

        if (firstname.getText() == null || firstname.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }
        if (username.getText() == null || username.getText().length() == 0) {
            errorMessage += "No valid username!\n";
        }

        if (phone.getText() == null || phone.getText().length() == 0) {
            errorMessage += "No valid phone number!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(phone.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid phone number (must be an integer)!\n";
            }
        }

        if (enterprise.getText() == null || enterprise.getText().length() == 0) {
            errorMessage += "No valid enterprise's name!\n";
        }

        if (mail.getText() == null || mail.getText().length() == 0) {
            errorMessage += "No valid mail!\n";
        }

        if (password.getText() == null || password.getText().length() == 0) {
            errorMessage += "No valid password!\n";
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