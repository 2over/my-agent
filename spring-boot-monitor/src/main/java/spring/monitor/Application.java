package spring.monitor;

import java.lang.instrument.Instrumentation;

public class Application {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("spring boot monitor start...");

        inst.addTransformer(new MyClassFileTransformer());
    }
}
