package com.DataLabelingSystem;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class DataLabelingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        logger.info("Starting simulation.");

        JsonParser jsonParser = JsonParser.getJsonParser();
        HashMap<String, Object> config = jsonParser.readConfig("config.json");
        @SuppressWarnings("unchecked") // This should return datasets, no need to check return type.
        ArrayList<Dataset> datasets = (ArrayList<Dataset>) config.get("datasets");
        @SuppressWarnings("unchecked") // This should return a users no need to check return type.
        ArrayList<User> users = (ArrayList<User>) config.get("users");

        Integer datasetId = (Integer) config.get("current dataset id");
        Optional<Dataset> optionalDataset = datasets.stream().filter(d -> d.getId() == datasetId).findFirst();
        if (!optionalDataset.isPresent())
            throw new RuntimeException("Current dataset couldn't found with id=" + datasetId);
        Dataset currentDataset = optionalDataset.get();

        boolean isSameDataset = !currentDataset.getLabelAssignments().isEmpty();

        logger.trace("Processing dataset: " + currentDataset);
        for (User user : currentDataset.getAssignedUsers()) {
            logger.trace("Processing with user: " + user);
            for (Instance instance : currentDataset.getInstances()) {
                if (isSameDataset) { // If previous dataset is processing again, pass the instances that labeled.
                    ArrayList<Instance> labeledInstances = new ArrayList<>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == user)
                            labeledInstances.add(labelAssignment.getInstance());

                    if (labeledInstances.contains(instance)) // Check if instance previously labeled by this user.
                        continue;
                }

                user.labelWithMechanism(instance, currentDataset.getLabels());
                jsonParser.writeAll(currentDataset, datasets, users);
                int labelAgainProbability = (int) (Math.random() * 101);
                // Labeling random labeled instance again if consistency check probability maintains.
                if (labelAgainProbability <= user.getConsistencyCheckProbability() * 100) {
                    ArrayList<Instance> labeledInstances = new ArrayList<>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == user)
                            labeledInstances.add(labelAssignment.getInstance());

                    int randomInstanceIndex = (int) (Math.random() * labeledInstances.size());
                    user.labelWithMechanism(labeledInstances.get(randomInstanceIndex), currentDataset.getLabels());
                    jsonParser.writeAll(currentDataset, datasets, users);
                }
            }
        }
        logger.info("Ending simulation.");
    }

}