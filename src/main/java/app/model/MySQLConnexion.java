package app.model;

import javafx.scene.control.Alert;
import app.model.DBConnexion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnexion extends DBConnexion {

    public MySQLConnexion() throws ClassNotFoundException, SQLException{
        // Driver test
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            System.out.println("Pas de driver");
            throw e;
        }
        String[] logBdd= new String[3];
        String fichier ="logBdd.txt";

        //lecture du fichier texte
        try{
            InputStream ips=new FileInputStream(fichier);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            int i = 0;
            while ((ligne=br.readLine())!=null && i<3){
                logBdd[i]=ligne;
                i++;
            }
            br.close();

            // Set info
            this.setUrl(logBdd[0]);
            this.setUser(logBdd[1]);
            this.setPwd(logBdd[2]);

            // Try to connect
            try{
                this.setConnexion(DriverManager.getConnection(this.url, this.user, this.pwd));
            }catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors de la connection à la base de données");
                alert.setHeaderText("Les informations de connexion de la base de données présentes dans le fichier \"logBdd.txt\" sont incorrectes.");
                alert.showAndWait();
            }
        }
        catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur lors de la connection à la base de données");
            alert.setHeaderText("Le fichier de configuration de la base de données \"logBdd.txt\" est introuvable.");
            alert.showAndWait();
        }
    }
}