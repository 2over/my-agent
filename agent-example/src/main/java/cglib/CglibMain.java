package cglib;


import net.sf.cglib.core.DebuggingClassWriter;
import service.impl.MyMath;

public class CglibMain {

    public static void main(String[] args) {
        // 这里需要填你的项目路径
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\aa-my\\my-agent");

        MyMath o = (MyMath)new CglibInvocationHandler(new MyMath()).getProxy();
        System.out.println(o.div(4, 2));
    }
}
