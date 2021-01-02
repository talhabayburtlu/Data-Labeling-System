package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.assignment.LabelAssignmentManager;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AutoKeywordLabelingMechanism implements LabelingMechanism {

    private static AutoKeywordLabelingMechanism AutoKeywordLabelingMechanism;

    private static AutoKeywordLabelingMechanism getInstance() {
        return new AutoKeywordLabelingMechanism();
    }

    protected static AutoKeywordLabelingMechanism getAutoKeywordLabelingMechanism() {
        if (AutoKeywordLabelingMechanism == null)
            AutoKeywordLabelingMechanism = getInstance();
        return AutoKeywordLabelingMechanism;
    }

    @Override
    public void label(User user, Instance instance, ArrayList<Label> label) {
        Instant labelingStart = Instant.now();

        ArrayList<Label> assignedLabels = new ArrayList<>();
        for (Label label1 : label) {
            if (instance.getContent().toLowerCase().contains(label1.getText().toLowerCase())) {
                Label selectedLabel = label1;


                if (assignedLabels.size() <= instance.getDataset().getMaxNumberOfLabelsPerInstance())
                    assignedLabels.add(selectedLabel);
                else
                    break;
            }

        }

        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager();

        try {
            TimeUnit.MILLISECONDS.sleep(500); // synthetic delay
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Duration labelingDuration = Duration.between(labelingStart, Instant.now());
        labelAssignmentManager.createLabelAssignment(user, instance, assignedLabels, labelingDuration); // Creating label assignment.

        user.getMetric().updateLabelAssignment();
        instance.getInstanceMetric().updateLabelAssignments();
        instance.getDataset().getDatasetMetric().updateLabelAssignments();

    }

}
