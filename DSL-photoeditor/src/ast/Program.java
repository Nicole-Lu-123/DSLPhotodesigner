package ast;

import libs.Node;

import java.io.IOException;

public class Program extends Node {
    // PROGRAM :: = CREATE RUN
    private Create create;
    private Run run;

    public Program(Create create, Run run) {
        this.create = create;
        this.run = run;
    }

    public Create getCreate() {
        return create;
    }

    public void setCreate(Create create) {
        this.create = create;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}

