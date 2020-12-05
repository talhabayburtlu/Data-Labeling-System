package com.DataLabelingSystem;

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
        Dataset dataset = instance.getDataset();
        int maxNumberOfLabelsPerInstance = dataset.getMaxNumberOfLabelsPerInstance();
        ArrayList<Label> selectedLabelsAsList = new ArrayList<>();

        randomNumberOfLabelsPerInstance = (int) (Math.random() * maxNumberOfLabelsPerInstance + 1); // Randomizing algorithm, between 0 and maxNumberOfLabelsPerInstance (included)
        for (int i = 0; i < randomNumberOfLabelsPerInstance; i++) { // Selecting labels based on random number of label
            Label theLabel = labels[(int) (Math.random() * labels.length)]; // Selecting random label.

            while (selectedLabelsAsList.contains(theLabel)) // Checking duplicates
                theLabel = labels[(int) (Math.random() * labels.length)];

            selectedLabelsAsList.add(theLabel);
        }

        Label[] selectedLabels = selectedLabelsAsList.toArray(new Label[selectedLabelsAsList.size()]); // Creating label array from arraylist.

        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager(); // Getting manager of label assignments.

        LabelAssignment labelAssignment = labelAssignmentManager.createLabelAssignment(user, instance, selectedLabels); // Creating label assignment.
        labelAssignmentManager.addToDataset(labelAssignment); // Adding label assignment to the dataset.
    }
}
