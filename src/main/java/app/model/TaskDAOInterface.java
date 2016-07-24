package app.model;

import app.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface TaskDAOInterface {
    int findTaskNotFinish(int id);
    void updateRealEndDate(int id);
    void deleteRealStartDate(int id);
    void deleteRealEndDate(int id);
    void updateStatus(int idProject, int idStatus);
    void addUserToTask(int userId, int projectId);
    ArrayList<User> findUsersTask(int id);
    void updateDateRealStartDate(int id);
    ArrayList<Task> findTask(String date,int idUser, int idProject);
    ArrayList<Task> findAllTask(String date,int idUser);
    Task find(String name);
    boolean update(Task task, int oldStatus);
    String[] findPriority();
    String[] findStatus();
    int findIdPriority(String namePriority);
    String findNamePriority(int idPriority);
    String findStatusName(int idStatus);
    List<Task> findTaskProject(int idProject);
    List<Task> findMyTask(int idProject, int idUser);
    int findUserTask(int taskId);
    void affectUserToTask(int taskId, int userId);
    int insert(String name, String description, int priority, int duration, String estimateStartDate, String estimateEndDate) throws SQLException;
    boolean delete(int id);
    Task find(int id);
    void deleteExecuteUser(int idUser, int idTask);
    void setMainApp(Main mainApp);
    int findIdStatus(String nameStatus);
}
