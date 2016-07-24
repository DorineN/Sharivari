package app.model;

import app.Main;

import java.sql.*;

public class PluginDAO extends DAO<Project> {

    private PreparedStatement[] requests = new PreparedStatement[3];

    public PluginDAO(Connection connection){

        super(connection);

        try {

            requests[0] = this.connection.prepareStatement("SELECT statusPlugin FROM plugin WHERE namePlugin=? ");
            requests[1] = this.connection.prepareStatement("DELETE FROM plugin WHERE namePlugin = ? ");
            requests[2] = this.connection.prepareStatement("INSERT INTO plugin(namePlugin) VALUES ( ? ) ");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Retrieve the user of the current session
    int userId = Main.getMyUser().userId;

    public String checkStatu(String name){

        String status ="";

        try{
            PreparedStatement req = requests[0];
            ResultSet res;

            req.setString(1, name);
            res = req.executeQuery();

            if(res.first()){
                status=res.getString("statusPlugin");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return status;
    }

    public void deletePugin(String name){



        try{
            PreparedStatement req = requests[1];


            req.setString(1, name);
            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void insertPugin(String name){



        try{
            PreparedStatement req = requests[2];


            req.setString(1, name);
            req.executeUpdate();



        }catch(Exception e){
            e.printStackTrace();
        }


    }



}