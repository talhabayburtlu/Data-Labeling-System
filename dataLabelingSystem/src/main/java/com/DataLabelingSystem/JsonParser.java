package com.DataLabelingSystem;

import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
    private static ObjectMapper objectMapper;

    private JsonParser() {
        objectMapper = JsonMapper.builder().findAndAddModules().build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static JsonParser getJsonParser() {
        return instance;
    }

    public String getReport(Object metric) throws JsonProcessingException { //TODO Create an abstract metrics superclass with the update method and use that method polymorphically here?
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metric);
    }

    public void writeAll(Dataset currentDataset, ArrayList<Dataset> datasets, ArrayList<User> users) throws IOException {
        writeDatasetWithUsers(currentDataset);

        String outputFilename = "report.json";
        File outputFile = new File(outputFilename);
        StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("{").append(System.lineSeparator());
        outputBuilder.append("\"dataset metrics\":").append("[");
        for (Dataset dataset : datasets) {
            outputBuilder.append(this.getReport(dataset.getDatasetMetric()));
            outputBuilder.append(',');
            outputBuilder.append(System.lineSeparator());
        }
        outputBuilder.deleteCharAt(outputBuilder.lastIndexOf(","));
        outputBuilder.append("],").append(System.lineSeparator()).append("\"instance metrics\":").append(System.lineSeparator()).append("[").append(System.lineSeparator());
        for (Instance instance : currentDataset.getInstances()) {
            outputBuilder.append(this.getReport(instance.getInstanceMetric()));
            outputBuilder.append(',');
            outputBuilder.append(System.lineSeparator());
        }
        outputBuilder.deleteCharAt(outputBuilder.lastIndexOf(","));
        outputBuilder.append("],").append(System.lineSeparator()).append("\"user metrics\":").append(System.lineSeparator()).append("[").append(System.lineSeparator());
        for (User user : users) {
            outputBuilder.append(this.getReport(user.getMetric()));
            outputBuilder.append(',');
            outputBuilder.append(System.lineSeparator());
        }
        outputBuilder.deleteCharAt(outputBuilder.lastIndexOf(","));
        outputBuilder.append("]}");
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(outputBuilder.toString());
        fileWriter.close();
    }

    // TODO: Config should include assigned user ids for each dataset and assign related user objects to dataset.
    public HashMap<String, Object> readConfig(String filename) throws FileNotFoundException, JsonProcessingException, InvalidObjectException {
        String jsonString = readAllLines(filename);
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

    public Dataset readDataset(String filename) throws FileNotFoundException, JsonProcessingException {
        logger.info("Starting to read dataset from " + filename + " file.");
        String jsonString = readAllLines(filename);
        return objectMapper.readValue(jsonString, Dataset.class);
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

    public void writeDatasetWithUsers(Dataset dataset) throws IOException, IllegalArgumentException {
        String outputFilename = "output-" + dataset.getId() + ".json";
        logger.trace("Writing dataset id:" + dataset.getId() + " to " + outputFilename + " file.");

        File outputFile = new File(outputFilename);
        String outputJson;

        outputJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataset);

        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(outputJson);
        fileWriter.close();
    }
}
