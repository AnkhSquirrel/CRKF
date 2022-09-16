package fr.kyo.crkf.dao;

import fr.kyo.crkf.Entity.Ecole;
import fr.kyo.crkf.Tools.Pair;
import fr.kyo.crkf.Searchable.SearchableEcole;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class EcoleDAO extends DAO<Ecole> {
    private final int lgpage = 25;
    protected EcoleDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public Ecole getByID(int id) {
        Ecole ecole = null;
        try {

            // Determine the column set column

            String strCmd = "SELECT id_ecole, Nom, id_adresse from Ecole where id_ecole = ?";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            s.setInt(1,id);
            ResultSet rs = s.executeQuery();

            rs.next();
            ecole =  new Ecole(rs.getInt(1), rs.getString(2),rs.getInt(3));

            rs.close();

        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        return ecole;
    }

    @Override
    public ArrayList<Ecole> getAll(int page) {
        ArrayList<Ecole> liste = new ArrayList<>();
        try (Statement stmt = connexion.createStatement()) {

            // Determine the column set column

            String strCmd = "SELECT id_ecole, Nom, id_adresse from Ecole order by Nom OFFSET " + lgpage + " * (? -1)  ROWS FETCH NEXT " + lgpage + " ROWS ONLY";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            s.setInt(1,page);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                liste.add(new Ecole(rs.getInt(1), rs.getString(2),rs.getInt(3)));
            }
            rs.close();
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public ArrayList<Ecole> getLike(SearchableEcole searchableEcole, int page) {
        ArrayList<Ecole> liste = new ArrayList<>();
        try {

            // Determine the column set column

            String strCmd = "exec SP_ECOLE_FILTER  @nom = ?, @ville = ?, @departement = ?, @lgpage = 25, @page = ?";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            s.setString(1,searchableEcole.getNom());
            s.setInt(2,searchableEcole.getVille().getId_ville());
            s.setInt(3, searchableEcole.getDepartement().getId_departement());
            s.setInt(4, page);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                Ecole ecole = (new Ecole(rs.getInt(1),rs.getString(2),rs.getInt(3)));

                liste.add(ecole);
            }
            rs.close();
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public ArrayList<Ecole> getByDepartement(int id) {
        ArrayList<Ecole> list = new ArrayList<>();
        try{
            String strCmd = "SELECT id_ecole, Nom, id_adresse from ecole where id_adresse in (select id_adresse from Adresse where id_ville in (select id_ville from Ville where id_departement = ? ))";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            s.setInt(1,id);
            ResultSet rs = s.executeQuery();

            while (rs.next())
                list.add(new Ecole(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            rs.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Pair<Ecole, Double>> getByDistance(float latitudePointA, float longitudePointA, int page) {
        ArrayList<Pair<Ecole, Double>> ecolesEtDistances = new ArrayList<>();
        try{
            // ********* Version avec pagination ******************
            // String strCmd = "SELECT id_ecole, Nom, id_adresse from Ecole order by Nom OFFSET " + lgpage + " * (? -1)  ROWS FETCH NEXT " + lgpage + " ROWS ONLY";
            // ...
            // s.setInt(1,page);
            // _______________________________________________________

            String strCmd = "SELECT id_ecole, Nom, id_adresse from Ecole";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                float latitudePointB = DAOFactory.getAdresseDAO().getByID(rs.getInt(3)).getVille().getLatitude();
                float longitudePointB = DAOFactory.getAdresseDAO().getByID(rs.getInt(3)).getVille().getLongitude();
                double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((Math.toRadians(latitudePointB - latitudePointA)) / 2), 2) + Math.pow(Math.sin((Math.toRadians(longitudePointB - longitudePointA)) / 2), 2) * Math.cos((Math.toRadians(latitudePointA))) * Math.cos(Math.toRadians(latitudePointB)))) * 6371.009;

                if (distance < 50){
                    Ecole ecole = (new Ecole(rs.getInt(1), rs.getString(2),rs.getInt(3)));
                    Pair<Ecole, Double> pair = new Pair<>(ecole, distance);
                    ecolesEtDistances.add(pair);
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ecolesEtDistances.sort(Comparator.comparingDouble(Pair<Ecole,Double>::getSecond));
        return ecolesEtDistances;
    }

    @Override
    public int insert(Ecole objet) {
        try {
            String requete = "INSERT INTO Ecole (Nom,id_adresse) VALUES (?,?)";
            PreparedStatement  preparedStatement = connexion().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString( 1 , objet.getNom());
            preparedStatement.setInt(2, objet.getAdresse().getId_adresse());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            int id = 0;
            if(rs.next())
                id = rs.getInt(1);
            preparedStatement.close();
            return id;
        }catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public boolean update(Ecole object) {
        try {
            String requete = "UPDATE Ecole SET Nom = ?, id_adresse = ? WHERE id_ecole = ?";
            PreparedStatement  preparedStatement = connexion().prepareStatement(requete);
            preparedStatement.setString(1, object.getNom());
            preparedStatement.setInt(2, object.getAdresse().getId_adresse());
            preparedStatement.setInt(3, object.getId_ecole());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Ecole object) {
        try {
            String requete = "DELETE FROM Ecole WHERE id_ecole=?";
            PreparedStatement preparedStatement = connexion().prepareStatement(requete);
            preparedStatement.setInt(1, object.getId_ecole());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
