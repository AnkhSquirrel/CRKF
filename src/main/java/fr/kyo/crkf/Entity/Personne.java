package fr.kyo.crkf.Entity;

import java.util.ArrayList;

public class Personne {
    private String nom;
    private String prenom;
    private int vehiculeCv;
    private Adresse adresse;
    private Ecole embauche;
    private Ecole travaille;

    private ArrayList<Instrument> instruments;
    private ArrayList<Diplome> diplomes;

    public Personne(String nom, String prenom, int vehiculeCv, Adresse adresse, Ecole embauche, Ecole travaille) {
        this.nom = nom;
        this.prenom = prenom;
        this.vehiculeCv = vehiculeCv;
        this.adresse = adresse;
        this.embauche = embauche;
        this.travaille = travaille;
        instruments = new ArrayList<>();
        diplomes = new ArrayList<>();
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public int getVehiculeCv() {
        return vehiculeCv;
    }
    public void setVehiculeCv(int vehiculeCv) {
        this.vehiculeCv = vehiculeCv;
    }
    public Adresse getAdresse() {
        return adresse;
    }
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
    public Ecole getEmbauche() {
        return embauche;
    }
    public void setEmbauche(Ecole embauche) {
        this.embauche = embauche;
    }
    public Ecole getTravaille() {
        return travaille;
    }
    public void setTravaille(Ecole travaille) {
        this.travaille = travaille;
    }
    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }
    public void setInstruments(ArrayList<Instrument> instruments) {
        this.instruments = instruments;
    }
    public ArrayList<Diplome> getDiplomes() {
        return diplomes;
    }
    public void setDiplomes(ArrayList<Diplome> diplomes) {
        this.diplomes = diplomes;
    }
}
