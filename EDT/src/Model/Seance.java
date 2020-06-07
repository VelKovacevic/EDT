package Model;

import java.awt.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;

public class Seance {
    private Enseignant[] enseignants;
    private Location[] lieu;
    private Groupe[] groupes;
    private int semaine, id;
    private String cours, typeCours;
    private LocalTime debut, fin;
    private LocalDate date;
    private int etat;
    private Color color;
    
    public Seance(int id, int semaine, String cours, String typeCours, Enseignant[] enseignants,
            Location[] lieu, Groupe[] groupes, LocalTime debut, LocalTime fin, LocalDate date, int etat){
        this.id = id;
        this.semaine = semaine;
        this.cours = cours;
        this.typeCours = typeCours;
        this.enseignants = enseignants;
        this.lieu = lieu;
        this.groupes = groupes;
        this.debut = debut;
        this.fin = fin;
        this.date = date;
        this.etat = etat; 
    }
    
    public Seance(int id, int semaine, String cours, String typeCours, 
            LocalTime debut, LocalTime fin, LocalDate date, int etat){
        this.id = id;
        this.semaine = semaine;
        this.cours = cours;
        this.typeCours = typeCours;
        this.debut = debut;
        this.fin = fin;
        this.date = date;
        this.etat = etat; 
    }

    public Seance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int getSemaine() {
        return semaine;
    }

    public void setSemaine(int semaine) {
        this.semaine = semaine;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public String getTypeCours() {
        return typeCours;
    }

    public void setTypeCours(String typeCours) {
        this.typeCours = typeCours;
    }

    public Enseignant[] getEnseignants() {
        return enseignants;
    }

    public void setEnseignants(Enseignant[] enseignants) {
        this.enseignants = enseignants;
    }

    public Location[] getLieu() {
        return lieu;
    }

    public void setLieu(Location[] lieu) {
        this.lieu = lieu;
    }

    public Groupe[] getGroupes() {
        return groupes;
    }

    public void setGroupes(Groupe[] groupes) {
        this.groupes = groupes;
    }

    public LocalTime getDebut() {
        return debut;
    }

    public void setDebut(LocalTime debut) {
        this.debut = debut;
    }

    public LocalTime getFin() {
        return fin;
    }

    public void setFin(LocalTime fin) {
        this.fin = fin;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int isEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
        
}