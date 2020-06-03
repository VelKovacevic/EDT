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
public class Etudiant extends Utilisateur {

    ///Attributs
    private int numero;
    private Groupe groupe;

    /**
     * Constructeur par défault
     */
    public Etudiant() {
        super();
    }

    /**
     * Constructeur surchargé
     *
     * @param id
     * @param email
     * @param password
     * @param nom
     * @param prenom
     * @param droit
     * @param numero
     * @param groupe
     */
    public Etudiant(int id, String email, String password, String nom, String prenom, int droit, int numero, Groupe groupe) {
        super(id, email, password, nom, prenom, droit);
        this.numero = numero;
        this.groupe = groupe;
    }

    /**
     * Constructeur surchargé copie
     *
     * @param etudiant
     */
    public Etudiant(Etudiant etudiant) {
        super(etudiant);
        this.numero = etudiant.numero;
        this.groupe = etudiant.groupe;
    }

    ///Méthodes
    //Getter

    /**
     *
     * @return
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     *
     * @return
     */
    public Groupe getGroupe() {
        return this.groupe;
    }

    //Setter

    /**
     *
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     *
     * @param groupe
     */
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    //
    @Override
    public String toString()
    {
        return super.toString() + ", Numero: " + this.numero + ", Groupe: " + this.groupe;
    }
}
