package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class InstanceMetric {

    private Instance instance;
    private ArrayList<LabelAssignment> labelAssignments;

    public InstanceMetric(Instance instance) {
        this.instance = instance;
    }

    public void updateLabelAssignments() {
        ArrayList<LabelAssignment> labelAssignments = this.instance.getDataset().getLabelAssignments()
                .stream()
                .filter(l -> l.getInstance() == this.instance)
                .collect(Collectors.toCollection(ArrayList::new));

        this.labelAssignments = labelAssignments;
    }

    public int getAssignmentCount() { // Returns total number of label assignments.
        return this.labelAssignments.size();
    }

    public int getUniqueAssignmentCount() { // Returns total number of unique label assignments.
        HashSet<Label> labelSet = new HashSet<Label>();

        for (LabelAssignment labelAssignment : this.labelAssignments)
            for (Label label : labelAssignment.getLabels())
                labelSet.add(label); // Adding label into label set or not adding if same label added before.

        return labelSet.size();
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return labelAssignments;
    }

    public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
        this.labelAssignments = labelAssignments;
    }
}
