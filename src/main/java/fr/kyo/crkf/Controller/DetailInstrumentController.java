package fr.kyo.crkf.Controller;

import fr.kyo.crkf.ApplicationCRKF;
import fr.kyo.crkf.Entity.Famille;
import fr.kyo.crkf.Entity.Instrument;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DetailInstrumentController {
    @FXML
    private Label nom;
    @FXML
    private Label classification;
    @FXML
    private TableView<Famille> familleTableView;
    @FXML
    private TableColumn<Famille,String> familleColumn;
    @FXML
    private ApplicationCRKF applicationCRKF;
    private Instrument instrument;

    @FXML
    private void initialize(){
        familleColumn.setCellValueFactory(cellData -> cellData.getValue().getFamilleStringProperty());
    }

    public void setApplicationCRKF(ApplicationCRKF applicationCRKF){
        this.applicationCRKF = applicationCRKF;
    }

    @FXML
    private void openInstrumentList(){
        applicationCRKF.openInstrumentList();
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
        nom.setText(instrument.getNom());
        classification.setText(instrument.getFamilles().get(0).getclassification().getclassification());
        familleTableView.setItems(FXCollections.observableArrayList(instrument.getFamilles()));
    }
}
