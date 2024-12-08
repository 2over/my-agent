package javassit;

import service.impl.MyMath;

public class JavassistMain {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        MyMath o = (MyMath)new JavassistMethodHandler().getProxy(MyMath.class);

        System.out.println(o.div(4, 2));
    }
}
