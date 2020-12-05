package com.DataLabelingSystem;

import java.io.IOException;
import java.util.ArrayList;

public class DataLabelingSystem {

	public static void main(String[] args) throws IOException {

		JsonParser jsonParser = JsonParser.getJsonParser();
		ArrayList<Dataset> datasets = jsonParser.readDatasets(new String[]{"input-1.json", "input-2.json"});
		ArrayList<User> users = jsonParser.readUsers("users.json");

		for (Dataset dataset : datasets) {
			for (User user : users) {
				for (Instance instance : dataset.getInstances()) {
					user.labelWithMechanism(instance, dataset.getLabels().toArray(new Label[0]));
				}
			}
		}
		jsonParser.writeDatasetsWithUsers(new String[]{"output-1.json", "output-2.json"}, datasets, users);
		System.out.println("Simulation complete.");
	}

}