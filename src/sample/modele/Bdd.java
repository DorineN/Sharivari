package sample.modele;

import javafx.scene.control.Alert;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



/**
 * Created by 9403929M on 23/03/2016.
 */
public class Bdd {

    private String serverBdd;
    private String identifiantBdd;
    private String passwordBdd;
    private String fileName;
    private Connection connection;

    public Bdd(){

        this.fileName = "properties\\bddConf.properties";
        //lecture du fichier texte
        Properties properties=new Properties();
        try {
            FileInputStream in =new FileInputStream(fileName);
            properties.load(in);
            in.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur connexion base de données !");
            alert.setHeaderText("Erreur : " + e.toString() + ". \nVeuillez contacter un administrateur.");
            alert.showAndWait();
        }

        //let's do the magic
        this.serverBdd= properties.getProperty("srv", "NULL");
        this.identifiantBdd = properties.getProperty("usr", "NULL");
        this.passwordBdd = properties.getProperty("password", "NULL");
    }

    public Connection getConnexion(){

        try {
            this.connection = DriverManager.getConnection(this.serverBdd, this.identifiantBdd, this.passwordBdd);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur connexion base de données !");
            alert.setHeaderText("Erreur : " + e.toString() + ". \nVeuillez contacter un administrateur.");
            alert.showAndWait();
        }


        return (connection);
    }



}
