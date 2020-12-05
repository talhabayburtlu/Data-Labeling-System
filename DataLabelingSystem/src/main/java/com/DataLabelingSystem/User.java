package com.DataLabelingSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("user id")
    private int id;
    @JsonProperty("user name")
    private String name;
    @JsonProperty("user type")
    private String type;
    @JsonIgnore
    private LabelingMechanism mechanism;

    @JsonCreator
    User(@JsonProperty("user id") int id,
         @JsonProperty("user name") String name,
         @JsonProperty("user type") String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        //noinspection SwitchStatementWithTooFewBranches
        switch (type) {
            case "RandomBot":
                this.mechanism = new RandomLabelingMechanism("RandomLabelingMechanism");
        }
    }

    User(int id, String type, LabelingMechanism mechanism) {
        this.id = id;
        this.type = type;
        this.mechanism = mechanism;
    }

    public void labelWithMechanism(Instance instance, Label[] labels) {
        mechanism.label(this, instance, labels);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LabelingMechanism getMechanism() {
        return mechanism;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMechanism(LabelingMechanism mechanism) {
        this.mechanism = mechanism;
    }
}
