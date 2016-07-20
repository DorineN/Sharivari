package app.model;

import app.util.AnnotationsParser;
import app.util.UserDAOInterface;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class UserDAOFactory {
    private UserDAOFactory(){}

    public static UserDAOInterface newInstance(Connection connection){
        return (UserDAOInterface) Proxy.newProxyInstance(AnnotationsParser.class.getClassLoader(),
                new Class[]{UserDAOInterface.class},
                new AnnotationsParser(new UserDAO(connection)));
    }
}