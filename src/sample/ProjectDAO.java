package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class ProjectDAO extends DAO<Project> {

    public ProjectDAO(Connection connection){super(connection); }

    public void insert(int id, String name, String description, Date startDate, Date realEndDaTeEnd, Date estimateEndDate){

        try{

            PreparedStatement prepare = connection.prepareStatement("INSERT INTO project(idProject, nameProject, descriptionProject, startDateProject, realEndDaTeEndProject, estimateEndDateProject) VALUES(" +
                    "?, ?, ?, ?, ?, ?, ?, ?) ");


            prepare.setInt(1, id);
            prepare.setString(2, name);
            prepare.setString(3, description);
            prepare.setDate(4, startDate);
            prepare.setDate(5, realEndDaTeEnd);
            prepare.setDate(6, estimateEndDate);
            prepare.executeUpdate();

            prepare.close();

        }catch(Exception e){
            e.printStackTrace();
        }

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
                project = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDateProject"), res.getDate("estimateEndDateProject"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return project;
    }

    public Project find(String name){
        Project project = new Project();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM project WHERE nameProject=?");
            ResultSet res;

            prepare.setString(1, name);
            res = prepare.executeQuery();

            if(res.first()){
                project = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDateProject"), res.getDate("estimateEndDateProject"));
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
            String query = "UPDATE project SET idProject = ?, nameProject = ?, descriptionProject = ?,, startDateProject = ?, realEndDaTeEndProject = ?, estimateEndDateProject = ? WHERE idProject = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setInt(1, project.getProjectId());
            prepare.setString(2, project.getProjectName());
            prepare.setString(3, project.getProjectDesc());
            prepare.setDate(4, (Date) project.getProjectStart());
            prepare.setDate(5, (Date) project.getProjectDeadline());
            prepare.setDate(6, (Date) project.getProjectEnd());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
