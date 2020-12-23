package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UserMetric {
    private User user;
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

    public UserMetric(User user) {
        this.user = user;
        this.updateDataset();
    }

    /*    This method updates the labelAssignments field of this class with using
     LabelAssignments ArrayList from dataset for a specific user.
    */
    public void updateDataset(){
        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if (labelAssignment.getUser().getId() == getUser().getId()) {
                    this.labelAssignments.set(j, labelAssignment);
                }
            }
        }
    }

    /* A-1 This methods brings the number of assigned datasets for a specific user. */
    public int getNumberOfDatasets(){
        return user.getAssignedDatasets().size();
    }
    /* A-2 This methods lists all the datasets which a specific user assigned
       with their completeness percentage.
    */
    public HashMap<Dataset,Integer> getDatasetWithCompletenessPercentage(){
        HashMap<Dataset,Integer> completenessPercentage = new HashMap<>();
        ArrayList<Integer> tempInstances = new ArrayList<>();
        int InstanceCounter = 0;

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            int numOfInstanceInDataset = dataset.getInstances().size();
            tempInstances.clear();
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);

                if(labelAssignment.getUser().getId()==getUser().getId()) {

                    if (!tempInstances.contains(labelAssignment.getInstanceId())) {
                        tempInstances.add(labelAssignment.getInstanceId());
                    }
                }
            }

            InstanceCounter = tempInstances.size();
            int compPercentage = (InstanceCounter/numOfInstanceInDataset)*100;
            completenessPercentage.put(dataset,compPercentage);
        }
        return completenessPercentage;
    }
    /* A-3 This method brings total number of instances which are labeled by a specific user. */
    public int getInstancesLabeledCount(){
        int InstanceCounter = 0;

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    InstanceCounter++;
                }
            }
        }
        return InstanceCounter;
    }
    /* A-4 This method brings total number instances which are labeled just one time by a specific user. */
    public int getUniqueInstancesLabeledCount(){
        int InstanceCounter = 0;
        ArrayList<Integer> tempInstances = new ArrayList<>();

        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    if(!tempInstances.contains(labelAssignment.getInstanceId())){
                        tempInstances.add(labelAssignment.getInstanceId());
                    }
                }
            }
        }
        InstanceCounter = tempInstances.size();
        return InstanceCounter;
    }
    /* A-5 This method calculates labeling consistency of a specific user
       and returns it with percentage form.
    */
    public double getConsistencyPercentage(){
        int globalInstanceCounter = 0; //label'ı birden fazla olan instance'ların sayısı
        HashMap<Instance,ArrayList<Integer>> tempInstances = new HashMap<Instance,ArrayList<Integer>>();
        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);

                if(labelAssignment.getUser().getId()==getUser().getId()){

                        if(!tempInstances.containsKey(labelAssignment.getInstance()) ) {
                            ArrayList<Integer> labelIds = labelAssignment.getLabels()
                                    .stream()
                                    .map(label -> label.getId())
                                    .collect(Collectors.toCollection(ArrayList::new));
                            tempInstances.put(labelAssignment.getInstance(),labelIds);
                        }
                        else if(tempInstances.containsKey(labelAssignment.getInstance())){
                            ArrayList<Label> currentLabelsList = labelAssignment.getLabels();

                            for (int k = 0; k < currentLabelsList.size(); k++) {
                                tempInstances.get(labelAssignment.getInstance()).add(currentLabelsList.get(k).getId());
                            }
                        }
                }
            }
        }
        int labelsMoreThanOne = 0;
        int inconsistent = 0;

        for (HashMap.Entry everySingleInstance : tempInstances.entrySet()) {
            if (everySingleInstance.getValue().toString().length() > 1) {
                labelsMoreThanOne++;
                for (int i = 0; i < everySingleInstance.getValue().toString().length() ; i++) {
                    if (!(everySingleInstance.getValue().toString().charAt(0)==everySingleInstance.getValue().toString().charAt(i)))
                              {
                        inconsistent++;
                        break;
                    }
                }
            }
        }
        int consistency = (labelsMoreThanOne - inconsistent) / labelsMoreThanOne * 100; //FIXME integer division

        return consistency;

    }
    /* A-6 This method calculates the average time spent at labeling an instance in seconds */
    public double getAverageLabelTime(){
        double averageTime = 0;
        int labelAssignmentCounter = 0;
        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
                for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                    LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                    if(labelAssignment.getUser().getId()==getUser().getId()){
                    averageTime += labelAssignment.getDuration().getSeconds();
                    labelAssignmentCounter++;
                    }
                }
        }
        averageTime = averageTime/labelAssignmentCounter;

        return averageTime;
    }

    /* A-7 This method calculates Standard Deviation of time spent at labeling an instance in seconds */
    public long getStandardDeviation(){
        long standartDeviation = 0;
        ArrayList<Long> durations = new ArrayList<Long>();
        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);
            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);
                if(labelAssignment.getUser().getId()==getUser().getId()){
                    durations.add(labelAssignment.getDuration().getSeconds());
                }

            }
        }
        double averageTime = getAverageLabelTime();
        for (int i = 0; i < durations.size(); i++) {
        standartDeviation += (Math.pow((durations.get(i)-averageTime),2) / (durations.size()-1) );
        }
        standartDeviation = (long) Math.sqrt(standartDeviation);
        return standartDeviation;
    }

    //Getter and Setter methods.
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return labelAssignments;
    }

    public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
        this.labelAssignments = labelAssignments;
    }

    }


