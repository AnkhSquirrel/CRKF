package fr.kyo.crkf.Entity;

public class Departement {
    private int id_departement;
    private String numero_departement;
    private String departement;

    public Departement(int id_departement, String numero_departement, String departement) {
        this.id_departement = id_departement;
        this.numero_departement = numero_departement;
        this.departement = departement;
    }

    public int getId_departement() {
        return id_departement;
    }

    public void setId_departement(int id_departement) {
        this.id_departement = id_departement;
    }

    public String getNumero_departement() {
        return numero_departement;
    }

    public void setNumero_departement(String numero_departement) {
        this.numero_departement = numero_departement;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
}
