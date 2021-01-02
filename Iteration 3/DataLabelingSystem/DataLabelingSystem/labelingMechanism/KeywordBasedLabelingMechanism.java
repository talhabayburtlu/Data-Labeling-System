package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.assignment.LabelAssignmentManager;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KeywordBasedLabelingMechanism implements LabelingMechanism {

    private static KeywordBasedLabelingMechanism keywordBasedLabelingMechanism;

    private static KeywordBasedLabelingMechanism getInstance() {
        return new KeywordBasedLabelingMechanism();
    }

    protected static KeywordBasedLabelingMechanism getKeywordBasedLabelingMechanism() {
        if (keywordBasedLabelingMechanism == null)
            keywordBasedLabelingMechanism = getInstance();
        return keywordBasedLabelingMechanism;
    }

    @Override
    public void label(User user, Instance instance, ArrayList<Label> label) {
        Instant labelingStart = Instant.now();
        HashMap<String, Integer> keywords = user.getKeywords().get(instance.getDataset().getId());

        if (keywords == null) {
            System.out.println("KeywordBot with id=" + user.getId() + " should have at least one keyword related to database id=" + instance.getDataset().getId() + " .");
            System.exit(-1);
        }

        ArrayList<Label> assignedLabels = new ArrayList<>();
        for (String keyword : keywords.keySet()) {
            if (instance.getContent().toLowerCase().contains(keyword.toLowerCase())) {
                Label selectedLabel = null;
                for (Label label1 : label)
                    if (label1.getId() == keywords.get(keyword)) {
                        selectedLabel = label1;
                        break;
                    }

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
