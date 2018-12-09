/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.io.Serializable;
import utilidades.Util;
import weka.core.Attribute;

/**
 *
 * @author debian
 */
public class MatProbabilidad implements Serializable {

    public double[][] matrizProb;
    public final double[] sumTotal;
    public final Attribute atributo;
    private String[] filasNumerica = {"Desviacion", "Promedio"};
    public Attribute varClase;

    public MatProbabilidad(double[][] matrizProb, double[] sumTotal, Attribute atributo, Attribute varClase) {
        this.matrizProb = matrizProb;
        this.sumTotal = sumTotal;
        this.atributo = atributo;
        this.varClase = varClase;
    }

    public double get(int fil, int col) {
        if (atributo.isNominal()) {
            return matrizProb[fil][col] / sumTotal[fil];
        }
        return matrizProb[fil][col];
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();

        cadena.append("\n").append(atributo.name()).append("\n");
        cadena.append(formato(""));
        for (int k = 0; k < matrizProb.length; k++) {
            cadena.append(formato(varClase.value(k)));
        }
        cadena.append("\n");
        for (int i = 0; i < matrizProb[0].length; i++) {
            if (atributo.isNominal()) {
                cadena.append(formato(atributo.value(i)));
            } else {
                cadena.append(formato(filasNumerica[i]));
            }

            for (double[] matrizProb1 : matrizProb) {
                cadena.append(formato(Util.formatearDouble(matrizProb1[i]))).append(" ");
            }
            cadena.append("\n");
        }
        return cadena.toString();
    }

    String formato(String entrada) {
        return String.format("%" + (Util.NUM_DECIMALES + 5) + "s", entrada);
    }
}
