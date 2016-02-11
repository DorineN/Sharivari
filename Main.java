package Sharivari;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Subscribe db = new Subscribe();
        try {
            db.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }
}