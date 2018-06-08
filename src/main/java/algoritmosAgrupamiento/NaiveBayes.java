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
public class NaiveBayes {

    MatProbabilidad[] vectorMatProbCondicionales;
    double[] probVarClase;
    Instances instancias;
    Attribute varClase;
    double[] vectorPrecision;
    private boolean laplace = true;

    private static double[] calcularPrecision(Instances instancias) {
        double sumaUnos;
        double sumaValores;
        double[] vectorPrecisiones = new double[instancias.numAttributes()];
        double valAnterior;
        double valActual;

        List<Attribute> listAtributos = Util.listaAtributos(instancias);

        System.out.println("PRECISION:");
        //calculo de presicion
        int j = 0;
        for (Attribute atributoActual : listAtributos) {
            sumaUnos = 0;
            sumaValores = 0;
            if (atributoActual.isNumeric()) {
                instancias.sort(atributoActual);
                valAnterior = instancias.instance(0).value(atributoActual);
                for (int i = 1; i < instancias.numInstances(); i++) {
                    valActual = instancias.instance(i).value(atributoActual);
                    if (valActual != valAnterior) {
                        sumaUnos++;
                        // suma diferencias
                        sumaValores += valActual - valAnterior;
                        valAnterior = valActual;
                    }
                }
            }
            if (sumaUnos > 0) {
                vectorPrecisiones[j] = sumaValores / sumaUnos;
            }
            System.out.println(String.format("%12s", "" + atributoActual.name()) + " =>" + Util.formatearDouble(vectorPrecisiones[atributoActual.index()]));
            j++;
        }
        return vectorPrecisiones;
    }

    /**
     *
     * @param instanciasOriginales
     */
    public void crearModelo(Instances instanciasOriginales) {
        this.instancias = new Instances(instanciasOriginales);
        varClase = instancias.classAttribute();

        // calculo de precisiones
        vectorPrecision = calcularPrecision(instancias);
        int sumContadores = 0;
        // probabilidad de variable de clase
        if (varClase.isNominal()) {
            probVarClase = new double[varClase.numValues()];
            for (int k = 0; k < probVarClase.length; k++) {
                probVarClase[k] +=  0;
//                probVarClase[k] += this.laplace ? 1 : 0;
            }
            sumContadores += 0;
//            sumContadores += this.laplace ? probVarClase.length : 0;
            sumContadores += instancias.numInstances();
            //conteo
            for (int i = 0; i < instancias.numInstances(); i++) {
                int val = (int) instancias.instance(i).value(varClase);
                probVarClase[val] += 1;
            }
            // conteo_atributo[i]/total_atributos
            for (int i = 0; i < probVarClase.length; i++) {
                probVarClase[i] /= sumContadores;
            }

        }

        //calculo de probabilidades condicionadas con respecto ala variable de clase
        vectorMatProbCondicionales = new MatProbabilidad[instancias.numAttributes()];
        double[] vectorSumaValores;
        for (int j = 0; j < instancias.numAttributes(); j++) {
            Attribute atributoActual = instancias.attribute(j);

            if (j != varClase.index()) {
                if (atributoActual.isNominal()) {
                    vectorSumaValores = new double[varClase.numValues()];
                    double[][] matProb = new double[varClase.numValues()][atributoActual.numValues()];
                    // laplace
                    for (int i = 0; i < matProb.length; i++) {
                        for (int k = 0; k < matProb[i].length; k++) {
                            matProb[i][k] = this.laplace ? 1 : 0;
                        }
                        vectorSumaValores[i] += this.laplace ? matProb[i].length : 0;
                    }
//                    System.out.println("atributo: " + atributoActual.name());
                    //conteo 
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        Instance instancia = instancias.instance(i);
                        int valSeg = (int) instancia.value(atributoActual);
                        int valPrin = (int) instancia.value(varClase);
                        matProb[valPrin][valSeg]++;
                        vectorSumaValores[valPrin]++;
                    }

                    vectorMatProbCondicionales[j] = new MatProbabilidad(matProb, vectorSumaValores, atributoActual);
                } else if (atributoActual.isNumeric()) {
                    vectorSumaValores = new double[varClase.numValues()];
                    double[][] contador = new double[varClase.numValues()][2];
//                    System.out.println("atributo: " + atributoActual.name());
                    //suma total agrupado por clase
                    for (int i = 0; i < instancias.numInstances(); i++) {
                        // se tiene en cuante la precision
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
//                        System.out.println("[" + varClase.value(i) + "]:" + Util.imprimirVectorDouble(contador[i]));
                    }
                    vectorMatProbCondicionales[j] = new MatProbabilidad(contador, null, atributoActual);
                }
            }
        }
        this.instancias = new Instances(instanciasOriginales);
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        cadena.append("=================== matrices de probabilidad =====================\n");
        cadena.append("probabilidad var clase:\n(")
                .append(varClase.toString())
                .append(")\n[")
                .append(Util.imprimirVectorDouble(probVarClase))
                .append("]");

