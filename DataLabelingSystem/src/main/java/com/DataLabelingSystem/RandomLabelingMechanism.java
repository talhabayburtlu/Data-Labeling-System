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

        ArrayList<Label> selectedLabelsAsList = new ArrayList<Label>();
        for (int i = 0; i < maxNumberOfLabelsPerInstance ; i++) {

            Label theLabel = labels[(int)(Math.random() * labels.length)];

            while (selectedLabelsAsList.contains(theLabel))
                theLabel = labels[(int)(Math.random() * labels.length)];

            selectedLabelsAsList.add(theLabel);

            int random1 = (int)(Math.random() * 100);
            int random2 = (int)(Math.random() * 100);

            if (random1 > random2) // Stop adding label with based on two random numbers' comparison.
                break;
        }

        Label[] selectedLabels = (Label[]) selectedLabelsAsList.toArray();
        LabelAssignment labelAssignment = new LabelAssignment(user,instance,selectedLabels);
    }
}
