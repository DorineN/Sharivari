package sample.model;

import sample.Main;
import sample.model.DAO;
import sample.model.Project;

import java.sql.Connection;
import java.sql.*;

public class ProjectDAO extends DAO<Project> {

    private PreparedStatement[] requests = new PreparedStatement[7];

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

            requests[5] = this.connection.prepareStatement("UPDATE project SET idProject = ?, nameProject = ?, descriptionProject = ?, startDateProject = ?, realEndDaTeEndProject = ?, estimateEndDateProject = ? WHERE idProject = ?");

            requests[6] = this.connection.prepareStatement("INSERT INTO participate(idRole, idUser, idProject) VALUES(" +
                    "?, ?, ?) ");

         }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Retrieve the user of the current session
    int userId = Main.getMyUser().userId;

    public Project insert(String name, String description, String startDate, String estimateEndDate)throws SQLException {

        Project projet = null;

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
            /*prepare.setInt(5, userId);
            prepare.setInt(6, projectId);*/
            req.execute();

            //Retrieve the id of the project created
            ResultSet rs = req.getGeneratedKeys();
            if (rs != null && rs.first()) {
                // Retrieve the generated id
                idProject = rs.getInt(1);
                System.out.print("L'id du projet créé est : " + idProject);
            }
            try {
                PreparedStatement req2 = requests[1];
                ResultSet res;

                req2.setInt(1, idProject);
                res = req2.executeQuery();

                if (res.first()) {
                    projet = new Project(res.getInt("idProject"), res.getString("nameProject"), res.getString("descriptionProject"),
                            res.getDate("startDateProject"), res.getDate("estimateEndDateProject"), res.getDate("realEndDateProject"));
                }

                res.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Insertion to the participate table to link the user to the project
            try {
                req3 = requests[2];

                //This user is the administrator of the project (2), so it will be always the same value
                int rule = 2;
                req3.setInt(1, rule);
                req3.setInt(2, userId);
                req3.setInt(3, idProject);

                req3.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (req3 != null) {
                req3.close();
                req1.close();
            }
        }

        return projet;
    }

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

    public boolean update(Project project){
        boolean result = false;

        try{
            PreparedStatement req = requests[5];

            req.setInt(1, project.getProjectId());
            req.setString(2, project.getProjectName());
            req.setString(3, project.getProjectDesc());
            req.setDate(4, (Date) project.getProjectStart());
            req.setDate(5, (Date) project.getProjectDeadline());
            req.setDate(6, (Date) project.getProjectEnd());

            if(req.executeUpdate() == 1)
                result = true;


        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public void addUserToProject(int userId, int roleId, int projectId){

        try{
            PreparedStatement req = requests[6];

            req.setInt(1, roleId);
            req.setInt(2, userId);
            req.setInt(3, projectId);
            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
