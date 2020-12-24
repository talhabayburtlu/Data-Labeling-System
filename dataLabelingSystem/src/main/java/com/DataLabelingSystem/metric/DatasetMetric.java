package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO JsonPropertyOrder and names for each Metric class
public class DatasetMetric {
    private final Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;

    public DatasetMetric(Dataset dataset) {
        this.dataset = dataset;
        this.updateLabelAssignments();
    }

    public void updateLabelAssignments() {
        labelAssignments = dataset.getLabelAssignments();
    }

    public Object getDataset() {
        return new Object() {
            @JsonProperty("dataset id")
            public final int datasetId = dataset.getId();
            @JsonProperty("dataset name")
            public final String name = dataset.getName();
        };
    }

    public int getCompletenessPercentage() {
        ArrayList<Instance> labeledInstances = new ArrayList<>();
        for (LabelAssignment labelAssignment : labelAssignments) {
            if (!labeledInstances.contains(labelAssignment.getInstance())) {
                labeledInstances.add(labelAssignment.getInstance());
            }
        }
        double labeledCount = labeledInstances.size();
        double totalCount = dataset.getInstances().size();
        return (int) Math.round((labeledCount / totalCount) * 100);
    }

    public HashMap<Label, Integer> getFinalLabelsWithPercentages() {
        HashMap<Label, Integer> finalLabelCounts = new HashMap<>();
        for (Label label : dataset.getLabels()) {
            finalLabelCounts.put(label, 0);
        }
        for (Instance instance : dataset.getInstances()) {
            Label finalLabel = instance.getFinalLabel();
            if (finalLabel == null) {
                continue;
            }
            int curVal = finalLabelCounts.get(finalLabel);
            finalLabelCounts.put(finalLabel, curVal + 1);
        }
        int allLabelsCount = 0;
        for (Label label : finalLabelCounts.keySet()) {
            if (finalLabelCounts.get(label) != 0) {
                allLabelsCount++;
            }
        }

        for (Label label : finalLabelCounts.keySet()) {
            int curVal = finalLabelCounts.get(label);
            finalLabelCounts.put(label, (int) Math.round((((double) curVal) / finalLabelCounts.size()) * 100));
        }
        return finalLabelCounts;
    }

    public HashMap<Label, Integer> getLabelsWithUniqueInstanceCount() {
        HashMap<Label, Integer> labelsWithUniqueInstanceCount = new HashMap<>(dataset.getLabels().size());
        for (Label label : dataset.getLabels()) {
            labelsWithUniqueInstanceCount.put(label, 0);
        }
        for (LabelAssignment labelAssignment : labelAssignments) {
            for (Label assignedLabel : labelAssignment.getLabels()) {
                int curVal = labelsWithUniqueInstanceCount.get(assignedLabel);
                labelsWithUniqueInstanceCount.put(assignedLabel, curVal + 1);
            }
        }
        return labelsWithUniqueInstanceCount;
    }

    @JsonIgnore
    public ArrayList<User> getUsersAssigned() {
        return dataset.getAssignedUsers();
    }

    public Integer getNumberOfUsersAssigned() {
        return dataset.getAssignedUsers().size();
    }

    public HashMap<User, Integer> getUsersWithCompletenessPercentages() { // TODO maybe use stream filtering here instead of nested for loops?
        ArrayList<User> assignedUsers = getUsersAssigned();
        HashMap<User, Integer> usersWithCompletenessPercentages = new HashMap<>(dataset.getAssignedUsers().size());
        for (User user : assignedUsers) {
            usersWithCompletenessPercentages.put(user, 0);
            ArrayList<Instance> instancesCounted = new ArrayList<>(dataset.getInstances().size());
            for (LabelAssignment labelAssignment : labelAssignments) {
                if (labelAssignment.getUser().equals(user)) {
                    if (!instancesCounted.contains(labelAssignment.getInstance())) {
                        usersWithCompletenessPercentages.put(user, usersWithCompletenessPercentages.get(user) + 1);
                        instancesCounted.add(labelAssignment.getInstance());
                    }
                }
            }
        }
        usersWithCompletenessPercentages.replaceAll((u, v) -> (int) Math.round((double) usersWithCompletenessPercentages.get(u) / dataset.getInstances().size()) * 100);
        return usersWithCompletenessPercentages;
    }

    public HashMap<User, Integer> getUsersWithConsistencyPercentages(ArrayList<User> assignedUsers) {
        //ArrayList<User> assignedUsers = getUsersAssigned();
        Map<User, Map<Instance, List<LabelAssignment>>> userLabelAssignmentsByInstance = dataset.getLabelAssignments().stream()
                .collect(Collectors.groupingBy(LabelAssignment::getUser,
                        Collectors.groupingBy(LabelAssignment::getInstance)));
        HashMap<User, Integer> usersWithConsistencyPercentages = new HashMap<>(assignedUsers.size());

        for (User user : userLabelAssignmentsByInstance.keySet()) {
            Map<Instance, List<LabelAssignment>> instanceLabelAssignments = userLabelAssignmentsByInstance.get(user);
            double userPercentage = 0;
            //   double userPercentageCount = instanceLabelAssignments.keySet().size();

            for (Instance instance : instanceLabelAssignments.keySet()) {
                int consistencyPercentageForInstance = 0;
                // Calculate the consistency percentage for all recurrences of this instance for this user
                // Map<ArrayList<Label>, List<LabelAssignment>> classLabelAssignmentFrequencies = instanceLabelAssignments.get(instance).stream().collect(Collectors.groupingBy(LabelAssignment::getLabels));
                List<LabelAssignment> labels = instanceLabelAssignments.get(instance);
                int labelingMoreThanOnce = 0;
                boolean consistency = true;
                int inconsistency = 0;
                if (labels.size() > 1) {
                    labelingMoreThanOnce++;
                    for (int i = 0; i < labels.size(); i++) {
                        for (LabelAssignment label : labels) {
                            if (labels.get(i) != label) {
                                consistency = false;
                                break;
                            }
                        }
                    }
                    if (!consistency) {
                        inconsistency++;
                    }
                }
                userPercentage = ((labelingMoreThanOnce - inconsistency) * 1.0) / labelingMoreThanOnce;
            }
            usersWithConsistencyPercentages.put(user, (int) Math.round(userPercentage));
        }
        return usersWithConsistencyPercentages;
    }
}
