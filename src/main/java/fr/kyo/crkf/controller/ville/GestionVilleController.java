package fr.kyo.crkf.controller.ville;

import fr.kyo.crkf.ApplicationCRKF;
import fr.kyo.crkf.Entity.Departement;
import fr.kyo.crkf.Entity.Ville;
import fr.kyo.crkf.Searchable.Filter;
import fr.kyo.crkf.Searchable.SearchableVille;
import fr.kyo.crkf.dao.DAOFactory;
import fr.kyo.crkf.dao.VilleDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

public class GestionVilleController {
    @FXML
    private TableView<Ville> villeTable;
    @FXML
    private TableColumn<Ville, String> villeColumn;
    @FXML
    private TableColumn<Ville, String> departementColumn;
    @FXML
    private SearchableComboBox<Departement> departement;
    @FXML
    private TextField libelle;
    private String ville;
    private int page;
    private ApplicationCRKF applicationCRKF;
    private int departementId;

    @FXML
    private void openMainMenu(){
        applicationCRKF.openMainMenu();
    }

    public void setApplicationCRKF(ApplicationCRKF applicationCRKF){

        this.applicationCRKF = applicationCRKF;
    }

    @FXML
    private void initialize(){
        departementId = 0;
        ville = "";
        page = 1;
        Filter filter = new Filter();

        // intialize TableView
        villeColumn.setCellValueFactory(cellDate -> cellDate.getValue().getVilleStringProperty());
        departementColumn.setCellValueFactory(cellDate -> cellDate.getValue().getDepartement().getDepartementStringProperty());

        // initialize Combobox
        departement.setItems(FXCollections.observableArrayList(filter.getDepartements()));
        departement.getSelectionModel().selectedItemProperty().addListener(observable -> filter());
        departement.getSelectionModel().select(0);

        libelle.textProperty().addListener(observable -> filter());

        villeTable.setItems(FXCollections.observableArrayList(DAOFactory.getVilleDAO().getLike(ville, departement.getSelectionModel().getSelectedItem().getId_departement())));
    }

    private void filter(){
       if(!libelle.getText().equals(ville)){
           ville = libelle.getText();
           page = 1;
       }
        if(departement.getSelectionModel().getSelectedItem() != null ){
           departementId = departement.getSelectionModel().getSelectedItem().getId_departement();
            page = 1;
        }
        villeTable.setItems(FXCollections.observableArrayList(DAOFactory.getVilleDAO().getLike(ville, departementId)));
    }
    @FXML
    private void openCreateModal(){
        applicationCRKF.openModalCreateVille(this);
    }
    @FXML
    private void openUpdateModal(){
        applicationCRKF.openModalUpdateVille(this, villeTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void reset(){
        libelle.setText("");
        departement.getSelectionModel().select(0);
    }
    @FXML
    private void pagePlus(){
        if(!villeTable.getItems().isEmpty()){
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
}
