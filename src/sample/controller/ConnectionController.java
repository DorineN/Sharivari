package sample.controller;

// Imports appli
import javafx.scene.input.KeyCode;
import sample.Main;
import sample.MySQLConnexion;

// Imports of javafx
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.User;
import sample.UserDAO;

// Other imports
import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionController {
    // Attributes
    private Main mainApp;

    int id;
    String firstName;
    String lastName;
    String mail;
    String type;
    int phone;
    String company;

    //Link FXML
    @FXML
    private TextField login;
    @FXML
    private PasswordField pwd;
    @FXML
    private Label msgError;
    @FXML
    private Hyperlink btnInscription;


    public ConnectionController() {
        // Empty constructor
    }

    @FXML
    //Methode to press ENTER
    public void handleEnterLoginPwd(){

        this.pwd.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                try {
                    handleBtnConnexion();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        });

        this.login.setOnKeyPressed(event2 -> {
            if(event2.getCode() == KeyCode.ENTER){
                try {
                    handleBtnConnexion();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    private void handleBtnConnexion() throws NamingException {
        String login = this.login.getText();
        String password = pwd.getText();

        if ("".equals(login) || "".equals(password)){
            this.msgError.setText("Veuillez saisir un identifiant et un mot de passe !");
        }else {

            try {
                UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                User user = userDao.findConnection(login,password);
                if (user.getUserLogin()!=""){
                    Main.setMyUser(user);
                    //GO HOME
                    try {
                        mainApp.showHome();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    this.msgError.setText("Erreur combinaison identifiant / mot de passe !");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

          /*  */

        }
    }

    @FXML
    public void handleBtnInscription() throws NamingException {
        try {
            mainApp.showSubscribe();
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
