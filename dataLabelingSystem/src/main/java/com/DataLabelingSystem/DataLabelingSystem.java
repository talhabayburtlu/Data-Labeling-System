package com.DataLabelingSystem;

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

    public static void main(String[] args) throws IOException {
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

        logger.info("Looping through all datasets, user and instances for random labeling.");
        for (Dataset dataset : datasets) {
            logger.trace("Processing dataset: " + dataset);
            for (User user : users) {
                logger.trace("Processing with user: " + user);
                for (Instance instance : dataset.getInstances()) {
                    user.labelWithMechanism(instance, dataset.getLabels().toArray(new Label[0]));
                }
            }
        }

        jsonParser.writeDatasetsWithUsers(outputFiles.toArray(new String[0]), datasets, users);
        logger.info("Ending simulation.");
    }

}