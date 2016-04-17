package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO extends DAO<User> {

    public UserDAO(Connection connection){
        super(connection);
    }

    @Override
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
}
