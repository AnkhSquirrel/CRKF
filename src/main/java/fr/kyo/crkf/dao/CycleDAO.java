package fr.kyo.crkf.dao;

import fr.kyo.crkf.Entity.Cycle;

import java.sql.*;
import java.util.ArrayList;

public class CycleDAO extends DAO<Cycle> {
    protected CycleDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public Cycle getByID(int id) {
        Cycle cycle = null;
        try {

            // Determine the column set column

            String strCmd = "SELECT id_cycle, libelle, cycle from Cycle where id_cycle = ?";
            PreparedStatement s = connexion.prepareStatement(strCmd);
            s.setInt(1,id);
            ResultSet rs = s.executeQuery();

            rs.next();
            cycle =  new Cycle(rs.getInt(1), rs.getString(2), rs.getInt(3));

            rs.close();

        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        return cycle;
    }

    @Override
    public ArrayList<Cycle> getAll(int page) {
        ArrayList<Cycle> liste = new ArrayList<>();
        try (Statement stmt = connexion.createStatement()) {

            // Determine the column set column

            String strCmd = "SELECT id_cycle, libelle, cycle from Cycle order by cycle";
            ResultSet rs = stmt.executeQuery(strCmd);

            while (rs.next()) {
                liste.add(new Cycle(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            rs.close();
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public int insert(Cycle objet) {
        try {
            String requete = "INSERT INTO Cycle (libelle,cycle) VALUES (?,?)";
            PreparedStatement  preparedStatement = connexion().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString( 1 , objet.getLibelle());
            preparedStatement.setInt(2, objet.getCycle());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        }catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public boolean update(Cycle object) {
        try {
            String requete = "UPDATE Cycle SET libelle = ?, cycle = ? WHERE id_cycle = ?";
            PreparedStatement  preparedStatement = connexion().prepareStatement(requete);
            preparedStatement.setString(1, object.getLibelle());
            preparedStatement.setInt(2, object.getCycle());
            preparedStatement.setInt(3, object.getId_cycle());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Cycle object) {
        try {
            String requete = "DELETE FROM Cycle WHERE id_cycle=?";
            PreparedStatement preparedStatement = connexion().prepareStatement(requete);
            preparedStatement.setInt(1, object.getId_cycle());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
