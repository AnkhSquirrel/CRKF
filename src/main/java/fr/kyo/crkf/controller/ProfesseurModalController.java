package fr.kyo.crkf.controller;

import fr.kyo.crkf.Entity.*;
import fr.kyo.crkf.Searchable.Filter;
import fr.kyo.crkf.dao.DAOFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class ProfesseurModalController {
    //Professeur
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField cv;
    //Adresse
    @FXML
    private TextField adresse;
    @FXML
    private SearchableComboBox<Departement> departement;
    @FXML
    private ComboBox<Ville> ville;
    @FXML
    private ComboBox<Ecole> ecole;
    private Stage modal;
    private boolean create;
    private ProfesseurController professeurController;
    private Filter filter;

    @FXML
    private void initialize(){
        filter = new Filter();

        ecole.setEditable(true);
        ecole.getEditor().textProperty().addListener(observable -> ecoleFilter());
        ecole.setItems(FXCollections.observableArrayList(filter.getEcolesLike("")));

        cv.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cv.setText(newValue.replaceAll("\\D", ""));
            }
        });

        departement.setItems(FXCollections.observableArrayList(filter.getDepartements()));
        departement.getSelectionModel().selectedItemProperty().addListener(observable -> filterDepartement());
        departement.getSelectionModel().select(0);

        ville.setEditable(true);
        ville.setItems(FXCollections.observableArrayList(filter.getVilleLike("", 0)));
        ville.getSelectionModel().select(0);
    }

    private void filterDepartement() {
        /*if(departement.getSelectionModel().getSelectedItem() != null){
            ville.setItems(FXCollections.observableArrayList(filter.getVilleLike("", departement.getSelectionModel().getSelectedItem().getId_departement())));
            ville.getSelectionModel().select(0);
        }*/

    }

    private void ecoleFilter() {
        if(ecole.getSelectionModel().getSelectedItem() == null || !ecole.getEditor().getText().equals(ecole.getSelectionModel().getSelectedItem().getNom())){
            ecole.setItems(FXCollections.observableArrayList(filter.getEcolesLike(ecole.getEditor().getText())));
        }
    }

    @FXML
    void validate() {
        System.out.println(ville.getSelectionModel().getSelectedItem());
        boolean adresseComplete = ville.getSelectionModel().getSelectedItem() != null &&
                ville.getSelectionModel().getSelectedItem().getId_ville() != 0 &&
                !adresse.getText().isEmpty();
        boolean professeurComplete =!nom.getText().isEmpty() && !prenom.getText().isBlank() && !cv.getText().isBlank() && ecole.getSelectionModel().getSelectedItem() != null && ecole.getSelectionModel().getSelectedItem().getId_ecole() != 0;
        if(adresseComplete && professeurComplete){
            System.out.println("in");
            if(create){
                System.out.println("create");
                createPersonne();
            }
            else
                updatePersonne();
        }
    }

    private void updatePersonne() {
        //TODO update personne
    }

    private void createPersonne() {
        Adresse adresseObject = new Adresse(0, adresse.getText(),ville.getSelectionModel().getSelectedItem().getId_ville());
        int adresseId = DAOFactory.getAdresseDAO().insert(adresseObject);
        if(adresseId != 0){
            Personne personne = new Personne(0,nom.getText(),prenom.getText(),Integer.parseInt(cv.getText()), adresseId,ecole.getSelectionModel().getSelectedItem().getId_ecole());
            if(DAOFactory.getPersonneDAO().insert(personne) != 0){
                professeurController.filter();
                modal.close();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Il y a eu une erreur lors de la création du professeur.\n Merci de vérifier que vous avez entrée des informations valides");
                alert.showAndWait();
            }
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Il y a eu une erreur lors de la création de l'adresse.\n Merci de vérifier que vous avez entrée des informations valides");
            alert.showAndWait();
        }
    }

    @FXML
    void closeModal() {
        modal.close();
    }

    public void setModal(Stage modal) {
        this.modal = modal;
    }

    public void setCreate(boolean bool) {
        this.create = bool;
    }

    public void setProfesseurController(ProfesseurController professeurController) {
        this.professeurController = professeurController;
    }
}
