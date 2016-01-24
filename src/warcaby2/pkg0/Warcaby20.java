package warcaby2.pkg0;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Rodzina
 */
public class Warcaby20 extends JFrame {

    public JPanel okno;
    int rozmiarX = 8, rozmiarY = 8;
    int rozmiarPionek = 80;
    private Color zPionkiem = Color.ORANGE, bezPionka = Color.GREEN, skupiony = Color.red;
    private ImageIcon bialyPion = null;
    private ImageIcon czarnyPion = null;
    private Sluchacz sluchacz = null;
    private Pole skad = null;
    private Pole[][] pola = new Pole[rozmiarX][rozmiarY];
    private Pole biezace = null;
    public String gracz = "white"; // white or black
    public static ArrayList<pionek> listaCzarnych = new ArrayList();
    public static ArrayList<pionek> listaBialych = new ArrayList();
    public static ArrayList<Pole> listaWszystkich = new ArrayList();
    public Pole doZbicia = new Pole(0, 0);
    public int stan = 0; //  0 ruch gracza 1 -ruch komputera
    public static int[][] stanPlanszyPoczatek = new int[8][8];
    public static int[][] tempStanPlanszy = new int[8][8];
    public static int[][] wybranyStanPlanszy = new int[8][8];
    public static int[][] obecnyStanPlanszy = new int[8][8];
    public static ArrayList<int[][]> listaStanPlanszy = new ArrayList();

