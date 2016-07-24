package app.model;

import app.util.AnnotationsParser;

import java.lang.reflect.Proxy;
import java.sql.Connection;

public class TaskDAOFactory {
    private TaskDAOFactory(){}

    public static TaskDAOInterface newInstance(Connection connection){
        return (TaskDAOInterface) Proxy.newProxyInstance(AnnotationsParser.class.getClassLoader(),
                new Class[]{TaskDAOInterface.class},
                new AnnotationsParser(new TaskDAO(connection)));
    }
}
