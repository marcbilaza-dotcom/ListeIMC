package fr.imc.dao;

import fr.imc.beans.MonIMC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Accès à la base ListeIMC (table patient) via JDBC.
 */
public class PatientDAO {

    private static final String URL  = "jdbc:mysql://localhost:3306/ListeIMC?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String MDP  = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Pilote MySQL introuvable", e);
        }
    }

    private Connection connexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, MDP);
    }

    public List<MonIMC> lister() throws SQLException {
        List<MonIMC> liste = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, masse, taille, imc, interpretation FROM patient ORDER BY nom, prenom";
        try (Connection c = connexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(lire(rs));
        }
        return liste;
    }

    public MonIMC trouver(int id) throws SQLException {
        String sql = "SELECT id, nom, prenom, masse, taille, imc, interpretation FROM patient WHERE id = ?";
        try (Connection c = connexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? lire(rs) : null;
            }
        }
    }

    public void ajouter(MonIMC p) throws SQLException {
        p.calculer();
        String sql = "INSERT INTO patient (nom, prenom, masse, taille, imc, interpretation) VALUES (?,?,?,?,?,?)";
        try (Connection c = connexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            remplir(ps, p);
            ps.executeUpdate();
            try (ResultSet cle = ps.getGeneratedKeys()) {
                if (cle.next()) p.setId(cle.getInt(1));
            }
        }
    }

    public void modifier(MonIMC p) throws SQLException {
        p.calculer();
        String sql = "UPDATE patient SET nom=?, prenom=?, masse=?, taille=?, imc=?, interpretation=? WHERE id=?";
        try (Connection c = connexion(); PreparedStatement ps = c.prepareStatement(sql)) {
            remplir(ps, p);
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        try (Connection c = connexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM patient WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private static void remplir(PreparedStatement ps, MonIMC p) throws SQLException {
        ps.setString(1, p.getNom());
        ps.setString(2, p.getPrenom());
        ps.setDouble(3, p.getMasse());
        ps.setDouble(4, p.getTaille());
        ps.setDouble(5, p.getImc());
        ps.setString(6, p.getInterpretation());
    }

    private static MonIMC lire(ResultSet rs) throws SQLException {
        MonIMC p = new MonIMC();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setMasse(rs.getDouble("masse"));
        p.setTaille(rs.getDouble("taille"));
        p.setImc(rs.getDouble("imc"));
        p.setInterpretation(rs.getString("interpretation"));
        return p;
    }
}
