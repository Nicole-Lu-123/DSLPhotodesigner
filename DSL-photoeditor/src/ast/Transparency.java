package ast;

import java.io.IOException;

public class Transparency extends Function{
    // TRANSPARENCY:: = "transparency" ":" NUM
    private Integer number;

    public Transparency(String number) {
        super();
        this.number = Integer.parseInt(number);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
