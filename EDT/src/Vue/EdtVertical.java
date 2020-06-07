package Vue;

import Model.Seance;
import Model.Funtions;
import Model.Cache;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class EdtVertical extends JFrame implements ActionListener{
    
    private final int SIZE_X = 800, SIZE_Y = 600, id, type;
    private final LocalDate debut;
    private Seance[] seances, seancesAnnule;
    private final String nom;
    private JButton[] button;
    private LocalDate[] debutDates;
    private JPanel panel = new JPanel();
    private JMenuBar mb = new JMenuBar();
    private JMenu submenu = new JMenu("Emploi du temps");
    private JMenu deco = new JMenu("Deconnexion");
    private JMenuItem vertical = new JMenuItem("EDT Vertical", new ImageIcon("./src/image/edtvertical.png"));
    private JMenuItem horizontal = new JMenuItem("EDT Horizontal", new ImageIcon("./src/image/edthorizontal.png"));
    private JMenuItem liste = new JMenuItem("EDT Liste", new ImageIcon("./src/image/edtliste.png"));
    private JMenuItem deconnexion = new JMenuItem("Deconnexion", new ImageIcon("./src/image/disconnect.png"));
    private JMenu recap = new JMenu("Recapitulatif");
    private JMenuItem courS = new JMenuItem("Cours", new ImageIcon("./src/image/recap.png"));
    private JMenu recherche =new JMenu("Recherche");
    private JMenuItem  rechercher= new JMenuItem("Rechercher", new ImageIcon("./src/image/search.png"));
    private JMenu admin = new JMenu("Admin");
    private JMenuItem listeEDT = new JMenuItem("EDT Liste", new ImageIcon("./src/image/editlisteadm.png"));
    
    EdtVertical(Seance[] seancesAnnule,Seance[] seances, LocalDate debut, int id, String nom, int type){
        this.seancesAnnule = seancesAnnule;
        this.seances = seances;
        this.debut = debut;
        this.id = id;
        this.nom = nom;
        this.type = type;
        
        this.setTitle("Emploi du temps");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, SIZE_Y);
        
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
        
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(Color.white);
                g.fillRect(SIZE_X/6, SIZE_Y/7, SIZE_X, 12*SIZE_Y/7);
                g.setColor(new Color(241, 130, 128));
                g.fillRect(SIZE_X/6+1, 1+150*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.fillRect(SIZE_X/6+1, 1+255*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.fillRect(SIZE_X/6+1, 1+360*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.fillRect(SIZE_X/6+1, 1+465*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.fillRect(SIZE_X/6+1, 1+570*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.fillRect(SIZE_X/6+1, 1+675*SIZE_Y/420, SIZE_X, 15*SIZE_Y/420);
                g.setColor(new Color(130, 130, 130));

                for(int i=1; i <= 7; ++i){
                    g.drawLine(i*SIZE_X/6, SIZE_Y/7, i*SIZE_X/6, 13*SIZE_Y/7);
                }
                for(int i=1; i <= 13; ++i){
                    g.drawLine(SIZE_X/6, i*SIZE_Y/7, 7*SIZE_X/6, i*SIZE_Y/7);
                }
                // Afficher les rectangles Couleurs pour les différents cours
                if(seances != null){
                    if(seances.length > 0){
                        int seanceTime, seanceTimeDeb, jour;
                        LocalTime debutCours, finCours, temp;
                        Color color;
                        for(int i=0; i<seances.length; ++i){
                            color = seances[i].getColor();
                            debutCours = seances[i].getDebut();
                            finCours = seances[i].getFin();
                            jour = Funtions.convertWeekOfDay(seances[i].getDate().getDayOfWeek());
                            temp = finCours.minusMinutes(Duration.between(LocalTime.of(0,0,0), debutCours).toMinutes());
                            seanceTime = temp.getHour()*60 + temp.getMinute();
                            seanceTimeDeb = debutCours.getHour()*60 + debutCours.getMinute()-450;
                            g.setColor(color);
                            g.fillRect(jour*SIZE_X/6+1, seanceTimeDeb*SIZE_Y/420+1, SIZE_X/6-1, seanceTime*SIZE_Y/420+1);
                        }
                    }
                }
                if(seancesAnnule != null){
                    if(seancesAnnule.length > 0){
                        int seanceTime, seanceTimeDeb, jour;
                        LocalTime debutCours, finCours, temp;
                        Color color;
                        for(int i=0; i<seancesAnnule.length; ++i){
                            color = seancesAnnule[i].getColor();
                            debutCours = seancesAnnule[i].getDebut();
                            finCours = seancesAnnule[i].getFin();
                            jour = Funtions.convertWeekOfDay(seancesAnnule[i].getDate().getDayOfWeek());
                            temp = finCours.minusMinutes(Duration.between(LocalTime.of(0,0,0), debutCours).toMinutes());
                            seanceTime = temp.getHour()*60 + temp.getMinute();
                            seanceTimeDeb = debutCours.getHour()*60 + debutCours.getMinute()-450;
                            g.setColor(color);
                            g.fillRect(jour*SIZE_X/6+1, seanceTimeDeb*SIZE_Y/420+1, SIZE_X/6-1, seanceTime*SIZE_Y/420+1);
                        }
                    }
                }
                
            }  
        };
        this.addHeuresVert(panel);
        panel.setLayout(null);
        this.addJours();
        int dayOfWeek = 0,timeDuration= 0, timeDurationDeb= 0;
        long min= 0;
        String TITLE;
        LocalTime temp,debutTime,finTime;
        if(seances != null){
            JLabel tf[] = new JLabel[seances.length];
            for(int i=0; i<seances.length; ++i){
                TITLE = "<html>";
                dayOfWeek = Funtions.convertWeekOfDay(seances[i].getDate().getDayOfWeek());
                TITLE += seances[i].getCours();

                for(int z=0; z<seances[i].getEnseignants().length; ++z){
                    TITLE += "<br>M." + seances[i].getEnseignants()[z].getNom().toUpperCase();
                }
                TITLE += "<br><span style='font-size:8'>"; 
                for(int z=0; z<seances[i].getGroupes().length; ++z){
                    TITLE += seances[i].getGroupes()[z].getPromotion() + "-" + seances[i].getGroupes()[z].getNom()+ " ";
                }
                for(int z=0; z<seances[i].getLieu().length; ++z){
                    TITLE += "<br>Salle " + seances[i].getLieu()[z].getSalle() + " - " + seances[i].getLieu()[z].getSite();
                }
                TITLE += "</span></html>";
                debutTime = seances[i].getDebut();
                finTime = seances[i].getFin();
                min = Duration.between(LocalTime.of(0, 0, 0), debutTime).toMinutes();
                temp = finTime.minusMinutes(min);
                timeDuration = temp.getHour()*60 + temp.getMinute();
                timeDurationDeb = debutTime.getHour()*60 + debutTime.getMinute() - 450;
                tf[i] = new JLabel(TITLE, JLabel.CENTER);
                tf[i].setBounds(dayOfWeek*SIZE_X/6, timeDurationDeb*SIZE_Y/420, SIZE_X/7, timeDuration*SIZE_Y/420);
                panel.add(tf[i]);
            }
        }
        
        if(seancesAnnule != null){
            JLabel tf[] = new JLabel[seancesAnnule.length];
            for(int i=0; i<seancesAnnule.length; ++i){
                TITLE = "<html>ANNULE<br>";
                dayOfWeek = Funtions.convertWeekOfDay(seancesAnnule[i].getDate().getDayOfWeek());
                TITLE += seancesAnnule[i].getCours();

                for(int z=0; z<seancesAnnule[i].getEnseignants().length; ++z){
                    TITLE += "<br>M." + seancesAnnule[i].getEnseignants()[z].getNom().toUpperCase();
                }
                TITLE += "<br><span style='font-size:8'>"; 
                for(int z=0; z<seancesAnnule[i].getGroupes().length; ++z){
                    TITLE += seancesAnnule[i].getGroupes()[z].getPromotion() + "-" + seancesAnnule[i].getGroupes()[z].getNom()+ " ";
                }
                for(int z=0; z<seancesAnnule[i].getLieu().length; ++z){
                    TITLE += "<br>Salle " + seancesAnnule[i].getLieu()[z].getSalle() + " - " + seancesAnnule[i].getLieu()[z].getSite();
                }
                TITLE += "</span></html>";
                debutTime = seancesAnnule[i].getDebut();
                finTime = seancesAnnule[i].getFin();
                min = Duration.between(LocalTime.of(0, 0, 0), debutTime).toMinutes();
                temp = finTime.minusMinutes(min);
                timeDuration = temp.getHour()*60 + temp.getMinute();
                timeDurationDeb = debutTime.getHour()*60 + debutTime.getMinute() - 450;
                tf[i] = new JLabel(TITLE, JLabel.CENTER);
                tf[i].setBounds(dayOfWeek*SIZE_X/6, timeDurationDeb*SIZE_Y/420, SIZE_X/7, timeDuration*SIZE_Y/420);
                panel.add(tf[i]);
            }
        }
        JLabel label = new JLabel("Nom : " + nom.toUpperCase());
        label.setFont(new Font("Avenir", 1, 9));
        label.setBounds(20, 55, label.getPreferredSize().width, 12);
        panel.add(label);
        button = new JButton[20];
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int actualWeek = debut.get(weekFields.weekOfWeekBasedYear()), d = 0;
        LocalDate temporalDate = debut.minusDays(7*8);
        debutDates = new LocalDate[20];
        
        for(int i=1; i<=20; i++){
            debutDates[i-1] = temporalDate;
            button[i-1] = new JButton(temporalDate.get(weekFields.weekOfWeekBasedYear())+"");
            button[i-1].setBounds(d, 20, button[i-1].getPreferredSize().width, button[i-1].getPreferredSize().height);
            button[i-1].setBackground(new Color(47,111,119));
            button[i-1].setForeground(Color.WHITE);
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
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(0, 0, 14000, 19000);
        
        this.add(scrollPane);
        panel.setPreferredSize(new Dimension(950,1150));
        // Afficher le frame
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    
        
    public void addHeuresVert(JPanel edt){
        JLabel zz = new JLabel("10h00", JLabel.CENTER);
        zz.setFont(new java.awt.Font("Avenir", 1, 9));
        zz.setBounds(6*SIZE_X/60, 143*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(zz);
        JLabel Az = new JLabel("10h15", JLabel.CENTER);
        Az.setFont(new java.awt.Font("Avenir", 1, 9));
        Az.setBounds(6*SIZE_X/60, 157*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(Az);
        
        JLabel pause1 = new JLabel("11h45", JLabel.CENTER);
        pause1.setFont(new java.awt.Font("Avenir", 1, 9));
        pause1.setBounds(6*SIZE_X/60, 248*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause1);
        JLabel pause2 = new JLabel("12h00", JLabel.CENTER);
        pause2.setFont(new java.awt.Font("Avenir", 1, 9));
        pause2.setBounds(6*SIZE_X/60, 262*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause2);
        
        JLabel pause12 = new JLabel("13h30", JLabel.CENTER);
        pause12.setFont(new java.awt.Font("Avenir", 1, 9));
        pause12.setBounds(6*SIZE_X/60, 353*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause12);
        JLabel pause22 = new JLabel("13h45", JLabel.CENTER);
        pause22.setFont(new java.awt.Font("Avenir", 1, 9));
        pause22.setBounds(6*SIZE_X/60, 367*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause22);
        
        JLabel pause3 = new JLabel("15h15", JLabel.CENTER);
        pause3.setFont(new java.awt.Font("Avenir", 1, 9));
        pause3.setBounds(6*SIZE_X/60, 458*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause3);
        JLabel pause4 = new JLabel("15h30", JLabel.CENTER);
        pause4.setFont(new java.awt.Font("Avenir", 1, 9));
        pause4.setBounds(6*SIZE_X/60, 472*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause4);
        
        JLabel pause33 = new JLabel("17h00", JLabel.CENTER);
        pause33.setFont(new java.awt.Font("Avenir", 1, 9));
        pause33.setBounds(6*SIZE_X/60, 563*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause33);
        JLabel pause43 = new JLabel("17h15", JLabel.CENTER);
        pause43.setFont(new java.awt.Font("Avenir", 1, 9));
        pause43.setBounds(6*SIZE_X/60, 577*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause43);
        
        JLabel pause5 = new JLabel("18h45", JLabel.CENTER);
        pause5.setFont(new java.awt.Font("Avenir", 1, 9));
        pause5.setBounds(6*SIZE_X/60, 668*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause5);
        JLabel pause6 = new JLabel("19h00", JLabel.CENTER);
        pause6.setFont(new java.awt.Font("Avenir", 1, 9));
        pause6.setBounds(6*SIZE_X/60, 682*SIZE_Y/420, 35*SIZE_X/420, 20);
        edt.add(pause6);
        
        
        int heure = 8;
        JLabel[] tabLab = new JLabel[14];
        for(int i=1; i <= 13; ++i){
            
            tabLab[i] = new JLabel(heure+"h30", JLabel.CENTER);
            tabLab[i].setFont(new java.awt.Font("Avenir", 1, 9));
            tabLab[i].setBounds(6*SIZE_X/60, (i*SIZE_Y/7)-(7*SIZE_Y/420), 35*SIZE_X/420, 20);
            
            if(heure != 15 && heure != 13)
                edt.add(tabLab[i]);
            heure++;
        }
    }
    
    public void addJours(){
        JLabel[] jours = new JLabel[6];
        LocalDate date = debut;
        jours[0] = new JLabel("Lundi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[0].setFont(new Font("Avenir", 1, 9));
        jours[0].setBounds(SIZE_X/6, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[0]);
        date = date.plusDays(1);
        jours[1] = new JLabel("Mardi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[1].setFont(new Font("Avenir", 1, 9));
        jours[1].setBounds(2*SIZE_X/6, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[1]);
        date = date.plusDays(1);
        jours[2] = new JLabel("Mercredi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[2].setFont(new Font("Avenir", 1, 9));
        jours[2].setBounds(3*SIZE_X/6, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[2]);
        date = date.plusDays(1);
        jours[3] = new JLabel("Jeudi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[3].setFont(new Font("Avenir", 1, 9));
        jours[3].setBounds(4*SIZE_X/6, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[3]);
        date = date.plusDays(1);
        jours[4] = new JLabel("Vendredi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[4].setFont(new Font("Avenir", 1, 9));
        jours[4].setBounds(5*SIZE_X/6, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[4]);
        date = date.plusDays(1);
        jours[5] = new JLabel("Samedi " + date.getDayOfMonth() , JLabel.CENTER);
        jours[5].setFont(new Font("Avenir", 1, 9));
        jours[5].setBounds(SIZE_X, 3*SIZE_Y/70, SIZE_X/6, SIZE_Y/7);
        panel.add(jours[5]);
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
                        new EdtVertical(seancesAnnule, seances, targetDebutDate, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    case 2:
                        seances = Funtions.seancesEnseignant(id, 1, targetDebutDate, targetFinDate);
                        seancesAnnule = Funtions.seancesEnseignant(id, 2, targetDebutDate, targetFinDate);
                        new EdtVertical(seancesAnnule, seances, targetDebutDate, id, nom, type);
                        this.setVisible(false);
                        this.dispose();
                        break;
                    case 3:
                        seances = Funtions.seancesSalle(id, 1, targetDebutDate, targetFinDate);
                        seancesAnnule = Funtions.seancesSalle(id, 2, targetDebutDate, targetFinDate);
                        new EdtVertical(seancesAnnule, seances, targetDebutDate, id, nom, type);
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
            new EdtVertical(seancesAnnule, seances, debut, id, nom, type);
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

