/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interpro.main;

import com.interpro.mineria.Mineria;

import java.io.IOException;

/**
 *
 * @author debian
 */
public class Consola {

    public static void main(String[] args) throws IOException {
        Mineria mineria = new Mineria();
        String archivoEntrenamiento;
        String atributoClase;
        String modelo = "modelo";
        if (args.length != 0) {
            if ("-e".equals(args[0])) { //entrenar
                archivoEntrenamiento = args[1];
                atributoClase = args[2];
                mineria.entrenar(modelo, archivoEntrenamiento, atributoClase);
            } else if ("-p".equals(args[0])) {
                String archivoInstancia = args[1];
                modelo = args[2];
                mineria.predecir("modelo", archivoInstancia);
            }
        } else {
            System.out.println("Ayuda");
        }
    }

}
