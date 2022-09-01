package fr.kyo.crkf.Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Classification {
    private int id_classification;
    private String classification;

    public Classification(int id_classification, String classification) {
        this.id_classification = id_classification;
        this.classification = classification;
    }
    public int getId_classification() {
        return id_classification;
    }
    public void setId_classification(int id_classification) {
        this.id_classification = id_classification;
    }
    public String getclassification() {
        return classification;
    }
    public void setclassification(String classification) {
        this.classification = classification;
    }
    public ObservableValue<String> getClassificationStringProperty(){
        return new SimpleStringProperty(classification);
    }
    @Override
    public String toString() {
        return classification;
    }
}
