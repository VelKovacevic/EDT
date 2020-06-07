package Vue;

import Model.Seance;
import Model.Groupe;
import Model.Funtions;
import java.awt.Color;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;

public class AddGroupe extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 200;
    private final JButton cancel, ajouter;
    private final JTextField saisie;
    private final LocalDate debut; 
    private final String nom;
    private final int id, type;
    private final Seance seance;

    public AddGroupe(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        
        this.setTitle("Ajout de groupe");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel label = new JLabel("Veuillez entrer le groupe (PROMO GROUPE)");
        label.setBounds(40, 50, label.getPreferredSize().width, 20);
        panel.add(label);
        
        cancel = new JButton("Annuler");
        cancel.addActionListener(this);
        cancel.setBounds(60, 120, cancel.getPreferredSize().width, 20);
        cancel.setBackground(new Color(38,114,236));
        cancel.setForeground(Color.WHITE);
        panel.add(cancel);
        
        ajouter = new JButton("Ajouter");
        ajouter.addActionListener(this);
        ajouter.setBounds(70 + cancel.getPreferredSize().width, 120, ajouter.getPreferredSize().width, 20);
        ajouter.setBackground(new Color(38,114,236));
        ajouter.setForeground(Color.WHITE);
        panel.add(ajouter);
        
        saisie = new JTextField();
        saisie.setBounds(40, 80, label.getPreferredSize().width, 25);
        panel.add(saisie);
        
        this.add(panel);
        
        
        // Afficher le frame
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancel){
            switch (type) {
                case 1:
                    {
                        Seance[] seances = Funtions.seancesUtilisateur(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesUtilisateur(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                case 2:
                    {
                        Seance[] seances = Funtions.seancesEnseignant(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesEnseignant(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                case 3:
                    {
                        Seance[] seances = Funtions.seancesSalle(id, 1, debut, debut.plusDays(6));
                        Seance[] seancesAnnule = Funtions.seancesSalle(id, 2, debut, debut.plusDays(6));
                        new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    }
                default:
                    break;
            }
        }else if(e.getSource() == ajouter){
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
                String nomEntier = saisie.getText();
                String[] split = nomEntier.split(" ");
                if(split.length != 2){
                    // FORMAT NON RESPECTE
                }else{
                    String sql = "SELECT \n" +
                            "	groupe.ID\n" +
                            "FROM \n" +
                            "	groupe\n" +
                            "JOIN \n" +
                            "	promotion ON promotion.ID = groupe.ID\n" +
                            "WHERE \n" +
                            "	groupe.NOM LIKE \"%" + split[1] + "%\" AND promotion.NOM LIKE \"%" + split[0] + "%\"";
                
                    rs = stmt.executeQuery(sql);
                    int idGrp = 0;
                    while(rs.next()){
                        idGrp = rs.getInt("ID");
                    }
                    
                    if(idGrp == 0){
                        JOptionPane.showMessageDialog(null, "Ce groupe n'existe pas");
                    }else{
                        Groupe[] grps = seance.getGroupes();
                        boolean doublons = false;
                        for(int i=0; i<grps.length; ++i){
                            if(grps[i].getNom().toUpperCase().equals(split[1].toUpperCase()) && grps[i].getPromotion().toUpperCase().equals(split[0].toUpperCase()))
                                doublons = true;
                        }
                        if(doublons){
                            JOptionPane.showMessageDialog(null, "Le groupe participe deja à la seance");
                        }else{
                            int capaciteSalle = 0;
                            for(int i=0; i<seance.getLieu().length; ++i){
                                sql = "SELECT \n" +
                                    "	salle.CAPACITE\n" +
                                    "FROM\n" +
                                    "	salle\n" +
                                    "JOIN\n" +
                                    "	site ON salle.ID_SITE = site.ID\n" +
                                    "WHERE \n" +
                                    "	site.NOM = \"" + seance.getLieu()[i].getSite() + "\" AND salle.NOM = \"" + seance.getLieu()[i].getSalle() + "\"";
                                rs = stmt.executeQuery(sql);
                                
                                while(rs.next()){
                                    capaciteSalle += rs.getInt("CAPACITE");
                                }
                            }
                            int nbEleve = 0;
                            for(int i=0; i<seance.getGroupes().length; ++i){
                                sql = "SELECT \n" +
                                    "	COUNT(*) AS TOTAL\n" +
                                    "FROM \n" +
                                    "	etudiant\n" +
                                    "JOIN\n" +
                                    "	groupe ON groupe.ID = etudiant.ID_GROUPE\n" +
                                    "JOIN\n" +
                                    "	promotion ON promotion.ID = groupe.ID_PROMOTION\n" +
                                    "WHERE\n" +
                                    "	promotion.NOM = \"" + seance.getGroupes()[i].getPromotion() + "\" AND "
                                        + "groupe.NOM = \"" + seance.getGroupes()[i].getNom() + "\"";
                                rs = stmt.executeQuery(sql);
                                
                                while(rs.next()){
                                    nbEleve += rs.getInt("TOTAL");
                                }
                            }
                            
                            sql = "SELECT \n" +
                            "	COUNT(*) AS TOTAL\n" +
                            "FROM \n" +
                            "	etudiant\n" +
                            "JOIN\n" +
                            "	groupe ON groupe.ID = etudiant.ID_GROUPE\n" +
                            "JOIN\n" +
                            "	promotion ON promotion.ID = groupe.ID_PROMOTION\n" +
                            "WHERE\n" +
                            "	promotion.NOM LIKE \"%" + split[0] + "%\" AND groupe.NOM LIKE \"%" + split[1] + "%\"";
                            rs = stmt.executeQuery(sql);
                            int newTD = 0;
                            while(rs.next()){
                                newTD = rs.getInt("TOTAL");
                            }
                            
                            if(capaciteSalle < (nbEleve + newTD)){
                                JOptionPane.showMessageDialog(null, "La capacite des salles n'est pas assez grande pour accuillir tous les élèves");
                            }else{
                                boolean groupeIndispo = false;
                                String finT = seance.getFin().getHour() + ":" + seance.getFin().getMinute()
                                    , debutT = seance.getDebut().getHour() + ":" + seance.getDebut().getMinute();
                                sql = "SELECT\n" +
                                        "	COUNT(*) AS TOTAL\n" +
                                        "FROM \n" +
                                        "	seance_groupe\n" +
                                        "JOIN\n" +
                                        "	seance ON seance.ID = seance_groupe.ID_SEANCE\n" +
                                        "WHERE \n" +
                                        "	seance_groupe.ID_GROUPE = " + idGrp + " AND seance.DATE = \"" + seance.getDate().toString() + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                                rs = stmt.executeQuery(sql);
                                int total = 2;
                                while(rs.next()){
                                    total = rs.getInt("TOTAL");
                                }
                                if(total != 0)
                                    groupeIndispo = true;
                                if(groupeIndispo){
                                    JOptionPane.showMessageDialog(null, "GROUPE INDISPONIBLE SUR CETTE HORAIRE");
                                }else{
                                    sql = "INSERT INTO `seance_groupe` (`ID_SEANCE`, `ID_GROUPE`) VALUES ('" + seance.getId() + "', '" + idGrp + "');";
                                    stmt.executeUpdate(sql);
                                    // AJOUT REUSSI !
                                    switch (type) {
                                        case 1:
                                            {
                                                Seance[] seances = Funtions.seancesUtilisateur(id, 1, debut, debut.plusDays(6));
                                                Seance[] seancesAnnule = Funtions.seancesUtilisateur(id, 2, debut, debut.plusDays(6));
                                                new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                this.setVisible(false);
                                                this.dispose();
                                                break;
                                            }
                                        case 2:
                                            {
                                                Seance[] seances = Funtions.seancesEnseignant(id, 1, debut, debut.plusDays(6));
                                                Seance[] seancesAnnule = Funtions.seancesEnseignant(id, 2, debut, debut.plusDays(6));
                                                new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                this.setVisible(false);
                                                this.dispose();
                                                break;
                                            }
                                        case 3:
                                            {
                                                Seance[] seances = Funtions.seancesSalle(id, 1, debut, debut.plusDays(6));
                                                Seance[] seancesAnnule = Funtions.seancesSalle(id, 2, debut, debut.plusDays(6));
                                                new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
                                                this.setVisible(false);
                                                this.dispose();
                                                break;
                                            }
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
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
        }
    }
}
