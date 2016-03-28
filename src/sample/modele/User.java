package sample.modele;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by 9403929M on 21/03/2016.
 */
public class User {

    private String  login;
    private String  firstName;
    private String  lastName;
    private String  mail;
    //for admin or simple user
    private String type;





    /**
     * Default constructor.
     */
    public User() {

    }


    public User(String login, String firstName, String lastName, String mail, String type){

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.type = type;

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
