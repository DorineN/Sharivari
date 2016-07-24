package app.model;

import app.util.AnnotationsParser;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ProjectDAOFactory {
    private ProjectDAOFactory(){}

    public static ProjectDAOInterface newInstance(Connection connection){
        return (ProjectDAOInterface) Proxy.newProxyInstance(AnnotationsParser.class.getClassLoader(),
                new Class[]{ProjectDAOInterface.class},
                new AnnotationsParser(new ProjectDAO(connection)));
    }
}
