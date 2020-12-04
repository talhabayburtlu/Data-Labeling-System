package com.DataLabelingSystem;

import java.util.Date;

public class LabelAssignment {
    private Instance instance;
    private Label[] labels;
    private User user;
    private Date datetime;

    protected LabelAssignment(User user, Instance instance, Label[] labels) {
        this.user = user;
        this.instance = instance;
        this.labels = labels;
        this.datetime = new Date();
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