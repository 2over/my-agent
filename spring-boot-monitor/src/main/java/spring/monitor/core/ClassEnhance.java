package spring.monitor.core;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassEnhance {

    private ClassLoader loader;
    private String className;

    private ClassPool classPool = ClassPool.getDefault();
    private CtClass ctClass;

    public ClassEnhance(ClassLoader loader, String className) {
        this.loader = loader;
        this.className = className;

        try {
            ctClass = classPool.get(className);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // 增强方法,加入监控逻辑
        enhanceMethods();
        System.out.println("开始执行 enhanceMethods...........");
    }

    public void enhanceMethods() {
        CtMethod[] methods = ctClass.getMethods();
        for (CtMethod method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) continue;
            if (Modifier.isNative(method.getModifiers())) continue;
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (Modifier.isAbstract(method.getModifiers())) continue;

            // 将Object类中的几个去掉
            List<String> objectsMethods = new ArrayList<String>();
            objectsMethods.add("equals");
            objectsMethods.add("toString");
            objectsMethods.add("wait");

            if (objectsMethods.contains(method.getName())) continue;
            // 增强
            new MethodEnhance(method, ctClass);
        }
    }

    public byte[] toBytes() {
        try {
            ctClass.writeFile();

            // 一定
            return ctClass.toBytecode();
        } catch (NotFoundException | IOException | CannotCompileException e) {
           e.printStackTrace();
        }

        return null;
    }


}
