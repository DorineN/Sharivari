package sample.controller;

// Imports appli
import javafx.scene.input.KeyCode;
import sample.Main;
import sample.MySQLConnexion;

// Imports of javafx
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.User;

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
        String user = login.getText();
        String password = pwd.getText();

        if ("".equals(user) || "".equals(password)){
            this.msgError.setText("Veuillez saisir un identifiant et un mot de passe !");
        }else {

            Connection connection = null;

            try {
                connection = new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion();
            }catch(ClassNotFoundException e){
                this.msgError.setText("An error occured...");
            }catch(SQLException e){
                this.msgError.setText("Un problème est survenu lors de la connexion à la base de données...");
            }

            try {
                if(connection != null) {
                    Statement statement = connection.createStatement();

                    ResultSet resultat = statement.executeQuery("SELECT COUNT(idUser) as rsCount, idUser, lastNameUser, firstNameUser , mailUser, phoneUser, companyUser, typeUser " +
                            "FROM user WHERE loginUser ='" + user + "' AND pwdUser = '" + password + "'");

                    if (resultat.next()) {
                        //No user
                        if (resultat.getInt("rsCount") == 0) {
                            this.msgError.setText("Combinaison identifiant/mot de passe incorrecte");
                        }
                        //Connection
                        else {
                            id = resultat.getInt("idUser");
                            firstName = resultat.getString("firstNameUser");
                            lastName = resultat.getString("lastNameUser");
                            mail = resultat.getString("mailUser");
                            company = resultat.getString("companyUser");
                            type = resultat.getString("typeUser");
                            phone = resultat.getInt("phoneUser");

                            //create object User
                            Main.setMyUser(new User(id, user, firstName, lastName, mail, phone, company, type));

                            //GO HOME
                            mainApp.showMyAccount();
                        }
                    }

                    resultat.close();
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Code erreur 1 : " + e);
            }
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
