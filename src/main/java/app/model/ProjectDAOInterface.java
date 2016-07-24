package app.model;

import app.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProjectDAOInterface {
    void updateEtatProjectFinish(int idProject);
    void updateDeadLine(int idProject, String date);
    int[] findMasterUserProject(int id);
    void updateToProject(int idUser, int idRole, int idProject);
    int insert(String name, String description, String startDate, String estimateEndDate) throws SQLException;
    void updateEtatProject(int idProject);
    public Project find(int id);
    public Project find(String name);
    boolean update(Project project);
    void addUserToProject(int userId, int roleId, int projectId);
    ArrayList<User> findUsersProject(int projectId);
    boolean delete(int idProject);
    boolean deleteUserProject(int idUser, int idProject);
    List<Task> findTaskProject(int idProject);
    String findRole(int idUser, int idProject);
    void setMainApp(Main mainApp);
}
