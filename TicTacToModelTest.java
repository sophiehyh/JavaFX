package tictacto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TicTacToModelTest {

    private TicTacToModel model;

    @BeforeEach
    void setUp() {
        model = new TicTacToModel();
    }

    @Test
    void testAnfangLeer() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(TicTacToModel.leer, model.getZelle(i,j));
            }
        }
    }

    @Test
    // gibt eine leere Zelle zurück
    void zufaelligeLeereZelle(){
        int[] zelle = model.zufaelligeLeereZelle();
        assertNotNull(zelle);
        assertEquals(TicTacToModel.leer, model.getZelle(zelle[0], zelle[1]));
    }

    @Test
    void symbolSetztenBenutzer(){
        model.symbolSetzten(TicTacToModel.benutzer, 0, 0);
        assertEquals('X', model.getZelle(0,0));
    }

    @Test
    void symbolSetzenComputer(){
        model.symbolSetzten(TicTacToModel.computer, 1, 1);
        assertEquals('O', model.getZelle(1, 1));
    }

    @Test
    void reiheGewonnen() {
        model.symbolSetzten(TicTacToModel.benutzer, 0, 0);
        model.symbolSetzten(TicTacToModel.benutzer, 0, 1);
        model.symbolSetzten(TicTacToModel.benutzer, 0, 2);
        assertEquals('X', model.gewinnerErmitteln());
    }

    @Test
    void spalteGewonnen() {
        model.symbolSetzten(TicTacToModel.computer, 0, 0);
        model.symbolSetzten(TicTacToModel.computer, 1, 0);
        model.symbolSetzten(TicTacToModel.computer, 2, 0);
        assertEquals('O', model.gewinnerErmitteln());
    }

    @Test
    void diagonaleGewonnen(){
        model.symbolSetzten(TicTacToModel.benutzer, 0, 0);
        model.symbolSetzten(TicTacToModel.benutzer, 1, 1);
        model.symbolSetzten(TicTacToModel.benutzer, 2, 2);
        assertEquals('X', model.gewinnerErmitteln());
    }

    @Test
    void unentschiedenTesten(){
        model.symbolSetzten(TicTacToModel.benutzer, 0, 0);
        model.symbolSetzten(TicTacToModel.computer, 0, 1);
        model.symbolSetzten(TicTacToModel.benutzer, 0, 2);
        model.symbolSetzten(TicTacToModel.computer, 1, 0);
        model.symbolSetzten(TicTacToModel.benutzer, 1, 1);
        model.symbolSetzten(TicTacToModel.computer, 2, 0);
        model.symbolSetzten(TicTacToModel.benutzer, 1, 2);
        model.symbolSetzten(TicTacToModel.computer, 2, 2);
        model.symbolSetzten(TicTacToModel.benutzer, 2, 1);
        assertEquals('U', model.gewinnerErmitteln());
    }

}
