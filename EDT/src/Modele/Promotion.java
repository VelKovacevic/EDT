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
public class Promotion {

    ///Attributs
    private int id;
    private String nom;

    ///Constructeur par défault

    /**
     *
     */
    public Promotion() {

    }

    ///Constructeur 

    /**
     *
     * @param id
     * @param nom
     */
    public Promotion(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    ///Constructeur bis

    /**
     *
     * @param promotion
     */
    public Promotion(Promotion promotion) {
        this.id = promotion.id;
        this.nom = promotion.nom;
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
    public String getNom() {
        return this.nom;
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
