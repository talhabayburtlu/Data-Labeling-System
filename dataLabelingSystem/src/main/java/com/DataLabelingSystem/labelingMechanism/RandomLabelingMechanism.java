package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.assignment.LabelAssignmentManager;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

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
    public void label(User user, Instance instance, Label[] labels) {
        Instant labelingStart = Instant.now();
        Dataset dataset = instance.getDataset();
        int maxNumberOfLabelsPerInstance = dataset.getMaxNumberOfLabelsPerInstance();
        ArrayList<Label> selectedLabelsAsList = new ArrayList<>();

        int randomNumberOfLabelsPerInstance = (int) (Math.random() * maxNumberOfLabelsPerInstance + 1); // Randomizing algorithm, between 0 and maxNumberOfLabelsPerInstance (included)
        for (int i = 0; i < randomNumberOfLabelsPerInstance; i++) { // Selecting labels based on random number of label
            Label theLabel = labels[(int) (Math.random() * labels.length)]; // Selecting random label.

            while (selectedLabelsAsList.contains(theLabel)) // Checking duplicates
                theLabel = labels[(int) (Math.random() * labels.length)];

            selectedLabelsAsList.add(theLabel);
        }

        Label[] selectedLabels = selectedLabelsAsList.toArray(new Label[0]); // Creating label array from arraylist.

        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager(); // Getting manager of label assignments.

        Duration labelingDuration = Duration.between(labelingStart, Instant.now());
        labelAssignmentManager.createLabelAssignment(user, instance, selectedLabels, labelingDuration); // Creating label assignment.
    }
}
