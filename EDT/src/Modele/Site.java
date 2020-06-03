
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
public class Site {
     ///Attributs
    private int id;
    private String nom;

    ///Constructeur par défault

    /**
     * Constructeur par défault
     */
    public Site() {

    }

    /**
     * Constructeur
     * @param id
     * @param nom
     */
    public Site(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    ///Constructeur bis

    /**
     * Constructeur à partir d'un autre ojet de type Site
     * @param site
     */
    public Site(Site site) {
        this.id = site.id;
        this.nom = site.nom;
    }

    ///Méthodes
    //Getter

    /**
     * Getter id
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter nom
     * @return
     */
    public String getNom() {
        return this.nom;
    }

    //Setter

    /**
     * Setter id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter nom
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    @Override
    public String toString(){
        return "Id: " + this.id+ ", Nom: " + this.nom;
    }
}
