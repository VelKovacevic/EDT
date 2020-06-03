/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

/**
 *
 * @author florentfonsalas
 */
public class Enseignant extends Utilisateur {

    ///Attributs
    Cours cours;

    /**
     *
     */
    public Enseignant() {
        super();
    }

    /**
     *
     * @param id
     * @param email
     * @param password
     * @param nom
     * @param prenom
     * @param droit
     * @param cours
     */
    public Enseignant(int id, String email, String password, String nom, String prenom, int droit, Cours cours) {
        super(id, email, password, nom, prenom, droit);
        this.cours = cours;
    }

    /**
     * Constructeur surchargé copie
     *
     * @param enseignant
     */
    public Enseignant(Enseignant enseignant) {
        super(enseignant);
        this.cours = enseignant.cours;
    }

    ///Méthodes
    //Getter
    public Cours getCours() {
        return cours;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public int getDroit() {
        return droit;
    }

    //Setter
    public void setCours(Cours cours) {
        this.cours = cours;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setDroit(int droit) {
        this.droit = droit;
    }

    @Override
    public String toString() {
        return super.toString() + "cours=" + cours;
    }
}
