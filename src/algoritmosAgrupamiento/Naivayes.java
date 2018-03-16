/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosAgrupamiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utilidades.Util;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author debian
 */
public class Naivayes {

    MatProbabilidad[] vectorMatProbCondicionales;
    double[] probVarClase;
    Instances instancias;
    Attribute varClase;
    double[] vectorPresicion;

    /**
     *
     */
    public Naivayes() {
        this.instancias = null;
        varClase = null;
    }

    List<Attribute> listaAtributos() {
        List listAtributos = new ArrayList();
        for (int i = 0; i < instancias.numAttributes(); i++) {
            listAtributos.add(instancias.attribute(i));
        }
        return listAtributos;
    }

    /**
     *
     * @param instanciasOriginales
     */
    public void crearModelo(Instances instanciasOriginales) {
        this.instancias = new Instances(instanciasOriginales);
        varClase = instancias.classAttribute();

        double sumaUnos;
        double sumaValores;
        vectorPresicion = new double[instancias.numAttributes()];
        double valAnteior;
        double valActual;

        List<Attribute> listAtributos = listaAtributos();
        int j = 0;
        //calculo de presicion
        for (Attribute atributoActual : listAtributos) {
            sumaUnos = 0;
            sumaValores = 0;
            if (atributoActual.isNumeric()) {
                instancias.sort(atributoActual);
                valAnteior = instancias.instance(0).value(atributoActual);
                for (int i = 1; i < instancias.numInstances(); i++) {
                    valActual = instancias.instance(i).value(atributoActual);

                    if (valActual != valAnteior) {
                        sumaUnos++;
                        sumaValores += valActual - valAnteior;
                        valAnteior = valActual;
                    }
                }
            }
            if (sumaUnos > 0) {
                vectorPresicion[j] = sumaValores / sumaUnos;
            }
            System.out.println("sum valores[" + j + "]: " + sumaValores);
            System.out.println("vector presicion[" + j + "]: " + Util.formatearDouble(vectorPresicion[j]));
            j++;
        }

        // remueve instancias sin variable de clase
//        instancias.deleteWithMissingClass();
        // probabilidad de variable de clase
        if (varClase.isNominal()) {
            probVarClase = new double[varClase.numValues()];
            //conteo
            for (int i = 0; i < instancias.numInstances(); i++) {
                int val = (int) instancias.instance(i).value(varClase);
                probVarClase[val] += 1;
            }
            // conteo_atributo[i]/total_atributos
            for (int i = 0; i < probVarClase.length; i++) {
                probVarClase[i] /= instancias.numInstances();
            }
            System.out.println("probabilidad var clase:(" + varClase.toString() + ")\n[" + Util.imprimirVectorDouble(probVarClase) + "]");
        }

        //calculo de probabilidades condicionadas con respecto ala variable de clase
        vectorMatProbCondicionales = new MatProbabilidad[instancias.numAttributes() - 1];
        double[] vectorSumaValores;
        for (j = 0; j < instancias.numAttributes(); j++) {
            Attribute atributoActual = instancias.attribute(j);

            if (j != varClase.index()) {
                if (atributoActual.isNominal()) {
                    vectorSumaValores = new double[varClase.numValues()];
                    double[][] matProb = new double[varClase.numValues()][atributoActual.numValues()];
                    for (int i = 0; i < matProb.length; i++) {
                        for (int k = 0; k < matProb[i].length; k++) {
                            matProb[i][k] = 0;
                        }
                    }
                    System.out.println("atributo: " + atributoActual.name());
                    //conteo 
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        int valSeg = (int) instancias.instance(i).value(atributoActual);
                        int valPrin = (int) instancias.instance(i).value(varClase);
                        matProb[valPrin][valSeg] += 1;
                        vectorSumaValores[valPrin]++;
                    }
                    for (int i = 0; i < matProb.length; i++) {
                        System.out.println("[" + varClase.value(i) + "]:" + Arrays.toString(matProb[i]));
                        for (int l = 0; l < matProb[i].length; l++) {
                            matProb[i][l] = (matProb[i][l] + 1) / (vectorSumaValores[i] + atributoActual.numValues());

                        }
//                        System.out.println("[" + varClase.value(i) + "]:" + Util.imprimirVectorDouble(matProb[i]));
                    }
                    vectorMatProbCondicionales[j] = new MatProbabilidad(matProb);
                } else if (atributoActual.isNumeric()) {
                    vectorSumaValores = new double[varClase.numValues()];
                    double[][] contador = new double[varClase.numValues()][2];
                    System.out.println("atributo: " + atributoActual.name());
                    //suma total por agrupado por atributo principal
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        double valSeg = getValor(instancias.instance(i), atributoActual);
//                        double valSeg = instancias.instance(i).value(atributoActual);
                        int valPrin = (int) instancias.instance(i).value(varClase);
                        contador[valPrin][1] += valSeg;
                        vectorSumaValores[valPrin]++;
                    }
                    // promedio agrupado por atributo principal
                    for (int l = 0; l < vectorSumaValores.length; l++) {
                        //promedio
                        contador[l][1] /= vectorSumaValores[l];
                    }
                    // calculo de desviacion estandar
                    for (int k = 0; k < instancias.numInstances(); k++) {
                        double valSeg = getValor(instancias.instance(k), atributoActual);
                        int valPrin = (int) instancias.instance(k).value(varClase);
                        // sumatoria de los cuadrados de la diferencia entre cada elemento y el promedio
                        contador[valPrin][0] += Math.pow(valSeg - contador[valPrin][1], 2);
                    }
                    //completar calculo de desviacion
                    for (int i = 0; i < vectorSumaValores.length; i++) {
                        // raiz del cociente, de la sumatoria entre numero de elementos
                        contador[i][0] = Math.sqrt(contador[i][0] / (vectorSumaValores[i]));
                        System.out.println("[" + varClase.value(i) + "]:" + Util.imprimirVectorDouble(contador[i]));
                    }
                    vectorMatProbCondicionales[j] = new MatProbabilidad(contador);
                }
            }
        }
    }

    private class MatProbabilidad {

        double[][] matrizProb;

        public MatProbabilidad(double[][] matrizProb) {
            this.matrizProb = matrizProb;
        }

        public double get(int fil, int col) {
            return matrizProb[fil][col];
        }
    }

    /**
     * probabilidad de la variable de clase de acuerdo a las instancias
     *
     * @param instancias
     * @return double[instancia][atributo]
     */
    public double[][] evaluarInstancias(Instances instancias) {
        if (varClase == null) {
            throw new RuntimeException("Error, Se debe primero crear el modelo.");
        }
        double[][] probabilidadesVarClase = new double[instancias.numInstances()][];
//        for (int i = 0; i < 5; i++) {
        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instanciaActual = instancias.instance(i);
            probabilidadesVarClase[i] = evaluarInstancia(instanciaActual);
//            System.out.println("");
        }
        return probabilidadesVarClase;
    }

    /**
     * probabilidad de la variable de clase de acuerdo a una instancia
     *
     * @param instanciaActual
     * @return double[atributo]
     */
    public double[] evaluarInstancia(Instance instanciaActual) {
        if (varClase == null) {
            throw new RuntimeException("Error, Se debe primero crear el modelo.");
        }
        double suma = 0;
        String valorVarClase;
        int indiceValorVarClase;
        double[] probabilidadesVarClase = new double[varClase.numValues()];
        for (int j = 0; j < varClase.numValues(); j++) {
            valorVarClase = varClase.value(j);
            indiceValorVarClase = varClase.indexOfValue(valorVarClase);
            suma += probabilidadesVarClase[j] = evaluarInstancia(instancias, instanciaActual, indiceValorVarClase);
        }
        for (int j = 0; j < varClase.numValues(); j++) {
            probabilidadesVarClase[j] = (probabilidadesVarClase[j] / suma) * 100;
            valorVarClase = varClase.value(j);
//            System.out.println("[" + var++ + "]instancia: " + instanciaActual.toString()
//                    + "; probabiliad[" + valorVarClase + "]: "
//                    + String.format("%.2f", probabilidadesVarClase[j]) + "%");
        }
        return probabilidadesVarClase;
    }
    int var = 0;

    private double evaluarInstancia(Instances instancias, Instance instanciaActual, int valAttPrincipal) {
        double probabilidadTotal = 1;
        for (int j = 0; j < instancias.numAttributes(); j++) {
            Attribute atributoActual = instancias.attribute(j);
            if (j == varClase.index()) {
                probabilidadTotal *= probVarClase[valAttPrincipal];
            } else {
                // optener la matriz de probabilidad condiccionada respectiva de atributoaActual
                MatProbabilidad matP = vectorMatProbCondicionales[atributoActual.index()];
                if (atributoActual.isNominal()) {
                    int col = (int) instanciaActual.value(atributoActual);
                    // optener probabilidad del valor del atributoActual y atributoPrincipal
                    probabilidadTotal *= matP.get(valAttPrincipal, col);
//                    System.out.println("prob: "+matP.get(valAttPrincipal, col));
                } else {
//                    double valor = getValor(instanciaActual, atributoActual);
                    double valor = instanciaActual.value(atributoActual);
                    // obtener promedio desde la posicion 1
                    double media = matP.get(valAttPrincipal, 1);
                    // obtener desviacion desde la posicion 1
                    double desvicacion = matP.get(valAttPrincipal, 0);
                    // probabilidad del valor del atributo actual
                    double probaAtrib = Util.probabilidad(valor, media, desvicacion);
//                    System.out.println("prob: "+probaAtrib);
                    probabilidadTotal *= probaAtrib;
                }
            }
//            System.out.println("");
        }
        return probabilidadTotal;
    }

    public double getValor(Instance instancia, Attribute atributo) {
        double valor;
        valor = instancia.value(atributo) / vectorPresicion[atributo.index()];
        valor = Math.round(valor) * vectorPresicion[atributo.index()];
        return valor;
    }
}
