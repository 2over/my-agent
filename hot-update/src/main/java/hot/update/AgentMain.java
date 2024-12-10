package hot.update;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.HashMap;
import java.util.Map;

/**
 * java -jar hot-update-1.0-SNAPSHOT. jar pid replaceClassName
 */
public class AgentMain {

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("======= 开始热更新 =======");

        Map<String, String> args = parseArgs(agentArgs);
        String replaceClassName = args.get("replaceClassName");

        try {
            Class<?> replaceClass = Class.forName(replaceClassName);
            String replaceClassPath = replaceClass.getProtectionDomain().getCodeSource().getLocation().getPath();
            replaceClassPath += replaceClassName.replace('.', '/') + ".class";

            System.out.println("\t 替换文件: " + replaceClassName);
            System.out.println("\t 文件路径: " + replaceClassPath);

            inst.addTransformer(new MyClassFileTransformer(replaceClassName, replaceClassPath), true);

            inst.retransformClasses(replaceClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnmodifiableClassException e) {
            throw new RuntimeException(e);
        }

        System.out.println("======= 完成热更新 =======");

    }

    private static Map<String, String> parseArgs(String args) {
        Map<String, String> ret = new HashMap<>();

        String[] argsArr = args.split(";");
        for (String arg : argsArr) {
            String[] strings = arg.split("=");
            ret.put(strings[0], strings[1]);
        }

        return ret;
    }
}
