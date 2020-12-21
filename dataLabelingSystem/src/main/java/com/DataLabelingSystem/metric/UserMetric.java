package com.DataLabelingSystem.metric;

import com.DataLabelingSystem.assignment.LabelAssignment;
import com.DataLabelingSystem.model.Dataset;
import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserMetric {
    private User user ;
    private ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

    UserMetric(User user){
        this.user = user;
    }

    public int getNumberOfDatasets(){
        int getNumberOfDatasets = user.getAssignedDatasets().size();
        return getNumberOfDatasets;
    }

    public HashMap<Dataset,Integer> getDatasetWithCompletenessPercentage{

    }
    public int getInstancesLabeledCount(){

    }
    public int getUniqueInstancesLabeledCount(){

    }
    public double getConsistencyPercentage(){

    }
    public double getAverageLabelTime(){

    }
    public double getStandartDeviation(){

    }

}
