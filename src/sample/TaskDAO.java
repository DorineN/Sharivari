package sample;

/**
 * Created by Loïc on 21/05/2016.
**/
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Date;

public class TaskDAO extends DAO<Task>{

    public TaskDAO(Connection connection){
        super(connection);
    }

    @Override
    public Task find(int p_id) {
        return null;
    }

    @Override
    public boolean update(Task p_obj) {
        return false;
    }


    public Task[] findTask(Date date,int idUser, int idProject){

        Task[] tTask = new Task[50];
        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM task WHERE estiamteStartDateTask=?, idUser=? , idProject=?");
            ResultSet res;

            prepare.setDate(1, date);
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


}
