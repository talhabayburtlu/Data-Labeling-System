package com.DataLabelingSystem;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataLabelingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static <Booelan> void main(String[] args) throws IOException {
        logger.info("Starting simulation.");

        if (args.length < 3) {
            logger.error("At least 3 arguments are required.");
            System.exit(-1);
        } else if (args.length % 2 != 1) {
            logger.error("You must provide an input filename for users, and a pair of filenames for each input, one input and one output.");
            System.exit(-1);
        }
        String usersFile = args[0];
        if (!(new File(usersFile).exists())) {
            logger.error("File \"" + usersFile + " does not exist.");
            System.exit(-1);
        }
        ArrayList<String> inputFiles = new ArrayList<>();
        ArrayList<String> outputFiles = new ArrayList<>();
        for (int i = 1; i < args.length; i += 2) {
            inputFiles.add(args[i]);
            outputFiles.add(args[i + 1]);
        }


        JsonParser jsonParser = JsonParser.getJsonParser();
        ArrayList<Dataset> datasets = jsonParser.readDatasets(inputFiles.toArray(new String[0]));
        ArrayList<User> users = jsonParser.readUsers(usersFile);

        Integer datasetId = null;
        Boolean isSameDataset = null;
        Dataset currentDataset = datasets.get(datasetId);


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
                if (labelAgainProbability > user.getConsistencyCheckProbability() * 100) {
                    ArrayList<Instance> labeledInstances = new ArrayList<Instance>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == user)
                            labeledInstances.add(labelAssignment.getInstance());

                    Integer randomInstanceIndex = (int) (Math.random() * labeledInstances.size());
                    user.labelWithMechanism(labeledInstances.get(randomInstanceIndex), currentDataset.getLabels().toArray(new Label[0]));
                }
            }
        }


        // jsonParser.writeDatasetsWithUsers(outputFiles.toArray(new String[0]), datasets, users);
        // logger.info("Ending simulation.");
    }

}