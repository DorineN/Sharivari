package sample;

import java.sql.Connection;

public abstract class DAO<T> {
    protected Connection connection = null;

    // Constructors
    public DAO(Connection p_connection){
        this.setConnection(p_connection);
    }


    // Abstracts Methods
    public abstract T find(int p_id);

    //public abstract boolean create(T p_obj);

    public abstract boolean update(T p_obj);

    //public abstract boolean delete(T p_obj);


    // Getters and Setters
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}