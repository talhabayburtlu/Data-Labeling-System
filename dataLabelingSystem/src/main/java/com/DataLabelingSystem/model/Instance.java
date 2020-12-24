package com.DataLabelingSystem.model;

import com.DataLabelingSystem.metric.InstanceMetric;
import com.fasterxml.jackson.annotation.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@JsonIdentityInfo(scope = Instance.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "instance"})
public class Instance {

    @JsonProperty("id")
    private int id;
    @JsonProperty("instance")
    private String content;
    @JsonIgnore
    private Dataset dataset;
    @JsonIgnore
    private final InstanceMetric instanceMetric = new InstanceMetric(this);
    @JsonIgnore
    private ArrayList<SubInstance> subInstances = new ArrayList<>();

    Instance() {
    }

    Instance(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public ArrayList<SubInstance> getSubInstances() {
        return subInstances;
    }

    public void setSubInstances(ArrayList<SubInstance> subInstances) {
        this.subInstances = subInstances;
    }

    public InstanceMetric getInstanceMetric() {
        return instanceMetric;
    }

    @Nullable
    //TODO Ignore this and add separate JSON Getter that returns a string representation for the object?
    public Label getFinalLabel() {
        return instanceMetric.getMostFrequentLabel();
    }

    @Override
    public String toString() {
        return "Instance{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
