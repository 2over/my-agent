package service.impl;

import service.IMath;

public class MyMath implements IMath {
    @Override
    public int div(int x, int y) {
        return x / y;
    }

    @Override
    public int add(int x, int y) {
        return x + y;
    }
}
