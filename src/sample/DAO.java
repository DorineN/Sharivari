package sample;

import java.sql.Connection;

public abstract class DAO<T> {
    protected Connection connection = null;

    // Constructors
    public DAO(Connection connection){
        this.setConnection(connection);
    }

    // Getters and Setters
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}