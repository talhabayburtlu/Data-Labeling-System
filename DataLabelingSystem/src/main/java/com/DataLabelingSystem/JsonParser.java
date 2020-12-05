package com.DataLabelingSystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class JsonParser {
    private static final JsonParser instance = new JsonParser();

    private JsonParser() {
    }

    public static JsonParser getJsonParser() {
        return instance;
    }

    public ArrayList<Dataset> readDatasets(String[] filenames) throws FileNotFoundException, JsonProcessingException {
        ArrayList<Dataset> datasets = new ArrayList<>();
        for (String filename :
                filenames) {
            String jsonString = readAllLines(filename);
            ObjectMapper objectMapper = new ObjectMapper();
            Dataset dataset = objectMapper.readValue(jsonString, Dataset.class);
            datasets.add(dataset);
        }
        return datasets;
    }

    public ArrayList<User> readUsers(String filename) throws FileNotFoundException, JsonProcessingException {
        String jsonString = readAllLines(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        User[] parsedUsers = objectMapper.readValue(jsonString, User[].class);
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

    public void writeDatasetsWithUsers(String[] filenames, ArrayList<Dataset> datasets, ArrayList<User> users) throws IOException {
        //TODO should we take a dictionary as an input parameter? ex: collection of <filename, dataset> tuples
        //TODO this overwrites existing files by default
        //TODO users is not used...
        if (datasets.size() != filenames.length) {
            throw new IllegalArgumentException("You must provide at least one filename for each dataset.");
        }

        for (int i = 0; i < datasets.size(); i++) {
            File outputFile = new File(filenames[i]);
            String outputJson;
            ObjectMapper objectMapper = new ObjectMapper();
            outputJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(datasets.get(i));
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(outputJson);
            fileWriter.close();
        }
    }
}
