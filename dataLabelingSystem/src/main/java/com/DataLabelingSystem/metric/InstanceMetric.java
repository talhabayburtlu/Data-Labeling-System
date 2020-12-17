package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.util.*;
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

    public int getUniqueUserCount() { // Returns number of unique users that labeled this instance.
        HashSet<User> userSet = new HashSet<User>();

        for (LabelAssignment labelAssignment : this.labelAssignments)
            userSet.add(labelAssignment.getUser()); // Adding user into user set or not adding if same user added before.

        return userSet.size();
    }

    public HashMap<Label, Integer> getMostFrequentLabel() { // Returns most frequent label with it's frequency.
        HashMap<Label, Integer> labelIntegerMap = new HashMap<Label, Integer>();

        for (LabelAssignment labelAssignment : this.labelAssignments)
            for (Label label : labelAssignment.getLabels()) {
                if (labelIntegerMap.containsKey(label)) // Increments occurrence of label if exist in map.
                    labelIntegerMap.replace(label, labelIntegerMap.get(label) + 1);
                else // Adds label to the map if doesn't exist.
                    labelIntegerMap.put(label, 0);
            }

        // Determines label which label is the most frequent one.
        Label label = Collections.max(labelIntegerMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        // Computing percentage
        Integer percentage = (int) (((labelIntegerMap.get(label) * 1.0) / this.getUniqueAssignmentCount()) * 100);

        return (HashMap<Label, Integer>) Collections.singletonMap(label, percentage);
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
