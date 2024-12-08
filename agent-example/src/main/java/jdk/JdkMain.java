package jdk;

import service.IMath;
import service.impl.MyMath;

import java.lang.reflect.Proxy;


public class JdkMain {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");


        JdkInvocationHandler invocationHandler = new JdkInvocationHandler(new MyMath());
        IMath o = (IMath) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{IMath.class}, invocationHandler);
        System.out.println(o.div(2,2));
    }
}
