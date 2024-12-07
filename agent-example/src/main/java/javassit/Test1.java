package javassit;

import com.sun.tools.jdi.VMModifiers;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Date;

public class Test1 {

    public static void main(String[] args) throws CannotCompileException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        // 创建一个类
        CtClass ctClass = classPool.makeClass("com.cover.javassit.Demo1");
        classPool.importPackage(Date.class.getName());

        // 设置属性(两种方式)
        // 方式一 基本类型
        CtField field1 = CtField.make("private int i;", ctClass);
        ctClass.addField(field1);

        // 引用类型
        CtField field2 = CtField.make("private String s;", ctClass);
        ctClass.addField(field2);

        // 方式二 通过创建对象的方式创建
        // 引用类型
        CtField field3 = new CtField(classPool.get(String.class.getName()), "name", ctClass);
        field3.setModifiers(Modifier.PUBLIC);
        ctClass.addField(field3);

        // 基本类型
        CtField field4 = new CtField(CtClass.intType, "age", ctClass);
        ctClass.addField(field4);

        // 构造main方法的形参:String 数组
        CtClass stringArrClass = classPool.get(String[].class.getName());
        CtClass[] stringArr = {stringArrClass};

        // 创建方法(三种方式)
        // 方式一  创建对象的方式
        CtMethod method = new CtMethod(CtClass.voidType, "main", stringArr, ctClass);
        // 设置权限
        method.setModifiers(VMModifiers.PUBLIC + Modifier.STATIC);
        method.setBody("{System.out.println(\"Cover\"); Date d = new Date();}");
        ctClass.addMethod(method);

        // 方式二
        CtMethod method1 = CtMethod.make(
                "public int hello() {" +
                        "return 10;" +
                        "}", ctClass);

        ctClass.addMethod(method1);

        // 方式三
        CtMethod newMethod1 = CtNewMethod.setter("setS", field2);
        ctClass.addMethod(newMethod1);

        // 创建构造方法
        // 注意: 添加了构造方法，默认的就没了
        CtConstructor constructor = new CtConstructor(new CtClass[]{CtClass.intType,
                classPool.get(String.class.getName())}, ctClass);

        // 如果赋值的属性不存在，会报错
        constructor.setBody("{$0.age=$1;$0.name=$2;}");
        ctClass.addConstructor(constructor);

        CtConstructor constructor1 = CtNewConstructor.make("public Demo(){}", ctClass);
        ctClass.addConstructor(constructor1);

        try {
            ctClass.writeFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
