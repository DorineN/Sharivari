package app.model;

import app.Main;
import app.annotations.CreateProject;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO extends DAO<Project> implements ProjectDAOInterface{

    private Main mainApp;

    private PreparedStatement[] requests = new PreparedStatement[20];

    public ProjectDAO(Connection connection){

        super(connection);

        try {

            requests[0] = this.connection.prepareStatement("INSERT INTO project(nameProject, descriptionProject, startDateProject, estimateEndDateProject) VALUES(" +
                    "?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);

            requests[1] = this.connection.prepareStatement("SELECT * FROM project WHERE idProject=? ");

            requests[2] = this.connection.prepareStatement("INSERT INTO participate(idRole, idUser, idProject) VALUES (" +
                    "?, ?, ?)");

            requests[3] = this.connection.prepareStatement("SELECT * FROM project WHERE idProject=?");

            requests[4] = this.connection.prepareStatement("SELECT * FROM project WHERE nameProject=?");

            requests[5] = this.connection.prepareStatement("UPDATE project SET nameProject = ?, descriptionProject = ?, startDateProject = ?, estimateEndDateProject = ? WHERE idProject = ?");

            requests[6] = this.connection.prepareStatement("INSERT INTO participate(idRole, idUser, idProject, postIt) VALUES(" +
                    "?, ?, ?, ?) ");

            requests[7] = this.connection.prepareStatement("UPDATE participate set idRole=?  WHERE idProject = ? AND idUser = ?");

            requests[8] = this.connection.prepareStatement("SELECT * FROM user, participate WHERE user.idUser = participate.idUser AND idProject=?");

            requests[9] = this.connection.prepareStatement("DELETE FROM `execute` WHERE idTask = ?");

            requests[10] = this.connection.prepareStatement("DELETE task FROM `task` WHERE idTask = ?");

            requests[11] = this.connection.prepareStatement("DELETE FROM `participate` WHERE idProject = ?");

            requests[12] = this.connection.prepareStatement("DELETE project FROM `project` WHERE idProject = ?");

            requests[13] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus, execute.idUser FROM `task` LEFT JOIN `execute` ON task.idTask = execute.idTask LEFT JOIN `priority` ON task.idPriority = priority.idPriority LEFT JOIN `status` ON task.idStatus = status.idStatus WHERE idProject=? ORDER BY task.estimateStartDateTask");

            requests[14] = this.connection.prepareStatement("SELECT nameRole FROM participate , role WHERE role.idRole = participate.idRole AND idUser = ? AND idProject = ?");

            requests[15] = this.connection.prepareStatement("UPDATE participate set idRole = ?  WHERE idProject = ? AND idUser = ?");

            requests[16] = this.connection.prepareStatement("SELECT idUser FROM participate WHERE idProject = ? AND (idRole = 1 OR idRole = 2)");

            requests[17] = this.connection.prepareStatement("UPDATE project set estimateEndDateProject = ?  WHERE idProject = ? ");

            requests[18] = this.connection.prepareStatement("UPDATE project set realEndDateProject = NULL  WHERE idProject = ? ");

            requests[19] = this.connection.prepareStatement("UPDATE project set realEndDateProject = ?  WHERE idProject = ? ");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Retrieve the user of the current session
    int userId = Main.getMyUser().userId;

    @Override
    public void updateEtatProjectFinish(int idProject){

        try{
            PreparedStatement req = requests[19];


            java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();

            String today = formater.format(date);



            req.setString(1, today);
            req.setInt(2, idProject);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateEtatProject(int idProject){

        try{
            PreparedStatement req = requests[18];


            req.setInt(1, idProject);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateDeadLine(int idProject, String date){

        try{
            PreparedStatement req = requests[17];

            req.setString(1, date);
            req.setInt(2, idProject);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int[] findMasterUserProject(int id){
        int[] tab = null;
        int i = 0;
        int rowcount = 0;

        try{
            PreparedStatement req = requests[16];
            ResultSet res;

            req.setInt(1, id);
            res = req.executeQuery();

            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            tab = new int[rowcount];

            while (res.next()) {
                tab[i] = res.getInt("idUser");
                i++;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return tab;
    }

    @Override
    public void updateToProject(int idUser, int idRole, int idProject){

        try{
            PreparedStatement req = requests[15];

            req.setInt(1, idRole);
            req.setInt(2, idProject);
            req.setInt(3, idUser);
            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @CreateProject
    @Override
    public int insert(String name, String description, String startDate, String estimateEndDate) throws SQLException {

        int idProject = 0;

        //Insertion to the project table to create the new project
        PreparedStatement req1 = null;
        PreparedStatement req3 = null;

        try {
            PreparedStatement req = requests[0];

            req.setString(1, name);
            req.setString(2, description);
            req.setString(3, startDate);
            req.setString(4, estimateEndDate);
            req.execute();

            // Retrieve the id of the project created
            ResultSet rs = req.getGeneratedKeys();
            if (rs != null && rs.first()) {
                // Retrieve the generated id
                idProject = rs.getInt(1);
                // System.out.print("L'id du projet créé est : " + idProject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return idProject;
    }

    @Override
    public Project find(int id){
        Project project = null;

        try{
            PreparedStatement req = requests[3];
            ResultSet res;

            req.setInt(1, id);
            res = req.executeQuery();

            if(res.first()){
                project = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDateProject"), res.getDate("estimateEndDateProject"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return project;
    }

    @Override
    public Project find(String name){
        Project project = null;

        try{
            PreparedStatement req = requests[4];
            ResultSet res;

            req.setString(1, name);
            res = req.executeQuery();

            if(res.first()){
                project = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDateProject"), res.getDate("estimateEndDateProject"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return project;
    }

    @Override
    public boolean update(Project project){
        boolean result = false;

        /**Specific transformed date to string to retrieve it in sql format **/
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varStart = sdf.format(project.getProjectStart());
        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varDeadline = sdf2.format(project.getProjectDeadline());
        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd");


        try{
            PreparedStatement req = requests[5];

            req.setString(1, project.getProjectName());
            req.setString(2, project.getProjectDesc());
            req.setDate(3, Date.valueOf(varStart));

            req.setDate(4, Date.valueOf(varDeadline));
            req.setInt(5, project.getProjectId());

            if(req.executeUpdate() == 1)
                result = true;


        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void addUserToProject(int userId, int roleId, int projectId){

        try{
            PreparedStatement req = requests[6];

            req.setInt(1, roleId);
            req.setInt(2, userId);
            req.setInt(3, projectId);
            req.setString(4, "");
            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> findUsersProject(int projectId){
        ArrayList<User> users= new ArrayList<>();

        try{
            PreparedStatement prepare = requests[8];
            ResultSet res;
            prepare.setInt(1, projectId);
            res = prepare.executeQuery();


            while (res.next()) {

                users.add(new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser")));

            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean delete(int idProject){

        boolean result = false;
        int idTask;


        int res = 0;

        try{
            PreparedStatement prepare = requests[9];
            PreparedStatement prepare2 = requests[10];
            PreparedStatement prepare3 = requests[11];
            PreparedStatement prepare4 = requests[12];

            List<Task> taskList = findTaskProject(idProject);
            //Retrieve and delete join between task and users of tasks in this project
            if(taskList.size() > 0) {
                for (int i = 0; i < taskList.size(); i++) {
                    idTask = taskList.get(i).getIdTask();
                    prepare.setInt(1, idTask);
                    prepare2.setInt(1, idTask);
                    res += prepare.executeUpdate();
                    res += prepare2.executeUpdate();
                }
            }

            //Delete all users from this project
            try {
                prepare3.setInt(1, idProject);
                res += prepare3.executeUpdate();
            }catch (Exception e ){
                e.printStackTrace();
            }

            //Delete definitively this project
            try {
                prepare4.setInt(1, idProject);
                res += prepare4.executeUpdate();
            }catch (Exception e ){
                e.printStackTrace();
            }

            if(res > 0){
                result = true;
            }



        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean deleteUserProject(int idUser, int idProject) {
        boolean result = false;

        try{
            PreparedStatement prepare = requests[9];

            //Retrieve and delete join between task and user
            prepare.setInt(1, idUser);
            prepare.setInt(2, idProject);

            if(prepare.executeUpdate() == 1){
                result = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Task> findTaskProject(int idProject){
        List<Task> data = new ArrayList<Task>();
        int count = 0;
        int i;
        int idUser;

        try{
            PreparedStatement prepare = requests[13];
            ResultSet res;

            prepare.setInt(1, idProject);
            res = prepare.executeQuery();

            if (res.last()) {
                count = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            if(res.first()){
                for(i = 0; i < count; i++) {
                    idUser = res.getInt("idUser");
                    data.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("durationTask"), res.getInt("idPriority"),
                            res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                            res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority"),mainApp.userDao.findLoginUser(idUser)));
                    res.next();
                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return data;

    }

    @Override
    public String findRole(int idUser, int idProject) {
        String role ="";
        try{
            PreparedStatement prepare = requests[14];
            ResultSet res;

            //Retrieve and delete join between task and user
            prepare.setInt(1, idUser);
            prepare.setInt(2, idProject);
            res = prepare.executeQuery();
            if(res.first()){
                role = res.getString("nameRole");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return role;
    }

    @Override
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}