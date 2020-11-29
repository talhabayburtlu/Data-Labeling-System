package com.DataLabelingSystem;

import java.util.Date;

public class LabelAssignment {
    private Instance instance;
    private Label[] labels;
    private User user;
    private Date datetime;

    LabelAssignment(Instance instance, Label[] labels, User user) {
        this.instance = instance;
        this.labels = labels;
        this.user = user;

        addToDataset();
    }


    private void addToDataset(){
        Dataset dataset = this.instance.getDataset(); //get dataset from instance
        dataset.getLabelAssignment().add(this);

    }


    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
