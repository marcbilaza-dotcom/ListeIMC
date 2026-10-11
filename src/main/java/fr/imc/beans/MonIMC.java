package fr.imc.beans;

import java.io.Serializable;

/**
 * Question 1 : Bean MonIMC.
 * Un Bean = classe publique, Serializable, constructeur sans argument,
 * attributs privés accessibles par getters / setters.
 */
public class MonIMC implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;               // identifiant (utile pour Modifier / Supprimer)
    private String nom;
    private String prenom;
    private double masse;         
    private double taille;        
    private double imc;
    private String interpretation;

    public MonIMC() {
    }

    public MonIMC(int id, String nom, String prenom, double masse, double taille) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.masse = masse;
        this.taille = taille;
        calculer();
    }

    /* Calcule l'IMC (masse / taille²) et son interprétation selon les normes OMS. */
    public void calculer() {
        if (taille > 0) {
            this.imc = masse / (taille * taille);
            this.interpretation = interpreter(this.imc);
        } else {
            this.imc = 0;
            this.interpretation = "Taille invalide";
        }
    }

    public static String interpreter(double imc) {
        if (imc < 18.5) return "Insuffisance pondérale";
        if (imc < 25)   return "Corpulence normale";
        if (imc < 30)   return "Surpoids";
        if (imc < 35)   return "Obésité modérée";
        if (imc < 40)   return "Obésité sévère";
        return "Obésité morbide";
    }

    // Les Getters et les Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public double getMasse() { return masse; }
    public void setMasse(double masse) { this.masse = masse; }

    public double getTaille() { return taille; }
    public void setTaille(double taille) { this.taille = taille; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getInterpretation() { return interpretation; }
    public void setInterpretation(String interpretation) { this.interpretation = interpretation; }
}
