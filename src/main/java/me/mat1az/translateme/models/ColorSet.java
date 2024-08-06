package me.mat1az.translateme.models;

public class ColorSet {

    private int ID;
    private String name;
    private Color a,b;

    public ColorSet(int ID, String name, Color a, Color b) {
        this.ID = ID;
        this.name = name;
        this.a = a;
        this.b = b;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getA() {
        return a;
    }

    public void setA(Color a) {
        this.a = a;
    }

    public Color getB() {
        return b;
    }

    public void setB(Color b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "ColorSet{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}
