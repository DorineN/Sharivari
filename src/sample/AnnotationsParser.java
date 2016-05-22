package sample;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AnnotationsParser implements InvocationHandler{
    private Object obj;

    public AnnotationsParser(Object obj){
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        System.out.println("testing....");

        return method.invoke(this.obj, args);
    }
}
