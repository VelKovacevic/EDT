package Vue;

import Model.Seance;
import Model.Funtions;
import Model.Cache;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javax.swing.*;

/**
 *
 * @author kovac
 */
public class ListEdt extends JFrame implements ActionListener{
    private final int SIZE_X = 820, SIZE_Y = 600;
    private Seance[] seances, seancesAnnule;
    private String[] titles = {"Jour", "Debut", "Fin", "Cours", "Groupe", "Lieu", "Type"};
    private LocalDate debut;
    private int id, type;
    private String nom;
    private JButton[] button;
    private LocalDate[] debutDates;
    private JMenuBar mb = new JMenuBar();
    private JMenu submenu = new JMenu("Emploi du temps");
    private JMenu deco = new JMenu("Deconnexion");
    private JMenuItem vertical = new JMenuItem("EDT Vertical");
    private JMenuItem horizontal = new JMenuItem("EDT Horizontal");
    private JMenuItem liste = new JMenuItem("EDT Liste");
    private JMenuItem deconnexion = new JMenuItem("Deconnexion");
    private JMenu recap = new JMenu("Recapitulatif");
    private JMenuItem courS = new JMenuItem("Cours");
    private JMenu recherche =new JMenu("Recherche");
    private JMenuItem  rechercher= new JMenuItem("Rechercher");
    private JMenu admin = new JMenu("Admin");
    private JMenuItem listeEDT = new JMenuItem("EDT Liste");
            
