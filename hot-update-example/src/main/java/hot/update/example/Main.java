package hot.update.example;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        pid();

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Hello().say();
        }
    }

    private static void pid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name.split("@")[0]);
    }
}
