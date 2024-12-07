package javassit;

import javassist.*;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class Test2 {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(Sample.class.getName());

        System.out.println(ctClass.getDeclaredMethods().length);
        System.out.println(ctClass.getMethods().length);

        // 修改方法:打印参数信息
        // 基本类型的参数
        CtMethod method = ctClass.getDeclaredMethod("add");
        // 引用类型的参数
        CtMethod method1 = ctClass.getDeclaredMethod("concat");

        System.out.println(method);
        System.out.println(method1);

        // 根据方法名和方法描述符获取
        CtMethod method2 = ctClass.getMethod("add", "(II)I");
        // 判断是否是静态方法
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        // 如果是非静态方法，第一个参数是this指针，但是这里返回的结果是不包含this指针的
        CtClass[] parameterTypes = method2.getParameterTypes();
        System.out.println(parameterTypes.length);

        MethodInfo methodInfo = method2.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        // 这里需要区分是否是静态方法。非静态方法第一个参数是this指针
        int step = isStatic ? 0 : 1;
        for (int i = 0 ; i < parameterTypes.length + step; i++) {
            System.out.println(attribute.variableName(i));
        }

        // 当我们使用自己定义的局部变量时会报错，代码如下
        // 插入语句
        try {
            method2.insertBefore("System.out.println(\"加法运算开始\");\n");
            method2.insertAfter("System.out.println(\"加法运算结束\");\n");

        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }

        // =========================关键代码=========================
        // 加上会报错
//        method2.insertBefore("long start = System.currentTimeMillis();");
//        method2.insertAfter("long end = System.currentTimeMillis();");
//        method2.insertAfter("long time = end - start;");

        // 统计方法的执行时长
        CtMethod method2New = CtNewMethod.copy(method2, ctClass, null);
        // 这里需要设置名字，因为无法存在一个同名同参的方法
        method2New.setName(method2.getName() + "$enhanced");
        ctClass.addMethod(method2New);


        // 再尝试修改原方法的代码
        // $$ 表示所有参数
        StringBuffer buffer = new StringBuffer();
        buffer.append("try { int a = 10;");
        buffer.append("} catch (Exception e) {");
        buffer.append("} finally {");
        buffer.append("return " + method2.getName() + "($$);");

        buffer.append("}");

        method2New.setBody(buffer.toString());

        ctClass.writeFile();

    }
}