    public ListEdt(Seance[] seancesAnnule, Seance[] seances, LocalDate debut, int id, String nom, int type){
        this.seances = seances;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.seancesAnnule = seancesAnnule;
        
        vertical.addActionListener(this);
        horizontal.addActionListener(this);
        liste.addActionListener(this);
        deconnexion.addActionListener(this);
        
        submenu.add(vertical);
        submenu.add(horizontal);
        submenu.add(liste);
        deco.add(deconnexion);
        mb.add(submenu);
        if(type == 2){
            courS.addActionListener(this);
            recap.add(courS);
            mb.add(recap);
        }
        if(Cache.utilisateur.getDroit() == 1){
            listeEDT.addActionListener(this);
            admin.add(listeEDT);
            mb.add(admin);
        }
        if(Cache.utilisateur.getDroit() == 1 || Cache.utilisateur.getDroit() == 2){
            rechercher.addActionListener(this);
            recherche.add(rechercher);
            mb.add(recherche);
        }
        mb.add(deco);
        this.setJMenuBar(mb);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        if(seances != null){
            Object[][] cours = new Object[seances.length][7];
            String str;
            for(int i=0; i<seances.length; ++i){
                cours[i][0] =  Funtions.convertWeekOfDaytoString(seances[i].getDate().getDayOfWeek()) + " " + seances[i].getDate().getDayOfMonth()
                        + "/" + seances[i].getDate().getMonthValue() + "/" + seances[i].getDate().getYear();
                cours[i][1] = Funtions.timeToString(seances[i].getDebut().getHour()) + "H" + Funtions.timeToString(seances[i].getDebut().getMinute());
                cours[i][2] = Funtions.timeToString(seances[i].getFin().getHour()) + "H" + Funtions.timeToString(seances[i].getFin().getMinute());
                cours[i][3] = seances[i].getCours();
                str = "";
                for(int e=0; e<seances[i].getGroupes().length; ++e){
                    str += seances[i].getGroupes()[e].getPromotion() + "-" + seances[i].getGroupes()[e].getNom();
                    if(e != seances[i].getGroupes().length-1)
                        str += " ";
                }
                cours[i][4] = str;
                str = "";
                for(int e=0; e<seances[i].getLieu().length; ++e){
                    str += seances[i].getLieu()[e].getSite() + "-" + seances[i].getLieu()[e].getSalle();
                    if(e != seances[i].getLieu().length-1)
                        str += " ";
                }
                cours[i][5] = str;
                cours[i][6] = seances[i].getTypeCours();
            }
            JTable coursTable = new JTable(cours, titles){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            coursTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            coursTable.getColumnModel().getColumn(1).setMaxWidth(50);
            coursTable.getColumnModel().getColumn(2).setMaxWidth(50);
            coursTable.getColumnModel().getColumn(3).setMaxWidth(100);
            coursTable.getColumnModel().getColumn(4).setPreferredWidth(coursTable.getColumnModel().getColumn(4).getPreferredWidth());
            coursTable.getColumnModel().getColumn(5).setPreferredWidth(coursTable.getColumnModel().getColumn(5).getPreferredWidth());
            coursTable.getColumnModel().getColumn(6).setMaxWidth(50);

            JScrollPane s = new JScrollPane(coursTable);
            s.setBounds(0, 100, 800, 500);
            panel.add(s);
        }
        
        JLabel label = new JLabel("Nom : " + nom.toUpperCase());
        label.setBounds(20, 85, label.getPreferredSize().width, 12);
        button = new JButton[17];
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int actualWeek = debut.get(weekFields.weekOfWeekBasedYear()), d = 0;
        LocalDate temporalDate = debut.minusDays(7*8);
        debutDates = new LocalDate[17];
        for(int i=1; i<=17; i++){
            debutDates[i-1] = temporalDate;
            button[i-1] = new JButton(temporalDate.get(weekFields.weekOfWeekBasedYear())+"");
            button[i-1].setBounds(d, 50, button[i-1].getPreferredSize().width, button[i-1].getPreferredSize().height);
            button[i-1].setBackground(new Color(220,220,220));
            button[i-1].setOpaque(true);
            d += button[i-1].getPreferredSize().width;
            button[i-1].setBorder(null);
            button[i-1].setBorderPainted(false);
            button[i-1].addActionListener(this);
            panel.add(button[i-1]);
            if(temporalDate.get(weekFields.weekOfWeekBasedYear()) == actualWeek){
                button[i-1].setEnabled(false);
            }
            temporalDate = temporalDate.plusDays(7);
        }
        this.add(panel);
        this.setTitle("Emploi du temps (Liste)");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SIZE_X, SIZE_Y);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i=0; i<17; ++i){
            if(e.getSource() == button[i]){
                LocalDate targetDebutDate = debutDates[i];
                LocalDate targetFinDate = targetDebutDate.plusDays(6);
                switch (type) {
                    case 1:
                        seances = Funtions.seancesUtilisateur(id, 1, targetDebutDate, targetFinDate);
                        seancesAnnule = Funtions.seancesUtilisateur(id, 2, targetDebutDate, targetFinDate);
                        new ListEdt(seancesAnnule, seances, targetDebutDate, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    case 2:
                        seances = Funtions.seancesEnseignant(id, 1, targetDebutDate, targetFinDate);
                        seancesAnnule = Funtions.seancesEnseignant(id, 2, targetDebutDate, targetFinDate);
                        new ListEdt(seancesAnnule, seances, targetDebutDate, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    case 3:
                        seances = Funtions.seancesSalle(id, 1, targetDebutDate, targetFinDate);
                        seancesAnnule = Funtions.seancesSalle(id, 2, targetDebutDate, targetFinDate);
                        new ListEdt(seancesAnnule, seances, targetDebutDate, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                }
            }
        }
        if(e.getSource() == deconnexion){
            new Connexion();
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == vertical){
            //new EdtVertical(seances, debut, id, nom, type);
            new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == horizontal){
            new Edt(seancesAnnule,seances, debut, id, nom, type);
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == liste){
            new ListEdt(seancesAnnule, seances, debut, id, nom, type);
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == courS){
            new PageDates(seancesAnnule, seances, debut, id, nom, type);
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == listeEDT){
            new ListEdtAdmin(seancesAnnule, seances, debut, id, nom, type);
            this.setVisible(false);
            this.dispose();
        }else if(e.getSource() == rechercher){
            new Recherche();
            this.setVisible(false);
            this.dispose();
        }
    }
}
