package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO JsonPropertyOrder and names for each Metric class
public class DatasetMetric {
    private final Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;
    private static final Logger logger = LogManager.getLogger(); //

    public DatasetMetric(Dataset dataset) {
        this.dataset = dataset;
        this.updateLabelAssignments();
        logger.trace("Created DatasetMetric with information " + toString()); //
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
        logger.trace("Calculating Completeness Percentage for dataset : " + getDataset()); //

        ArrayList<Instance> labeledInstances = new ArrayList<>();
        for (LabelAssignment labelAssignment : labelAssignments) {
            logger.trace("Processing with labelAssignment : " + labelAssignment);
            if (!labeledInstances.contains(labelAssignment.getInstance())) {
                labeledInstances.add(labelAssignment.getInstance());
            }
        }
        double labeledCount = labeledInstances.size();
        double totalCount = dataset.getInstances().size();
        logger.info("Completeness Percentage calculated.");
        return (int) Math.round((labeledCount / totalCount) * 100);

    }

    public HashMap<Label, Integer> getFinalLabelsWithPercentages() {
        logger.trace("Final label with percentages for dataset : " + getDataset()); //

        HashMap<Label, Integer> finalLabelCounts = new HashMap<>();
        for (Label label : dataset.getLabels()) {
            logger.trace("Processing with label : " + label); //
            finalLabelCounts.put(label, 0);
        }
        for (Instance instance : dataset.getInstances()) {
            logger.trace("Processing with instance : " + instance); //
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
        logger.info("Got final labels with percentages."); //
        return finalLabelCounts;
    }

    public HashMap<Label, Integer> getLabelsWithUniqueInstanceCount() {
        logger.trace("Labels with unique instance number for dataset : " + getDataset()); //
        HashMap<Label, Integer> labelsWithUniqueInstanceCount = new HashMap<>(dataset.getLabels().size());
        for (Label label : dataset.getLabels()) {
            logger.trace("Processing with label : " + label); //
            labelsWithUniqueInstanceCount.put(label, 0);
        }
        for (LabelAssignment labelAssignment : labelAssignments) {
            logger.trace("Processing with labelAssignment : " + labelAssignment); //
            for (Label assignedLabel : labelAssignment.getLabels()) {
                int curVal = labelsWithUniqueInstanceCount.get(assignedLabel);
                labelsWithUniqueInstanceCount.put(assignedLabel, curVal + 1);
            }
        }
        logger.info("Got labels with unique instance counts."); //
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
        logger.trace("Users with completeness percentages for dataset : " + getDataset()); //
        ArrayList<User> assignedUsers = getUsersAssigned();
        HashMap<User, Integer> usersWithCompletenessPercentages = new HashMap<>(dataset.getAssignedUsers().size());
        for (User user : assignedUsers) {
            logger.trace("Processing with assigned user : " + user); //
            usersWithCompletenessPercentages.put(user, 0);
            ArrayList<Instance> instancesCounted = new ArrayList<>(dataset.getInstances().size());
            for (LabelAssignment labelAssignment : labelAssignments) {
                logger.trace("Processing with labelAssignment : " + labelAssignment); //
                if (labelAssignment.getUser().equals(user)) {
                    if (!instancesCounted.contains(labelAssignment.getInstance())) {
                        usersWithCompletenessPercentages.put(user, usersWithCompletenessPercentages.get(user) + 1);
                        instancesCounted.add(labelAssignment.getInstance());
                    }
                }
            }
        }
        usersWithCompletenessPercentages.replaceAll((u, v) -> (int) Math.round((double) usersWithCompletenessPercentages.get(u) / dataset.getInstances().size()) * 100);
        logger.info("Got Users with completeness percentages."); //
        return usersWithCompletenessPercentages;
    }

    public HashMap<User, Integer> getUsersWithConsistencyPercentages(ArrayList<User> assignedUsers) {
        //ArrayList<User> assignedUsers = getUsersAssigned();
        logger.trace("Users with consistency percentages for dataset : " + getDataset());
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
        logger.info("Got users with consistency percentages.");
        return usersWithConsistencyPercentages;
    }
}
