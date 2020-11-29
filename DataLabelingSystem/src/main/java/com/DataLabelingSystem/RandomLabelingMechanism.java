package com.DataLabelingSystem;

import java.util.ArrayList;

public class RandomLabelingMechanism extends LabelingMechanism {

    @Override
    public void label(User user, Instance instance, Label[] labels) {

        Database database = instance.getDatabase();
        int maxNumberOfLabelsPerInstance = database.getMaxNumberOfLabelsPerInstance();

        ArrayList<Label> selectedLabels = new ArrayList<Label>();
        for (int i = 0; i < maxNumberOfLabelsPerInstance ; i++) {

            Label theLabel = label[(int)(Math.random() * label.length)];

            while (selectedLabels.contains(theLabel))
                theLabel = label[(int)(Math.random() * label.length)];

            selectedLabels.add(theLabel);

            if ((int)(Math.random() * maxNumberOfLabelsPerInstance)
                    == (maxNumberOfLabelsPerInstance / 5)) // Stop adding label with %20 chance.
                break;
        }

        LabelAssignment labelAssignment = new LabelAssignment(user,instance,selectedLabels);
    }
}
