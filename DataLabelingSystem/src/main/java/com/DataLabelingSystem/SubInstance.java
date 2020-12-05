package com.DataLabelingSystem;

public class SubInstance extends Instance {

    private String content;
    private Instance instance;
    private Dataset dataset;

    SubInstance(String content, Instance instance) {
        super();
        this.content = content;
        this.instance = instance;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    @Override
    public Dataset getDataset() {
        return dataset;
    }

    @Override
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

}
