package fr.kyo.crkf.dao;

import fr.kyo.crkf.entity.Diplome;
import fr.kyo.crkf.entity.Personne;
import fr.kyo.crkf.searchable.SearchableProfesseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneDAO extends DAO<Personne> {

    protected PersonneDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Personne getByID(int id) {
        String requete = "select id_personne,Nom,Prenom,VehiculeCV,id_adresse,id_ecole from Personne where id_personne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Personne personne = new Personne(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5), rs.getInt(6));
                getDiplomesOfPersonne(id, personne);
                return personne;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Personne> getAll(int page) {
        List<Personne> liste = new ArrayList<>();
        String requete = "SELECT id_personne,Nom,Prenom,VehiculeCV,id_adresse,id_ecole from Personne order by nom, prenom asc";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(requete);
            while (rs.next()) {
                Personne personne = new Personne(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5), rs.getInt(6));
                getDiplomesOfPersonne(rs.getInt(1), personne);
                liste.add(personne);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public List<Personne> getLike(SearchableProfesseur searchableProfesseur, int page) {
        List<Personne> liste = new ArrayList<>();
        String requete = "exec SP_PROFESSEUR_FILTER  @nometprenom = ?, @vehiculecv = ?, @idville = ?, @iddepartement = ?, @lgpage = 25, @page = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setString(1,searchableProfesseur.getNomEtPrenom());
            preparedStatement.setInt(2,searchableProfesseur.getVehiculeCV());
            preparedStatement.setInt(3,searchableProfesseur.getVilleId());
            preparedStatement.setInt(4,searchableProfesseur.getDepartementId());
            preparedStatement.setInt(5,page);
            ResultSet rs = preparedStatement.executeQuery();
            createPersonneFromResultSet(liste, rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public List<Personne> getLikeAllPersonne(SearchableProfesseur searchableProfesseur) {
        List<Personne> liste = new ArrayList<>();
        String requete = "exec SP_PROFESSEUR_FILTER  @nometprenom = ?, @vehiculecv = ?, @idville = ?, @iddepartement = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete);){
            preparedStatement.setString(1,searchableProfesseur.getNomEtPrenom());
            preparedStatement.setInt(2,searchableProfesseur.getVehiculeCV());
            preparedStatement.setInt(3,searchableProfesseur.getVilleId());
            preparedStatement.setInt(4,searchableProfesseur.getDepartementId());
            ResultSet rs = preparedStatement.executeQuery();
            createPersonneFromResultSet(liste, rs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    public List<Personne> getByEcole (int ecoleId) {
        List<Personne> liste = new ArrayList<>();
        String requete = "SELECT nom, prenom from Personne where id_ecole = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setInt(1, ecoleId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Personne personne = new Personne();
                personne.setPersonneNom(rs.getString(1));
                personne.setPersonnePrenom(rs.getString(2));
                liste.add(personne);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public int insert(Personne objet) {
        int id = 0;
        String requete = "INSERT INTO Personne (Nom,Prenom,VehiculeCV,id_adresse,id_ecole) VALUES (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString( 1 , objet.getPersonneNom());
            preparedStatement.setString(2,objet.getPersonnePrenom());
            preparedStatement.setInt(3,objet.getVehiculeCv());
            preparedStatement.setInt(4,objet.getAdresseId().getAdresseId());
            preparedStatement.setInt(5,objet.getEcoleID().getEcoleId());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()) id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        String requete2 = "INSERT INTO Personne_Diplome (id_cycle,id_personne,id_instrument) VALUES (?,?,?)";
        for(Diplome diplome : objet.getDiplomes()){
            try(PreparedStatement preparedStatement2 = connection.prepareStatement(requete2, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement2.setInt( 1, id);
                preparedStatement2.setInt(2, diplome.getCycle().getCycleId());
                preparedStatement2.setInt(3, diplome.getInstrument().getInstrumentId());
                preparedStatement2.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
                return 0;
            }
        }
        return id;
    }

    @Override
    public boolean update(Personne object) {
        String requete = "UPDATE Personne SET Nom = ?, Prenom = ?, VehiculeCV = ?, id_adresse = ?, id_ecole = ? WHERE id_personne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setString(1, object.getPersonneNom());
            preparedStatement.setString(2, object.getPersonnePrenom());
            preparedStatement.setInt(3, object.getVehiculeCv());
            preparedStatement.setInt(4, object.getAdresseId().getAdresseId());
            preparedStatement.setInt(5, object.getEcoleID().getEcoleId());
            preparedStatement.setInt(6, object.getPersonneId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Personne object) {
        String requete = "DELETE FROM Personne WHERE id_personne=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setInt(1, object.getPersonneId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        String requete2 = "DELETE FROM Personne_Diplome WHERE id_personne=?";
        try (PreparedStatement preparedStatement2 = connection.prepareStatement(requete2)){
            preparedStatement2.setInt(1, object.getPersonneId());
            preparedStatement2.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void getDiplomesOfPersonne(int id, Personne personne){
        String requete = "select id_libelle, id_instrument from Personne_Diplome where id_personne = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)){
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) personne.addDiplome(new Diplome(rs.getInt(1),rs.getInt(2)));
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void createPersonneFromResultSet(List<Personne> liste, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Personne personne = new Personne();
            int id = rs.getInt(1);
            personne.setPersonneId(id);
            personne.setPersonneNom(rs.getString(2));
            personne.setPersonnePrenom(rs.getString(3));
            personne.setVehiculeCv(rs.getInt(4));
            personne.setAdresseId(DAOFactory.getAdresseDAO().getByID(rs.getInt(5)));
            personne.setEcoleID(DAOFactory.getEcoleDAO().getByID(rs.getInt(6)));
            getDiplomesOfPersonne(id, personne);
            liste.add(personne);
        }
    }

}
