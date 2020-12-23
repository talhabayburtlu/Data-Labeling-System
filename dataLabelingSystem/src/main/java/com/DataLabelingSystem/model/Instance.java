package com.DataLabelingSystem.model;

import com.DataLabelingSystem.metric.InstanceMetric;
import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;

@JsonIdentityInfo(scope = Instance.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonPropertyOrder({"id", "instance"})
public class Instance {

    @JsonProperty("id")
    private int id;
    @JsonProperty("instance")
    private String content;
    @JsonIgnore
    private Dataset dataset;
    @JsonIgnore
    private InstanceMetric instanceMetric;
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

    //TODO output to JSON?
    public Label getFinalLabel() {
        instanceMetric.updateLabelAssignments(); //FIXME delegate update to separate method
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
