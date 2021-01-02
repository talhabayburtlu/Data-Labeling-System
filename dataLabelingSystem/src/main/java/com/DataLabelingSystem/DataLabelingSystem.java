package com.DataLabelingSystem;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class DataLabelingSystem {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {
        logger.info("Starting simulation.");
        Scanner in = new Scanner(System.in);

        JsonParser jsonParser = JsonParser.getJsonParser();
        HashMap<String, Object> config = jsonParser.readConfig("config.json");
        @SuppressWarnings("unchecked") // This should return datasets, no need to check return type.
        ArrayList<Dataset> datasets = (ArrayList<Dataset>) config.get("datasets");
        @SuppressWarnings("unchecked") // This should return a users no need to check return type.
        ArrayList<User> users = (ArrayList<User>) config.get("users");

        Integer datasetId = (Integer) config.get("current dataset id");
        Optional<Dataset> optionalDataset = datasets.stream().filter(d -> d.getId() == datasetId).findFirst();
        if (!optionalDataset.isPresent())
            throw new RuntimeException("Current dataset couldn't found with id=" + datasetId);
        Dataset currentDataset = optionalDataset.get();

        boolean isSameDataset = !currentDataset.getLabelAssignments().isEmpty();


        boolean successfulLogin = false;
        boolean isUserBot = false;
        User humanUser = null;
        while (!successfulLogin && !isUserBot) {
            System.out.print("User name: ");
            String username = in.nextLine();
            System.out.print("User password: ");
            String password = in.nextLine();
            if (username.equals("") && password.equals(""))
                isUserBot = true;

            for (User user : users) {
                if (user.getUsername() == null || user.getPassword() == null)
                    continue;

                if (user.getUsername().equals(username) && user.getPassword().equals(password))
                    humanUser = user;
            }

            if (!isUserBot && humanUser == null)
                System.out.println("The user with user name:" + username + " and password " + password + " couldn't find in config.json.");


            successfulLogin = humanUser != null;
        }

        logger.trace("Processing dataset: " + currentDataset);

        if (successfulLogin) { // User is human.
            for (Instance instance : currentDataset.getInstances()) {
                if (isSameDataset) { // If previous dataset is processing again, pass the instances that labeled.
                    ArrayList<Instance> labeledInstances = new ArrayList<>();
                    for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                        if (labelAssignment.getUser() == humanUser)
                            labeledInstances.add(labelAssignment.getInstance());

                    if (labeledInstances.contains(instance)) // Check if instance previously labeled by this user.
                        continue;
                }

                System.out.print("Labels : ");
                for (Label label : currentDataset.getLabels())
                    System.out.print("[ id=" + label.getId() + ",label text=" + label.getText() + " ]");
                System.out.println("\nCurrent instance: [ id=" + instance.getId() + ", instance=" + instance.getContent() + " ]");
                System.out.print("Which label(s) do you want to label this instance? (Enter id's with blank space): ");
                String inLine = in.nextLine();
                ArrayList<Integer> ids = new ArrayList<>();
                ArrayList<Label> labels = new ArrayList<>();
                Arrays.stream(inLine.split(" ")).forEach(id -> ids.add(Integer.parseInt(id)));
                currentDataset.getLabels().stream().forEach(l -> {
                    if (ids.contains(l.getId()))
                        labels.add(l);
                });

                humanUser.labelWithMechanism(instance, labels);
                jsonParser.writeAll(currentDataset, datasets, users);
            }
        } else { // Users are bot.
            for (User user : currentDataset.getAssignedUsers()) {
                if (user.getUsername() != null || user.getPassword() != null || !user.getType().toLowerCase().contains("bot"))
                    continue;

                logger.trace("Processing with user: " + user);
                for (Instance instance : currentDataset.getInstances()) {
                    if (isSameDataset) { // If previous dataset is processing again, pass the instances that labeled.
                        ArrayList<Instance> labeledInstances = new ArrayList<>();
                        for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                            if (labelAssignment.getUser() == user)
                                labeledInstances.add(labelAssignment.getInstance());

                        if (labeledInstances.contains(instance)) // Check if instance previously labeled by this user.
                            continue;
                    }

                    user.labelWithMechanism(instance, currentDataset.getLabels());
                    jsonParser.writeAll(currentDataset, datasets, users);
                    int labelAgainProbability = (int) (Math.random() * 101);
                    // Labeling random labeled instance again if consistency check probability maintains.
                    if (labelAgainProbability <= user.getConsistencyCheckProbability() * 100) {
                        logger.info("Checking consistency probability for user : " + user.toString());
                        ArrayList<Instance> labeledInstances = new ArrayList<>();
                        for (LabelAssignment labelAssignment : currentDataset.getLabelAssignments())
                            if (labelAssignment.getUser() == user)
                                labeledInstances.add(labelAssignment.getInstance());

                        int randomInstanceIndex = (int) (Math.random() * labeledInstances.size());
                        user.labelWithMechanism(labeledInstances.get(randomInstanceIndex), currentDataset.getLabels());
                        jsonParser.writeAll(currentDataset, datasets, users);
                    }
                }
            }
        }


        logger.info("Ending simulation.");
    }

}