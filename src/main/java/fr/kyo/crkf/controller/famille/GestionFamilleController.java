package fr.kyo.crkf.controller.famille;

import fr.kyo.crkf.ApplicationCRKF;
import fr.kyo.crkf.entity.Classification;
import fr.kyo.crkf.entity.Famille;
import fr.kyo.crkf.searchable.Filter;
import fr.kyo.crkf.searchable.SearchableFamille;
import fr.kyo.crkf.dao.DAOFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.SearchableComboBox;
import java.util.Optional;

public class GestionFamilleController {

    @FXML
    private TableColumn<Famille,String> libelleColumn;
    @FXML
    private TableColumn<Famille,String> classificationColumn;
    @FXML
    private SearchableComboBox<Classification> classification;
    @FXML
    private TextField libelle;
    @FXML
    private TableView<Famille> familleTable;
    @FXML
    private Label pageNumber;
    @FXML
    private Label numberOfPage;
    private int pageTotale;
    private int page;
    private SearchableFamille searchableFamille;
    private ApplicationCRKF applicationCRKF;

    @FXML
    private void initialize(){
        searchableFamille = new SearchableFamille();
        page = 1;
        Filter filter = new Filter();

        libelleColumn.setCellValueFactory(cellData -> cellData.getValue().getFamilleStringProperty());
        classificationColumn.setCellValueFactory(cellData ->cellData.getValue().getclassification().getClassificationStringProperty());

        classification.setItems(FXCollections.observableArrayList(filter.getClassifications()));
        classification.getSelectionModel().selectedItemProperty().addListener(observable -> filter());
        classification.getSelectionModel().select(0);

        libelle.textProperty().addListener((observable, oldValue, newValue) -> {
            libelle.setText(newValue.replaceAll("[\\d'], ""));
            filter();
        });

        familleTable.setItems(FXCollections.observableArrayList(DAOFactory.getFamilleDAO().getLike(searchableFamille,page)));

        pageTotale =DAOFactory.getFamilleDAO().getNumberOfFamilles(searchableFamille) / 25;
        if(pageTotale == 0)
            pageTotale ++;

        numberOfPage.setText(String.valueOf(pageTotale));

        reset();
        filter();
    }

    public void filter(){
        if(!libelle.getText().equals(searchableFamille.getNom())){
            searchableFamille.setNom(libelle.getText());
            page = 1;
        }
        if(classification.getSelectionModel().getSelectedItem() != null && classification.getSelectionModel().getSelectedItem().getClassificationId() != searchableFamille.getClassification()){
            searchableFamille.setClassification(classification.getSelectionModel().getSelectedItem().getClassificationId());
            page = 1;
        }
        familleTable.setItems(FXCollections.observableArrayList(DAOFactory.getFamilleDAO().getLike(searchableFamille,page)));

        pageTotale =DAOFactory.getFamilleDAO().getNumberOfFamilles(searchableFamille) / 25;
        if (pageTotale == 0)
            pageTotale ++;
        numberOfPage.setText(String.valueOf(pageTotale));

        pageNumber.setText("Page " + page + " / ");
    }

    @FXML
    private void openCreateModal(){
        applicationCRKF.openModalCreateFamille(this);
    }

    @FXML
    private void reset(){
        libelle.setText("");
        classification.getSelectionModel().select(0);
    }

    @FXML
    private void pagePlus(){
        if(!familleTable.getItems().isEmpty() && pageTotale > page ){
            page++;
            filter();
        }
    }
    @FXML
    private void pageMoins(){
        if (page > 1){
            page--;
            filter();
        }
    }

    @FXML
    private void lastPage(){
        page = pageTotale;
        filter();
    }

    @FXML
    private void firstPage(){
        page = 1;
        filter();
    }

    public void setApplicationCRKF(ApplicationCRKF applicationCRKF){
        this.applicationCRKF = applicationCRKF;
    }

    @FXML
    private void openMainMenu(){
        applicationCRKF.openMainMenu();
    }

    @FXML
    private void remove(){
        if (familleTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer");
            alert.setHeaderText("Voulez-vous vraiment supprimer cet element?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                DAOFactory.getFamilleDAO().delete(familleTable.getSelectionModel().getSelectedItem());
            filter();
        }
    }

    @FXML
    private void update(){
        if (familleTable.getSelectionModel().getSelectedItem() != null)
            applicationCRKF.openModalUpdateFamille(this, familleTable.getSelectionModel().getSelectedItem());
    }

}

