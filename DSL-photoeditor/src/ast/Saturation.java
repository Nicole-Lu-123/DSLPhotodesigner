package ast;

import java.io.IOException;

public class Saturation extends Function {
    private String sign;
    private int num;

    public Saturation(String str) {
        String sign = str.substring(0,1); // sign = "+" or "-"
        this.sign = sign;
        int num = Integer.parseInt(str.substring(1)); // convert the "20" to int 20
        this.num = num;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
