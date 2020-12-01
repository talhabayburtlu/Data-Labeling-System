package com.DataLabelingSystem;

import java.util.ArrayList;

public class JsonParser {
    private static final JsonParser instance = new JsonParser();

    private JsonParser() {
    }

    public static JsonParser getJsonParser() {
        return instance;
    }

    public ArrayList<Dataset> readDatasets(String[] filenames) {
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }

    public ArrayList<User> readUsers(String[] filenames) {
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }

    public void writeDatasetsWithUsers(String[] filenames, ArrayList<Dataset> datasets, ArrayList<User> users) {
        throw new UnsupportedOperationException("This method has not been implemented yet.");
    }
}
