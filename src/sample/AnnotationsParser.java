package sample;

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
            String userLogin = user.getUserLogin();

            if(!"".equals(userLogin)){
                FileWriter fw = new FileWriter("connections.log", true);
                SimpleDateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
                Date date = new Date();

                fw.write(dateFormat.format(date) + " " + userLogin + " s'est connecté...\n");
                fw.close();
                return user;
            }
        }

        return method.invoke(this.obj, args);
    }
}