/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Valores;
import Game.Jugador;
public abstract class Podio implements Props {
    private Jugador primerLugar;
    private Jugador segundoLugar;
    private Jugador tercerLugar;
    public Podio() {
    }
    public void asignarPrimerLugar(Jugador jugador) {
        primerLugar = jugador;
    }
    public void asignarSegundoLugar(Jugador jugador) {
        segundoLugar = jugador;
    }
    public void asignarTercerLugar(Jugador jugador) {
        tercerLugar = jugador;
    }
       public Jugador primerLugar() {
        return primerLugar;
    }
      public Jugador segundoLugar() {
        return segundoLugar;
    }
       public Jugador tercerLugar() {
        return tercerLugar;
    }
     public Boolean lleno() {
        Boolean lleno = false;
        if (primerLugar != null && segundoLugar != null && tercerLugar != null) {
            lleno = true;
        }
        return lleno;
    }
}
