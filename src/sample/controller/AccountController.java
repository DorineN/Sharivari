package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.Main;
import sample.MySQLConnexion;
import sample.User;
import sample.UserDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountController {

    private Main mainApp;

    @FXML
    private Label loginLabel;
    @FXML
    private Label pwdLabel;
    @FXML
    private Label confirmPwdLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label companyLabel;
    @FXML
    private  Label errorLabel;

    @FXML
    private TextField loginInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextArea firstNameInput;
    @FXML
    private TextArea emailInput;
    @FXML
    private TextArea phoneInput;
    @FXML
    private TextArea companyInput;

    @FXML
    private PasswordField pwdInput;
    @FXML
    private PasswordField  confirmPwdInput;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public AccountController(){
        // Empty constructor
    }

    @FXML
    public void initialize(){

    }

    @FXML
    public void handleSaveButton(){
        String login = this.loginInput.getText();
        String pwd = this.pwdInput.getText();
        String confirmPwd = this.confirmPwdInput.getText();
        String firstName = this.firstNameInput.getText();
        String name = this.nameInput.getText();
        String email = this.emailInput.getText();
        String company = this.companyInput.getText();
        int phone = Integer.parseInt(this.phoneInput.getText());

        if(checkInfo(login, pwd, confirmPwd, firstName, name, email, company, phone)){
            Main.getMyUser().setUserLogin(login);
            Main.getMyUser().setUserFirstName(firstName);
            Main.getMyUser().setUserName(name);
            Main.getMyUser().setUserMail(email);
            Main.getMyUser().setUserPhone(phone);
            Main.getMyUser().setUserCompany(company);

            try {
                UserDAO user = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                user.update(Main.getMyUser(), pwd);
            }catch(ClassNotFoundException | SQLException e) {
                System.out.println("Error controller !");
            }
        }else{
            errorLabel.setText("Un champ a été mal rempli...");
        }
    }

    public boolean checkInfo(String login, String pwd, String confirmPwd, String firstName, String name, String email, String company, int phone){
        if("".equals(login))
            return false;

        if("".equals(pwd))
            return false;

        if("".equals(confirmPwd))
            return false;
        else {
            if (!pwd.equals(confirmPwd))
                return false;
        }

        if("".equals(firstName))
            return false;

        if("".equals(name))
            return false;

        if("".equals(email))
            return false;

        if("".equals(company))
            return false;

        return !"".equals(Integer.toString(phone));
    }

    @FXML
    public void backHome(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Main getMainApp() {
        return mainApp;
    }

    public Label getLoginLabel() {
        return loginLabel;
    }

    public void setLoginLabel(Label loginLabel) {
        this.loginLabel = loginLabel;
    }

    public Label getPwdLabel() {
        return pwdLabel;
    }

    public void setPwdLabel(Label pwdLabel) {
        this.pwdLabel = pwdLabel;
    }

    public Label getConfirmPwdLabel() {
        return confirmPwdLabel;
    }

    public void setConfirmPwdLabel(Label confirmPwdLabel) {
        this.confirmPwdLabel = confirmPwdLabel;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public Label getFirstNameLabel() {
        return firstNameLabel;
    }

    public void setFirstNameLabel(Label firstNameLabel) {
        this.firstNameLabel = firstNameLabel;
    }

    public Label getEmailLabel() {
        return emailLabel;
    }

    public void setEmailLabel(Label emailLabel) {
        this.emailLabel = emailLabel;
    }

    public Label getPhoneLabel() {
        return phoneLabel;
    }

    public void setPhoneLabel(Label phoneLabel) {
        this.phoneLabel = phoneLabel;
    }

    public Label getCompanyLabel() {
        return companyLabel;
    }

    public void setCompanyLabel(Label companyLabel) {
        this.companyLabel = companyLabel;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    public TextField getLoginInput() {
        return loginInput;
    }

    public void setLoginInput(TextField loginInput) {
        this.loginInput = loginInput;
    }

    public TextField getNameInput() {
        return nameInput;
    }

    public void setNameInput(TextField nameInput) {
        this.nameInput = nameInput;
    }

    public TextArea getFirstNameInput() {
        return firstNameInput;
    }

    public void setFirstNameInput(TextArea firstNameInput) {
        this.firstNameInput = firstNameInput;
    }

    public TextArea getEmailInput() {
        return emailInput;
    }

    public void setEmailInput(TextArea emailInput) {
        this.emailInput = emailInput;
    }

    public TextArea getPhoneInput() {
        return phoneInput;
    }

    public void setPhoneInput(TextArea phoneInput) {
        this.phoneInput = phoneInput;
    }

    public TextArea getCompanyInput() {
        return companyInput;
    }

    public void setCompanyInput(TextArea companyInput) {
        this.companyInput = companyInput;
    }

    public PasswordField getPwdInput() {
        return pwdInput;
    }

    public void setPwdInput(PasswordField pwdInput) {
        this.pwdInput = pwdInput;
    }

    public PasswordField getConfirmPwdInput() {
        return confirmPwdInput;
    }

    public void setConfirmPwdInput(PasswordField confirmPwdInput) {
        this.confirmPwdInput = confirmPwdInput;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
