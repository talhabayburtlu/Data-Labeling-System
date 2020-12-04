package com.DataLabelingSystem;

import java.util.ArrayList;

public class Instance {

    private int id;
    private String content;
    private Dataset dataset;

    private ArrayList<SubInstance> subInstances = new ArrayList<>();


    Instance(){

    }

    Instance(int id, String content){
        this.id = id;
        this.content = content;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public ArrayList<SubInstance> getSubInstances() {
        return subInstances;
    }

    public void setSubInstances(ArrayList<SubInstance> subInstances) {
        this.subInstances = subInstances;
    }

}
