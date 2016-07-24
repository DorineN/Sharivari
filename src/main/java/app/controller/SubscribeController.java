package app.controller;
import app.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import app.model.User;
import app.util.Fields;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public void handleOk() throws NoSuchAlgorithmException {
        if (isInputValid()) {
            String varFirstname = firstname.getText();
            String varName = name.getText();
            String varUsername = username.getText();
            int varPhone =parseInt(phone.getText());
            String varEnterprise = company.getText();
            String varMail = mail.getText();
            String varPassword = password.getText();

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(varPassword.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }





            mainApp.userDao.insert(varUsername,sb.toString(),varName, varFirstname, varMail, varPhone, varEnterprise);


            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Inscription validée !");
            alert.setHeaderText("Votre inscription a bien été prise en compte.");
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
            errorMessage += "Champ Prénom non valide !\n";
        }
        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Champ nom non valide !\n";
        }
        if (username.getText() == null || username.getText().length() == 0) {
            errorMessage += "Champ Identifiant non valide !\n";
        } else if(Fields.verifyLogin(username.getText())==false){
            errorMessage += "Cet identifiant est non valide (chiffres et lettres uniquement, 3 à 16 caractères)!\n";
        }else{
            //check if login is already taken
            int idUser = mainApp.userDao.find(username.getText());
            if (idUser != 0) {
                errorMessage += "Cet identifiant est déjà utilisé !\n";
            }
        }
        if (phone.getText() == null || phone.getText().length() == 0) {
            errorMessage += "Champ Numéro de téléphone non valide !\n";
        } else {
            // try to parse the postal code into an int.
            try {
                parseInt(phone.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Champ Numéro de téléphone non valide (doit être sous la forme 0123456789 )!\n";
            }
        }

        if (company.getText() == null || company.getText().length() == 0) {
            errorMessage += "Champ Entreprise non valide !\n";
        }

        if (mail.getText() == null || mail.getText().length() == 0) {
            errorMessage += "Champ Mail non valide !\n";
        }else if(!Fields.verifyMail(mail.getText())){
            errorMessage += "Champ Mail non valide !\n";
        }

        if (password.getText() == null || password.getText().length() == 0) {
            errorMessage += "Champ Mot de passe non valide!\n";

        }if(!password.getText().equals(confirmPassword.getText())){
            errorMessage += "Les deux mots de passe ne correspondent pas !\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Formulaire d'inscription non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
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