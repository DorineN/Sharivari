package Sharivari;

/**
 * Created by Dorine on 21/02/2016.
 */
import java.io.InvalidObjectException;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.lang.String;

public class Subscribe {

    private Connection con;

    //Access to the database
    public void connect() throws Exception {

        if (con != null) return;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String connectionURL = "jdbc:mysql://localhost:3306/sharin?autoReconnect=true&useSSL=false";

            con = DriverManager.getConnection(connectionURL, "root", "sharin");

            Statement myStmt = con.createStatement();

            //Name
            System.out.println("Saisissez votre nom :");
            Scanner sc1 = new Scanner(System.in);
            String name = sc1.nextLine();
            //Firstname
            System.out.println("Saisissez votre prénom :");
            Scanner sc2 = new Scanner(System.in);
            String firstname = sc2.nextLine();
            //Mail
            System.out.println("Saisissez un mail valide :");
            Scanner sc3 = new Scanner(System.in);
            String mail = sc3.nextLine();
            //Pasword
            System.out.println("Saisissez un mot de passe :");
            Scanner sc4 = new Scanner(System.in);
            String pwd = sc4.nextLine();

            //SQL query to insert new user
            String sql = "INSERT INTO users (name, firstname, email, password) VALUES ('"+name+"', '"+firstname+"', '"+mail+"', '"+pwd+"');";
            myStmt.executeUpdate(sql);

            //SQL query to display all users
            ResultSet myRs = myStmt.executeQuery("SELECT * from users");
            while(myRs.next()){
                System.out.println(myRs.getString("name") + " , " + myRs.getString("firstname") + " , " + myRs.getString("email")+ " , " + myRs.getString("password"));
            }
        } catch (ClassNotFoundException e) {
            throw new Exception("No database");
        }


    }

    //Close database access
    public void close() {
        if (con != null) {
            try {
                System.out.println("Connexion fermée");
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}