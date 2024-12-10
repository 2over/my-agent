package hot.update;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class AttachMain {

    public static void main(String[] args) {
        // 这里需要填入Java进程的pid
        String pid = "25284";
        String replaceClassName = "hot.update.example.Hello";

        VirtualMachine virtualMachine = null;

        // 这里要调整路径
        String jarPath = "D:\\aa-my\\my-agent\\hot-update\\target\\hot-update-1.0-SNAPSHOT.jar";

        StringBuffer agentArgs = new StringBuffer();
        agentArgs.append("replaceClassName=" + replaceClassName + ";");

        try {
            virtualMachine = VirtualMachine.attach(pid);
            virtualMachine.loadAgent(jarPath, agentArgs.toString());
        } catch (AttachNotSupportedException | IOException | AgentInitializationException | AgentLoadException e) {
            e.printStackTrace();
        } finally {
            try {
                virtualMachine.detach();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
