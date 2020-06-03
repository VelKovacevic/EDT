/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.sql.Time;
import java.util.Date;



/**
 *
 * @author florentfonsalas
 */
public class Seance {
    ///Attributs
    protected int id;
    protected int semaine;
    protected Date date;
    protected Time heure_debut;
    protected Time heure_fin;
    protected Cours cours;
    protected Type_Cours type_cours;
    
    /**
     * Constructeur par défault
     */
    public Seance(){
        
    }

    /**
     *
     * @param id
     * @param semaine
     * @param date
     * @param heure_debut
     * @param heure_fin
     * @param cours
     * @param type_cours
     */
    public Seance(int id, int semaine, Date date, Time heure_debut, Time heure_fin, Cours cours, Type_Cours type_cours) {
        this.id = id;
        this.semaine = semaine;
        this.date = date;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.cours = cours;
        this.type_cours = type_cours;
    }
    
    /**
     *
     * @param seance
     */
    public Seance(Seance seance){
        this.id = seance.id;
        this.semaine = seance.semaine;
        this.date = seance.date;
        this.heure_debut = seance.heure_debut;
        this.heure_fin = seance.heure_fin;
        this.cours = seance.cours;
        this.type_cours = seance.type_cours;
    }
    
    ///Méthodes
    //Getter

    /**
     *
     * @return
     */

    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getSemaine() {
        return semaine;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return
     */
    public Time getHeure_debut() {
        return heure_debut;
    }

    /**
     *
     * @return
     */
    public Time getHeure_fin() {
        return heure_fin;
    }

    /**
     *
     * @return
     */
    public Cours getCours() {
        return cours;
    }

    /**
     *
     * @return
     */
    public Type_Cours getType_cours() {
        return type_cours;
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
     * @param semaine
     */
    public void setSemaine(int semaine) {
        this.semaine = semaine;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @param heure_debut
     */
    public void setHeure_debut(Time heure_debut) {
        this.heure_debut = heure_debut;
    }

    /**
     *
     * @param heure_fin
     */
    public void setHeure_fin(Time heure_fin) {
        this.heure_fin = heure_fin;
    }

    /**
     *
     * @param cours
     */
    public void setCours(Cours cours) {
        this.cours = cours;
    }

    /**
     *
     * @param type_cours
     */
    public void setType_cours(Type_Cours type_cours) {
        this.type_cours = type_cours;
    }

    @Override
    public String toString() {
        return "Seance{" + "id=" + id + ", semaine=" + semaine + ", date=" + date + ", heure_debut=" + heure_debut + ", heure_fin=" + heure_fin + ", cours=" + cours + ", type_cours=" + type_cours + '}';
    }
    
    
    
}
