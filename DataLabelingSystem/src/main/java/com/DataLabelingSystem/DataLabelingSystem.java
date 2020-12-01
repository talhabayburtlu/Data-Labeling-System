package com.DataLabelingSystem;

import java.util.ArrayList;

public class DataLabelingSystem {

	public static void main(String[] args) {
	JsonParser jsonParser= JsonParser.getJsonParser();
		ArrayList<Dataset> datasets = jsonParser.readDatasets("input-1.json"); //TODO json files will be called
	}

}
