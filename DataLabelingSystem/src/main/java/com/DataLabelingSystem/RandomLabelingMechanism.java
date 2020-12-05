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
        maxNumberOfLabelsPerInstance = (int) (Math.random() * maxNumberOfLabelsPerInstance); // Randomizing algorithm based on maxNumberOfLabelsPerInstance
        for (int i = 0; i < maxNumberOfLabelsPerInstance; i++) { // Selecting labels based on determined number of labels per instance
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
