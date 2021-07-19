/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Game.GanadoresBD;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Persistencia {
        public void crearRegistro(GanadoresBD ganadores) {
        try {
            ganadores1.create(ganadores);
        } catch (Exception ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void modificarRegistro(GanadoresBD ganadores) {
        try {
            ganadores1.edit(ganadores);
        } catch (Exception ex) {
            Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
