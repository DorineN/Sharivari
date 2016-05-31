package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO extends DAO<User> {

    public UserDAO(Connection connection){
        super(connection);
    }



    public void insert(String login, String pwd, String lastName, String firstName, String mail, int phone, String company){

        try{

            PreparedStatement prepare = connection.prepareStatement("INSERT INTO user(loginUser, pwdUser, lastNameUser, firstNameUser, mailUser, phoneUser, companyUser, typeUser) VALUES(" +
                    "?, ?, ?, ?, ?, ?, ?,'user') ");


            prepare.setString(1, login);
            prepare.setString(2, pwd);
            prepare.setString(3, lastName);
            prepare.setString(4, firstName);
            prepare.setString(5, mail);
            prepare.setInt(6, phone);
            prepare.setString(7, company);
            prepare.executeUpdate();

            prepare.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public User findConnection(String login, String pwd){
        User user = null;

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM user WHERE loginUser=? AND pwdUser=? ");
            ResultSet res;

            prepare.setString(1, login);
            prepare.setString(2, pwd);
            res = prepare.executeQuery();

            if(res.first()){
                user = new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }


    @Override
    public User find(int id){
        User user = null;

        try{
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM user WHERE idUser=?");
            ResultSet res;

            prepare.setInt(1, id);
            res = prepare.executeQuery();

            if(res.first()){
                user = new User(res.getInt("idUser"), res.getString("loginUser"), res.getString("lastNameUser"),
                        res.getString("firstNameUser"), res.getString("mailUser"), res.getInt("phoneUser"), res.getString("companyUser"),
                        res.getString("typeUser"));
            }

            prepare.close();
            res.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }




    @Override
    public boolean update(User user){
        boolean result = false;

        try{
            String query = "UPDATE user SET loginUser = ?, lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setString(1, user.getUserLogin());
            prepare.setString(2, user.getUserName());
            prepare.setString(3, user.getUserFirstName());
            prepare.setString(4, user.getUserMail());
            prepare.setInt(5, user.getUserPhone());
            prepare.setString(6, user.getUserCompany());
            prepare.setInt(7, user.getUserId());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public int[] findUserProject(){

        //Retrieve the user of the current session
        int userId = Main.getMyUser().userId;
        int[] tab = null;
        int i = 0;
        int rowcount = 0;
        try{
            System.out.println(userId);
            PreparedStatement prepare = connection.prepareStatement("SELECT * FROM participate WHERE idUser=? ");
            ResultSet res;

            prepare.setInt(1, userId);
            res = prepare.executeQuery();

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
            prepare.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return tab;
    }
}
