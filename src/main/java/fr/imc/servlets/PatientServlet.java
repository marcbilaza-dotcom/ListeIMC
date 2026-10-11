package fr.imc.servlets;

import fr.imc.beans.MonIMC;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contrôleur unique de l'application.
 *   GET  /patients                      -> liste des patients (liste.jsp)
 *   GET  /patients?action=ajouter       -> formulaire d'ajout (formulaire.jsp)
 *   GET  /patients?action=modifier&id=N -> formulaire pré-rempli
 *   GET  /patients?action=supprimer&id=N-> suppression puis retour à la liste
 *   POST /patients                      -> création ou mise à jour d'un patient
 */

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private List<MonIMC> patients;
    private AtomicInteger compteur;

    /** Question 2 : redéfinition de init() avec une liste de MonIMC. */
    @Override
    public void init() throws ServletException {
        compteur = new AtomicInteger(0);
        patients = new CopyOnWriteArrayList<>();
        patients.add(new MonIMC(compteur.incrementAndGet(), "DIOP",  "Daouda",   70, 1.70));
        patients.add(new MonIMC(compteur.incrementAndGet(), "FALL",  "Aminata",  52, 1.68));
        patients.add(new MonIMC(compteur.incrementAndGet(), "SOW",   "Moussa",   88, 1.75));
        patients.add(new MonIMC(compteur.incrementAndGet(), "NDIAYE","Fatou",    98, 1.65));
        // La liste est partagée avec les JSP via le scope application
        getServletContext().setAttribute("patients", patients);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "ajouter":
                req.getRequestDispatcher("/WEB-INF/formulaire.jsp").forward(req, resp);
                break;

            case "modifier":
                MonIMC p = trouver(req.getParameter("id"));
                if (p == null) {
                    resp.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                req.setAttribute("patient", p);
                req.getRequestDispatcher("/WEB-INF/formulaire.jsp").forward(req, resp);
                break;

            case "supprimer":
                MonIMC aSupprimer = trouver(req.getParameter("id"));
                if (aSupprimer != null) patients.remove(aSupprimer);
                resp.sendRedirect(req.getContextPath() + "/patients");
                break;

            default:
                req.setAttribute("patients", patients);
                req.getRequestDispatcher("/WEB-INF/liste.jsp").forward(req, resp);
        }
    }

    /** Question 3 : la servlet crée un Bean MonIMC et l'ajoute à la liste. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nom    = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        Double masse  = lireNombre(req.getParameter("masse"));
        Double taille = lireNombre(req.getParameter("taille"));

        // Validation simple
        if (estVide(nom) || estVide(prenom) || masse == null || taille == null
                || masse <= 0 || taille <= 0 || taille > 3) {
            req.setAttribute("erreur",
                    "Veuillez renseigner tous les champs (taille en mètres, ex. 1.70).");
            MonIMC saisie = new MonIMC();
            saisie.setId(lireEntier(req.getParameter("id")));
            saisie.setNom(nom);
            saisie.setPrenom(prenom);
            if (masse != null) saisie.setMasse(masse);
            if (taille != null) saisie.setTaille(taille);
            req.setAttribute("patient", saisie);
            req.getRequestDispatcher("/WEB-INF/formulaire.jsp").forward(req, resp);
            return;
        }

        int id = lireEntier(req.getParameter("id"));
        MonIMC existant = (id > 0) ? trouver(String.valueOf(id)) : null;

        if (existant != null) {
            // Modification
            existant.setNom(nom.trim().toUpperCase());
            existant.setPrenom(prenom.trim());
            existant.setMasse(masse);
            existant.setTaille(taille);
            existant.calculer();
        } else {
            // Création : l'IMC et l'interprétation sont calculés par le Bean
            MonIMC nouveau = new MonIMC(compteur.incrementAndGet(),
                    nom.trim().toUpperCase(), prenom.trim(), masse, taille);
            patients.add(nouveau);
        }

        // Post/Redirect/Get : évite la double soumission en cas de rafraîchissement
        resp.sendRedirect(req.getContextPath() + "/patients");
    }

    // ----- Utilitaires -----

    private MonIMC trouver(String idParam) {
        int id = lireEntier(idParam);
        for (MonIMC p : patients) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    private static boolean estVide(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static Double lireNombre(String s) {
        if (estVide(s)) return null;
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int lireEntier(String s) {
        if (estVide(s)) return 0;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
