package sample.controller;
/**
 * Created by 9403929M on 21/03/2016.
 */

//import appli
import javafx.scene.input.KeyCode;
import sample.Main;
import sample.modele.Bdd;

//import javafx
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.modele.User;

//other import
import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Controller_Connexion {

    //Attributes
    private Main mainApp;

    String firstName;
    String lastName;
    String mail;
    String type;


    //Link FXML
    @FXML
    private TextField login;
    @FXML
    private PasswordField pwd;
    @FXML
    private Label msgError;




    public Controller_Connexion() {

    }

    @FXML
    //Methode to press ENTER
    private void handleEnterLoginPwd(){

        this.pwd.setOnKeyPressed((event) -> {
           if(event.getCode() == KeyCode.ENTER){
               try {
                   handleBtnConnexion();
               } catch (NamingException e) {
                   e.printStackTrace();
               }
           }
        });

       this.login.setOnKeyPressed((event2) -> {
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

        if (user.equals("") | password.equals("")){
            this.msgError.setText("Veuillez saisir un identifiant et un mot de passe !");
        }else {
            Bdd bdd = new Bdd();

                    try {
                        Connection connection = bdd.getConnexion();
                        Statement statement = connection.createStatement();

                        ResultSet resultat = statement.executeQuery("SELECT COUNT(idUser) as rsCount, lastNameUser, firstNameUser , mailUser, typeUser " +
                                "FROM user WHERE loginUser ='" + user + "' AND pwdUser = '" +password+"'");


                        while (resultat.next()) {


                            //No user
                            if (resultat.getInt("rsCount") == 0) {
                                this.msgError.setText("Combinaison identifiant/mot de passe incorrecte");
                            }
                                //Connexion
                                else {
                                firstName = resultat.getString("firstNameUser");
                                lastName = resultat.getString("lastNameUser");
                                mail = resultat.getString("mailUser");
                                type = resultat.getString("typeUser");

                                //create object User
                                mainApp.myUser = new User(user, firstName, lastName, mail, type);

                                //GO HOME
                                mainApp.showHome();

                            }
                        }




                    } catch (Exception e) {
                        System.out.println("Code erreur 1 : " + e);
                    }




        }
    }

    @FXML
    private void handleBtnInscription() throws NamingException {
        try {
            mainApp.showSubscribe();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}