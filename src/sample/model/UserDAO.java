package sample.model;

import sample.Main;
import sample.util.UserDAOInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends DAO<User> implements UserDAOInterface {
    private PreparedStatement[] requests = new PreparedStatement[10];

    public UserDAO(Connection connection){
        super(connection);

        try {
            // Insert request
            requests[0] = this.connection.prepareStatement("INSERT INTO user(loginUser, pwdUser, lastNameUser, firstNameUser, mailUser, phoneUser, companyUser, typeUser) VALUES(" +
                    "?, ?, ?, ?, ?, ?, ?,'user') ");

            // Select request (connection)
            requests[1] = this.connection.prepareStatement("SELECT * FROM user WHERE loginUser = ? AND pwdUser = ? ");

            // Update request
            requests[2] = this.connection.prepareStatement("UPDATE user SET loginUser = ?, pwdUser = ?, lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?");

            //select request participate
            requests[3] = this.connection.prepareStatement("SELECT * FROM participate WHERE idUser=? ");

            //select request info user by id
            requests[4] = this.connection.prepareStatement("SELECT * FROM user WHERE idUser=?");

            //select request info user by login
            requests[5] = this.connection.prepareStatement("SELECT * FROM user WHERE loginUser=?");

            requests[6] = this.connection.prepareStatement("SELECT loginUser FROM user ");

            requests[7] = this.connection.prepareStatement("SELECT * FROM role");

            requests[8] = this.connection.prepareStatement("SELECT roleId FROM role WHERE roleName=?");

            requests[9] = this.connection.prepareStatement("UPDATE user SET loginUser = ?, pwdUser = ?, lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void insert(String login, String pwd, String lastName, String firstName, String mail, int phone, String company){
        try{
            PreparedStatement req = requests[0];

            req.setString(1, login);
            req.setString(2, pwd);
            req.setString(3, lastName);
            req.setString(4, firstName);
            req.setString(5, mail);
            req.setInt(6, phone);
            req.setString(7, company);

            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public int[] findUserProject(){

        //Retrieve the user of the current session
        int userId = Main.getMyUser().userId;
        int[] tab = null;
        int i = 0;
        int rowcount = 0;
        try{
            PreparedStatement req = requests[3];
            ResultSet res;

            req.setInt(1, userId);
            res = req.executeQuery();

            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            tab = new int[rowcount];

            while (res.next()) {
                tab[i] = res.getInt("idProject");
                i++;
            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return tab;
    }

    @sample.util.Connection
    @Override
    public User findConnection(String login, String pwd){
        User user =null;

        try{
            PreparedStatement req = requests[1];
            ResultSet res;

            req.setString(1, login);
            req.setString(2, pwd);
            res = req.executeQuery();

            if(res.first()){
                user = new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public User find(int id){
        User user = new User();

        try{
            PreparedStatement req = requests[4];
            ResultSet res;

            req.setInt(1, id);
            res = req.executeQuery();

            if(res.first()){
                user = new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser"));
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public int find(String name){
        int idUser = 0;
        try{
            PreparedStatement req = requests[5];
            ResultSet res;
            req.setString(1, name);
            res = req.executeQuery();

            if(res.first()){
                idUser = res.getInt("idUser");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return idUser;
    }

    /** SEARCH USERS NAME IN SHARIN **/
    public String[] findUsersName(){

        //Retrieve the user of the current session
        int userId = Main.getMyUser().userId;
        String[] tab = null;
        int i = 0;
        int rowcount = 0;
        try{
            PreparedStatement req = requests[6];
            ResultSet res;
            res = req.executeQuery();

            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            tab = new String[rowcount];

            while (res.next()) {
                tab[i] = res.getString("loginUser");
                i++;
            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return tab;
    }

    public String[] findRole(){

        String[] tab = null;
        int i = 0;
        int rowcount = 0;
        try{
            PreparedStatement req = requests[7];
            ResultSet res;
            res = req.executeQuery();

            if (res.last()) {
                rowcount = res.getRow();
                res.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            tab = new String[rowcount];

            while (res.next()) {
                tab[i] = res.getString("nameRole");
                i++;
            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return tab;
    }

    public int findRoleId(String roleName){
        int roleId = 0;
        try{
            PreparedStatement req = requests[6];
            ResultSet res;
            req.setString(1, roleName);
            res = req.executeQuery();

            if(res.first()){
                roleId = res.getInt("roleId");
            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return roleId;
    }

    @Override
    public boolean update(User user, String pwd){
        boolean result = false;

        try{
            PreparedStatement req = requests[9];

            req.setString(1, user.getUserLogin());
            req.setString(2, pwd);
            req.setString(3, user.getUserName());
            req.setString(4, user.getUserFirstName());
            req.setString(5, user.getUserMail());
            req.setInt(6, user.getUserPhone());
            req.setString(7, user.getUserCompany());
            req.setInt(8, user.getUserId());

            if(req.executeUpdate() == 1)
                result = true;


        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}