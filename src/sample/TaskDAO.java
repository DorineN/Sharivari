package sample;

/**
 * Created by Loïc on 21/05/2016.
 **/
import java.sql.*;
import java.sql.Connection;

public class TaskDAO extends DAO<Task>{

    //Retrieve the user of the current session
    int userId = Main.getMyUser().userId;

    //Retrieve the project of the current session
    int projectId = Main.getMyProject().idProject;

    public TaskDAO(Connection connection){
        super(connection);
    }

    public Task find(int id){
        Task task = new Task();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM task WHERE idTask=?");
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                task = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"),  res.getInt("idPriority"),
                        res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }

    public Task find(String name){
        Task task = new Task();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM task WHERE nameTask=?");
            ResultSet res;

            prepare.setString(1, name);
            res = prepare.executeQuery();

            if(res.first()){
                task = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("idPriority"),
                        res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"));
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

        try{
            String query = "UPDATE task SET idTask = ?, nameTask = ?, descriptionTask = ?, idPriority = ?, estimateStartDateTask = ?, estimateEndDateTask = ?, realStartDateTask = ?, realEndDateTask = ? WHERE idTask = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setInt(1, task.getIdTask());
            prepare.setString(2, task.getNameTask());
            prepare.setString(3, task.getDescriptionTask());
            prepare.setInt(4, task.getIdPriority());
            prepare.setDate(5, (Date) task.getEstimateStartDateTask());
            prepare.setDate(6, (Date) task.getEstimateEndDateTask());
            prepare.setDate(7, (Date) task.getRealStartDateTask());
            prepare.setDate(8, (Date) task.getRealEndDateTask());

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
            PreparedStatement prepare = connection.prepareStatement("SELECT namePriority FROM priority");
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
           /* while(res.next() ) {
                result[i] = res.getString("namePriority");
                i++;
            }*/

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public Task[] findTask(String date,int idUser, int idProject){
        Task[] tTask = null;

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT count (idTask) as nbResult, * FROM task WHERE estiamteStartDateTask=?, idUser=? , idProject=?");
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
    }

    public Task insert(String name, String description, int priority, String estimateStartDate, String estimateEndDate)throws SQLException {

        Task task = new Task();
        PreparedStatement prepare = null;
        PreparedStatement participate = null;
        int idTask = 0;
        int idStat = 1;

        System.out.println("L'id de l'utilisateur est " + userId);

        //Insertion to the project table to create the new project
        try{
            prepare = connection.prepareStatement("INSERT INTO task(nameTask, descriptionTask, idPriority, estimateStartDateTask, estimateEndDateTask, idProject, idStatus) VALUES(" +
                    "?, ?, ?, ?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);
            prepare.setString(1, name);
            prepare.setString(2, description);
            prepare.setInt(3, priority);
            prepare.setString(4, estimateStartDate);
            prepare.setString(5, estimateEndDate);
            prepare.setInt(6, projectId);
            prepare.setInt(7, idStat);
            prepare.execute();

            //Retrieve the id of the project created
            ResultSet rs = prepare.getGeneratedKeys();
            if (rs != null && rs.first()) {
                // Retrieve the generated id
                idTask = rs.getInt(1);
                System.out.print("L'id de la tâche créée est : " + idTask);
            }
            try {
                PreparedStatement prepare2 = connection.prepareStatement("SELECT * FROM task WHERE idTask=? ");
                ResultSet res;

                prepare2.setInt(1, idTask);
                res = prepare2.executeQuery();

                if (res.first()) {
                    task = new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getInt("idPriority"),
                            res.getDate("estimateStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realStartDateTask"), res.getDate("realEndDateTask"));
                }
                prepare2.close();
                res.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            //Insertion to the participate table to link the user to the project
            try {
                participate = connection.prepareStatement("INSERT INTO execute(idUser, idTask) VALUES (" +
                        "?, ?)");

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
}
