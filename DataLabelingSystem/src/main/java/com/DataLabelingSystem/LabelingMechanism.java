package com.DataLabelingSystem;

public abstract class LabelingMechanism {

    private String name;

    public LabelingMechanism(String name) {
        this.name = name;
    }

    public abstract void label(User user, Instance instance, Label[] label);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
