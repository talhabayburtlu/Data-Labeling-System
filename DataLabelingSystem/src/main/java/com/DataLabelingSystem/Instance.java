package com.DataLabelingSystem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Instance {

    @JsonProperty("id")
    private int id;
    @JsonProperty("instance")
    private String content;
    @JsonIgnore
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
