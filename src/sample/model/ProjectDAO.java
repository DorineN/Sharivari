package sample.model;

import sample.Main;
import sample.model.DAO;
import sample.model.Project;

import java.sql.Connection;
import java.sql.*;
import java.util.Objects;

public class ProjectDAO extends DAO<Project> {

    private PreparedStatement[] requests = new PreparedStatement[9];

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

            requests[6] = this.connection.prepareStatement("INSERT INTO participate(idRole, idUser, idProject) VALUES("+"?, ?, ?) ");

            requests[7] = this.connection.prepareStatement("UPDATE participate set idRole=?  WHERE idProject = ? AND idUser = ?");

            requests[8] = this.connection.prepareStatement("SELECT idUser, idRole FROM participate WHERE idProject=?");


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

    public void addUserToProject(int userId, int roleId, int projectId) {

        String[][] currentUsers = findUsersProject(projectId);
        boolean result = false;
        try {
            //Check if the user already participate to the project
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            for (int i = 0; i < currentUsers.length; i++) {
                if (Objects.equals(currentUsers[i][0], userDao.findLoginUser(userId))) {
                    result = true;
                } else {
                    result = false;
                }
            }
            //We update if the user is already defined
            if (result == true) {
                try {
                    PreparedStatement prepare = requests[7];

                    prepare.setInt(1, roleId);
                    prepare.setInt(2, projectId);
                    prepare.setInt(3, userId);
                    prepare.executeUpdate();

                    prepare.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //We insert if it's a new user
            } else {
                try {
                    PreparedStatement prepare = requests[6];

                    prepare.setInt(1, roleId);
                    prepare.setInt(2, userId);
                    prepare.setInt(3, projectId);
                    prepare.executeUpdate();

                    prepare.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[][] findUsersProject(int projectId){
        String[][] tab = null;
        int i;
        int rowcount = 0;
        UserDAO userDao = null;
        try {
            userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            try{
                PreparedStatement prepare = requests[8];
                ResultSet res;
                prepare.setInt(1, projectId);
                res = prepare.executeQuery();

                if (res.last()) {
                    rowcount = res.getRow();
                    res.beforeFirst();}
                tab = new String[rowcount][2];
                i = 0;
                while (res.next()) {
                    tab[i][0] = userDao.findLoginUser(res.getInt("idUser"));
                    tab[i][1] = userDao.findRoleName(res.getInt("idRole"));
                    i++;
                }

                res.close();
                prepare.close();

            }catch(Exception e){
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return tab;
    }

}
