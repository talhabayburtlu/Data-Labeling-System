package com.DataLabelingSystem;

import java.util.ArrayList;

public class Dataset {

    private int id;
    private String name;
    private String instanceType;
    private int maxNumberOfLabelsPerInstance;

    private ArrayList<Label> labels = new ArrayList<Label>()
    private ArrayList<Instance> instances = new ArrayList<Instance>();
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<LabelAssignment>();

    Dataset(int id, String name, String instanceType, int maxNumberOfLabelsPerInstance,
            ArrayList<Label> labels, ArrayList<Instance> instances){
        this.id = getId();
        this.name = getName();
        this.instanceType = getInstanceType();
        this.maxNumberOfLabelsPerInstance = getMaxNumberOfLabelsPerInstance();
        this.labels = getLabels();
        this.instances = getInstances();
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

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public int getMaxNumberOfLabelsPerInstance() {
        return maxNumberOfLabelsPerInstance;
    }

    public void setMaxNumberOfLabelsPerInstance(int maxNumberOfLabelsPerInstance) {
        this.maxNumberOfLabelsPerInstance = maxNumberOfLabelsPerInstance;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public ArrayList<Instance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Instance> instances) {
        this.instances = instances;
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return labelAssignments;
    }

    public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
        this.labelAssignments = labelAssignments;
    }
}
