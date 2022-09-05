package fr.kyo.crkf.Controller;

import fr.kyo.crkf.Entity.Classification;
import fr.kyo.crkf.Entity.Departement;
import fr.kyo.crkf.Entity.Ecole;
import fr.kyo.crkf.Entity.Ville;
import fr.kyo.crkf.Searchable.Filter;
import fr.kyo.crkf.Searchable.SearchableEcole;
import fr.kyo.crkf.dao.DAOFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

public class EcoleController {

        @FXML
        private TableView<Object> ecoleTable;

        @FXML
        private TableColumn<Ecole, String> nomColumn;

        @FXML
        private TableColumn<Ecole, String> adresseColumn;

        @FXML
        private TableColumn<Ecole, String> villeColumn;

        @FXML
        private TableColumn<Ecole, String> departementColumn;

        @FXML
        private SearchableComboBox<Ville> ville;

        @FXML
        private SearchableComboBox<Departement> departement;

        private Filter filter;

        @FXML
        private Button reset;

        @FXML
        private TextField nomEcole;
        private SearchableEcole searchableEcole;
        @FXML
        private void initialize(){
                 filter = new Filter();

                searchableEcole = new SearchableEcole();

                // Intialisation des colomnes
                nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomStringProperty());
                villeColumn.setCellValueFactory(cellData -> cellData.getValue().getAdresse().getVille().getVilleStringProperty());
                adresseColumn.setCellValueFactory(cellData -> cellData.getValue().getAdresse().getAdresseStringProperty());
                departementColumn.setCellValueFactory(cellData -> cellData.getValue().getAdresse().getVille().getDepartement().getDepartementStringProperty());

                // Intialisation des comboBox
                ville.setItems(FXCollections.observableArrayList(filter.getVilles()));
                ville.valueProperty().addListener(observable -> filter());

                departement.setItems(FXCollections.observableArrayList(filter.getDepartements()));
                departement.valueProperty().addListener(observable -> filterByDepartement());


                nomEcole.textProperty().addListener(observable -> filter());

                ecoleTable.setItems(FXCollections.observableArrayList(DAOFactory.getEcoleDAO().getAll()));

        }

        @FXML
        private void reset(){
                nomEcole.setText("");
                departement.getSelectionModel().selectFirst();
                ville.getSelectionModel().selectFirst();
        }

        @FXML
        private void filter(){
                if(!nomEcole.getText().isEmpty() || !nomEcole.getText().equals(searchableEcole.getNom())) {
                        searchableEcole.setNom(nomEcole.getText());
                }

                if(ville.getSelectionModel().getSelectedItem() != null && ville.getSelectionModel().getSelectedItem() != searchableEcole.getVille()){
                        searchableEcole.setVille(ville.getSelectionModel().getSelectedItem());
                        departement.getSelectionModel().select(ville.getSelectionModel().getSelectedItem().getDepartement().getId_departement());
                }

                if(departement.getSelectionModel().getSelectedItem() != null && departement.getSelectionModel().getSelectedItem() != searchableEcole.getVille().getDepartement() ){
                        searchableEcole.setDepartement(departement.getSelectionModel().getSelectedItem());
                }

                ecoleTable.setItems(FXCollections.observableArrayList(DAOFactory.getEcoleDAO().getLike(searchableEcole)));
        }

        private void filterByDepartement() {
                if (departement.getSelectionModel().getSelectedItem() != null && (departement.getSelectionModel().getSelectedItem()).getId_departement() != 0) {
                        int id = ville.getSelectionModel().getSelectedItem().getId_ville();
                        ville.setItems(FXCollections.observableArrayList(DAOFactory.getVilleDAO().gettByDepartementID(departement.getSelectionModel().getSelectedItem().getId_departement())));
                        ville.getSelectionModel().select(id);
                } else {
                        ville.setItems(FXCollections.observableArrayList(filter.getVilles()));
                        ville.getSelectionModel().select(0);
                }
                filter();
        }


}
