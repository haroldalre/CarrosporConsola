/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Identificacion.Nombre;
import java.awt.Color;

public class Jugador {

    private Nombre name;
    private Color color;
    private Integer score;
    public Jugador(Nombre nombre, Color color, Integer puntos) {
        this.name = nombre;
        this.color = color;
        this.score = puntos;
    }
    Jugador(Nombre nombre, Color color, int i) {
        throw new UnsupportedOperationException("Todavia no es posible."); 
    }
    public Nombre nombre() {
        return name;
    }
    public Color color() {
        return color;
    }
    public void darScore(Integer score) {
        score = score;
    }
}
