package sample.model;

/**
 * Created by Loïc on 21/05/2016.
**/
 import sample.Main;
 import sample.model.DAO;
 import sample.model.Task;

 import java.sql.*;
 import java.util.ArrayList;
 import java.util.List;

public class TaskDAO extends DAO<Task> {
    private PreparedStatement[] requests = new PreparedStatement[17];
    public TaskDAO(Connection connection)  {

        super(connection);

        // Insert request
        try {

            requests[0] = this.connection.prepareStatement("SELECT DISTINCT(nameTask) as nameTask, task.idTask, descriptionTask, estimateStartDateTask, realStartDateTask, estimateEndDateTask, realEndDateTask, idProject, idPriority FROM task, execute WHERE estimateStartDateTask= ? AND idUser = ? AND idProject= ? ");
            requests[1] = this.connection.prepareStatement("SELECT task.*, priority.namePriority, status.nameStatus FROM task LEFT JOIN priority ON task.idPriority = priority.idPriority LEFT JOIN sharin.status ON task.idStatus = status.idStatus WHERE nameTask=?");
            requests[2] = this.connection.prepareStatement("UPDATE task SET idTask = ?, nameTask = ?, descriptionTask = ?, idPriority = ?, estimateStartDateTask = ?, estimateEndDateTask = ?, realStartDateTask = ?, realEndDateTask = ?, idStatus = ? WHERE idTask = ?");
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


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Task> findTask(String date,int idUser, int idProject){
        ArrayList<Task> tTask =  new ArrayList();
        try{
            PreparedStatement req = requests[0];
            ResultSet res;
            req.setString(1, date);
            req.setInt(2, idUser);
            req.setInt(3, idProject);
            res = req.executeQuery();

            int i = 0;
            if(res.first()){
                tTask.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("durationTask"), res.getInt("idPriority"), res.getDate("estimateStartDateTask"),res.getDate("realStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realEndDateTask"), res.getInt("idStatus"),  res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority")));
            }

            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return tTask;
    }

    public Task find(String name){
        Task task = new Task();

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

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }

    public boolean update(Task task){
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
            prepare.setInt(10, task.getIdTask());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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

            prepare.close();
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
                for(i = 0; i < count; i++) {
                    data.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("durationTask"), res.getInt("idPriority"),
                            res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"),
                            res.getInt("idStatus"), res.getInt("idProject"), res.getString("nameStatus"), res.getString("namePriority")));
                    res.next();
                }
            }

            prepare.close();
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

            prepare.close();

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

            prepare.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Task insert(String name, String description, int priority, int duration, String estimateStartDate, String estimateEndDate)throws SQLException {

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
                System.out.print("L'id de la tâche créée est : " + idTask);
            }
            try {
                find(idTask);
            }catch(Exception e){
                e.printStackTrace();
            }

            //Insertion to the participate table to link the user to the project
            try {
                participate = requests[13];

                participate.setInt(1, userId);
                participate.setInt(2, idTask);

                participate.execute();

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(prepare != null){
                prepare.close();
                participate.close();
            }
        }

        return task;
    }

    public boolean delete(Task task){
        boolean result = false;

        try{
            PreparedStatement prepare = requests[14];
            PreparedStatement prepare2 = requests[15];

            prepare.setInt(1, task.getIdTask());
            prepare2.setInt(1, task.getIdTask());

            if(prepare.executeUpdate() == 1 && prepare2.executeUpdate() == 1)
                result = true;

            prepare.close();
            prepare2.close();
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

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }

}