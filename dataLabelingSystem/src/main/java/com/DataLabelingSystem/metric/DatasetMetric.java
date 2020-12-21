package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatasetMetric {
    private final Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;
    private int completenessPercentage;

    public DatasetMetric(Dataset dataset) {
        this.dataset = dataset;
        this.updateLabelAssignments();
    }

    public void updateLabelAssignments() {
        labelAssignments = dataset.getLabelAssignments();
    }

    public void updateCompletenessPercentage() {
        ArrayList<Instance> labeledInstances = new ArrayList<>();
        for (LabelAssignment labelAssignment : labelAssignments) {
            if (!labeledInstances.contains(labelAssignment.getInstance())) {
                labeledInstances.add(labelAssignment.getInstance());
            }
        }
        double labeledCount = labeledInstances.size();
        double totalCount = dataset.getInstances().size();
        this.completenessPercentage = (int) Math.round((labeledCount / totalCount) * 100);
    }

    public int getCompletenessPercentage() {
        return completenessPercentage;
    }

    public HashMap<Label, Integer> getFinalLabelsWithPercentages() {
        throw new UnsupportedOperationException("Not implemented");
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

    public ArrayList<User> getUsersAssigned() {
        return dataset.getAssignedUsers();
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
            double userPercentageSum = 0;
            double userPercentageCount = instanceLabelAssignments.keySet().size();

            for (Instance instance : instanceLabelAssignments.keySet()) {
                int consistencyPercentageForInstance = 0;
                // Calculate the consistency percentage for all recurrences of this instance for this user
                Map<Label[], List<LabelAssignment>> classLabelAssignmentFrequencies = instanceLabelAssignments.get(instance).stream().collect(Collectors.groupingBy(LabelAssignment::getLabels));
                userPercentageSum += consistencyPercentageForInstance;
            }
            usersWithConsistencyPercentages.put(user, (int) Math.round(userPercentageSum / userPercentageCount));
        }
        return usersWithConsistencyPercentages;
    }
}
