package sample;

public interface UserDAOInterface {
    void insert(String login, String pwd, String lastName, String firstName, String mail, int phone, String company);
    User findConnection(String login, String pwd);
    public boolean update(User user, String pwd);
}
