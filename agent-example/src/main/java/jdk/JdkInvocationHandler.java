package jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkInvocationHandler implements InvocationHandler {

    private Object target;

    public JdkInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;

        try {
            System.out.println("前置通知");

            ret = method.invoke(target, args);
            System.out.println("返回通知");
        } catch (Exception e) {
            System.out.println("异常通知");
        } finally {
            System.out.println("后置通知");
        }

        return ret;
    }
}
