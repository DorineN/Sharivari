package sample;

import java.sql.*;
import java.sql.Connection;

import sample.Main;
import sample.MySQLConnexion;
import sample.User;
import sample.UserDAO;

public class ProjectDAO extends DAO<Project> {

    public ProjectDAO(Connection connection){super(connection); }

    public void insert(String name, String description, String startDate, String estimateEndDate)throws SQLException{

        PreparedStatement prepare = null;
        PreparedStatement participate = null;
        int idProject = 0;
        //Retrieve the user of the current session
        int userId = Main.getMyUser().userId;

        System.out.println("L'id de l'utilisateur est " + userId);

        //Insertion to the project table to create the new project
        try{
            prepare = connection.prepareStatement("INSERT INTO project(nameProject, descriptionProject, startDateProject, estimateEndDateProject) VALUES(" +
                    "?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);
            /* + "INSERT INTO participate(idRole, idUser, idProject) VALUES ("+"2,?,?)");*/
            prepare.setString(1, name);
            prepare.setString(2, description);
            prepare.setString(3, startDate);
            prepare.setString(4, estimateEndDate);
            /*prepare.setInt(5, userId);
            prepare.setInt(6, projectId);*/
            prepare.execute();

            //Retrieve the id of the project created
            ResultSet rs = prepare.getGeneratedKeys();
            if (rs != null && rs.first()) {
                // Retrieve the generated id
                idProject = rs.getInt(1);
                System.out.print("L'id du projet créé est : " + idProject);
            }

            //Insertion to the participate table to link the user to the project
            try {
                participate = connection.prepareStatement("INSERT INTO participate(idRole, idUser, idProject) VALUES (" +
                        "?, ?, ?)");

                //This user is the administrator of the project (2), so it will be always the same value
                int rule = 2;
                participate.setInt(1, rule);
                participate.setInt(2, userId);
                participate.setInt(3, idProject);

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

    }

    public Project find(int id){
        Project project = new Project();

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM project WHERE idProject=?");
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                project = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"), res.getDate("startDateProject"),
                        res.getDate("realEndDaTeEndProject"), res.getDate("estimateEndDateProject"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return project;
    }

    public boolean update(Project project){
        boolean result = false;

        try{
            String query = "UPDATE project SET idProject = ?, nameProject = ?, descriptionProject = ?, startDateProject = ?, realEndDaTeEndProject = ?, estimateEndDateProject = ? WHERE idProject = ?";
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
