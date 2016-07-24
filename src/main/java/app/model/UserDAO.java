package app.model;

import app.Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserDAO extends DAO<User> implements UserDAOInterface {
    private PreparedStatement[] requests = new PreparedStatement[21];

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
            requests[3] = this.connection.prepareStatement("SELECT * FROM participate, project WHERE project.idProject = participate.idProject AND idUser=? ORDER BY nameProject");

            //select request info user by id
            requests[4] = this.connection.prepareStatement("SELECT * FROM user WHERE idUser=?");

            //select request info user by login
            requests[5] = this.connection.prepareStatement("SELECT * FROM user WHERE loginUser=?");

            requests[6] = this.connection.prepareStatement("SELECT loginUser FROM user ");

            requests[7] = this.connection.prepareStatement("SELECT * FROM role");

            requests[8] = this.connection.prepareStatement("SELECT idRole FROM role WHERE nameRole=?");

            requests[9] = this.connection.prepareStatement("UPDATE user SET lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?");

            requests[10] = this.connection.prepareStatement("SELECT loginUser FROM user WHERE idUser=?");

            requests[11] = this.connection.prepareStatement("SELECT * FROM user WHERE idUser<>?");

            requests[12] = this.connection.prepareStatement("UPDATE user SET pwdUser = ?  WHERE idUser=?");

            requests[13] = this.connection.prepareStatement("DELETE FROM user WHERE idUser=?");

            requests[14] = this.connection.prepareStatement("UPDATE user SET typeUser = ?  WHERE idUser=?");

            requests[15] = this.connection.prepareStatement("UPDATE user SET pwdUser = ? WHERE idUser = ?");

            requests[16] = this.connection.prepareStatement("SELECT postIt FROM participate WHERE idUser=? AND idProject=?");

            requests[17] = this.connection.prepareStatement("UPDATE participate SET postIt = ? WHERE idUser=? AND idProject=?");

            requests[18] = this.connection.prepareStatement("SELECT nameRole FROM role WHERE idRole=?");

            requests[19] = this.connection.prepareStatement("DELETE FROM execute  WHERE idUser=?");

            requests[20] = this.connection.prepareStatement("DELETE FROM participate WHERE idUser=?");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteParticipate(int id){
        try{
            PreparedStatement req = requests[20];

            req.setInt(1, id);
            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteExecute(int id){
        try{
            PreparedStatement req = requests[19];

            req.setInt(1, id);
            req.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void resetPassword(int id){
        MessageDigest md = null;
        try {
            String pwd = "p@ssw0rd";
            md = MessageDigest.getInstance("SHA-256");
            md.update(pwd.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            PreparedStatement req = requests[12];

            try {
                req.setString(1, sb.toString());
                req.setInt(2, id);
                req.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }




        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }




    }

    public void deleteAccount(int id){
        PreparedStatement req = requests[13];

        try {
            req.setInt(1, id);
            req.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> findAllUserExeptOne(int id){
        ArrayList<User> lUser =  new ArrayList();
        try{
            PreparedStatement req = requests[11];
            ResultSet res;

            req.setInt(1, id);
            res = req.executeQuery();

            int i = 0;
            while(res.next()){

                lUser.add(new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser")));

            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return lUser;
    }



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

    public int[] findUserProject(int userId){

        //Retrieve the user of the current session

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

    @app.annotations.Connection
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


    public boolean update(User user, String pwd) {
        return false;
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
            PreparedStatement req = requests[8];
            ResultSet res;
            req.setString(1, roleName);
            res = req.executeQuery();

            if(res.first()){
                roleId = res.getInt("idRole");
            }

            res.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return roleId;
    }


    public boolean updateUser(User user){
        boolean result = false;

        try{
            PreparedStatement req = requests[9];


            req.setString(1, user.getUserName());
            req.setString(2, user.getUserFirstName());
            req.setString(3, user.getUserMail());
            req.setInt(4, user.getUserPhone());
            req.setString(5, user.getUserCompany());
            req.setInt(6, user.getUserId());

            if(req.executeUpdate() == 1)
                result = true;


        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public boolean updatePwd(String pwd, int id){
        boolean result = false;

        try{

            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-256");
            md.update(pwd.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            PreparedStatement req = requests[15];


            req.setString(1,sb.toString());
            req.setInt(2, id);

            if(req.executeUpdate() == 1)
                result = true;


        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public String findLoginUser(int idUser){
        String nameUser = "";
        try{
            PreparedStatement req = requests[10];
            ResultSet res;
            req.setInt(1, idUser);
            res = req.executeQuery();

            if(res.first()){
                nameUser = res.getString("loginUser");
            }


            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return nameUser;
    }

    public void updateTypeAccount(int id, String type) {


        PreparedStatement req = requests[14];

        try {
            req.setString(1, type);
            req.setInt(2, id);
            req.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public String findPostItUser(int idUser, int idProject) {
        String postit = "";

        try {
            PreparedStatement prepare = requests[16];
            ResultSet res;
            prepare.setInt(1, idUser);
            prepare.setInt(2, idProject);
            res = prepare.executeQuery();

            if(res.first()){
                postit = res.getString("postIt");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return postit;
    }

    public String updatePostitUser(String postit, int idUser, int idProject) {

        try {
            PreparedStatement prepare = requests[17];
            prepare.setString(1, postit);
            prepare.setInt(2, idUser);
            prepare.setInt(3, idProject);

            prepare.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }

        return postit;
    }
    public String findRoleName(int idRole){
        String roleName = "";
        try{
            PreparedStatement prepare = requests[18];
            ResultSet res;
            prepare.setInt(1, idRole);
            res = prepare.executeQuery();

            if(res.first()){
                roleName = res.getString("nameRole");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return roleName;
    }
}