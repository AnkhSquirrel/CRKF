package fr.kyo.crkf.controller.classification;

import fr.kyo.crkf.ApplicationCRKF;
import fr.kyo.crkf.entity.Classification;
import fr.kyo.crkf.dao.DAOFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class GestionClassificationController {

    @FXML
    private TableColumn<Classification,String> libelleColumn;
    @FXML
    private TableColumn<Classification,Integer> famillesColumn;
    @FXML
    private TextField libelle;
    @FXML
    private TableView<Classification> classificationTable;
    @FXML
    private Label pageNumber;
    @FXML
    private Label numberOfPage;
    private int pageTotale;
    private int page;
    private String classification;
    private ApplicationCRKF applicationCRKF;

    @FXML
    private void initialize(){
        classification = "";
        page = 1;
        libelleColumn.setCellValueFactory(cellData -> cellData.getValue().getClassificationStringProperty());
        famillesColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberFamilles());

        libelle.textProperty().addListener(observable -> filter());

        classificationTable.setItems(FXCollections.observableArrayList(DAOFactory.getClassificationDAO().getLike(classification,page)));

        pageTotale = DAOFactory.getClassificationDAO().getAllClassification(classification) / 25;
        if(pageTotale == 0)
            pageTotale ++;
        numberOfPage.setText(String.valueOf(pageTotale));

        reset();
        filter();
    }

    public void filter(){
        if(!libelle.getText().equals(classification)){
            classification = libelle.getText();
            page = 1;
        }
        classificationTable.setItems(FXCollections.observableArrayList(DAOFactory.getClassificationDAO().getLike(classification,page)));

        pageTotale = DAOFactory.getClassificationDAO().getAllClassification(classification) / 25;
        if (pageTotale == 0)
            pageTotale ++;
        numberOfPage.setText(String.valueOf(pageTotale));

        pageNumber.setText("Page " + page + " / ");
    }

    @FXML
    private void openCreateModal(){
        applicationCRKF.openCreateClassificationModal(this);
    }

    @FXML
    private void reset(){
        libelle.setText("");
    }

    @FXML
    private void pagePlus(){
        if(!classificationTable.getItems().isEmpty() && pageTotale > page){
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
        if (classificationTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer");
            alert.setHeaderText("Voulez vous vraiment supprimer cet element?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                if(classificationTable.getSelectionModel().getSelectedItem().getNumberFamilles().getValue() > 0){
                    alert.close();
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Erreur");
                    alert1.setHeaderText("Il y a eu une erreur lors de la suppresion de la classification.\nIl est impossible de supprimer une classification qui a des familles associées");
                    alert1.showAndWait();
                }else{
                    DAOFactory.getClassificationDAO().delete(classificationTable.getSelectionModel().getSelectedItem());
                    filter();
                }
            }
        }
    }
    @FXML
    private void update(){
        if (classificationTable.getSelectionModel().getSelectedItem() != null)
            applicationCRKF.openUpdateClassificationModal(this, classificationTable.getSelectionModel().getSelectedItem());
    }
}

