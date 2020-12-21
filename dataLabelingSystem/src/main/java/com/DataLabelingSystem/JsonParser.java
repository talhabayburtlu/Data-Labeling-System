package com.DataLabelingSystem;

import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class JsonParser {
    private static final JsonParser instance = new JsonParser();
    private static final Logger logger = LogManager.getLogger();

    private JsonParser() {
    }

    public static JsonParser getJsonParser() {
        return instance;
    }

    public HashMap<String, Object> readConfig(String filename) throws FileNotFoundException, JsonProcessingException, InvalidObjectException {
        String jsonString = readAllLines(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode configJsonObject = (ObjectNode) objectMapper.readTree(jsonString);

        ArrayList<User> users = new ArrayList<>(Arrays.asList(objectMapper.treeToValue(configJsonObject.get("users"), User[].class)));

        ArrayList<Dataset> datasets = new ArrayList<>();
        for (JsonNode node : configJsonObject.get("datasets")) {
            String filePath = node.get("file path").textValue();
            String outputFilename = "output-" + node.get("dataset id").intValue() + ".json";
            if ((new File(outputFilename)).exists()) {
                filePath = outputFilename;
            }
            Dataset dataset = readDataset(filePath);
            dataset.fixUserReferences(users);
            datasets.add(dataset);
        }

        Integer currentDatasetId = configJsonObject.get("current dataset id").intValue();

        return new HashMap<String, Object>() {{ // TODO a different return value can be used here instead of HashMap
            put("users", users);
            put("datasets", datasets);
            put("current dataset id", currentDatasetId);
        }};
    }

    public Dataset loadOutput(String filename) throws JsonProcessingException, FileNotFoundException {
        String jsonString = readAllLines(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode datasetJsonObject = objectMapper.readTree(jsonString);
        return objectMapper.readValue(jsonString, Dataset.class);
    }

    public Dataset readDataset(String filename) throws FileNotFoundException, JsonProcessingException {
        logger.info("Starting to read dataset from " + filename + " file.");
        String jsonString = readAllLines(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Dataset.class);
    }

    public ArrayList<Dataset> readDatasets(String[] filenames) throws FileNotFoundException, JsonProcessingException {
        logger.info("Starting to reading datasets from " + Arrays.toString(filenames) + " files.");

        ArrayList<Dataset> datasets = new ArrayList<>();

        for (String filename : filenames) {
            logger.info("Starting to read dataset from " + filename + " file.");
            String jsonString = readAllLines(filename);
            ObjectMapper objectMapper = new ObjectMapper();

            Dataset dataset = objectMapper.readValue(jsonString, Dataset.class);

            logger.trace("Adding dataset with information" + dataset.toString());
            datasets.add(dataset);
        }

        logger.info("Ending to reading datasets from " + Arrays.toString(filenames) + " files.");
        return datasets;
    }

    public ArrayList<User> readUsers(String filename) throws FileNotFoundException, JsonProcessingException {
        logger.info("Starting to reading users from " + filename + " file.");
        String jsonString = readAllLines(filename);

        ObjectMapper objectMapper = new ObjectMapper();
        User[] parsedUsers = objectMapper.readValue(jsonString, User[].class);

        logger.info("Ending to reading users from " + filename + " file.");
        return new ArrayList<>(Arrays.asList(parsedUsers));
    }

    @NotNull
    private String readAllLines(String filename) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(filename));
        StringBuilder sb = new StringBuilder();

        while (fileScanner.hasNextLine()) {
            sb.append(fileScanner.nextLine());
        }

        fileScanner.close();
        return sb.toString();
    }

    public void writeDatasetsWithUsers(ArrayList<Dataset> datasets, ArrayList<User> users) throws IOException, IllegalArgumentException {
        for (Dataset dataset : datasets) {
            String outputFilename = "output-" + dataset.getId() + ".json";
            logger.trace("Writing dataset id:" + dataset.getId() + " to " + outputFilename + " file.");

            File outputFile = new File(outputFilename);
            String outputJson;

            ObjectMapper objectMapper = new ObjectMapper();
            outputJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataset);

            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(outputJson);
            fileWriter.close();
        }
    }
}
