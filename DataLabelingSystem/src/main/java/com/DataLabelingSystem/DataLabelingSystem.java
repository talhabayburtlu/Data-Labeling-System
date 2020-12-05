package com.DataLabelingSystem;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DataLabelingSystem {

	public static void main(String[] args) throws FileNotFoundException, JsonProcessingException {
		JsonParser jsonParser = JsonParser.getJsonParser();
		ArrayList<Dataset> datasets = jsonParser.readDatasets(new String[]{"input-1.json", "input-2.json"});
		ArrayList<User> users = jsonParser.readUsers("users.json");
		for (Dataset dataset :
				datasets) {
			for (User user :
					users) {
				for (Instance instance :
						dataset.getInstances()) {
					user.labelWithMechanism(instance, dataset.getLabels().toArray(new Label[dataset.getLabels().size()]));
				}
			}
		}
		System.out.println("Simulation complete.");
	}

}