        for (int i = 0; i < vectorMatProbCondicionales.length; i++) {
            cadena.append(vectorMatProbCondicionales[i] == null ? "" : vectorMatProbCondicionales[i]);
        }
        return cadena.toString();
    }

    private class MatProbabilidad {

        double[][] matrizProb;
        private final double[] sumTotal;
        private final Attribute atributo;
        String[] filasNumerica = {"Desviacion", "Promedio"};

        public MatProbabilidad(double[][] matrizProb, double[] sumTotal, Attribute atributo) {
            this.matrizProb = matrizProb;
            this.sumTotal = sumTotal;
            this.atributo = atributo;
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
        for (int i = 0; i < instancias.numInstances(); i++) {
            Instance instanciaActual = instancias.instance(i);
            probabilidadesVarClase[i] = evaluarInstancia(instanciaActual);
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
        }
        return probabilidadesVarClase;
    }

    private double evaluarInstancia(Instances instancias, Instance instanciaActual, int valAttPrincipal) {
        double probabilidadTotal = 1;
        for (int j = 0; j < instancias.numAttributes(); j++) {
            Attribute atributoActual = instancias.attribute(j);
            if (j == varClase.index()) {
                probabilidadTotal += Math.log10(probVarClase[valAttPrincipal]);
            } else {
                // optener la matriz de probabilidad condiccionada respectiva de atributoaActual
                MatProbabilidad matP = vectorMatProbCondicionales[atributoActual.index()];
                if (atributoActual.isNominal()) {
                    int col = (int) instanciaActual.value(atributoActual);
                    // optener probabilidad del valor del atributoActual y atributoPrincipal
                    probabilidadTotal += Math.log10(matP.get(valAttPrincipal, col));
                } else {
                    double valor = getValor(instanciaActual, atributoActual);
                    // obtener promedio desde la posicion 1
                    double media = matP.get(valAttPrincipal, 1);
                    // obtener desviacion desde la posicion 0
                    double desvicacion = Math.max(matP.get(valAttPrincipal, 0), vectorPrecision[atributoActual.index()] / 6);
//                    double desvicacion = matP.get(valAttPrincipal, 0);
                    // probabilidad del valor del atributo actual
                    double probaAtrib = Util.probabilidad(valor, media, desvicacion);
//                    System.out.println("prob: "+probaAtrib);
                    probabilidadTotal += Math.log10(probaAtrib);
                }
            }
        }
        return Math.pow(10, probabilidadTotal);
    }

    /**
     * obtiene el valor de la instancia para una determinado atributo, teniendo
     * en cuenta la presicionpara dicho atributo.
     *
     * @param instancia
     * @param atributo
     * @return
     */
    public double getValor(Instance instancia, Attribute atributo) {
        double valor;
        valor = instancia.value(atributo);
        valor = valor / vectorPrecision[atributo.index()];
        valor = Math.round(valor) * vectorPrecision[atributo.index()];
        return valor;
    }
}
