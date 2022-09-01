package fr.kyo.crkf.Searchable;

import fr.kyo.crkf.Entity.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;


public class SearchableEcole {
    private int id_ecole;
    private String nom;

    private Adresse adresse;

    private Ville ville;

    private Departement departement;

    public SearchableEcole() {
        this.id_ecole = id_ecole;
        this.nom = nom;
        //adresse = new Adresse(0,"", new Ville(0,"",0F,0F,new Departement(0,"")));
    }
    public int getId_ecole() {
        return id_ecole;
    }
    public void setId_ecole(int id_ecole) {
        this.id_ecole = id_ecole;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Departement getDepartement() {
        return departement;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public ObservableValue<String> getNomStringProperty(){
        return new SimpleStringProperty(nom);
    }
}