package sample;

public class User {
    protected int userId = 0;
    protected String userLogin = "";
    protected String userName = "";
    protected String userFirstName = "";
    protected String userMail = "";
    protected int userPhone = 0;
    protected String userCompany = "";
    protected String userType = "";

    public User(){
        // Empty constructor
    }

    public User(int userId, String userLogin, String userName, String userFirstName, String userMail, int userPhone, String userCompany, String userType){
        this.setUserId(userId);
        this.setUserLogin(userLogin);
        this.setUserName(userName);
        this.setUserFirstName(userFirstName);
        this.setUserMail(userMail);
        this.setUserType(userType);
        this.setUserPhone(userPhone);
        this.setUserCompany(userCompany);
    }


    // Getters & Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public int getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(int userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", userName='" + userName + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userPhone=" + userPhone +
                ", userCompany='" + userCompany + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    // Inherited methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        if (userId != user.userId)
            return false;
        if (userLogin != null ? !userLogin.equals(user.userLogin) : user.userLogin != null)
            return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null)
            return false;
        if (userFirstName != null ? !userFirstName.equals(user.userFirstName) : user.userFirstName != null)
            return false;
        if (userMail != null ? !userMail.equals(user.userMail) : user.userMail != null)
            return false;
        return !(userType != null ? !userType.equals(user.userType) : user.userType != null);

    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userFirstName != null ? userFirstName.hashCode() : 0);
        result = 31 * result + (userMail != null ? userMail.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        return result;
    }
}
