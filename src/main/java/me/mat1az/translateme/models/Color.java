package me.mat1az.translateme.models;

public class Color {

    private int ID, level;
    private String value;

    public Color() {
    }

    public Color(int ID, int level, String value) {
        this.ID = ID;
        this.level = level;
        this.value = value;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Color{" +
                "ID=" + ID +
                ", level=" + level +
                ", value='" + value + '\'' +
                '}';
    }
}
