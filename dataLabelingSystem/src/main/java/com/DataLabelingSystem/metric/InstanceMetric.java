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
        this.labelAssignments = this.instance.getDataset().getLabelAssignments()
                .stream()
                .filter(l -> l.getInstance() == this.instance)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getAssignmentCount() { // Returns total number of label assignments.
        return this.labelAssignments.size();
    }

    public int getUniqueAssignmentCount() { // Returns total number of unique label assignments.
        HashSet<Label> labelSet = new HashSet<>();

        // Adding label into label set or not adding if same label added before.
        for (LabelAssignment labelAssignment : this.labelAssignments)
            labelSet.addAll(labelAssignment.getLabels());

        return labelSet.size();
    }

    public int getUniqueUserCount() { // Returns number of unique users that labeled this instance.
        HashSet<User> userSet = new HashSet<>();

        for (LabelAssignment labelAssignment : this.labelAssignments)
            userSet.add(labelAssignment.getUser()); // Adding user into user set or not adding if same user added before.

        return userSet.size();
    }

    private HashMap<Label, Integer> countLabelOccurrences() { // Counts occurrences per labels.
        HashMap<Label, Integer> labelIntegerMap = new HashMap<>();

        for (LabelAssignment labelAssignment : this.labelAssignments)
            for (Label label : labelAssignment.getLabels()) {
                if (labelIntegerMap.containsKey(label)) // Increments occurrence of label if exist in map.
                    labelIntegerMap.replace(label, labelIntegerMap.get(label) + 1);
                else // Adds label to the map if doesn't exist.
                    labelIntegerMap.put(label, 0);
            }
        return labelIntegerMap;
    }


    public HashMap<Label, Integer> getMostFrequentLabelWithFrequency() { // Returns most frequent label with it's frequency.
        HashMap<Label, Integer> labelIntegerMap = countLabelOccurrences();

        // Determines label which label is the most frequent one.
        Label label = Collections.max(labelIntegerMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        // Computing percentage
        Integer percentage = (int) (((labelIntegerMap.get(label) * 1.0) / this.getUniqueAssignmentCount()) * 100);
        return (HashMap<Label, Integer>) Collections.singletonMap(label, percentage);
    }

    public Label getMostFrequentLabel() {
        return Collections.max(countLabelOccurrences().entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public HashMap<Label, Integer> getAllLabelFrequencies() { // Returns all labels with their frequency.
        HashMap<Label, Integer> labelIntegerMap = countLabelOccurrences();

        HashMap<Label, Integer> labelPercentageMap = new HashMap<>();
        int uniqueAssignmentCount = this.getUniqueAssignmentCount();
        for (Label label : labelIntegerMap.keySet()) {
            // Computing percentage
            Integer percentage = (int) (((labelIntegerMap.get(label) * 1.0) / uniqueAssignmentCount) * 100);
            labelPercentageMap.put(label, percentage);
        }

        return labelPercentageMap;
    }

    public Double getEntropy() {
        HashMap<Label, Integer> labelPercentageMap = getAllLabelFrequencies();

        double entropy = 0.0;

        for (Label label : labelPercentageMap.keySet()) {
            double percentage = labelPercentageMap.get(label) / 100.0; // Based on formula given
            entropy += -1 * percentage * (Math.log(percentage) / Math.log(labelPercentageMap.size())); // Using labelPercentageMap.size() as num of unique labels
        }


        return entropy;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }
}
