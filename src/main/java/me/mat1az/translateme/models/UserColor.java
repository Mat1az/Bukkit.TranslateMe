package me.mat1az.translateme.models;

import java.util.UUID;

public class UserColor {

    private UUID user;
    private int a, b, c;

    public UserColor() {
    }

    public UserColor(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public UserColor(UUID user, int a, int b, int c) {
        this.user = user;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "UserColor{" +
                "user=" + user +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
