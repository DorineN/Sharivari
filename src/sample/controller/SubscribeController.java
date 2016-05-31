package sample.controller;
import sample.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.MySQLConnexion;
import sample.UserDAO;
import java.io.IOException;
import java.sql.*;

import static java.lang.Integer.parseInt;

/*************************************************************
 *************** Dialog to create a new user *****************
 *************************************************************
 *********** Created by Dorine on 28/03/2016.*****************
 ************************************************************/
public class SubscribeController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField name;
    @FXML
    private TextField firstname;
    @FXML
    private TextField username;
    @FXML
    private TextField company;
    @FXML
    private TextField mail;
    @FXML
    private TextField phone;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;
    @FXML


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
            String varFirstname = firstname.getText();
            String varName = name.getText();
            String varUsername = username.getText();
            int varPhone =parseInt(phone.getText());
            String varEnterprise = company.getText();
            String varMail = mail.getText();
            String varPassword = password.getText();


            try {
                UserDAO user = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                user.insert(varUsername,varPassword,varName, varFirstname, varMail, varPhone, varEnterprise);
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
                parseInt(phone.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid phone number (must be an integer)!\n";
            }
        }

        if (company.getText() == null || company.getText().length() == 0) {
            errorMessage += "No valid enterprise's name!\n";
        }

        if (mail.getText() == null || mail.getText().length() == 0) {
            errorMessage += "No valid mail!\n";
        }

        if (password.getText() == null || password.getText().length() == 0) {
            errorMessage += "No valid password!\n";

        }if(!password.getText().equals(confirmPassword.getText())){
            errorMessage += "Passwords aren't the same!\n";
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

    @FXML
    private void handleBtnBack() {
        try {
            mainApp.showConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Is called by the main application to give a reference back to itself.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}