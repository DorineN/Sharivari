package app.controller;

// Imports appli
import javafx.scene.input.KeyCode;
import app.Main;
import app.model.MySQLConnexion;

// Imports of javafx
import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.model.User;
import app.model.UserDAOFactory;
import app.model.UserDAOInterface;

// Other imports
import javax.naming.NamingException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

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
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
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
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    private void handleBtnConnexion() throws NamingException, NoSuchAlgorithmException, SQLException, ClassNotFoundException {
        String loginContent = this.login.getText();
        String password = pwd.getText();

        if ("".equals(loginContent) || "".equals(password)){
            this.msgError.setText("Veuillez saisir un identifiant et un mot de passe !");
        }else {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            User user = mainApp.userDao.findConnection(loginContent, sb.toString());

            if (user != null){
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