package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/*
public class TaskDAO extends DAO<User> {

    public TaskDAO(Connection connection){
        super(connection);
    }

    @Override
    public Task find(int id){
        Task task = new Task();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM task WHERE idTask=?");
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                task = new Task(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return task;
    }

    @Override
    public boolean update(Task task){
        boolean result = false;

        try{
            String query = "UPDATE task SET loginUser = ?, lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setString(1, task.getUserLogin());
            prepare.setString(2, task.getUserName());
            prepare.setString(3, task.getUserFirstName());
            prepare.setString(4, task.getUserMail());
            prepare.setInt(5, task.getUserPhone());
            prepare.setString(6, task.getUserCompany());
            prepare.setInt(7, task.getUserId());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}*/
