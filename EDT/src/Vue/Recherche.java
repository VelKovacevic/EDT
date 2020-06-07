package Vue;

import Model.Seance;
import Model.Funtions;
import java.awt.Color;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import javax.swing.*;


public class Recherche extends JFrame implements ActionListener{
    private final int SIZE_X = 400, SIZE_Y = 200;
    private JButton chercher;
    private String[] affichage = {"En grille", "En liste"}, type = {"Enseignant", "Etudiant", "Salle"};
    private JComboBox AFFICHAGE, TYPE;
    private JTextField saisi;
    
    public Recherche(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        AFFICHAGE = new JComboBox(affichage);
        AFFICHAGE.setBounds(100, 60, AFFICHAGE.getPreferredSize().width, AFFICHAGE.getPreferredSize().height);
        panel.add(AFFICHAGE);
        TYPE = new JComboBox(type);
        TYPE.setBounds(200, 60, TYPE.getPreferredSize().width, TYPE.getPreferredSize().height);
        panel.add(TYPE);
        
        JLabel Affichage = new JLabel("Affichage");
        Affichage.setBounds(100, 40, Affichage.getPreferredSize().width, Affichage.getPreferredSize().height);
        panel.add(Affichage);
        
        JLabel Type = new JLabel("Type");
        Type.setBounds(200, 40, Type.getPreferredSize().width, Type.getPreferredSize().height);
        panel.add(Type);
        
        JLabel Saisir = new JLabel("Saisir (nom prenom ou salle-site)");
        Saisir.setBounds(100, 88, Saisir.getPreferredSize().width, Saisir.getPreferredSize().height);
        panel.add(Saisir);
        
        saisi = new JTextField();
        saisi.setBounds(100, 105, 190, 20);
        panel.add(saisi);
        
        chercher = new JButton("Rechercher");
        chercher.setBounds(100, 127, 190, 23);
        chercher.setBackground(new Color(38,114,236));
        chercher.setForeground(Color.WHITE);
        chercher.addActionListener(this);
        panel.add(chercher);
        this.add(panel);
        this.setTitle("Recherche");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == chercher){
            String typeRecherche = TYPE.getSelectedItem().toString();
            String affichageRecherche = AFFICHAGE.getSelectedItem().toString();
            String saisie = saisi.getText();
            
            if(!saisie.isEmpty()){
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    System.out.println("Connecting to database...");
                    conn = DriverManager.getConnection(Funtions.DB_URL, Funtions.USER, Funtions.PASS);
        
                    //STEP 4: Execute a query
                    System.out.println("Creating statement...");
        
                    stmt = conn.createStatement();
                }catch(SQLException se){
                    //Handle errors for JDBC
                    se.printStackTrace();
                }catch(Exception es){
                    //Handle errors for Class.forName
                    es.printStackTrace();
                }
                
                try{
                    String sql = "";
                    int id = 0;
                    if(typeRecherche.equals("Enseignant")){
                        String[] split = saisie.split(" ");
                        sql = "SELECT * FROM utilisateur WHERE utilisateur.NOM LIKE \"" + split[0] + "%\" AND utilisateur.PRENOM LIKE \"%" + split[split.length - 1] + "\"";
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            id = rs.getInt("ID");
                        }
                        if(id == 0){
                            JOptionPane.showMessageDialog(null, "Aucun nom correspondant...");
                        }else{
                            LocalDate debut, fin, now = LocalDate.now();
                            debut = now.minusDays(Funtions.convertWeekOfDay(now.getDayOfWeek())-1);
                            fin = debut.plusDays(6);
                            Seance[] seances = Funtions.seancesEnseignant(id, 1, debut, fin);
                            Seance[] seancesAnnule = Funtions.seancesEnseignant(id, 2, debut, fin);
                            if(affichageRecherche.equals("En grille")){
                                new EdtVertical(seancesAnnule, seances, debut, id, saisie, 2);
                            }else{
                                new ListEdt(seancesAnnule, seances, debut, id, saisie, 2);
                            }
                            this.setVisible(false);
                            this.dispose();
                        }
                    }else if(typeRecherche.equals("Etudiant")){
                        String[] split = saisie.split(" ");
                        sql = "SELECT\n" +
                            "	utilisateur.*,\n" +
                            "    etudiant.ID_GROUPE\n" +
                            "FROM 	\n" +
                            "	utilisateur \n" +
                            "JOIN \n" +
                            "	etudiant ON etudiant.ID_UTILISATEUR = utilisateur.ID\n" +
                            "WHERE \n" +
                            "	utilisateur.NOM LIKE \"%" + split[0] + "%\" AND utilisateur.PRENOM LIKE \"%" + split[split.length - 1] + "%\"";
                        
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            id = rs.getInt("ID_GROUPE");
                        }
                        if(id == 0){
                            JOptionPane.showMessageDialog(null, "Aucun nom correspondant...");
                        }else{
                            LocalDate debut, fin, now = LocalDate.now();
                            debut = now.minusDays(Funtions.convertWeekOfDay(now.getDayOfWeek())-1);
                            fin = debut.plusDays(6);
                            Seance[] seances = Funtions.seancesUtilisateur(id, 1, debut, fin);
                            Seance[] seancesAnnule = Funtions.seancesUtilisateur(id, 2, debut, fin);
                            if(affichageRecherche.equals("En grille")){
                                new EdtVertical(seancesAnnule, seances, debut, id, saisie, 1);
                            }else{
                                new ListEdt(seancesAnnule, seances, debut, id, saisie, 1);
                            }
                            this.setVisible(false);
                            this.dispose();
                        }
                    }else{
                        String[] split = saisie.split("-");
                        if(split.length == 2){
                            sql = "SELECT\n" +
                            "	salle.ID\n" +
                            "FROM\n" +
                            "	salle\n" +
                            "JOIN\n" +
                            "	site ON salle.ID_SITE = site.ID\n" +
                            "WHERE\n" +
                            "	salle.NOM = \"" + split[0] + "\" AND site.NOM = \"" + split[1] + "\"";
                            
                            rs = stmt.executeQuery(sql);
                            while(rs.next()){
                                id = rs.getInt("ID");
                            }
                            if(id == 0){
                                JOptionPane.showMessageDialog(null, "Aucune salle correspondante...");
                            }else{
                                LocalDate debut, fin, now = LocalDate.now();
                                debut = now.minusDays(Funtions.convertWeekOfDay(now.getDayOfWeek())-1);
                                fin = debut.plusDays(6);
                                Seance[] seances  = Funtions.seancesSalle(id, 1, debut, fin);
                                Seance[] seancesAnnule  = Funtions.seancesSalle(id, 2, debut, fin);
                                if(affichageRecherche.equals("En grille")){
                                    new EdtVertical(seancesAnnule, seances, debut, id, saisie,3);
                                }else{
                                    new ListEdt(seancesAnnule, seances, debut, id, saisie,3);
                                }
                                this.setVisible(false);
                                this.dispose();
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Format du nom incorect...");
                        }
                    }
                    rs = stmt.executeQuery(sql);
                    int idTD = 0;
                    while(rs.next()){
                        idTD = rs.getInt("ID");
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
                
            }else{
                JOptionPane.showMessageDialog(null, "Veuillez remplir le champs de saisie.");
            }
        }
    }
}
