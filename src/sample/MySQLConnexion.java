package sample;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnexion extends DBConnexion {

    public MySQLConnexion(String url, String user, String pwd) throws ClassNotFoundException, SQLException{
        // Driver test
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e) {
            System.out.println("Pas de driver");
            throw e;
        }

        // Set info
        this.setUrl(url);
        this.setUser(user);
        this.setPwd(pwd);

        // Try to connect
        try{
            this.setConnexion(DriverManager.getConnection(this.url, this.user, this.pwd));
        }catch(SQLException e){
            throw e;
        }
    }
}
