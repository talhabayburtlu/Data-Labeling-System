package com.DataLabelingSystem;

import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class DataLabelingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        logger.info("Starting simulation.");

        JsonParser jsonParser = JsonParser.getJsonParser();
        ArrayList<Dataset> datasets = jsonParser.readDatasets(new String[]{"input-1.json", "input-2.json"});
        ArrayList<User> users = jsonParser.readUsers("users.json");

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

        jsonParser.writeDatasetsWithUsers(new String[]{"output-1.json", "output-2.json"}, datasets, users);
        logger.info("Ending simulation.");
    }

}