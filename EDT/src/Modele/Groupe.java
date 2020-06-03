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
public class Groupe {

    ///Attribut
    private int id;
    private String nom;
    private Promotion promotion;

    /**
     * Constructeur par défault
     */
    public Groupe() {

    }

    /**
     * Constructeur surchargé
     *
     * @param id
     * @param nom
     * @param promotion
     */
    public Groupe(int id, String nom, Promotion promotion) {
        this.id = id;
        this.nom = nom;
        this.promotion = promotion;
    }

    /**
     * Constructeur surchargé à partir d'un objet de type Groupe
     *
     * @param groupe
     */
    public Groupe(Groupe groupe) {
        this.id = groupe.id;
        this.nom = groupe.nom;
        this.promotion = groupe.promotion;
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

    /**
     *
     * @return
     */
    public Promotion getPromotion() {
        return this.promotion;
    }

    //Setter

    /**
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @param promotion
     */
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
    
    @Override
    public String toString(){
        return "id: " + this.id + ", nom: " + this.nom + ", promotion: " + this.promotion;
    }
}
