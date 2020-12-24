package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class InstanceMetric {
    private Instance instance;
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();


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
        logger.trace("Calculating total number of label assignments for instance id : " + this.instance.getId());
        int totalLabels = 0;
        for (LabelAssignment labelAssignment : this.labelAssignments)
            totalLabels += labelAssignment.getLabels().size();

        return totalLabels;
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
                    labelIntegerMap.put(label, 1); // 1 if adds first time
            }
        return labelIntegerMap;
    }

    @Nullable
    public Map<Label, Integer> getMostFrequentLabelWithFrequency() { // Returns most frequent label with it's frequency.
        HashMap<Label, Integer> labelIntegerMap = countLabelOccurrences();
        if (labelIntegerMap.isEmpty()) {
            return null;
        }
        // Determines label which label is the most frequent one.
        Label label = Collections.max(labelIntegerMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        int totalLabelCount = 0;
        for (Map.Entry<Label, Integer> labelIntegerEntry : labelIntegerMap.entrySet())
            totalLabelCount += labelIntegerEntry.getValue();

        // Computing percentage
        Integer percentage = (int) (((labelIntegerMap.get(label) * 1.0) / totalLabelCount * 100));
        return Collections.singletonMap(label, percentage);
    }

    @Nullable
    public Label getMostFrequentLabel() { //FIXME This is serialized as an integer in the JSON file for some reason...
        HashMap<Label, Integer> labelOccurrences = countLabelOccurrences();
        if (labelOccurrences.isEmpty()) {
            return null;
        }
        return Collections.max(labelOccurrences.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public HashMap<Label, Integer> getAllLabelFrequencies() { // Returns all labels with their frequency.
        HashMap<Label, Integer> labelIntegerMap = countLabelOccurrences();
        HashMap<Label, Integer> labelPercentageMap = new HashMap<>();

        int totalLabelCount = 0;
        for (Map.Entry<Label, Integer> labelIntegerEntry : labelIntegerMap.entrySet())
            totalLabelCount += labelIntegerEntry.getValue();

        for (Label label : labelIntegerMap.keySet()) {
            // Computing percentage
            Integer percentage = (int) (((labelIntegerMap.get(label) * 1.0) / totalLabelCount) * 100);
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
