package com.DataLabelingSystem.assignment;

import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Arrays;

public class LabelAssignmentManager {
    private static final Logger logger = LogManager.getLogger();
    private static LabelAssignmentManager labelAssignmentManager;

    private LabelAssignmentManager() {
    }

    private static LabelAssignmentManager createInstance() {
        return new LabelAssignmentManager();
    }

    public static LabelAssignmentManager getLabelAssignmentManager() {
        if (labelAssignmentManager == null)
            labelAssignmentManager = createInstance();
        return labelAssignmentManager;
    }

    public LabelAssignment createLabelAssignment(User user, Instance instance, Label[] labels, Duration duration) {
        logger.info("User " + user + " labeled instance: " + instance + " with class label(s):" + Arrays.toString(labels));

        LabelAssignment assignment = new LabelAssignment(user, instance, labels, duration);
        this.addToDataset(assignment);
        return assignment;
    }

    public void addToDataset(LabelAssignment labelAssignment) {
        Dataset dataset = labelAssignment.getInstance().getDataset(); //get dataset from instance

        dataset.getLabelAssignments().add(labelAssignment);
    }

    public void removeFromDataset(LabelAssignment labelAssignment) {
        Instance instance = labelAssignment.getInstance();
        Dataset dataset = instance.getDataset(); //get dataset from instance

        dataset.getLabelAssignments().remove(labelAssignment);
    }

}
