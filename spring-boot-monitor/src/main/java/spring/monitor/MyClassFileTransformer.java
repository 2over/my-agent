package spring.monitor;

import spring.monitor.core.ClassEnhance;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        // 第一件事情一定要干这个
        className = className.replace('/', '.');
        System.out.println(className);

        String packageName = "spring.demo";

        // 过滤: 不需要监控项目的类直接pass
        if (!className.startsWith(packageName)) {
            return classfileBuffer;
        }

        // 处理controller
        if (className.startsWith(packageName + ".controller")) {
            return new ClassEnhance(loader, className).toBytes();
        }
        return classfileBuffer;
    }
}
