package tictacto;

import java.util.*;

public class TicTacToModel {

    private char[][] gitter;
    public static final char benutzer = 'X';
    public static final char computer = 'O';
    public static final char leer = ' ';


    public TicTacToModel(){
        gitter = new char[3][3];

        for (int i = 0; i < gitter.length; i++) {
            for(int j = 0; j < gitter[0].length; j++) {
                gitter[i][j] = leer;
            }
        }
    }

    public char getZelle(int reihe, int spalte){
        return gitter[reihe][spalte];
    }

    public int[] zufaelligeLeereZelle(){
        // leere Zellen als Strings sammeln
        List<String> leereZellen = new ArrayList<>();

        for (int i = 0; i < gitter.length; i++) {
            for (int j = 0; j < gitter[0].length; j++){
                if(gitter[i][j] == leer){
                    leereZellen.add(i + "," + j);
                }
            }
        }

        if (leereZellen.isEmpty()) {
            return null;
        }

        Collections.shuffle(leereZellen);
        String auswahl = leereZellen.getFirst(); // ist das gleiche wie leereZellen.get(0) nimmt das erste Element aus der Liste

        // Reihe und Spalte aus dem String wiederherstellen zu Integer (Zahl)
        int reihe = Integer.parseInt(auswahl.split(",")[0]);
        int spalte = Integer.parseInt(auswahl.split(",")[1]);

        return new int[] {reihe, spalte};
    }

    public void symbolSetzten(char spieler, int reihe, int spalte){ // spieler - computer oder benutzer
        if(gitter[reihe][spalte] == leer) {
            gitter[reihe][spalte] = spieler; // trägt X oder O ein, nur wenn Feld leer ist
        }
    }

    public char gewinnerErmitteln() {
        // Reihen prüfen
        for (int reihe = 0; reihe < gitter.length; reihe++){
            if (gitter[reihe][0] != leer &&
                gitter[reihe][0] == gitter[reihe][1] &&
                gitter[reihe][1] == gitter[reihe][2]){
                    return gitter[reihe][0];
            }
        }

        // Spalten prüfen
        for (int spalte = 0; spalte < gitter[0].length; spalte++) {
            if (gitter[0][spalte] != leer &&
                gitter[0][spalte] == gitter[1][spalte] &&
                gitter[1][spalte] == gitter[2][spalte]){
                    return gitter[0][spalte];
            }
        }

        // Diagonalen prüfen oben-links -> unten-rechts
        if (gitter[0][0] != leer &&
            gitter[0][0] == gitter[1][1] &&
            gitter[1][1] == gitter[2][2]) {
                return gitter[0][0];
        }
        // Diagonale: oben-rechts -> unten-links
        if (gitter[0][2] != leer &&
            gitter[0][2] == gitter[1][1] &&
            gitter[1][1] == gitter[2][0]) {
             return gitter[0][2];
        }

        // Unentschieden prüfen - keine leeren Zeilen mehr
        boolean nochleer = false;
        for (int i = 0; i < gitter.length; i++){
            for (int j = 0; j < gitter[0].length; j++) {
                if (gitter[i][j] == leer) {
                    nochleer = true;
                }
            }
        }
        if (!nochleer) {
            return 'U'; // Unentschieden
        }

        return leer;
    }

    public void gitterVisualisieren(){
        System.out.println("-------");
        for (int i = 0; i < gitter.length; i++) {
            System.out.print("|");
            for (int j = 0; j < gitter[0].length; j++) {
                System.out.print(gitter[i][j] + "|");
            }
            System.out.println();
            System.out.println("-------");
        }
    }


}


