/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author debian
 */
public class Naivayes {

    public void ejecutar(Instances instancias, int indiceAtributo) {
        //            System.out.println("nn:" + instancias.toString());
        List lista = new ArrayList(instancias.numAttributes());
        int[][] vector = new int[instancias.numInstances()][instancias.numAttributes()];

        Attribute atributoPrincipal = instancias.attribute(indiceAtributo);
        // probabilidad 
        if (atributoPrincipal.isNominal()) {
            double[] contador = new double[atributoPrincipal.numValues()];
            for (int i = 0; i < instancias.numInstances(); i++) {
                int val = (int) instancias.instance(i).value(atributoPrincipal);
                contador[val] += 1;
            }
            for (int i = 0; i < contador.length; i++) {
                contador[i] /= instancias.numInstances();
            }
            lista.add(indiceAtributo);
            System.out.println("contador:" + Arrays.toString(contador));
        }

        MatProbabilidad[] matProb = new MatProbabilidad[instancias.numAttributes() - 1];
        double[] VectorSuma;
        for (int j = 0; j < instancias.numAttributes(); j++) {
            Attribute atributoActual = instancias.attribute(j);

            if (j != indiceAtributo) {
                if (atributoActual.isNominal()) {
                    VectorSuma = new double[atributoPrincipal.numValues()];
                    double[][] contador = new double[atributoPrincipal.numValues()][atributoActual.numValues()];
                    System.out.println("atributo: " + atributoActual.name());
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        int valSeg = (int) instancias.instance(i).value(atributoActual);
                        int valPrin = (int) instancias.instance(i).value(atributoPrincipal);
                        contador[valPrin][valSeg] += 1;
                        VectorSuma[valPrin]++;
                    }
                    for (int i = 0; i < contador.length; i++) {
                        for (int l = 0; l < contador[i].length; l++) {
                            contador[i][l] = (contador[i][l] + 1) / (VectorSuma[i] + 2);
                        }
                        System.out.println("contador:" + Arrays.toString(contador[i]));

                    }
                    matProb[j] = new MatProbabilidad(contador);
                } else if (atributoActual.isNumeric()) {
                    VectorSuma = new double[atributoPrincipal.numValues()];
                    double[][] contador = new double[atributoPrincipal.numValues()][2];
                    System.out.println("atributo: " + atributoActual.name());
                    //suma total por agrupado por atributo principal
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        double valSeg = instancias.instance(i).value(atributoActual);
                        int valPrin = (int) instancias.instance(i).value(atributoPrincipal);
                        contador[valPrin][1] += valSeg;
                        VectorSuma[valPrin]++;
                    }
                    // promedio agrupado por atributo principal
                    for (int l = 0; l < VectorSuma.length; l++) {
                        //promedio
                        contador[l][1] /= VectorSuma[l];
                    }
                    // calculo de desviacion est
                    for (int k = 0; k < instancias.numInstances(); k++) {
                        double valSeg = instancias.instance(k).value(atributoActual);
                        int valPrin = (int) instancias.instance(k).value(atributoPrincipal);
                        // sumatoria de los cuadrados de la diferencia entre cada elemento y el promedio
                        contador[valPrin][0] += Math.pow(valSeg - contador[valPrin][1], 2);
                    }
                    //completar calculo de desviacion
                    for (int i = 0; i < VectorSuma.length; i++) {
                        // raiz del cociente, de la sumatoria entre numero de elementos
                        contador[i][0] = Math.sqrt(contador[i][0] / (VectorSuma[i]-1));
                        System.out.println("contador:" + Arrays.toString(contador[i]));
                    }
                    matProb[j] = new MatProbabilidad(contador);
                }
            }
        }
    }

    class MatProbabilidad {

        double[][] matrizProb;

        public MatProbabilidad(double[][] matrizProb) {
            this.matrizProb = matrizProb;
        }

        public double get(int fil, int col) {
            return matrizProb[fil][col];
        }
    }
}
