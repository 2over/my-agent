package cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibInvocationHandler implements MethodInterceptor {

    private Object target;

    public CglibInvocationHandler(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        Enhancer enhancer = new Enhancer();

        // 这句代码不加会报错
        enhancer.setSuperclass(target.getClass());

        enhancer.setCallback(this);

        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Object ret = null;
        try {
            System.out.println("前置通知");
            ret = method.invoke(target, objects);

            System.out.println("返回通知");
        } catch (Exception e) {
            System.out.println("异常通知");
        } finally {
            System.out.println("后置通知");
        }

        return ret;
    }
}
