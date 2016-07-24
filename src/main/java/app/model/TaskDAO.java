package app.model;

/**
 * Created by Loïc on 21/05/2016.
 **/
import app.Main;
import app.model.DAO;
import app.model.Task;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskDAO extends DAO<Task> {
    private PreparedStatement[] requests = new PreparedStatement[29];
    private Main mainApp;

    public TaskDAO(Connection connection)  {

        super(connection);

        // Insert request
        try {

            requests[0] = this.connection.prepareStatement("SELECT DISTINCT(nameTask) as nameTask, task.idTask, descriptionTask, estimateStartDateTask," +
                    " realStartDateTask, estimateEndDateTask, realEndDateTask, idProject, task.idPriority, nameStatus, namePriority" +
                    " FROM task, execute, status, priority WHERE priority.idPriority = task.idPriority AND status.idStatus = task.idStatus AND task.idTask = execute.idTask AND execute.idUser = ? AND idProject= ? ");

            requests[1] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus FROM task LEFT JOIN priority ON task.idPriority = priority.idPriority LEFT JOIN sharin.status ON task.idStatus = status.idStatus WHERE nameTask=?");

            requests[2] = this.connection.prepareStatement("UPDATE task SET idTask = ?, nameTask = ?, descriptionTask = ?, idPriority = ?," +
                    " estimateStartDateTask = ?, estimateEndDateTask = ?, realStartDateTask = ?, realEndDateTask = ?, idStatus = ? , durationTask = ? WHERE idTask = ?");

            requests[3] = this.connection.prepareStatement("SELECT namePriority FROM priority");

            requests[4] = this.connection.prepareStatement("SELECT nameStatus FROM status");

            requests[5] = this.connection.prepareStatement("SELECT idPriority FROM priority WHERE namePriority = ?");

            requests[6] = this.connection.prepareStatement("SELECT idStatus FROM status WHERE nameStatus = ?");

            requests[7] = this.connection.prepareStatement("SELECT namePriority FROM priority WHERE idPriority=?");

            requests[8] = this.connection.prepareStatement("SELECT nameStatus FROM status WHERE idStatus=?");

            requests[9] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus FROM task LEFT JOIN priority ON task.idPriority = priority.idPriority LEFT JOIN sharin.status ON task.idStatus = status.idStatus WHERE idProject=?");

            requests[10] = this.connection.prepareStatement("SELECT idUser FROM  execute WHERE idTask =? ");

            requests[11] = this.connection.prepareStatement("INSERT INTO execute(idUser, idTask) VALUES(" +"?, ?) ");

            requests[12] = this.connection.prepareStatement("INSERT INTO task(nameTask, descriptionTask, idPriority, estimateStartDateTask, estimateEndDateTask, idProject, idStatus, durationTask ) VALUES(" +
                    "?, ?, ?, ?, ?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);

            requests[13] = this.connection.prepareStatement("INSERT INTO execute(idUser, idTask) VALUES ("+"?, ?)");

            requests[14] = this.connection.prepareStatement("DELETE FROM `execute` WHERE idTask = ?");

            requests[15] = this.connection.prepareStatement("DELETE task FROM `task` WHERE idTask = ?");

            requests[16] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus, execute.idUser FROM task LEFT JOIN priority ON task.idPriority = priority.idPriority LEFT JOIN sharin.status ON task.idStatus = status.idStatus LEFT JOIN execute ON execute.idTask = task.idTask WHERE task.idTask=?");


            requests[17] = this.connection.prepareStatement("SELECT DISTINCT(nameTask) as nameTask, task.idTask, descriptionTask, estimateStartDateTask," +
                    " realStartDateTask, estimateEndDateTask, realEndDateTask, task.idProject, priority.idPriority, nameStatus, nameProject, namePriority" +
                    " FROM task, execute, status, project, priority WHERE priority.idPriority = task.idPriority AND project.idProject = task.idProject AND status.idStatus = task.idStatus " +
                    "AND task.idTask = execute.idTask AND execute.idUser = ? ");

            requests[18] = this.connection.prepareStatement("UPDATE task set realStartDateTask = ?  WHERE idTask = ? ");

            requests[19] = this.connection.prepareStatement("UPDATE task set realEndDateTask = ?  WHERE idTask = ? ");

            requests[20] = this.connection.prepareStatement("SELECT * FROM user, execute WHERE user.idUser = execute.idUser AND idTask=?");

            requests[21] = this.connection.prepareStatement("INSERT INTO execute( idUser, idTask) VALUES(" +
                    "?, ?) ");

            requests[22] = this.connection.prepareStatement("UPDATE task set idStatus = ? WHERE idTask = ? ");

            requests[23] = this.connection.prepareStatement("UPDATE task set realStartDateTask = NULL  WHERE idTask = ? ");

            requests[24] = this.connection.prepareStatement("UPDATE task set realEndDateTask = NULL  WHERE idTask = ? ");

            requests[25] = this.connection.prepareStatement("UPDATE task set realEndDateTask = ?  WHERE idTask = ? ");

            requests[26] = this.connection.prepareStatement("SELECT COUNT(idTask) as cpt FROM task WHERE (idStatus = 1 OR idStatus = 2) AND idProject = ?");

            requests[27] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus FROM task LEFT JOIN priority ON task.idPriority = priority.idPriority LEFT JOIN execute ON task.idTask = execute.idTask LEFT JOIN sharin.status ON task.idStatus = status.idStatus WHERE idProject=? AND idUser = ?");

            requests[28] = this.connection.prepareStatement("DELETE FROM execute  WHERE idUser=? AND idTask = ? ");


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public int findTaskNotFinish(int id){
        int nb = 0;

        PreparedStatement prepare = requests[26];


        ResultSet res;

        try {
            prepare.setInt(1, id);
            res = prepare.executeQuery();
            if (res.first()){
                nb = res.getInt("cpt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        return nb;

    }

    public void updateRealEndDate(int id){


        try{
            PreparedStatement req = requests[25];

            java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( "yyyy-MM-dd");
            java.util.Date date = new java.util.Date();

            String today = formater.format(date);

            //recup date today

            req.setString(1, today);
            req.setInt(2, id);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void deleteRealStartDate(int id){


        try{
            PreparedStatement req = requests[23];

            req.setInt(1, id);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void deleteRealEndDate(int id){


        try{
            PreparedStatement req = requests[24];

            req.setInt(1, id);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }


    }
    public void updateStatus(int idProject, int idStatus){

        try{
            PreparedStatement req = requests[22];


            req.setInt(1, idStatus);
            req.setInt(2, idProject);

            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addUserToTask(int userId, int projectId){

        try{
            PreparedStatement req = requests[21];


            req.setInt(1, userId);
            req.setInt(2, projectId);

            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<User> findUsersTask(int id){
        ArrayList<User> users= new ArrayList<>();

        try{
            PreparedStatement prepare = requests[20];
            ResultSet res;
            prepare.setInt(1, id);
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


    public void updateDateRealStartDate(int id){


        try{
            PreparedStatement req = requests[18];

            java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( "yyyy-MM-dd");
            java.util.Date date = new java.util.Date();

            String today = formater.format(date);

            //recup date today

            req.setString(1, today);
            req.setInt(2, id);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public ArrayList<Task> findTask(String date,int idUser, int idProject){

        ArrayList<Task> tTask =  new ArrayList();
        try{
            PreparedStatement req = requests[0];
            ResultSet res;

            req.setInt(1, idUser);
            req.setInt(2, idProject);
            res = req.executeQuery();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date myDate = formatter.parse(date);

            int i =0;

            while(res.next()){

                if ((myDate.after(res.getDate("estimateStartDateTask")) && myDate.before(res.getDate("estimateEndDateTask"))) || myDate.equals(res.getDate("estimateStartDateTask"))  || myDate.equals(res.getDate("estimateEndDateTask"))){
                    tTask.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getDate("estimateStartDateTask"),res.getDate("realStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realEndDateTask"),  res.getInt("idProject"), res.getInt("idPriority"), res.getString("nameStatus")));
                    tTask.get(i).setNamePriority(res.getString("namePriority"));
                    i++;
                }



            }



            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return tTask;
    }
    public ArrayList<Task> findAllTask(String date,int idUser){

        ArrayList<Task> tTask =  new ArrayList();
        try{


            PreparedStatement req = requests[17];
            ResultSet res;


            req.setInt(1, idUser);
            res = req.executeQuery();



            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            java.util.Date myDate = formatter.parse(date);

            int i =0;
            while(res.next()){

                if ((myDate.after(res.getDate("estimateStartDateTask")) && myDate.before(res.getDate("estimateEndDateTask"))) || myDate.equals(res.getDate("estimateStartDateTask")) || myDate.equals(res.getDate("estimateEndDateTask"))){

                    tTask.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getDate("estimateStartDateTask"),res.getDate("realStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realEndDateTask"),  res.getInt("idProject"), res.getInt("idPriority"), res.getString("nameStatus"), res.getString("nameProject")));
                    tTask.get(i).setNamePriority(res.getString("namePriority"));


                    i++;
                }



            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return tTask;
    }


    public Task find(String name){
        Task task = null;

        try{
            PreparedStatement prepare = requests[1];
            ResultSet res;

            prepare.setString(1, name);
            res = prepare.executeQuery();

            if(res.first()){
                task = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"),  res.getInt("durationTask"),res.getInt("idPriority"),
                        res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                        res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }

    public boolean update(Task task, int oldStatus){
        boolean result = false;

        /**Specific transformed date to string to retrieve it in sql format **/
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varStart = sdf.format(task.getEstimateStartDateTask());
        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varEnd = sdf2.format(task.getEstimateEndDateTask());

        try{
            PreparedStatement prepare = requests[2];

            prepare.setInt(1, task.getIdTask());
            prepare.setString(2, task.getNameTask());
            prepare.setString(3, task.getDescriptionTask());
            prepare.setInt(4, task.getIdPriority());
            prepare.setDate(5, Date.valueOf(varStart));
            prepare.setDate(6, Date.valueOf(varEnd));
            prepare.setDate(7, (Date) task.getRealStartDateTask());
            prepare.setDate(8, (Date) task.getRealEndDateTask());
            prepare.setInt(9, task.getIdStatus());
            prepare.setInt(10, task.getDurationTask());
            prepare.setInt(11, task.getIdTask());

            if(prepare.executeUpdate() == 1)
                result = true;


            //Start the task only
            if(oldStatus == 1 &&  task.getIdStatus()== 2 ){
                mainApp.taskDAO.updateDateRealStartDate(task.getIdTask());

                //if Process to dont start
            }else if(oldStatus == 2 &&  task.getIdStatus()== 1 ) {
                mainApp.taskDAO.deleteRealStartDate(task.getIdTask());


            }else if(oldStatus == 3 &&  task.getIdStatus()== 1 ) {
                mainApp.taskDAO.deleteRealStartDate(task.getIdTask());
                mainApp.taskDAO.deleteRealEndDate(task.getIdTask());
                mainApp.projectDAO.updateEtatProject(mainApp.getMyProject().getProjectId());

            }else if(oldStatus == 1 &&  task.getIdStatus()== 3 ) {
                mainApp.taskDAO.updateDateRealStartDate(task.getIdTask());
                mainApp.taskDAO.updateRealEndDate(task.getIdTask());
                mainApp.projectDAO.updateEtatProjectFinish(mainApp.getMyProject().getProjectId());

            }else if(oldStatus == 2 &&  task.getIdStatus()== 3 ){
                mainApp.taskDAO.updateRealEndDate(task.getIdTask());

                //CHeck if lastTask to update the realEndStartProject
                int nb =  mainApp.taskDAO.findTaskNotFinish(mainApp.getMyProject().getProjectId());
                // System.out.println(nb);
                if(nb ==0){

                    mainApp.projectDAO.updateEtatProjectFinish(mainApp.getMyProject().getProjectId());

                }

            }else if(oldStatus == 3 &&  task.getIdStatus()== 2 ){
                mainApp.taskDAO.deleteRealEndDate(task.getIdTask());
                mainApp.projectDAO.updateEtatProject(mainApp.getMyProject().getProjectId());

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public String[] findPriority(){
        String[] result;
        result = new String [1];
        int count = 0;

        try{
            PreparedStatement prepare = requests[3];
            ResultSet res;
            res = prepare.executeQuery();
            int i;
            String name;

            if (res.last()) {
                count = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            result = new String [count];
            if(res.first()) {
                for(i = 0; i < count; i++) {
                    result[i] = res.getString("namePriority");
                    res.next();
                }
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String[] findStatus(){
        String[] result;
        result = new String [1];
        int count = 0;

        try{
            PreparedStatement prepare = requests[4];
            ResultSet res;
            res = prepare.executeQuery();
            int i;
            String name;

            if (res.last()) {
                count = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            result = new String [count];
            if(res.first()) {
                for(i = 0; i < count; i++) {
                    result[i] = res.getString("nameStatus");
                    res.next();
                }
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public int findIdPriority(String namePriority){
        int result = 0;

        try{
            PreparedStatement prepare = requests[5];
            ResultSet res;
            prepare.setString(1, namePriority);
            res = prepare.executeQuery();

            if(res.first()) {
                result = res.getInt("idPriority");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int findIdStatus(String nameStatus){
        int result = 0;

        try{
            PreparedStatement prepare = requests[6];
            ResultSet res;
            prepare.setString(1, nameStatus);
            res = prepare.executeQuery();

            if(res.first()) {
                result = res.getInt("idStatus");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

/*    public Task[] findTask(String date,int idUser, int idProject){
        Task[] tTask = null;
        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT count (idTask) as nbResult, * FROM task WHERE estimateStartDateTask = ?, idUser = ?, idProject = ?");
            ResultSet res;
            prepare.setString(1, date);
            prepare.setInt(2, idUser);
            prepare.setInt(3, idProject);
            res = prepare.executeQuery();
            if(res.first()){
            }

            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return tTask;
    }*/

    public String findNamePriority(int idPriority){
        String result = null;

        try{
            PreparedStatement prepare = requests[7];
            ResultSet res;
            prepare.setInt(1, idPriority);
            res = prepare.executeQuery();

            if(res.first()) {
                result = res.getString("namePriority");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String findStatusName(int idStatus){
        String result = null;

        try{
            PreparedStatement prepare = requests[8];
            ResultSet res;
            prepare.setInt(1, idStatus);
            res = prepare.executeQuery();

            if(res.first()) {
                result = res.getString("nameStatus");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Task> findTaskProject(int idProject){
        List<Task> data = new ArrayList<Task>();
        int count = 0;
        int i;

        try{
            PreparedStatement prepare = requests[9];
            ResultSet res;

            prepare.setInt(1, idProject);
            res = prepare.executeQuery();

            if (res.last()) {
                count = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            if(res.first()){
                Task myTask;
                for(i = 0; i < count; i++) {
                    myTask = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("durationTask"), res.getInt("idPriority"),
                            res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                            res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority"));
                    myTask.setMainApp(mainApp);
                    data.add(myTask);
                    res.next();
                }
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return data;
    }

    public List<Task> findMyTask(int idProject, int idUser){
        List<Task> data = new ArrayList<Task>();
        int count = 0;
        int i;

        try{
            PreparedStatement prepare = requests[27];
            ResultSet res;

            prepare.setInt(1, idProject);
            prepare.setInt(2, idUser);
            res = prepare.executeQuery();

            if (res.last()) {
                count = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            if(res.first()){
                Task myTask;
                for(i = 0; i < count; i++) {
                    myTask = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("durationTask"), res.getInt("idPriority"),
                            res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                            res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority"));
                    myTask.setMainApp(mainApp);
                    data.add(myTask);
                    res.next();
                }
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return data;
    }

    public int findUserTask(int taskId){
        int userId = 0;
        try{
            PreparedStatement prepare = requests[10];
            ResultSet res;
            prepare.setInt(1, taskId);
            res =  prepare.executeQuery();

            if(res.first()){
                userId = res.getInt("idUser");
            }



        }catch(Exception e){
            e.printStackTrace();
        }
        return userId;
    }

    public void affectUserToTask(int taskId, int userId){
        try{
            PreparedStatement prepare = requests[11];

            prepare.setInt(1, taskId);
            prepare.setInt(2, userId);
            prepare.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int insert(String name, String description, int priority, int duration, String estimateStartDate, String estimateEndDate)throws SQLException {



        Task task = new Task();
        PreparedStatement participate = null;
        PreparedStatement prepare = null;
        int projectId = Main.getMyProject().getProjectId();
        int userId = Main.getMyUser().getUserId();
        int idTask = 0;
        int idStat = 1;

        //Insertion to the project table to create the new project
        try{
            prepare = requests[12];

            prepare.setString(1, name);
            prepare.setString(2, description);
            prepare.setInt(3, priority);
            prepare.setString(4, estimateStartDate);
            prepare.setString(5, estimateEndDate);
            prepare.setInt(6, projectId);
            prepare.setInt(7, idStat);
            prepare.setInt(8, duration);
            prepare.execute();

            //Retrieve the id of the project created
            ResultSet rs = prepare.getGeneratedKeys();
            if (rs != null && rs.first()) {
                // Retrieve the generated id
                idTask = rs.getInt(1);
                //System.out.print("L'id de la tâche créée est : " + idTask);
            }
            try {

            }catch(Exception e){
                e.printStackTrace();
            }



        }catch(Exception e){
            e.printStackTrace();
        }
        //endDateproject = null
        mainApp.projectDAO.updateEtatProject(mainApp.getMyProject().getProjectId());


        return idTask;
    }

    public boolean delete(int id){
        boolean result = false;

        try{
            PreparedStatement prepare = requests[14];
            PreparedStatement prepare2 = requests[15];

            prepare.setInt(1, id);
            prepare2.setInt(1, id);

            prepare.executeUpdate();
            prepare2.executeUpdate();


            //check if all task finish to update endproject
            List nbtask =findTaskProject(mainApp.getMyProject().getProjectId());
            if (nbtask.size()>0){
                int nbNotFinish = findTaskNotFinish(mainApp.getMyProject().getProjectId());
                if (nbNotFinish == 0){
                    mainApp.projectDAO.updateEtatProjectFinish(mainApp.getMyProject().getProjectId());
                }

            }



        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public Task find(int id){
        Task task = new Task();

        try{
            PreparedStatement prepare = requests[16];
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                task = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"),  res.getInt("durationTask"),res.getInt("idPriority"),
                        res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                        res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }



    public void deleteExecuteUser(int idUser, int idTask){
        try{
            PreparedStatement req = requests[28];

            req.setInt(1, idUser);
            req.setInt(2, idTask);
            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;


    }
}