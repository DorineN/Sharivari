package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO<User> implements UserDAOInterface{
    private PreparedStatement[] requests = new PreparedStatement[3];

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
            req.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public User findConnection(String login, String pwd){
        User user = new User();

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

    public User find(int id){
        User user = new User();

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
    public boolean update(User user, String pwd){
        boolean result = false;

        try{
            String query = "UPDATE user SET loginUser = ?, pwdUser = ?, lastNameUser = ?, firstNameUser = ?, mailUser = ?, phoneUser = ?, companyUser = ? WHERE idUser = ?";
            PreparedStatement prepare = connection.prepareStatement(query);

            prepare.setString(1, user.getUserLogin());
            prepare.setString(2, pwd);
            prepare.setString(3, user.getUserName());
            prepare.setString(4, user.getUserFirstName());
            prepare.setString(5, user.getUserMail());
            prepare.setInt(6, user.getUserPhone());
            prepare.setString(7, user.getUserCompany());
            prepare.setInt(8, user.getUserId());

            if(prepare.executeUpdate() == 1)
                result = true;

            prepare.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
