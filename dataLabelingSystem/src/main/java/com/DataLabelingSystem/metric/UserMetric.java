package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMetric {
    private User user ;
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

    UserMetric(User user){
        this.user = user;
    }

    public int getNumberOfDatasets(){
        int numberOfDatasets = user.getAssignedDatasets().size();
        return numberOfDatasets;
    }

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
    //A-5
    public double getConsistencyPercentage(){
        int globalInstanceCounter = 0; //label'ı birden fazla olan instance'ların sayısı
        HashMap<Instance,ArrayList<Label>> tempInstances = new HashMap<Instance,ArrayList<Label>>();
        for (int i = 0; i <  user.getAssignedDatasets().size() ; i++) {
            Dataset dataset = user.getAssignedDatasets().get(i);

            for (int j = 0; j < dataset.getLabelAssignments().size(); j++) {
                LabelAssignment labelAssignment = dataset.getLabelAssignments().get(j);

                if(labelAssignment.getUser().getId()==getUser().getId()){
                        if(!tempInstances.containsKey(labelAssignment.getInstance()) ) {
                            tempInstances.put(labelAssignment.getInstance(),labelAssignment.getLabe);
                        }
                        else if(tempInstances.containsKey(labelAssignment.getInstance())){

                        }

                }
            }
        }
        return 0;
        }
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


