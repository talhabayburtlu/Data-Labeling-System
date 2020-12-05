package com.DataLabelingSystem.assignment;

import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public LabelAssignment createLabelAssignment(User user, Instance instance, Label[] labels) {
        logger.info("User " + user + " labeled instance: " + instance + " with class label(s):" + Arrays.toString(labels));

        return new LabelAssignment(user, instance, labels);
    }

    public void addToDataset(LabelAssignment labelAssignment) {
        Instance instance = labelAssignment.getInstance();
        Dataset dataset = instance.getDataset(); //get dataset from instance

        dataset.getLabelAssignments().add(labelAssignment);
    }

    public void removeFromDataset(LabelAssignment labelAssignment) {
        Instance instance = labelAssignment.getInstance();
        Dataset dataset = instance.getDataset(); //get dataset from instance

        dataset.getLabelAssignments().remove(labelAssignment);
    }

}
