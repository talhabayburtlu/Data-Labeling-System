package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.assignment.LabelAssignmentManager;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RandomLabelingMechanism implements LabelingMechanism {
    private static RandomLabelingMechanism randomLabelingMechanism;

    private static RandomLabelingMechanism getInstance() {
        return new RandomLabelingMechanism();
    }

    protected static RandomLabelingMechanism getRandomLabelingMechanism() {
        if (randomLabelingMechanism == null)
            randomLabelingMechanism = getInstance();
        return randomLabelingMechanism;
    }

    @Override
    public void label(User user, Instance instance, ArrayList<Label> labels) {
        Instant labelingStart = Instant.now();
        Dataset dataset = instance.getDataset();
        int maxNumberOfLabelsPerInstance = dataset.getMaxNumberOfLabelsPerInstance();
        ArrayList<Label> selectedLabels = new ArrayList<>();

        int randomNumberOfLabelsPerInstance = (int) (Math.random() * maxNumberOfLabelsPerInstance + 1); // Randomizing algorithm, between 0 and maxNumberOfLabelsPerInstance (included)
        for (int i = 0; i < randomNumberOfLabelsPerInstance; i++) { // Selecting labels based on random number of label
            Label theLabel = labels.get((int) (Math.random() * labels.size())); // Selecting random label.

            while (selectedLabels.contains(theLabel)) // Checking duplicates
                theLabel = labels.get((int) (Math.random() * labels.size()));

            selectedLabels.add(theLabel);
        }

        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager(); // Getting manager of label assignments.

        try {
            TimeUnit.MILLISECONDS.sleep(500); // synthetic delay
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        Duration labelingDuration = Duration.between(labelingStart, Instant.now());
        labelAssignmentManager.createLabelAssignment(user, instance, selectedLabels, labelingDuration); // Creating label assignment.
        user.getMetric().updateDataset();
        instance.getInstanceMetric().updateLabelAssignments();
        instance.getDataset().getDatasetMetric().updateLabelAssignments();
    }
}
