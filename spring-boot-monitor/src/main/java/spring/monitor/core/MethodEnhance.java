package spring.monitor.core;

import javassist.*;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.ArrayList;
import java.util.List;

public class MethodEnhance {

    private CtClass ctClass;
    private CtMethod ctMethod;

    private String methodName;
    private String methodDescriptor;

    private String enhanceMethodName;

    private int argsSize;
    private List<String> argsName = new ArrayList<>();
    private CtClass returnType;

    private ClassPool classPool = ClassPool.getDefault();

    public String getMethodName() {
        return ctMethod.getLongName();
    }

    public MethodEnhance(CtMethod ctMethod, CtClass ctClass) {
        this.ctMethod = ctMethod;
        this.ctClass = ctClass;
        System.out.println("==== 开始增强方法 " + ctMethod.getName() + " =====");
        parse();
        make();
    }

    /**
     * 获取方法的相关信息
     * 方法名
     * 方法签名
     * 参数: 个数 类型 参数名
     */
    private void parse() {
        methodName = ctMethod.getName();
        methodDescriptor = ctMethod.getSignature();
        enhanceMethodName = methodName + "$enhanced";

        try {
            returnType = ctMethod.getReturnType();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // 获取参数名称
        parseArgs();

    }

    private void parseArgs() {
        try {
            CtClass[] parameterTypes = ctMethod.getParameterTypes();
            // 注意: 这个是不包含this指针的参数个数
            argsSize = parameterTypes.length;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // 获取参数名称
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        LocalVariableAttribute localVariableAttribute =
                (LocalVariableAttribute) methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);

        // index = 0 的位置是this指针
        for (int i = 1; i < argsSize; i++) {
            argsName.add(localVariableAttribute.variableName(i));
        }
    }

    private void make() {
        String bodyTemplate = "{" +
                "long start = System.currentTimeMillis();" +
                "long end = 0;" +
                "Object ret = null;" +
                "try {" +
                "       ret = ($w)" + enhanceMethodName + "($$);" +
                "} catch (Exception) {" +
                "   spring.monitor.core.MonitorPrint.print(\"" + ctMethod.getLongName() + "\", e);" +
                "} finally {" +
                "   end = System.currentTimeMillis();" +
                "} " +
                "   spring.monitor.core.MonitorPrint.print(\"" + ctMethod.getLongName() + "\", start, end, ret, $args);" +
                " return ($r)ret;" +
                "} ";

        String voidTemplate = "{" +
                " long start = System.currentTimeMillis();" +
                " long end = 0;" +
                " try {" +
                "   " + enhanceMethodName + "($$);" +
                "} catch (Exception e) { " +
                "   spring.monitor.core.MonitorPrint.print(\"" + ctMethod.getLongName() + "\", e);" +
                "} finally {" +
                "   end = System.currentTimeMillis();" +
                "} " +
                " spring.monitor.core.MonitorPrint.print(\"" + ctMethod.getLongName() + "\", start, end, null, $args);" +
                "}";

        try {
            CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, null);
            newMethod.setName(enhanceMethodName);

            ctClass.addMethod(newMethod);

            // 原方法调用增强方法
            if (returnType.getName().equals("void")) {
                ctMethod.setBody(voidTemplate);
            } else {
                ctMethod.setBody(bodyTemplate);
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

    }


}
