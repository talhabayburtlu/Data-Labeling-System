package com.DataLabelingSystem;

import java.util.ArrayList;

public class RandomLabelingMechanism extends LabelingMechanism {

    public RandomLabelingMechanism(String name) {
        super(name);
    }

    @Override
    public void label(User user, Instance instance, Label[] labels) {

        Dataset dataset = instance.getDataset();
        int maxNumberOfLabelsPerInstance = dataset.getMaxNumberOfLabelsPerInstance();

        ArrayList<Label> selectedLabelsAsList = new ArrayList<>();
        for (int i = 0; i < maxNumberOfLabelsPerInstance; i++) { // Selecting labels based on determined number of labels per instance
            Label theLabel = labels[(int) (Math.random() * labels.length)]; // Selecting random label.

            while (selectedLabelsAsList.contains(theLabel)) // Checking duplicates
                theLabel = labels[(int) (Math.random() * labels.length)];

            selectedLabelsAsList.add(theLabel);

            int random1 = (int) (Math.random() * 100);
            int random2 = (int) (Math.random() * 100);

            if (random1 > random2) // Stop adding label with based on two random numbers' comparison.
                break;
        }

        Label[] selectedLabels = selectedLabelsAsList.toArray(new Label[selectedLabelsAsList.size()]); // Creating label array from arraylist.

        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager(); // Getting manager of label assignments.

        LabelAssignment labelAssignment = labelAssignmentManager.createLabelAssignment(user, instance, selectedLabels); // Creating label assignment.
        labelAssignmentManager.addToDataset(labelAssignment); // Adding label assignment to the dataset.
    }
}
