package GUI;

import edt.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import javax.swing.*;


public class ListeCours extends JFrame implements ActionListener{
    private final int SIZE_Y = 600, SIZE_X = 800;
    private Seance[] seances;
    private String[] titleColumns = {"Matière - Public", "Première séance", "Dernière séance", "Durée", "Nb."};
    private JTable coursTable;
    private Object[][] COURS;
    private JButton VOIR;
    private final LocalDate debut;
    private final int id, type;
    private final String nom;
    
    public ListeCours(Seance[] seances, LocalDate debut, int id, String nom, int type){
        this.seances = seances;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        loadCours();
        
        coursTable = new JTable(COURS, titleColumns){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        this.setTitle("Racpitulatif de cours");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        this.getContentPane().add(new JScrollPane(coursTable), BorderLayout.CENTER);
        VOIR = new JButton("Voir le detail");
        VOIR.addActionListener(this);
        this.getContentPane().add(VOIR, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void loadCours(){
            
            ArrayList<Seance> first = new ArrayList<>(), 
                last = new ArrayList<>();
            ArrayList<String> cours = new ArrayList<>();
            ArrayList<LocalTime> temps = new ArrayList<>();
            ArrayList<Integer> nb = new ArrayList<>();
            LocalTime total = LocalTime.of(0,0);
            int pos;
            long duree;
            for(int i=0; i<seances.length; ++i){
                for(int e=0; e<seances[i].getGroupes().length; ++e){
                    if(cours.contains(seances[i].getCours()+ "-" +seances[i].getGroupes()[e].getPromotion() + seances[i].getGroupes()[e].getNom())) {
                        pos = cours.indexOf(seances[i].getCours());
                        duree = Duration.between(seances[i].getDebut(), seances[i].getFin()).toMinutes();
                        
                        temps.set(pos, temps.get(pos).plusMinutes(duree));
                        nb.set(pos, nb.get(pos)+1);
                        if(last.get(pos).getDate().isBefore(seances[i].getDate()))
                            last.set(pos, seances[i]);
                        else if(last.get(pos).getDate().isEqual(seances[i].getDate())){
                            if(last.get(pos).getFin().isBefore(seances[i].getFin())){
                                last.set(pos, seances[i]);
                            }
                        }

                        if(first.get(pos).getDate().isAfter(seances[i].getDate()))
                            first.set(pos, seances[i]);
                        else if(first.get(pos).getDate().isEqual(seances[i].getDate())){
                            if(first.get(pos).getDebut().isBefore(seances[i].getDebut())){
                                first.set(pos, seances[i]);
                            }
                        }
                    }else{
                        nb.add(1);
                        cours.add(seances[i].getCours()+ "-" +seances[i].getGroupes()[e].getPromotion() + seances[i].getGroupes()[e].getNom());
                        first.add(seances[i]);
                        last.add(seances[i]);
                        temps.add(LocalTime.of(0, 0).plusMinutes(Duration.between(seances[i].getDebut(), seances[i].getFin()).toMinutes()));
                    }
                }
            }
            COURS = new Object[cours.size()][5];
            String str;
            for(int i=0; i<cours.size(); ++i){
                COURS[i][0] = cours.get(i);
                str = Funtions.convertWeekOfDaytoString(first.get(i).getDate().getDayOfWeek());
                str += " " + first.get(i).getDate().getDayOfMonth() + "/" + first.get(i).getDate().getMonthValue() + "/" + first.get(i).getDate().getYear();
                str += " de " + Funtions.timeToString(first.get(i).getDebut().getHour()) + "H" + Funtions.timeToString(first.get(i).getDebut().getMinute());
                str += " à " + Funtions.timeToString(first.get(i).getFin().getHour()) + "H" + Funtions.timeToString(first.get(i).getFin().getMinute());
                COURS[i][1] = str;
                str = Funtions.convertWeekOfDaytoString(last.get(i).getDate().getDayOfWeek());
                str += " " + last.get(i).getDate().getDayOfMonth() + "/" + last.get(i).getDate().getMonthValue() + "/" + first.get(i).getDate().getYear();
                str += " de " + Funtions.timeToString(last.get(i).getDebut().getHour()) + "H" + Funtions.timeToString(last.get(i).getDebut().getMinute());
                str += " à " + Funtions.timeToString(last.get(i).getFin().getHour()) + "H" + Funtions.timeToString(last.get(i).getFin().getMinute());
                COURS[i][2] = str;
                str = temps.get(i).getHour() + "H" + Funtions.timeToString(temps.get(i).getMinute());
                COURS[i][3] = str;
                COURS[i][4] = nb.get(i) + "";
            }
            
            
//               
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == VOIR){
            int[] row = coursTable.getSelectedRows();
            if(row.length > 0){
                String title = coursTable.getValueAt(row[0], 0).toString();
                String[] split = title.split("-");
                String cours = split[0], TD = split[1];
                int idEns = Cache.utilisateur.getId();
                
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
                    String sql = "SELECT ID FROM groupe WHERE groupe.NOM = \"" + TD + "\"";
                    rs = stmt.executeQuery(sql);
                    int idTD = 0;
                    while(rs.next()){
                        idTD = rs.getInt("ID");
                    }
                    sql = "SELECT \n" +
                        "    seance.*\n" +
                        "FROM \n" +
                        "    seance\n" +
                        "JOIN \n" +
                        "	seance_enseignants ON seance.ID = seance_enseignants.ID_SEANCE\n" +
                        "JOIN\n" +
                        "	seance_groupe ON seance.ID = seance_groupe.ID_SEANCE\n" +
                        "WHERE\n" +
                        "	seance_groupe.ID_GROUPE = " + idTD + " AND seance_enseignants.ID_ENSEIGNANT = " + idEns + " ORDER BY seance.DATE ASC, seance.HEURE_DEBUT ASC";
                    rs = stmt.executeQuery(sql);
                    ArrayList<LocalDate> date = new ArrayList<>();
                    ArrayList<LocalTime> from = new ArrayList<>(), to = new ArrayList<>(), dur = new ArrayList<>();
                    LocalTime tem1, tem2;
                    while(rs.next()){
                        date.add(rs.getDate("DATE").toLocalDate());
                        tem1 = rs.getTime("HEURE_DEBUT").toLocalTime();
                        tem2 = rs.getTime("HEURE_FIN").toLocalTime();
                        from.add(tem1);
                        to.add(tem2);
                        long duree = Duration.between(tem1, tem2).toMinutes();
                        dur.add(LocalTime.of(0,0).plusMinutes(duree));
                    }
                    Object[][] temp = new Object[date.size()][4];
                    for(int i=0; i<date.size(); ++i){
                        temp[i][0] = Funtions.convertWeekOfDaytoString(date.get(i).getDayOfWeek()) + " " + date.get(i).getDayOfMonth() + "/" + date.get(i).getMonthValue() + "/" + date.get(i).getYear();
                        temp[i][1] = Funtions.timeToString(from.get(i).getHour()) + "H" + Funtions.timeToString(from.get(i).getMinute());
                        temp[i][2] = Funtions.timeToString(to.get(i).getHour()) + "H" + Funtions.timeToString(to.get(i).getMinute());
                        temp[i][3] = Funtions.timeToString(dur.get(i).getHour()) + "H" + Funtions.timeToString(dur.get(i).getMinute());
                    }
                    new EnsCours(temp);

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