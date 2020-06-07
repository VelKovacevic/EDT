package Vue;

import Model.Seance;
import Model.Funtions;
import java.awt.Color;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import javax.swing.*;


public class AddSalle extends JFrame implements ActionListener{
    private static int SIZE_X = 300, SIZE_Y = 200;
    private JButton cancel, ajouter;
    private JTextField saisie;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    
    public AddSalle(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        
        this.setTitle("Ajout de salle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel label = new JLabel("Veuillez entrer la salle (SITE SALLE)");
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
                    JOptionPane.showMessageDialog(null, "FORMAT NON RESPECTE");
                }else{
                    String sql = "SELECT\n" +
                            "	salle.ID, salle.CAPACITE\n" +
                            "FROM \n" +
                            "	salle\n" +
                            "JOIN \n" +
                            "	site ON salle.ID_SITE = site.ID\n" +
                            "WHERE\n" +
                            "	salle.NOM LIKE \"%" + split[1] + "%\" AND site.NOM LIKE \"%" + split[0] + "%\"";
                    rs = stmt.executeQuery(sql);
                    int idSalle = 0;
                    int capacite = 0;
                    while(rs.next()){
                        idSalle = rs.getInt("ID");
                        capacite = rs.getInt("CAPACITE");
                    }
                    if(idSalle == 0){
                        JOptionPane.showMessageDialog(null, "Pas de salle correspondante ");
                    }else{
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
                        if(capacite < nbEleve){
                            JOptionPane.showMessageDialog(null, "LA SALLE NE PEUT PAS ACCUEILLIR TOUS LES ELEVES");
                        }else{
                            sql = "SELECT\n" +
                            "	COUNT(*) AS TOTAL\n" +
                            "FROM \n" +
                            "	seance_salles\n" +
                            "WHERE\n" +
                            "	seance_salles.ID_SEANCE = " + seance.getId() + " AND seance_salles.ID_SALLE = " + idSalle;
                            rs = stmt.executeQuery(sql);
                            int total = 2;
                            while(rs.next()){
                                total = rs.getInt("TOTAL");
                            }
                            if(total != 0){
                                JOptionPane.showMessageDialog(null, "CETTE SALLE EST DEJA AFFECTER A CETTE SEANCE");
                            }else{
                                boolean salleIndispo = false;
                                String finT = seance.getFin().getHour() + ":" + seance.getFin().getMinute()
                                    , debutT = seance.getDebut().getHour() + ":" + seance.getDebut().getMinute();
                                sql = "SELECT\n" +
                                        "	COUNT(*) AS TOTAL\n" +
                                        "FROM \n" +
                                        "	seance_salles\n" +
                                        "JOIN\n" +
                                        "	seance ON seance.ID = seance_salles.ID_SEANCE\n" +
                                        "WHERE \n" +
                                        "	seance_salles.ID_SALLE = " + idSalle + " AND seance.DATE = \"" + seance.getDate().toString() + "\" AND ((seance.HEURE_DEBUT < \"" + finT + "\"  AND seance.HEURE_DEBUT >= \"" + debutT + "\") OR (seance.HEURE_FIN <= \"" + finT + "\"  AND seance.HEURE_DEBUT > \"" + debutT + "\"))";
                                rs = stmt.executeQuery(sql);
                                total = 2;
                                while(rs.next()){
                                    total = rs.getInt("TOTAL");
                                }
                                if(total != 0)
                                    salleIndispo = true;
                                if(salleIndispo){
                                    JOptionPane.showMessageDialog(null, "SALLE INDISPONIBLE POUR CE CRENEAU");
                                }else{
                                    sql = "INSERT INTO `seance_salles` (`ID_SEANCE`, `ID_SALLE`) VALUES ('" + seance.getId() + "', '" + idSalle + "');";
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
