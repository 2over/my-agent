package javassit;

public class Sample {

    private int add(int a, int b) {
        a += a;
        return a + b;
    }

    private String concat(String s1, String s2) {
        return s1 + s2;
    }
}
