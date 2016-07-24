package app.util;

import app.Main;
import app.annotations.*;
import app.model.*;

import java.io.FileWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnnotationsParser implements InvocationHandler{
    private Object obj;

    public AnnotationsParser(Object obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        Method realMethod = this.obj.getClass().getMethod(method.getName(), method.getParameterTypes());

        if(realMethod.isAnnotationPresent(Connection.class) && realMethod.getReturnType() == User.class) {
            User user = (User) realMethod.invoke(this.obj, args);
            String userLogin = "";

            if(user != null)
                userLogin = user.getUserLogin();

            if(!"".equals(userLogin)){
                FileWriter fw = new FileWriter("annotations.log", true);
                SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
                Date date = new Date();

                fw.write(dateFormat.format(date) + " " + userLogin + " s'est connecté\n");
                fw.close();
                return user;
            }
        }

        if(realMethod.isAnnotationPresent(CreateProject.class) && realMethod.getReturnType() == int.class){
            int idProjet = (int) realMethod.invoke(this.obj, args);
            String user = Main.getMyUser().getUserLogin();
            String project = (String) args[0];

            FileWriter fw = new FileWriter("annotations.log", true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
            Date date = new Date();

            fw.write(dateFormat.format(date) + " " + user + " a créé un nouveau projet appelé " + project + "\n");
            fw.close();

            return idProjet;
        }

        if(realMethod.isAnnotationPresent(CreateTask.class) && realMethod.getReturnType() == int.class){
            int idTask = (int) realMethod.invoke(this.obj, args);

            if(idTask != 0) {
                String user = Main.getMyUser().getUserLogin();
                String project = Main.getMyProject().getProjectName();
                String task = (String) args[0];

                FileWriter fw = new FileWriter("annotations.log", true);
                SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
                Date date = new Date();

                fw.write(dateFormat.format(date) + " " + user + " a créé une nouvelle tâche appelée " + task + " dans le projet " + project + "\n");
                fw.close();
            }
        }

        if(realMethod.isAnnotationPresent(JoinProject.class) && realMethod.getReturnType() == void.class){
            int id = (int) args[0];
            String user = Main.getMyUser().getUserLogin();
            String project = Main.getMyProject().getProjectName();

            User userWhoJoined = new UserDAO(new MySQLConnexion().getConnexion()).find(id);

            FileWriter fw = new FileWriter("annotations.log", true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
            Date date = new Date();

            fw.write(dateFormat.format(date) + " " + user + " a ajouté " + userWhoJoined.getUserLogin() + " au projet " + project + "\n");
            fw.close();
        }

        if(realMethod.isAnnotationPresent(RemoveTask.class) && realMethod.getReturnType() == boolean.class){
            int id = (int) args[0];
            String user = Main.getMyUser().getUserLogin();
            String project = Main.getMyProject().getProjectName();

            Task taskDeleted = new TaskDAO(new MySQLConnexion().getConnexion()).find(id);

            FileWriter fw = new FileWriter("annotations.log", true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
            Date date = new Date();

            fw.write(dateFormat.format(date) + " " + user + " a supprimé la tâche " + taskDeleted + " du projet " + project + "\n");
            fw.close();
        }

        return method.invoke(this.obj, args);
    }
}