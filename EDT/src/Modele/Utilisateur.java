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
public class Utilisateur {

    ///Attributs
    protected int id;
    protected String email;
    protected String password;
    protected String nom;
    protected String prenom;
    protected int droit;

    ///Constructeur par défault 
    public Utilisateur() {

    }

    ///Constructeur 

    /**
     *Constructeur surchargé
     * @param id
     * @param email
     * @param password
     * @param nom
     * @param prenom
     * @param droit
     */
    public Utilisateur(int id, String email, String password, String nom, String prenom, int droit) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.droit = droit;
    }

    ///constructeur bis 

    /**
     * Construcetur à partir d'un autre objet de type Utilisateur
     * @param utilisateur
     */
    public Utilisateur(Utilisateur utilisateur) {
        this.id = utilisateur.id;
        this.email = utilisateur.email;
        this.password = utilisateur.password;
        this.nom = utilisateur.nom;
        this.prenom = utilisateur.prenom;
        this.droit = utilisateur.droit;
    }

    ///Méthodes 
    //Getter

    /**
     *
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return
     */
    public String getNom() {
        return this.nom;
    }

    /**
     *
     * @return
     */
    public String getPrenom() {
        return this.prenom;
    }

    /**
     *
     * @return
     */
    public int getDroit() {
        return this.droit;
    }

    //Setter

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     *
     * @param droit
     */
    public void setDroit(int droit) {
        this.droit = droit;
    }

    
    @Override
    public String toString() {
        return "id: " + this.id + ", email: " + this.email + ", password: " + this.password + ", nom: " + this.nom + ", prenom: " + this.prenom + ", droit: " + this.droit;
    }
}
