package app.model;


import java.util.ArrayList;

public interface UserDAOInterface {
    void insert(String login, String pwd, String lastName, String firstName, String mail, int phone, String company);
    User findConnection(String login, String pwd);
    boolean update(User user, String pwd);
    void deleteParticipate(int id);
    void deleteExecute(int id);
    void resetPassword(int id);
    void deleteAccount(int id);
    ArrayList<User> findAllUserExeptOne(int id);
    int[] findUserProject(int userId);
    int find(String name);
    User find(int id);
    String[] findUsersName();
    String[] findRole();
    int findRoleId(String roleName);
    boolean updateUser(User user);
    boolean updatePwd(String pwd, int id);
    String findLoginUser(int idUser);
    void updateTypeAccount(int id, String type);
    String findPostItUser(int idUser, int idProject);
    String updatePostitUser(String postit, int idUser, int idProject);
    String findRoleName(int idRole);
}