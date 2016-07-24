package app.controller;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import app.Main;
import app.util.Fields;

import static java.lang.Integer.parseInt;


public class AccountController {

    private Main mainApp;


    @FXML
    private TextField loginInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField mailInput;
    @FXML
    private TextField phoneInput;
    @FXML
    private TextField companyInput;

    @FXML
    private Label msgConfirm;

    @FXML
    private PasswordField pwdInput;
    @FXML
    private PasswordField  confirmPwdInput;


    public AccountController(){
        // Empty constructor
    }

    @FXML
    public void initialize(){

    }

    @FXML
    public void updateMyAccount(){



        String errorMessage="";

        if (firstNameInput.getText() == null || firstNameInput.getText().length() == 0) {
            errorMessage += "Champ Prénom non valide !\n";
        }
        if (nameInput.getText() == null || nameInput.getText().length() == 0) {
            errorMessage += "Champ nom non valide !\n";
        }
        if (mailInput.getText() == null || mailInput.getText().length() == 0) {
            errorMessage += "Champ Mail non valide !\n";
        }else if(!Fields.verifyMail(mailInput.getText())){
            errorMessage += "Champ Mail non valide !\n";
        }


        if (companyInput.getText() == null || companyInput.getText().length() == 0) {
            errorMessage += "Champ Entreprise non valide !\n";
        }
        if (phoneInput.getText() == null || phoneInput.getText().length() == 0) {
            errorMessage += "Champ Numéro de téléphone non valide !\n";
        } else {
            // try to parse the postal code into an int.
            try {
                parseInt(phoneInput.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Champ Numéro de téléphone non valide (doit être sous la forme 0123456789 )!\n";
            }
        }
        if (pwdInput.getText() != null || pwdInput.getText().length() < 0) {
            if(!pwdInput.getText().equals(confirmPwdInput.getText())){
                errorMessage += "Les deux mots de passe ne correspondent pas !\n";
            }
        }

        if (errorMessage.length() == 0) {
            String pwd = this.pwdInput.getText();
            String firstName = this.firstNameInput.getText();
            String name = this.nameInput.getText();
            String email = this.mailInput.getText();
            String company = this.companyInput.getText();
            int phone = Integer.parseInt(this.phoneInput.getText());

            //update actual user
            Main.getMyUser().setUserFirstName(firstName);
            Main.getMyUser().setUserName(name);
            Main.getMyUser().setUserMail(email);
            Main.getMyUser().setUserCompany(company);
            Main.getMyUser().setUserPhone(phone);

            //insert in bdd
            mainApp.userDao.updateUser(Main.getMyUser());
            if(!pwd.equals("")){
                mainApp.userDao.updatePwd(pwd, mainApp.getMyUser().getUserId());
            }

            msgConfirm.setText("Modification enregistrées !");

            updateInput();
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Modification de compte non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
            alert.setContentText(errorMessage);

            alert.showAndWait();


        }


    }


    public void updateInput(){

        loginInput.setText(mainApp.getMyUser().getUserLogin());

        nameInput.setText(mainApp.getMyUser().getUserName());

        firstNameInput.setText(mainApp.getMyUser().getUserFirstName());

        mailInput.setText(mainApp.getMyUser().getUserMail());

        phoneInput.setText(String.valueOf(mainApp.getMyUser().getUserPhone()));

        companyInput.setText(mainApp.getMyUser().getUserCompany());

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        updateInput();
    }
}
