package fr.kyo.crkf.controller;

import fr.kyo.crkf.Entity.Classification;
import fr.kyo.crkf.Entity.Famille;
import fr.kyo.crkf.Entity.Instrument;
import fr.kyo.crkf.Searchable.Filter;
import fr.kyo.crkf.dao.DAOFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CreateInstrumentModalController {
    @FXML
    private GridPane grid;
    @FXML
    private TextField nom;

    private HBox add;
    private Stage modal;
    private int rowsId;
    private int rowsCount;
    private Filter filter;


    @FXML
    private void initialize(){
        filter = new Filter();
        rowsId = 0;
        rowsCount = 0;

        Button button = new Button();
        button.setText("+");
        button.setOnAction(a -> addRow());
        add = new HBox();
        add.setAlignment(Pos.CENTER);
        add.getChildren().setAll(button);
        add.setId("add");
        grid.addRow(rowsId,add);
    }

    @FXML
    private void addRow(){
        Label label = new Label();
        label.setText("Famille : ");

        ComboBox<Famille> comboBox = new ComboBox<>();
        boolean insert = setComboboxItem(comboBox);
        if(insert && rowsCount < 5){
            Button button = new Button();
            int id = rowsId;
            button.setOnAction(a -> removeRow(id));
            button.setText("Delete");

            HBox hBox = new HBox();
            hBox.getChildren().setAll(label,comboBox,button);
            hBox.setAlignment(Pos.CENTER);
            hBox.setMinHeight(80);
            hBox.setMaxHeight(80);
            hBox.setId(String.valueOf(id));

            grid.getChildren().remove(add);
            grid.addRow(rowsId, hBox);
            rowsId++;
            rowsCount++;
            if(rowsCount < 5)
                grid.addRow(rowsId + 1,add);
        }

    }

    private boolean setComboboxItem(ComboBox<Famille> comboBox) {
        if(grid.getChildren().get(0) == add){
            comboBox.setItems(FXCollections.observableArrayList(filter.getFamilles()));
            comboBox.getSelectionModel().select(0);
            return true;
        }else{
            HBox hBox = (HBox) grid.getChildren().get(0);
            ComboBox<Famille> comboBoxTemp = (ComboBox<Famille>) hBox.getChildren().get(1);
            if(comboBoxTemp.getSelectionModel().getSelectedItem().getId_famille() != 0){
                ArrayList<Famille> familles = DAOFactory.getFamilleDAO().getByClassification(comboBoxTemp.getSelectionModel().getSelectedItem().getclassification().getId_classification());
                familles.add(0,new Famille(0,"Famille",new Classification(0,"")));
                comboBox.setItems(FXCollections.observableArrayList(familles));
                comboBox.getSelectionModel().select(0);
                comboBoxTemp.setDisable(true);
                return true;
            }
        }
        return false;
    }

    private void removeRow(int row) {

        for(int i = 0; i < grid.getChildren().size(); i++){
            Node node = grid.getChildren().get(i);
            HBox hBox = (HBox) node;
            if((node != grid.getChildren().get(0) && node.getId().equals(String.valueOf(row))) || (node == grid.getChildren().get(0) && rowsCount == 1)){
                grid.getChildren().remove(hBox);
                rowsCount--;
                if(rowsCount == 1){
                    HBox temp = (HBox) grid.getChildren().get(0);
                    ComboBox<Famille> comboBox = (ComboBox<Famille>) temp.getChildren().get(1);
                    comboBox.setDisable(false);
                }
                if (rowsCount == 4){
                    grid.addRow(rowsId + 1,add);
                }

            }
        }
    }

    @FXML
    private void addInstrument(){
        Instrument instrument = new Instrument(0,nom.getText());
        boolean allFamilleSet = getAllFamille(instrument);
        if(!instrument.getNom().equals("") && allFamilleSet){
            if(DAOFactory.getInstrumentDAO().insert(instrument) != 0){
                closeModal();
            }
        }else{
            System.out.println("Erreur");
        }
    }

    private boolean getAllFamille(Instrument instrument) {
        boolean allFamilleSet = true;
        for(Node node : grid.getChildren()){
            HBox hBox = (HBox) node;
            if(!hBox.getId().equals("add")){
                ComboBox<Famille> comboBox = (ComboBox<Famille>) hBox.getChildren().get(1);
                if(comboBox.getSelectionModel().getSelectedItem().getId_famille() != 0){
                    instrument.addFamille(comboBox.getSelectionModel().getSelectedItem());
                    System.out.println(comboBox.getSelectionModel().getSelectedItem());
                }else{
                    allFamilleSet = false;
                }
            }
        }
        return  allFamilleSet;
    }

    @FXML
    private void closeModal(){
        modal.close();
    }

    public void setModal(Stage modal) {
        this.modal = modal;
    }
}
