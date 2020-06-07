/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.time.*;
import java.util.*;
/**
 *
 * @author kovac
 */
public class Funtions {
    // Connexion DB
    public final static String DB_URL="jdbc:mysql://localhost/edt";
    public final static String USER ="root", PASS="";
    private final static Color[] colors = {new Color(104,242,216), new Color(174,219,105), new Color(242,194,104),
                            new Color(235,131,129), new Color(130,217,140),new Color(242,234,131),new Color(219,173,129),new Color(245,149,185),
                            new Color(174,155,235), new Color(235,189,98), new Color(110, 170, 219), new Color(155,219,145), new Color(235, 106, 101)};
    // ETAT : 1 -> VALIDE / 2 -> ANNULE / 3 -> EN COURS DE VALIDATION
    public static Seance[] seancesUtilisateur(int ID_GROUPE, int ETAT, LocalDate debut, LocalDate fin){
        Seance[] seances = null;
        
        Connection conn = null;
        Statement stmt = null, stmt2 = null;
        ResultSet rs = null, rs2 = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            String sql = "SELECT \n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM \n" +
                        "	seance_groupe\n" +
                        "INNER JOIN\n" +
                        "	seance ON seance_groupe.ID_SEANCE = ID \n" +
                        "WHERE \n" +
                        "seance_groupe.ID_GROUPE = " + ID_GROUPE + " AND seance.ETAT = " + ETAT + 
                        " AND seance.DATE <= \"" + fin + "\" AND seance.DATE >= \"" + debut + "\"";
            rs = stmt.executeQuery(sql);

            int total = 0;
            while(rs.next()){
                total = rs.getInt("TOTAL");
            }

            if(total == 0){
                // Send message to say that the connexion is not established
                System.out.print("No seances");
                seances = null;
            }else{
                seances = new Seance[total];
           
                sql = "SELECT\n" +
                    "	seance.ID,\n" +
                    "    SEMAINE,\n" +
                    "    DATE, \n" +
                    "    HEURE_DEBUT,\n" +
                    "    HEURE_FIN,\n" +
                    "    ETAT,\n" +
                    "    cours.NOM AS COURS_NAME,\n" +
                    "    type_cours.NOM AS TYPE_COURS\n" +
                    "FROM\n" +
                    "	seance \n" +
                    "INNER JOIN \n" +
                    "	type_cours ON ID_TYPE = type_cours.ID\n" +
                    "INNER JOIN\n" +
                    "	cours ON seance.ID_COURS = cours.ID\n" +
                    "INNER JOIN\n" +
                    "	seance_groupe ON seance_groupe.ID_SEANCE = seance.ID\n" +
                    "WHERE \n" +
                    "	seance_groupe.ID_GROUPE = " + ID_GROUPE + " AND seance.ETAT = " + ETAT +
                    " AND seance.DATE <= \"" + fin + "\" AND seance.DATE >= \"" + debut + "\""    ;
                rs = stmt.executeQuery(sql);
                int id, semaine, etat, e=0, posColor=0;
                String coursName, typeCours;
                LocalDate date;
                LocalTime timeDebut, timeFin;
                ArrayList<String> colorCours = new ArrayList<>(); 
                
                while(rs.next()){
                    id = rs.getInt("ID");
                    semaine = rs.getInt("SEMAINE");
                    etat = rs.getInt("ETAT");
                    coursName = rs.getString("COURS_NAME");
                    typeCours = rs.getString("TYPE_COURS");
                    date = rs.getDate("DATE").toLocalDate();
                    timeDebut = rs.getTime("HEURE_DEBUT").toLocalTime();
                    timeFin = rs.getTime("HEURE_FIN").toLocalTime();
                    if(colorCours.contains(coursName)){
                        posColor = colorCours.indexOf(coursName);
                    }else{
                        colorCours.add(coursName);
                        posColor = colorCours.size() - 1;
                    }
                    seances[e] = new Seance(id, semaine, coursName, typeCours, timeDebut, timeFin, date, etat);
                    seances[e].setColor(colors[posColor]);
                    ++e;
                }
                int totalTemp = 0;
                Enseignant[] enseignants;
                Location[] lieu;
                Groupe[] groupes;
                for(int i=0; i<seances.length; ++i){
                    id = seances[i].getId();
                    
                    // SELECT ALL ENSEIGNANT
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    enseignants = new Enseignant[totalTemp];
                    sql = "SELECT\n" +
                        "	utilisateur.*,\n" +
                        "    cours.NOM AS COURS_NOM\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        " JOIN\n" +
                        "	enseignant ON enseignant.ID_UTILISATEUR = utilisateur.ID\n" +
                        " JOIN \n" +
                        "	cours ON cours.ID = enseignant.ID_COURS\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    ArrayList<String> cours;
                    String email, name, prenom;
                    while(rs.next()){
                        int ID = rs.getInt("ID");
                        email = rs.getString("EMAIL");
                        name = rs.getString("NOM");
                        prenom = rs.getString("PRENOM");
                        
                        sql = "SELECT cours.NOM FROM enseignant JOIN cours ON cours.ID = enseignant.ID_COURS WHERE enseignant.ID_UTILISATEUR = " + ID;
                        rs2 = stmt2.executeQuery(sql);
                        cours = new ArrayList<>();
                        
                        while(rs2.next()){
                            cours.add(rs2.getString("NOM"));
                        }
                        enseignants[e] = new Enseignant(ID, email, name, prenom, cours);
                        ++e;
                    }
                    seances[i].setEnseignants(enseignants);
                    
                    // SELECT INFO LOCATION COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    lieu = new Location[totalTemp];
                    sql = "SELECT\n" +
                        "	salle.NOM AS SALLE_NOM,\n" +
                        "    site.NOM AS SITE_NOM\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    while(rs.next()){
                        lieu[e] = new Location(rs.getString("SITE_NOM"), rs.getString("SALLE_NOM"));
                        ++e;
                    }
                    seances[i].setLieu(lieu);
                    
                    // SELECT GROUPS OF COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "INNER JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    groupes = new Groupe[totalTemp];
                    
                    sql = "SELECT\n" +
                        "	groupe.NOM, promotion.NOM AS NAME\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "JOIN promotion ON promotion.ID = groupe.ID_PROMOTION WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    while(rs.next()){
                        groupes[e] = new Groupe(rs.getString("NOM"), rs.getString("NAME"));
                        ++e;
                    }
                    seances[i].setGroupes(groupes);
                }
                
            }
            
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            System.out.print("Closing connection...");
            rs.close();
            stmt.close();
            stmt2.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try   
        return seances;
    }
    
    public static Seance[] seancesEnseignant(int ID_ENSEIGNANT, int ETAT, LocalDate debut, LocalDate fin){
        Seance[] seances = null;
        
        Connection conn = null;
        Statement stmt = null, stmt2 = null;
        ResultSet rs = null, rs2 = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            String sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM \n" +
                        "	seance_enseignants\n" +
                        "JOIN\n" +
                        "	seance ON seance_enseignants.ID_SEANCE = seance.ID\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_ENSEIGNANT = " + ID_ENSEIGNANT + 
                        " AND seance.DATE >= \"" + debut +
                        "\" AND seance.DATE <= \"" + fin + "\" AND seance.ETAT = " + ETAT;
            rs = stmt.executeQuery(sql);

            int total = 0;
            while(rs.next()){
                total = rs.getInt("TOTAL");
            }

            if(total == 0){
                // Send message to say that the connexion is not established
                System.out.print("No seances");
            }else{
                seances = new Seance[total];
           
                sql = "SELECT\n" +
                    "	seance.ID,\n" +
                    "    SEMAINE,\n" +
                    "    DATE, \n" +
                    "    HEURE_DEBUT,\n" +
                    "    HEURE_FIN,\n" +
                    "    ETAT,\n" +
                    "    cours.NOM AS COURS_NAME,\n" +
                    "    type_cours.NOM AS TYPE_COURS\n" +
                    "FROM\n" +
                    "	seance \n" +
                    "INNER JOIN \n" +
                    "	type_cours ON ID_TYPE = type_cours.ID\n" +
                    "INNER JOIN\n" +
                    "	cours ON seance.ID_COURS = cours.ID\n" +
                    "INNER JOIN\n" +
                    "	seance_enseignants ON seance_enseignants.ID_SEANCE = seance.ID\n" +
                    "WHERE \n" +
                    "	seance_enseignants.ID_ENSEIGNANT = " + ID_ENSEIGNANT + " AND seance.ETAT = " + ETAT +
                    " AND seance.DATE <= \"" + fin + "\" AND seance.DATE >= \"" + debut + "\""    ;
                rs = stmt.executeQuery(sql);
                int id, semaine, etat, e=0;
                String coursName, typeCours;
                LocalDate date;
                LocalTime timeDebut, timeFin;


                while(rs.next()){
                    id = rs.getInt("ID");
                    semaine = rs.getInt("SEMAINE");
                    etat = rs.getInt("ETAT");
                    coursName = rs.getString("COURS_NAME");
                    typeCours = rs.getString("TYPE_COURS");
                    date = rs.getDate("DATE").toLocalDate();
                    timeDebut = rs.getTime("HEURE_DEBUT").toLocalTime();
                    timeFin = rs.getTime("HEURE_FIN").toLocalTime();

                    seances[e] = new Seance(id, semaine, coursName, typeCours, timeDebut, timeFin, date, etat);
                    ++e;
                }
                int totalTemp = 0;
                Enseignant[] enseignants;
                Location[] lieu;
                Groupe[] groupes;
                for(int i=0; i<seances.length; ++i){
                    id = seances[i].getId();
                    
                    // SELECT ALL ENSEIGNANT
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    enseignants = new Enseignant[totalTemp];
                    sql = "SELECT\n" +
                        "	utilisateur.*,\n" +
                        "    cours.NOM AS COURS_NOM\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        " JOIN\n" +
                        "	enseignant ON enseignant.ID_UTILISATEUR = utilisateur.ID\n" +
                        " JOIN \n" +
                        "	cours ON cours.ID = enseignant.ID_COURS\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    String email, name, prenom;
                    while(rs.next()){
                        int ID = rs.getInt("ID");
                        email = rs.getString("EMAIL");
                        name = rs.getString("NOM");
                        prenom = rs.getString("PRENOM");
                        
                        sql = "SELECT cours.NOM FROM enseignant JOIN cours ON cours.ID = enseignant.ID_COURS WHERE enseignant.ID_UTILISATEUR = " + ID;
                        rs2 = stmt2.executeQuery(sql);
                        ArrayList<String> cours = new ArrayList<>();
                        while(rs2.next()){
                            cours.add(rs2.getString("NOM"));
                        }
                        enseignants[e] = new Enseignant(ID, email, name, prenom, cours);
                        ++e;
                    }
                    seances[i].setEnseignants(enseignants);
                    
                    // SELECT INFO LOCATION COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    lieu = new Location[totalTemp];
                    sql = "SELECT\n" +
                        "	salle.NOM AS SALLE_NOM,\n" +
                        "    site.NOM AS SITE_NOM\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    while(rs.next()){
                        lieu[e] = new Location(rs.getString("SITE_NOM"), rs.getString("SALLE_NOM"));
                        ++e;
                    }
                    seances[i].setLieu(lieu);
                    
                    // SELECT GROUPS OF COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "INNER JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    groupes = new Groupe[totalTemp];
                    
                    sql = "SELECT\n" +
                        "	groupe.NOM, promotion.NOM AS NAME\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "JOIN promotion ON promotion.ID = groupe.ID_PROMOTION WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    while(rs.next()){
                        groupes[e] = new Groupe(rs.getString("NOM"), rs.getString("NAME"));
                        ++e;
                    }
                    seances[i].setGroupes(groupes);
                }
                
            }
            
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            System.out.print("Closing connection...");
            rs.close();
            stmt.close();
            stmt2.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try   
        return seances;
    }
    
    public static Seance[] seancesSalle(int ID_SALLE, int ETAT, LocalDate debut, LocalDate fin){
        Seance[] seances = null;
        
        Connection conn = null;
        Statement stmt = null, stmt2 = null;
        ResultSet rs = null, rs2 = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            String sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM \n" +
                        "	seance_salles\n" +
                        "JOIN\n" +
                        "	seance ON seance_salles.ID_SEANCE = seance.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SALLE = " + ID_SALLE + 
                        " AND seance.DATE >= \"" + debut +
                        "\" AND seance.DATE <= \"" + fin + "\" AND seance.ETAT = " + ETAT;
            rs = stmt.executeQuery(sql);

            int total = 0;
            while(rs.next()){
                total = rs.getInt("TOTAL");
            }

            if(total == 0){
                // Send message to say that the connexion is not established
                System.out.print("No seances");
            }else{
                seances = new Seance[total];
           
                sql = "SELECT\n" +
                    "	seance.ID,\n" +
                    "    SEMAINE,\n" +
                    "    DATE, \n" +
                    "    HEURE_DEBUT,\n" +
                    "    HEURE_FIN,\n" +
                    "    ETAT,\n" +
                    "    cours.NOM AS COURS_NAME,\n" +
                    "    type_cours.NOM AS TYPE_COURS\n" +
                    "FROM\n" +
                    "	seance \n" +
                    "INNER JOIN \n" +
                    "	type_cours ON ID_TYPE = type_cours.ID\n" +
                    "INNER JOIN\n" +
                    "	cours ON seance.ID_COURS = cours.ID\n" +
                    "INNER JOIN\n" +
                    "	seance_salles ON seance_salles.ID_SEANCE = seance.ID\n" +
                    "WHERE \n" +
                    "	seance_salles.ID_SALLE = " + ID_SALLE + " AND seance.ETAT = " + ETAT +
                    " AND seance.DATE <= \"" + fin + "\" AND seance.DATE >= \"" + debut + "\""    ;
                rs = stmt.executeQuery(sql);
                int id, semaine, etat, e=0;
                String coursName, typeCours;
                LocalDate date;
                LocalTime timeDebut, timeFin;


                while(rs.next()){
                    id = rs.getInt("ID");
                    semaine = rs.getInt("SEMAINE");
                    etat = rs.getInt("ETAT");
                    coursName = rs.getString("COURS_NAME");
                    typeCours = rs.getString("TYPE_COURS");
                    date = rs.getDate("DATE").toLocalDate();
                    timeDebut = rs.getTime("HEURE_DEBUT").toLocalTime();
                    timeFin = rs.getTime("HEURE_FIN").toLocalTime();

                    seances[e] = new Seance(id, semaine, coursName, typeCours, timeDebut, timeFin, date, etat);
                    ++e;
                }
                int totalTemp = 0;
                Enseignant[] enseignants;
                Location[] lieu;
                Groupe[] groupes;
                for(int i=0; i<seances.length; ++i){
                    id = seances[i].getId();
                    
                    // SELECT ALL ENSEIGNANT
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    enseignants = new Enseignant[totalTemp];
                    sql = "SELECT\n" +
                        "	utilisateur.*,\n" +
                        "    cours.NOM AS COURS_NOM\n" +
                        "FROM\n" +
                        "	utilisateur\n" +
                        " JOIN \n" +
                        "	seance_enseignants ON utilisateur.ID = seance_enseignants.ID_ENSEIGNANT\n" +
                        " JOIN\n" +
                        "	enseignant ON enseignant.ID_UTILISATEUR = utilisateur.ID\n" +
                        " JOIN \n" +
                        "	cours ON cours.ID = enseignant.ID_COURS\n" +
                        "WHERE\n" +
                        "	seance_enseignants.ID_SEANCE = " + id;
                    
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    String email, name, prenom;
                    while(rs.next()){
                        int ID = rs.getInt("ID");
                        email = rs.getString("EMAIL");
                        name = rs.getString("NOM");
                        prenom = rs.getString("PRENOM");
                        sql = "SELECT cours.NOM FROM enseignant JOIN cours ON cours.ID = enseignant.ID_COURS WHERE enseignant.ID_UTILISATEUR = " + ID;
                        rs2 = stmt2.executeQuery(sql);
                        ArrayList<String> cours = new ArrayList<>();
                        while(rs2.next()){
                            cours.add(rs2.getString("NOM"));
                        }
                        enseignants[e] = new Enseignant(ID, email, name, prenom, cours);
                        ++e;
                    }
                    seances[i].setEnseignants(enseignants);
                    
                    // SELECT INFO LOCATION COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    lieu = new Location[totalTemp];
                    sql = "SELECT\n" +
                        "	salle.NOM AS SALLE_NOM,\n" +
                        "    site.NOM AS SITE_NOM\n" +
                        "FROM\n" +
                        "	seance_salles\n" +
                        "INNER JOIN \n" +
                        "	salle ON seance_salles.ID_SALLE = salle.ID\n" +
                        "INNER JOIN\n" +
                        "	site ON salle.ID_SITE = site.ID\n" +
                        "WHERE\n" +
                        "	seance_salles.ID_SEANCE = " + id;
                    rs = stmt.executeQuery(sql);
                    e = 0;
                    while(rs.next()){
                        lieu[e] = new Location(rs.getString("SITE_NOM"), rs.getString("SALLE_NOM"));
                        ++e;
                    }
                    seances[i].setLieu(lieu);
                    
                    // SELECT GROUPS OF COURS
                    sql = "SELECT\n" +
                        "	COUNT(*) AS TOTAL\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "INNER JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        totalTemp = rs.getInt("TOTAL");
                    }
                    groupes = new Groupe[totalTemp];
                    
                    sql = "SELECT\n" +
                        "	groupe.NOM, promotion.NOM AS NAME\n" +
                        "FROM\n" +
                        "	groupe\n" +
                        "JOIN\n" +
                        "	seance_groupe ON seance_groupe.ID_GROUPE = groupe.ID\n" +
                        "JOIN promotion ON promotion.ID = groupe.ID_PROMOTION WHERE\n" +
                        "	seance_groupe.ID_SEANCE =" + id;
                    e = 0;
                    rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        groupes[e] = new Groupe(rs.getString("NOM"), rs.getString("NAME"));
                        ++e;
                    }
                    seances[i].setGroupes(groupes);
                }
                
            }
            
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }
        
        try{
            System.out.print("Closing connection...");
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception es){
            //Handle errors for Class.forName
            es.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try   
        return seances;
    }
    
    public static int convertWeekOfDay(DayOfWeek a){
        if(null == a)
            return 0;
        else switch (a) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            default:
                return 0;
        }
    }
    
    public static String convertWeekOfDaytoString(DayOfWeek a){
        if(null == a)
            return "Dimanche";
        else switch (a) {
            case MONDAY:
                return "Lun.";
            case TUESDAY:
                return "Mar.";
            case WEDNESDAY:
                return "Mer.";
            case THURSDAY:
                return "Jeu.";
            case FRIDAY:
                return "Ven.";
            case SATURDAY:
                return "Sam.";
            default:
                return "Dimanche";
        }
    }
    
    public static String timeToString(int time){
        if(time < 10){
            return "0" + time;
        }else 
            return time+"";
    }
}