    public static void main(String[] args) {
        new Warcaby20().setVisible(true);
        System.out.println("ilosc czarnych" + listaCzarnych.size());
        System.out.println("ilosc bialych" + listaBialych.size());
        System.out.println("ilosc wszysckih pol" + listaWszystkich.size());

        for (final pionek p : listaCzarnych) {
            System.out.println(p.x + ":" + p.y);
        }

        System.out.println("Poczatekowy stan!");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempStanPlanszy[i][j] = stanPlanszyPoczatek[i][j];
                obecnyStanPlanszy[i][j] = stanPlanszyPoczatek[i][j];
                System.out.print(stanPlanszyPoczatek[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Warcaby20() {
        sluchacz = new Sluchacz();
        zaladujIkony();
        this.setTitle("Warcaby");
        this.setResizable(false);
        okno = new Plansza();
        this.add(okno, BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class Plansza extends JPanel {

        public Plansza() {
            setBackground(Color.GRAY);
            setLayout(new GridLayout(rozmiarX, rozmiarY));
            for (int i = 0; i < rozmiarX; i++) {
                for (int j = 0; j < rozmiarY; j++) {
                    Pole nowe = new Pole(i, j);
                    nowe.setName(i + ":" + j);
                    listaWszystkich.add(nowe);
                    //////////////////////////////////////// kolorowanie planszy
                    if ((i + j) % 2 == 0) {
                        nowe.setBackground(bezPionka);
                        stanPlanszyPoczatek[i][j] = 0;
                    } else {
                        nowe.setBackground(zPionkiem);
                        nowe.addActionListener(sluchacz);
                        nowe.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                if (e.getButton() == MouseEvent.BUTTON3) {
                                    RuchKomputera();
                                }
                            }
                        });
                    }
                    /////////////////////////////////////////

                    ////////////////////////////////////////// rozmieszczenie pionkĂłw
                    if (i < rozmiarX / 2 - 1 && (i + j) % 2 == 1) {
                        nowe.setIcon(czarnyPion);
                        pionek p = new pionek(i, j);
                        listaCzarnych.add(p);
                        stanPlanszyPoczatek[i][j] = 2;
                    }
                    if (i > rozmiarX / 2 && (i + j) % 2 == 1) {

                        nowe.setIcon(bialyPion);
                        pionek p = new pionek(i, j);
                        listaBialych.add(p);
                        stanPlanszyPoczatek[i][j] = 1;
                    }
                    if (i == 4 && j == 5) {
                        nowe.setIcon(czarnyPion);
                        pionek p = new pionek(i, j);
                        listaCzarnych.add(p);
                        stanPlanszyPoczatek[i][j] = 2;
                    }
                    ///////////////////////////////////////////
                    add(nowe); // dodanie pionkĂłw do JPanel
                }
            }

        }
    }

    public void zaladujIkony() {
        Image white = new ImageIcon("images/white.png").getImage().getScaledInstance(rozmiarPionek, rozmiarPionek, java.awt.Image.SCALE_SMOOTH);
        Image black = new ImageIcon("images/black.png").getImage().getScaledInstance(rozmiarPionek, rozmiarPionek, java.awt.Image.SCALE_SMOOTH);

        bialyPion = new ImageIcon(white);
        czarnyPion = new ImageIcon(black);

    }

    public Icon ikonaGracza() {
        if (gracz.equals("white")) {
            return bialyPion;
        } else {
            return czarnyPion;
        }
    }

    class Pole extends JButton {

        public int wiersz;
        public int kolumna;
        public int koszt;

        public Pole(int wiersz, int kolumna) {
            this.wiersz = wiersz;
            this.kolumna = kolumna;
            this.setPreferredSize(new Dimension(rozmiarPionek, rozmiarPionek));
        }
    }

    class pionek {

        public int x;
        public int y;

        public pionek(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }

    }

    public boolean czyRuchMozliwy(int wierszOd, int kolumnaOd, int wierszDo, int kolumnaDo) {
        int wspolczynnik = 1;
        if (!gracz.equals("white")) {
            wspolczynnik = -1;
        }
        if (wierszOd - wspolczynnik == wierszDo && (kolumnaOd + 1 == kolumnaDo || kolumnaOd - 1 == kolumnaDo)) {
            return true;
        }

        //JOptionPane.showMessageDialog(rootPane, "Niedozwolny ruch");
        return false;
    }

    public boolean czyBicieMozliwe(int wierszOd, int kolumnaOd, int wierszDo, int kolumnaDo) {
        if (wierszOd - 2 == wierszDo && (kolumnaOd + 2 == kolumnaDo)) {
            for (int x = 0; x < listaCzarnych.size(); x++) {
                if (listaCzarnych.get(x).x == (wierszOd - 1) && listaCzarnych.get(x).y == (kolumnaOd + 1)) {
                    //System.out.println("jest na liscie czarny mozliwy do zbicia");
                    for (int y = 0; y < listaWszystkich.size(); y++) {
                        if (listaWszystkich.get(y).getName().equals((wierszOd - 1) + ":" + (kolumnaOd + 1))) {
                            doZbicia = listaWszystkich.get(y);
                        }
                    }
                    listaCzarnych.remove(x);
                    return true;

                }
            }

        }
        if (wierszOd - 2 == wierszDo && (kolumnaOd - 2 == kolumnaDo)) {
            for (int x = 0; x < listaCzarnych.size(); x++) {
                if (listaCzarnych.get(x).x == (wierszOd - 1) && listaCzarnych.get(x).y == (kolumnaOd - 1)) {
                    for (int y = 0; y < listaWszystkich.size(); y++) {
                        if (listaWszystkich.get(y).getName().equals((wierszOd - 1) + ":" + (kolumnaOd - 1))) {
                            doZbicia = listaWszystkich.get(y);
                        }
                    }
                    listaCzarnych.remove(x);
                    return true;

                }
            }
        }
        return false;

    }

    public void RuchKomputera() {
        listaStanPlanszy.clear();
        //punkt 1

        if (listaCzarnych.size() > 0) {
            System.out.println("tworze liste na 0 punktu");
            pionek temp = listaCzarnych.get(0);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(0).x + ":" + listaCzarnych.get(0).y);
            ArrayList<Pole> listaRuchowK0 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((listaRuchowK0 = GenerujRuchy(temp)) != null) {
                listaRuchowK0 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK0.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK0.get(x).wiersz + ":" + listaRuchowK0.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK0.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 1
        if (listaCzarnych.size() > 1) {
            System.out.println("tworze liste na 1 punktu");
            pionek temp = listaCzarnych.get(1);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(1).x + ":" + listaCzarnych.get(1).y);
            ArrayList<Pole> listaRuchowK1 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((listaRuchowK1 = GenerujRuchy(temp)) != null) {
                listaRuchowK1 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK1.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK1.get(x).wiersz + ":" + listaRuchowK1.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK1.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 2
        if (listaCzarnych.size() > 2) {
            System.out.println("tworze liste na 2 punktu");
            pionek temp = listaCzarnych.get(2);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(2).x + ":" + listaCzarnych.get(2).y);
            ArrayList<Pole> listaRuchowK2 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((listaRuchowK2 = GenerujRuchy(temp)) != null) {
                listaRuchowK2 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK2.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK2.get(x).wiersz + ":" + listaRuchowK2.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK2.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 3
        if (listaCzarnych.size() > 3) {
            System.out.println("tworze liste na 3 punktu");
            pionek temp = listaCzarnych.get(3);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(3).x + ":" + listaCzarnych.get(3).y);
            ArrayList<Pole> listaRuchowK3 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((listaRuchowK3 = GenerujRuchy(temp)) != null) {
                listaRuchowK3 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK3.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK3.get(x).wiersz + ":" + listaRuchowK3.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK3.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 4
        if (listaCzarnych.size() > 4) {
            System.out.println("tworze liste na 4 punktu");
            pionek temp = listaCzarnych.get(4);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(4).x + ":" + listaCzarnych.get(4).y);
            ArrayList<Pole> listaRuchowK4 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((listaRuchowK4 = GenerujRuchy(temp)) != null) {
                listaRuchowK4 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK4.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK4.get(x).wiersz + ":" + listaRuchowK4.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK4.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 5
        if (listaCzarnych.size() > 5) {
            System.out.println("tworze liste na 1 punktu");
            pionek temp = listaCzarnych.get(5);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(5).x + ":" + listaCzarnych.get(5).y);
            ArrayList<Pole> listaRuchowK5 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK5 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK5.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK5.get(x).wiersz + ":" + listaRuchowK5.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK5.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 5
        if (listaCzarnych.size() > 6) {
            System.out.println("tworze liste na 6 punktu");
            pionek temp = listaCzarnych.get(6);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(6).x + ":" + listaCzarnych.get(6).y);
            ArrayList<Pole> listaRuchowK6 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK6 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK6.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK6.get(x).wiersz + ":" + listaRuchowK6.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK6.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 7
        if (listaCzarnych.size() > 7) {
            System.out.println("tworze liste na 7 punktu");
            pionek temp = listaCzarnych.get(7);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(7).x + ":" + listaCzarnych.get(7).y);
            ArrayList<Pole> listaRuchowK7 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK7 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK7.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK7.get(x).wiersz + ":" + listaRuchowK7.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK7.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 8
        if (listaCzarnych.size() > 8) {
            System.out.println("tworze liste na 8 punktu");
            pionek temp = listaCzarnych.get(8);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(8).x + ":" + listaCzarnych.get(8).y);
            ArrayList<Pole> listaRuchowK8 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK8 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK8.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK8.get(x).wiersz + ":" + listaRuchowK8.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK8.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 9
        if (listaCzarnych.size() > 9) {
            System.out.println("tworze liste na 9 punktu");
            pionek temp = listaCzarnych.get(9);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(9).x + ":" + listaCzarnych.get(9).y);
            ArrayList<Pole> listaRuchowK9 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK9 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK9.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK9.get(x).wiersz + ":" + listaRuchowK9.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK9.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 10
        if (listaCzarnych.size() > 10) {
            System.out.println("tworze liste na 10 punktu");
            pionek temp = listaCzarnych.get(10);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(10).x + ":" + listaCzarnych.get(10).y);
            ArrayList<Pole> listaRuchowK10 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK10 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK10.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK10.get(x).wiersz + ":" + listaRuchowK10.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK10.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 11
        if (listaCzarnych.size() > 11) {
            System.out.println("tworze liste na 11 punktu");
            pionek temp = listaCzarnych.get(11);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(11).x + ":" + listaCzarnych.get(11).y);
            ArrayList<Pole> listaRuchowK11 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK11 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK11.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK11.get(x).wiersz + ":" + listaRuchowK11.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK11.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 12
        if (listaCzarnych.size() > 12) {
            System.out.println("tworze liste na 12 punktu");
            pionek temp = listaCzarnych.get(12);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(12).x + ":" + listaCzarnych.get(12).y);
            ArrayList<Pole> listaRuchowK12 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK12 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK12.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK12.get(x).wiersz + ":" + listaRuchowK12.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK12.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu
        //punkt 13
        if (listaCzarnych.size() > 13) {
            System.out.println("tworze liste na 13 punktu");
            pionek temp = listaCzarnych.get(13);
            Pole temp2 = new Pole(temp.x, temp.y);
            System.out.println("Dla punktu" + listaCzarnych.get(13).x + ":" + listaCzarnych.get(13).y);
            ArrayList<Pole> listaRuchowK13 = new ArrayList();
            //listaRuchowK0 = GenerujRuchy(temp);
            if ((GenerujRuchy(temp)) != null) {
                listaRuchowK13 = GenerujRuchy(temp);
                for (int x = 0; x < listaRuchowK13.size(); x++) {
                    System.out.println("mogliwy ruch to - " + listaRuchowK13.get(x).wiersz + ":" + listaRuchowK13.get(x).kolumna);
                    tempStanPlanszy = SymulujRuch(obecnyStanPlanszy, temp2, listaRuchowK13.get(x));
                    listaStanPlanszy.add(tempStanPlanszy);
                }
            }
        }
        //koniec punktu

        //akceptacja stanu i frukowanie
        System.out.println("wybrany stan planszy");
        wybranyStanPlanszy = WybierzRuch(listaStanPlanszy);
        //przypisanie
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                obecnyStanPlanszy[i][j] = wybranyStanPlanszy[i][j];
                System.out.print(obecnyStanPlanszy[i][j] + " ");
            }
            System.out.println();
        }

        ///////////////////////////////aktualizacja listy czarnych
        listaCzarnych.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (obecnyStanPlanszy[i][j] == 2) {
                    pionek p = new pionek(i, j);
                    listaCzarnych.add(p);
                }
            }
        }
        OdswierzPlansze(obecnyStanPlanszy);
        System.out.println("nowe wspolrzedne czarnych");
        for (final pionek p : listaCzarnych) {
            System.out.println(p.x + ":" + p.y);
        }

    }

    public int[][] WybierzRuch(ArrayList<int[][]> _list) {
        ArrayList<Ruch> kosztRuchow = new ArrayList();  // lista zawiera index ruchu z listy "_list" + koszt
        //losowanie nowego stanu planszy
        /* 
         int[][] wybranystan = new int[8][8];
         Random generator = new Random();
         int x = generator.nextInt(_list.size());
         System.out.println("!!!!!!!!!!!!!!!!!! wylosany liczba:" + x);
         for (int i = 0; i < 8; i++) {
         for (int j = 0; j < 8; j++) {
         wybranystan[i][j] = _list.get(x)[i][j];
         }
         }

         return wybranystan;*/

        System.out.println("LIsta mozliwych stanów");
        for (int x = 0; x < _list.size(); x++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(_list.get(x)[i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("_________________");
        }

        int[][] wybranystan = new int[8][8];
        int liczbaBialych = 0;
        int liczbaCzarnych = 0;
        int liczbaBialychObecna = 0;
        int liczbaCzarnychObecna = 0;
        int indexWybrany = 0;

        //analiza obecnego stanu
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (obecnyStanPlanszy[i][j] == 1) {
                    liczbaBialychObecna += 1;
                }
                if (obecnyStanPlanszy[i][j] == 2) {
                    liczbaCzarnychObecna += 1;
                }
            }
        }
        System.out.println("Obecna ilosc pionkow bialych / czarnych: " + liczbaBialychObecna + "/" + liczbaCzarnychObecna);
        //}
        //analiza mozliwego stanu
        for (int x = 0; x < _list.size(); x++) {
            Ruch r = new Ruch();
            r.index = x;
            liczbaBialych = 0;
            liczbaCzarnych = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (_list.get(x)[i][j] == 1) {
                        liczbaBialych += 1;
                    }
                    if (_list.get(x)[i][j] == 2) {
                        liczbaCzarnych += 1;
                    }
                }
            }
            if (liczbaBialych < liczbaBialychObecna) {
                //moliwe bicie przez komputer
                //przewidywanie czy gracz bedzie mogl zbic

                if ((PrzewidywanieBiciaGracza(_list.get(x))) == true) {
                    System.out.println("po tym biciu gracz BEDZIE mogl zbic");
                    r.koszt = 9;
                }
                if ((PrzewidywanieBiciaGracza(_list.get(x))) == false) {
                    System.out.println("po tym biciu gracz NIE bedzie mogl zbic");
                    r.koszt = 10;
                }
                //kosztRuchow.add(r);
                //indexWybrany = x;
            }
            if (liczbaBialych == liczbaBialychObecna) {
                //ruch bez bicia przez komputer
                if ((PrzewidywanieBiciaGracza(_list.get(x))) == true) {
                    System.out.println("po tym ruchu gracz bedzie mogl zbic");
                    r.koszt = 7;
                }
                if ((PrzewidywanieBiciaGracza(_list.get(x))) == false) {
                    System.out.println("po tym ruchu gracz nie bedzie mogl zbic");
                    r.koszt = 8;
                }

            }
            kosztRuchow.add(r);

        }
        //sortowanie listy rychow
        System.out.println("Lista ruchow z kosztem przed sortowaniem");
        for (final Ruch p : kosztRuchow) {
            System.out.println(p.koszt);
        }
        Collections.sort(kosztRuchow);
        System.out.println("Lista ruchow z kosztem po sortowaniu");
        for (final Ruch p : kosztRuchow) {
            System.out.println(p.koszt);
        }

        ///////
        indexWybrany=kosztRuchow.get(0).index;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                wybranystan[i][j] = _list.get(indexWybrany)[i][j];
            }
        }
        return wybranystan;

    }

    public boolean PrzewidywanieBiciaGracza(int[][] stanPlanszy) {
        boolean bicie = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (stanPlanszy[i][j] == 2) {
                    if ((i + 1) < 8 && (j + 1) < 8 && (i - 1) >= 0 && (j - 1) >= 0) {
                        if (stanPlanszy[i + 1][j + 1] == 1) {
                            if (stanPlanszy[i - 1][j - 1] == 0) {
                                return true;
                            } else {
                                bicie = false;
                            }
                        } else {
                            bicie = false;
                        }
                        if (stanPlanszy[i + 1][j - 1] == 1) {
                            if (stanPlanszy[i - 1][j + 1] == 0) {
                                return true;
                            } else {
                                bicie = false;
                            }
                        }else 
                            bicie = false;
                    } else {
                        bicie = false;
                    }
                }
            }
        }
        return bicie;
    }

    public ArrayList GenerujRuchy(pionek _p) {
        ArrayList<Pole> templistaRuchow = new ArrayList();
        templistaRuchow.clear();
        Pole temp = new Pole(0, 0);
        if (czyRuchMozliwyKomputer(_p.x, _p.y, _p.x + 1, _p.y + 1)) {
            temp = new Pole(_p.x + 1, _p.y + 1);
            templistaRuchow.add(temp);
        }
        if (czyRuchMozliwyKomputer(_p.x, _p.y, (_p.x + 1), (_p.y - 1))) {
            temp = new Pole(_p.x + 1, _p.y - 1);
            templistaRuchow.add(temp);
        }
        //sprawdzić czy możliwe bicie
        if (czyBicieMozliweKomputer(_p.x, _p.y, _p.x + 2, _p.y + 2)) {
            temp = new Pole(_p.x + 2, _p.y + 2);
            templistaRuchow.add(temp);
        }
        if (czyBicieMozliweKomputer(_p.x, _p.y, _p.x + 2, _p.y - 2)) {
            temp = new Pole(_p.x + 2, _p.y - 2);
            templistaRuchow.add(temp);
        }

        if (templistaRuchow.isEmpty()) {
            System.out.println("lista pusta");
            return null;
        } else {
            return (templistaRuchow);
        }
    }

    public boolean czyRuchMozliwyKomputer(int wierszOd, int kolumnaOd, int wierszDo, int kolumnaDo) {
        if (kolumnaDo >= 0 && wierszDo >= 0 && wierszDo < 8 && kolumnaDo < 8) {
            if (obecnyStanPlanszy[wierszDo][kolumnaDo] == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean czyBicieMozliweKomputer(int wierszOd, int kolumnaOd, int wierszDo, int kolumnaDo) {
        if (kolumnaDo >= 0 && wierszDo >= 0 && wierszDo < 8 && kolumnaDo < 8) {
            if (kolumnaDo > kolumnaOd) {
                if (obecnyStanPlanszy[wierszDo - 1][kolumnaDo - 1] == 1) {
                    if (obecnyStanPlanszy[wierszDo][kolumnaDo] == 0) {
                        return true;
                    }
                }
            }
            if (kolumnaDo < kolumnaOd) {
                if (obecnyStanPlanszy[wierszDo - 1][kolumnaDo + 1] == 1) {
                    if (obecnyStanPlanszy[wierszDo][kolumnaDo] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public int[][] SymulujRuch(int[][] stanPlanszy, Pole _poczatkowe, Pole _poRuchu) {
        int[][] generowanyStanPlanszy = new int[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                generowanyStanPlanszy[x][y] = stanPlanszy[x][y];
            }
        }
        generowanyStanPlanszy[_poczatkowe.wiersz][_poczatkowe.kolumna] = 0;
        generowanyStanPlanszy[_poRuchu.wiersz][_poRuchu.kolumna] = 2;
        //if bicie
        if (_poRuchu.wiersz == (_poczatkowe.wiersz + 2)) {
            if (_poRuchu.kolumna == (_poczatkowe.kolumna + 2)) {
                generowanyStanPlanszy[_poczatkowe.wiersz + 1][_poczatkowe.kolumna + 1] = 0;
            }
            if (_poRuchu.kolumna == (_poczatkowe.kolumna - 2)) {
                generowanyStanPlanszy[_poczatkowe.wiersz + 1][_poczatkowe.kolumna - 1] = 0;
            }
        }
        return generowanyStanPlanszy;
    }

    public void OdswierzPlansze(int[][] stanPlanszy) {
        int[][] tmpStanPlanszy = new int[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                tmpStanPlanszy[x][y] = stanPlanszy[x][y];
            }
        }
        //listaWszystkich.get(0).setIcon(bialyPion);
        int x = 0, y = 0;
        for (int a = 0; a < listaWszystkich.size(); a++) {

            x = Integer.parseInt(listaWszystkich.get(a).getName().substring(0, 1));
            y = Integer.parseInt(listaWszystkich.get(a).getName().substring(2));
            //System.out.println(x+"-"+y);
            if (tmpStanPlanszy[x][y] == 0) {
                //System.out.println("000000000000000000000");
                listaWszystkich.get(a).setIcon(null);
            }
            if (tmpStanPlanszy[x][y] == 1) {
                //System.out.println("1111111111111111111111111");
                listaWszystkich.get(a).setIcon(bialyPion);
            }
            if (tmpStanPlanszy[x][y] == 2) {
                //System.out.println("22222222222222222222222222");
                listaWszystkich.get(a).setIcon(czarnyPion);
            }
            //listaWszystkich.get(a).setIcon(null); 
        }

    }

    public class Sluchacz implements ActionListener {

        public void actionPerformed(ActionEvent zdarzenie) {

            int stan = 0;
            if (stan == 0) {
                System.out.println("RUCH GRACZA");
                if (skad != null && skad != (Pole) (zdarzenie.getSource()) && ((Pole) (zdarzenie.getSource())).getIcon() == ikonaGracza()) {
                    skad = (Pole) (zdarzenie).getSource();
                    biezace.setBackground(zPionkiem);
                    biezace = skad;
                    biezace.setBackground(skupiony);

                    return;
                }
                if (skad == null) {
                    biezace = (Pole) (zdarzenie.getSource()); // pobieranie klikniÄ™tego buttona

                    if (biezace.getIcon() == ikonaGracza()) {
                        skad = (Pole) (zdarzenie.getSource());
                        biezace.setBackground(skupiony);
                    }
                    return;

                }
                //drugie klikniecie
                Pole dokad = (Pole) (zdarzenie.getSource());

                if (dokad.getIcon() != null) {

                    return;

                }

                if (czyRuchMozliwy(biezace.wiersz, biezace.kolumna, dokad.wiersz, dokad.kolumna)) {

                    obecnyStanPlanszy[biezace.wiersz][biezace.kolumna] = 0;
                    obecnyStanPlanszy[dokad.wiersz][dokad.kolumna] = 1;
                    skad.setBackground(zPionkiem);
                    skad.setIcon(null); //ustawic na liscie
                    dokad.setIcon(bialyPion); //usawic na liscie
                    skad = null;
                    dokad = null;
                    biezace = null;
                    System.out.println("----------obecny stan planszy po ruchu gracza---------------");
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            System.out.print(obecnyStanPlanszy[i][j] + " ");
                        }
                        System.out.println();
                    }

                    //RuchKomputera();
                    //OdswierzPlansze(stanPlanszyPoczatek);
                    return;
                } else if (czyBicieMozliwe(biezace.wiersz, biezace.kolumna, dokad.wiersz, dokad.kolumna)) {

                    //System.out.println("bicie");
                    obecnyStanPlanszy[biezace.wiersz][biezace.kolumna] = 0;
                    obecnyStanPlanszy[dokad.wiersz][dokad.kolumna] = 1;
                    obecnyStanPlanszy[doZbicia.wiersz][doZbicia.kolumna] = 0;
                    skad.setBackground(zPionkiem);
                    skad.setIcon(null); // ustawic na liscie
                    dokad.setIcon(bialyPion); //usawic na liscie
                    skad = null;
                    dokad = null;
                    biezace = null;
                    //zbijany pionek
                    ///Pole temp = new Pole(0,0);

                    doZbicia.setIcon(null); // ustawic na liscie
                    doZbicia = null;
                    System.out.println("----------obecny stan planszy po biciu gracza-----------------");
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            System.out.print(obecnyStanPlanszy[i][j] + " ");
                        }
                        System.out.println();
                    }
                    for (final pionek p : listaCzarnych) {
                        System.out.println(p.x + ":" + p.y);
                    }
                    //RuchKomputera();

                    return;
                } else {
                    skad.setBackground(zPionkiem); // ustawic na liscie
                    skad = null;
                    dokad = null;

                }
                return;
            }

        }
    }

    public class Ruch implements Comparable<Ruch> {

        int index;
        int koszt;

        @Override
        public int compareTo(Ruch o) {
            return (int) (o.koszt - this.koszt);
        }
    }
}
