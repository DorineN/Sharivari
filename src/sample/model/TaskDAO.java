package sample.model;

/**
 * Created by Loïc on 21/05/2016.
**/
 import sample.model.DAO;
 import sample.model.Task;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;

public class TaskDAO extends DAO<Task> {
    private PreparedStatement[] requests = new PreparedStatement[1];
    public TaskDAO(Connection connection)  {

        super(connection);

        // Insert request
        try {

            requests[0] = this.connection.prepareStatement("SELECT DISTINCT(nameTask) as nameTask, task.idTask, descriptionTask, estimateStartDateTask, realStartDateTask, estimateEndDateTask, realEndDateTask, idProject, idPriority FROM task, execute WHERE estimateStartDateTask= ? AND idUser = ? AND idProject= ? ");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public Task find(int p_id) {
        return null;
    }


    public boolean update(Task p_obj) {
        return false;
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

                tTask.add(new Task(res.getInt("idTask"), res.getString("nameTask"), res.getString("descriptionTask"), res.getDate("estimateStartDateTask"),res.getDate("realStartDateTask"), res.getDate("estimateEndDateTask"), res.getDate("realEndDateTask"),  res.getInt("idProject"), res.getInt("idPriority")));


            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return tTask;
    }


}
