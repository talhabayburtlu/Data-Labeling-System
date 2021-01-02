package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.assignment.LabelAssignmentManager;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ManualLabelingMechanism implements LabelingMechanism {

    private static ManualLabelingMechanism manualLabelingMechanism;

    private static ManualLabelingMechanism getInstance() {
        return new ManualLabelingMechanism();
    }

    protected static ManualLabelingMechanism getManualLabelingMechanism() {
        if (manualLabelingMechanism == null)
            manualLabelingMechanism = getInstance();
        return manualLabelingMechanism;
    }

    @Override
    public void label(User user, Instance instance, ArrayList<Label> label) {
        Instant labelingStart = Instant.now();
        LabelAssignmentManager labelAssignmentManager = LabelAssignmentManager.getLabelAssignmentManager();

        try {
            TimeUnit.MILLISECONDS.sleep(500); // synthetic delay
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Duration labelingDuration = Duration.between(labelingStart, Instant.now());
        labelAssignmentManager.createLabelAssignment(user, instance, label, labelingDuration); // Creating label assignment.

        user.getMetric().updateLabelAssignment();
        instance.getInstanceMetric().updateLabelAssignments();
        instance.getDataset().getDatasetMetric().updateLabelAssignments();
    }
}
