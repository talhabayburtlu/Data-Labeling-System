package com.DataLabelingSystem.assignment;

import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

@JsonPropertyOrder({"instance id", "class label ids", "user id", "datetime"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabelAssignment {

    @JsonProperty(value = "instance id", access = JsonProperty.Access.WRITE_ONLY)
    private Instance instance = null;
    @JsonProperty(value = "class label ids", access = JsonProperty.Access.WRITE_ONLY)
    private ArrayList<Label> labels = null;
    @JsonProperty(value = "user id", access = JsonProperty.Access.WRITE_ONLY)
    private User user = null;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datetime = null;

    private Duration duration;

    protected LabelAssignment() {
    }

    protected LabelAssignment(User user, Instance instance, ArrayList<Label> labels, Duration duration) {
        this.user = user;
        this.instance = instance;
        this.labels = labels;
        this.datetime = new Date();
        this.duration = duration;
    }

    public Instance getInstance() {
        return instance;
    }

    public ArrayList<Label> getLabels() {
        return labels;
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

    @JsonGetter("instance id")
    public int getInstanceId() {
        return this.getInstance().getId();
    }

    @JsonGetter("class label ids")
    public ArrayList<Integer> getClassLabelIds() {
        ArrayList<Integer> classLabelIds = new ArrayList<>();

        for (Label label : getLabels()) {
            classLabelIds.add(label.getId());
        }

        return classLabelIds;
    }

    @JsonGetter("user id")
    public int getUserId() {
        return this.getUser().getId();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
