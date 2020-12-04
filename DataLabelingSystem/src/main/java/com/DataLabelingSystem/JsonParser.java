package com.DataLabelingSystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
            Scanner fileScanner = new Scanner(new File(filename));
            StringBuilder sb = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                sb.append(fileScanner.nextLine());
            }
            String jsonString = sb.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            Dataset dataset = objectMapper.readValue(jsonString, Dataset.class);
            datasets.add(dataset);
        }
        return datasets;
    }

    public ArrayList<User> readUsers(String[] filenames) {
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }

    public void writeDatasetsWithUsers(String[] filenames, ArrayList<Dataset> datasets, ArrayList<User> users) {
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }
}
