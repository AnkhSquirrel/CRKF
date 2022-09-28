package fr.kyo.crkf.controller;

import fr.kyo.crkf.ApplicationCRKF;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import org.controlsfx.control.ToggleSwitch;

import java.util.Objects;

public class AccueilController {

    @FXML
    private ToggleSwitch themeMode;
    @FXML
    private ApplicationCRKF application;
    @FXML
    public void initialize(Scene scene){
        themeMode.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if(themeMode.isSelected()){
                application.setLightMode(false);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fr/kyo/crkf/darkMode.css")).toExternalForm());
            }
            else
            {
                application.setLightMode(true);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fr/kyo/crkf/lightMode.css")).toExternalForm());
            }
        }
        });
    }

    public void setPositionToggleSwitch(){
        themeMode.setSelected(!Boolean.TRUE.equals(application.getLightMode()));
    }

    public void setMainApp(ApplicationCRKF application) {
        this.application = application;
    }

    @FXML
    protected void openProfesseurList() {
        application.openProfesseurList();
    }

    @FXML
    protected void openEcoleList() {
        application.openEcoleList();
    }

    @FXML
    protected void openInstrumentList() {
        application.openInstrumentList();
    }

    @FXML
    protected void openGestionVille() {
        application.openVilleGestion();
    }

    @FXML
    protected void openGestionDepartement() {
        application.openDepartementGestion();
    }

    @FXML
    protected void openGestionFamille() {
        application.openFamilleGestion();
    }

    @FXML
    protected void openGestionClassification() {
        application.openClassificationGestion();
    }

    @FXML
    protected void openGestionCycle() {
        application.openCycleGestion();
    }

}