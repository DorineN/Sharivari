package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
/*
public class ProjectDAO extends DAO<Project> {

    public ProjectDAO(Connection connection){
        super(connection);
    }

    @Override
    public Project find(int id){
        Project project = new Project();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM project WHERE idProject=?");
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                project = new Project(res.getInt("idProject"), res.getDate("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDaTeEndProject"), res.getDate("estimateEndDateProject"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return project;
    }

    @Override
    public boolean update(Project project){
        boolean result = false;

        try{
            String query = "UPDATE project SET idProject = ?, descriptionProject = ?, firstNameUser = ?, startDateProject = ?, realEndDaTeEndProject = ?, estimateEndDateProject = ? WHERE idProject = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setInt(1, project.getProjectId());
            prepare.setString(2, project.getProjectDesc());
            prepare.setDate(3, project.getProjectStart());
            prepare.setDate(4, project.getProjectDeadline());
            prepare.setDate(5, project.getProjectEnd());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}*/
