package me.mat1az.translateme.models;

public class ReplaceHolder {

    private int placeholder;
    private String replace;

    private ReplaceHolder(int placeholder, String replace) {
        this.placeholder = placeholder;
        this.replace = replace;
    }

    public static ReplaceHolder of(int placeholder, String replace) {
        return new ReplaceHolder(placeholder, replace);
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
