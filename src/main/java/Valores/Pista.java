/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Valores;
public class Pista implements Values {
    public Pista(Integer kilometros, Integer numeroDeCarriles) {
        this.kilometros = kilometros;
        this.numeroDeCarriles = numeroDeCarriles;
    }
    private Integer kilometros;
    private Integer numeroDeCarriles;
       public Integer kilometros() {
        return kilometros;
    }
    public Integer numeroDeCarriles() {
        return numeroDeCarriles;
    }

}
