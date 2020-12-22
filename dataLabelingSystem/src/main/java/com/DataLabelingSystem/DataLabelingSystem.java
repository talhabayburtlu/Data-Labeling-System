package com.DataLabelingSystem;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DataLabelingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Starting simulation.");

        JsonParser jsonParser = JsonParser.getJsonParser();
        HashMap<String, Object> config = jsonParser.readConfig("config.json");
        ArrayList<Dataset> datasets = (ArrayList<Dataset>) config.get("datasets");
        ArrayList<User> users = (ArrayList<User>) config.get("users");

        Integer datasetId = (Integer) config.get("current dataset id");
        Dataset currentDataset = datasets.stream().filter(d -> d.getId() == datasetId).findFirst().get();
        Boolean isSameDataset = !currentDataset.getLabelAssignments().isEmpty();

        currentDataset.setAssignedUsers(users);

        logger.trace("Processing dataset: " + currentDataset);
        for (User user : currentDataset.getAssignedUsers()) {
            logger.trace("Processing with user: " + user);
            for (Instance instance : currentDataset.getInstances()) {
                if (isSameDataset) { // If previous dataset is processing again, pass the instances that labeled.
                    ArrayList<Instance> labeledInstances = new ArrayList<Instance>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == user)
                            labeledInstances.add(labelAssignment.getInstance());

                    if (labeledInstances.contains(instance)) // Check if instance previously labeled by this user.
                        continue;
                }

                user.labelWithMechanism(instance, currentDataset.getLabels().toArray(new Label[0]));

                Integer labelAgainProbability = (int) (Math.random() * 101);
                // Labeling random labeled instance again if consistency check probability maintains.
                if (labelAgainProbability <= user.getConsistencyCheckProbability() * 100) {
                    ArrayList<Instance> labeledInstances = new ArrayList<Instance>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == user)
                            labeledInstances.add(labelAssignment.getInstance());

                    Integer randomInstanceIndex = (int) (Math.random() * labeledInstances.size());
                    user.labelWithMechanism(labeledInstances.get(randomInstanceIndex), currentDataset.getLabels().toArray(new Label[0]));
                }

                jsonParser.writeDatasetsWithUsers(datasets, users);
                TimeUnit.MILLISECONDS.sleep(500);
            }
        }
        logger.info("Ending simulation.");
    }

}