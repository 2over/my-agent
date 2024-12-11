package spring.demo.controller;


/**
 * 添加VM Options
 * -javaagent:D:\aa-ziya\ziya-agent-teach\sprint-boot-monitor\target\sprint-boot-monitor-1.0-SNAPSHOT.jar
 */
public class IndexController {

    public String index(int val) {
        return "index";
    }

    public int add(int a, int b) {
        return a + b;
    }

    public String concat(String s, int b) {
        return s + b;
    }

    public int show() {
        return 10;
    }

    public void test() {
        System.out.println("test");
    }

    public static void main(String[] args) {
        IndexController indexController = new IndexController();
        indexController.index(1);
        indexController.add(1, 2);
        indexController.concat("cover", 666);

    }
}
