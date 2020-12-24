package com.DataLabelingSystem.model;

import com.DataLabelingSystem.labelingMechanism.LabelingMechanism;
import com.DataLabelingSystem.labelingMechanism.LabelingMechanismFactory;
import com.DataLabelingSystem.metric.UserMetric;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

//TODO Change getMetric method name for all three classes
@JsonIdentityInfo(scope = User.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "user id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private static final Logger logger = LogManager.getLogger();

    @JsonProperty("user id")
    private int id;
    @JsonProperty("user name")
    private String name;
    @JsonProperty("user type")
    private String type;
    @JsonIgnore
    private LabelingMechanism mechanism;
    @JsonProperty("consistency check probability")
    private Double consistencyCheckProbability = 0.10;

    //new 22:49 17:12:2020
    @JsonIgnore
    private ArrayList<Dataset> assignedDatasets = new ArrayList<>();
    @JsonIgnore
    private final UserMetric metric = new UserMetric(this);

    public ArrayList<Dataset> getAssignedDatasets() {
        return assignedDatasets;
    }

    public void setAssignedDatasets(ArrayList<Dataset> assignedDatasets) {
        this.assignedDatasets = assignedDatasets;
    }

    public static void initialize(ArrayList<Dataset> assignedDatasets, Instance instance) {
        for (int i = 0; i < instance.getDataset().getId(); i++) {
            assignedDatasets.add(instance.getDataset());
        }
    }

    public UserMetric getMetric() {
        return metric;
    }


    //
    @JsonCreator
    User(@JsonProperty("user id") int id,
         @JsonProperty("user name") String name,
         @JsonProperty("user type") String type) throws UnsupportedOperationException {
        this.id = id;
        this.name = name;
        this.type = type;
        LabelingMechanismFactory labelingMechanismFactory = LabelingMechanismFactory.getLabelingMechanismFactory();
        this.mechanism = labelingMechanismFactory.getMechanism(type);

        logger.trace("User created with information: " + toString());
    }

    User(int id, String type, LabelingMechanism mechanism) {
        this.id = id;
        this.type = type;
        this.mechanism = mechanism;
    }

    public void labelWithMechanism(Instance instance, ArrayList<Label> labels) {
        mechanism.label(this, instance, labels);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LabelingMechanism getMechanism() {
        return mechanism;
    }

    public void setMechanism(LabelingMechanism mechanism) {
        this.mechanism = mechanism;
    }

    public Double getConsistencyCheckProbability() {
        return consistencyCheckProbability;
    }

    public void setConsistencyCheckProbability(Double consistencyCheckProbability) {
        this.consistencyCheckProbability = consistencyCheckProbability;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
