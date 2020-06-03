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
public class Type_Cours {
     ///Attributs
    private int id;
    private String nom;

    ///Constructeur par défault

    /**
     *
     */
    public Type_Cours() {

    }

    ///Constructeur 

    /**
     *
     * @param id
     * @param nom
     */
    public Type_Cours(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    ///Constructeur bis

    /**
     *
     * @param type_cours
     */
    public Type_Cours(Type_Cours type_cours) {
        this.id = type_cours.id;
        this.nom = type_cours.nom;
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
