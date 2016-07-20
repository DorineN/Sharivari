package app.util;

import app.model.User;

public interface UserDAOInterface {
    void insert(String login, String pwd, String lastName, String firstName, String mail, int phone, String company);
    User findConnection(String login, String pwd);
    boolean update(User user, String pwd);
}
