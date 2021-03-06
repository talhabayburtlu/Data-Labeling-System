package com.DataLabelingSystem.model;

import com.fasterxml.jackson.annotation.*;

@JsonIdentityInfo(scope = Label.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "label id")
@JsonPropertyOrder({"label id", "label text"})
public class Label {

    @JsonProperty("label id")
    private int id;
    @JsonProperty("label text")
    private String text;
    @JsonIgnore
    private Dataset dataset;

    Label() {
    }

    Label(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
