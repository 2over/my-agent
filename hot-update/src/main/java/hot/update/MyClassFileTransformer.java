package hot.update;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyClassFileTransformer implements ClassFileTransformer {

    private String getReplaceClassName;

    private String replaceClassPath;

    public MyClassFileTransformer(String getReplaceClassName, String replaceClassPath) {
        this.getReplaceClassName = getReplaceClassName.replace('.', '/');
        this.replaceClassPath = replaceClassPath;
    }

    private byte[] getData(String filePath) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        File file = new File(filePath);
        if (!file.exists()) return null;

        try {
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();

            int size = 0;
            byte[] buffer = new byte[1024];

            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return new byte[0];
    }
}
