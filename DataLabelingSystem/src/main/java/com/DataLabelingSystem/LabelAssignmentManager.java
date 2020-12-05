package com.DataLabelingSystem;

public class LabelAssignmentManager {
    private static LabelAssignmentManager labelAssignmentManager;

    private LabelAssignmentManager() { }

    private static LabelAssignmentManager createInstance() {
        return new LabelAssignmentManager();
    }

    public static LabelAssignmentManager getLabelAssignmentManager() {
        if (labelAssignmentManager == null)
            labelAssignmentManager = createInstance();
        return labelAssignmentManager;
    }

    public LabelAssignment createLabelAssignment(User user, Instance instance, Label[] labels) {
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
