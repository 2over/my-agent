package javassit;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

public class JavassistMethodHandler implements MethodHandler {

    public Object getProxy(Class target) throws InstantiationException, IllegalAccessException {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(target);
        proxyFactory.writeDirectory = System.getProperty("user.dir");

        Class<?> clazz = proxyFactory.createClass();
        ProxyObject proxy = (ProxyObject) clazz.newInstance();
        proxy.setHandler(this);
        return proxy;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {

        Object ret = null;
        System.out.println(thisMethod);
        System.out.println(proceed);

        try {
            System.out.println("前置通知");
            ret = proceed.invoke(self, args);

            System.out.println("返回通知");
        } catch (Exception e) {
            System.out.println("异常通知");
        } finally {
            System.out.println("后置通知");
        }

        return ret;
    }
}
