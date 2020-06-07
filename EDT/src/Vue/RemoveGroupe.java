package Vue;

import Model.Seance;
import Model.Groupe;
import Model.Funtions;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import javax.swing.*;


public class RemoveGroupe extends JFrame implements ActionListener{
    private final int SIZE_X = 300, SIZE_Y = 200;
    private Object[][] groupeInfo;
    private LocalDate debut; 
    private String nom;
    private int id, type;
    private Seance seance;
    private String[] title = {"Promotion", "Groupe"};
    private JTable groupeTable;
    private JButton remove, back;
    
    public RemoveGroupe(Seance seance, LocalDate debut, int id, String nom, int type){
        this.seance = seance;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Enlever un groupe");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        
        JPanel panel = new JPanel();
        
        back = new JButton("Retour");
        back.addActionListener(this);
        panel.add(back);
        
        remove = new JButton("Supprimer");
        remove.addActionListener(this);
        panel.add(remove);
        
        if(seance != null){
            if(seance.getGroupes() != null){
                groupeInfo = new Object[seance.getGroupes().length][2];
        
                for(int i=0; i<seance.getGroupes().length; ++i){
                    groupeInfo[i][0] = seance.getGroupes()[i].getPromotion();
                    groupeInfo[i][1] = seance.getGroupes()[i].getNom();
                }

                groupeTable = new JTable(groupeInfo, title){
                    @Override
                    public boolean isCellEditable(int row, int column){
                        return false;
                    }
                };
                JScrollPane s = new JScrollPane(groupeTable);
                this.getContentPane().add(s, BorderLayout.CENTER);    
            }
        }
        
        this.getContentPane().add(panel, BorderLayout.SOUTH);
        
        // Afficher le frame
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back){
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
        }else if(e.getSource() == remove){
            int[] selection = groupeTable.getSelectedRows();
            if(selection.length > 0){
                String promo = groupeTable.getValueAt(selection[0], 0).toString();
                String groupe = groupeTable.getValueAt(selection[0], 1).toString();
                
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
                    String sql = "SELECT\n" +
                            "	groupe.ID\n" +
                            "FROM \n" +
                            "	groupe\n" +
                            "JOIN\n" +
                            "	promotion ON promotion.ID = groupe.ID_PROMOTION\n" +
                            "WHERE\n" +
                            "	groupe.NOM = \"" + groupe + "\" AND promotion.NOM = \"" + promo + "\"";
                    rs = stmt.executeQuery(sql);
                    int idGrp = 0;
                    
                    while(rs.next()){
                        idGrp = rs.getInt("ID");
                    }
                    if(idGrp != 0){
                        sql = "DELETE FROM seance_groupe WHERE `ID_SEANCE`= " + seance.getId() + " AND `ID_GROUPE` = " + idGrp;
                        stmt.executeUpdate(sql);
                        int a=0;
                        Groupe[] tempsa = new Groupe[seance.getGroupes().length-1];
                        for(int i=0; i<seance.getGroupes().length; ++i){
                            
                            if(i == selection[0])
                                --a;
                            else{
                                tempsa[a] = seance.getGroupes()[i];
                            }
                            ++a;
                        }
                        seance.setGroupes(tempsa);
                        new RemoveGroupe(seance, debut, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
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
}
